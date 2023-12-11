package fr.uphf.a3ddy.controller.fragment.auth_insc;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import fr.uphf.a3ddy.service.AppService;
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.Accueil_fypActivity;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.UserApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCreationProfil extends Fragment {
    View view;
    Context context;
    private static final int REQUEST_IMAGE_PICK = 1;
    private TextInputLayout nomUtilisateur;
    private TextInputLayout bio;
    private ChipGroup chipGroup;
    private AppService appService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getContext();
        view = inflater.inflate(R.layout.creation_profil, container, false);
        nomUtilisateur = view.findViewById(R.id.TextInputLayout_nomUtilisateur);
        bio = view.findViewById(R.id.TextInputLayout_bio);
        chipGroup = view.findViewById(R.id.chipGroup);

        appService = (AppService) requireActivity().getApplication();

        // TODO : Mettre directement les données de la table "tags" dans la list au lieu de rentrer à la main
        List<String> tagList = Arrays.asList("Automobile", "Sport", "Objet");

        // Ajouter les chips à partir de la liste
        for (String tag : tagList) {
            Chip chip = new Chip(context);
            chip.setText(tag);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Réagir aux changements de sélection du chips
                if (isChecked) {
                    // Chip sélectionnée
                    Log.d("ChipSelection", "Selected: " + tag);
                } else {
                    // Chip désélectionnée
                    Log.d("ChipSelection", "Deselected: " + tag);
                }
            });

            chipGroup.addView(chip);
        }

        Button button = view.findViewById(R.id.bouton_ajouter_photoProfil);
        button.setOnClickListener(this::choisirImage);
        return view;
    }


    // Fonction permettant de choisir une image sur le téléphone
    public void choisirImage(View view) {
        // Créez un Intent pour ouvrir la galerie d'images
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");  // Filtre pour afficher uniquement les images
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // L'utilisateur a choisi une image
            Uri imageUri = data.getData();
            String nomUtilisateurText = nomUtilisateur.getEditText().getText().toString();
            String bioText = bio.getEditText().getText().toString();

            // Obtenir les chips sélectionnées
            StringBuilder selectedChipsText = new StringBuilder();

            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    String chipText = chip.getText().toString();
                    selectedChipsText.append(chipText).append("/");
                }
            }

            // Supprimer le dernier "/" si nécessaire
            if (selectedChipsText.length() > 0 && selectedChipsText.charAt(selectedChipsText.length() - 1) == '/') {
                selectedChipsText.deleteCharAt(selectedChipsText.length() - 1);
            }

            // Lancer la fonction de création de profil
            Button buttonValider = view.findViewById(R.id.validateButton);
            buttonValider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Gestion d'erreurs
                    //PP null
                    if (imageUri == null) {
                        Toast.makeText(context, "Vous devez choisir une photo de profil",
                                Toast.LENGTH_LONG).show();
                    }
                    //Nom d'utilisateur null
                    if (nomUtilisateurText.isEmpty()) {
                        Toast.makeText(context, "Veuillez saisir un nom d'utilisateur",
                                Toast.LENGTH_LONG).show();
                    }
                    //Bio null
                    if (bioText.isEmpty()) {
                        Toast.makeText(context, "Veuillez saisir une bio",
                                Toast.LENGTH_LONG).show();
                    }
                    //Tags null
                    if (selectedChipsText.toString().isEmpty()) {
                        Toast.makeText(context, "Vous devez choisir au moins un tag",
                                Toast.LENGTH_LONG).show();
                    }

                    creationProfil(nomUtilisateurText, bioText, selectedChipsText.toString(), imageUri);
                    Toast.makeText(context, "Inscription réussie, bienvenue sur 3DDY !",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void creationProfil(String nomUtilisateurText, String bioText, String textTags, Uri imageUri) {

        // Appel Retrofit
        RetrofitService retrofitService = new RetrofitService(new EncryptedPreferencesService(context).getAuthToken());

        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        try {
            // Utilisez ContentResolver pour ouvrir un InputStream à partir de l'URI
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            if (inputStream != null) {
                // Créez un fichier temporaire pour stocker l'image
                File imageFile = createTempImageFile();
                if (imageFile != null) {
                    // Copiez les données de l'InputStream vers le fichier temporaire
                    writeInputStreamToFile(inputStream, imageFile);
                    // Créez un objet RequestBody à partir du fichier temporaire
                    RequestBody imageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                    // Créez un MultipartBody.Part à partir de l'objet RequestBody
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("imageProfil", imageFile.getName(), imageRequestBody);

                    // Envoi de la demande avec l'image
                    Call<Utilisateur> call = utilisateurApi.creationProfil(
                            RequestBody.create(MediaType.parse("text/plain"), nomUtilisateurText),
                            RequestBody.create(MediaType.parse("text/plain"), bioText),
                            RequestBody.create(MediaType.parse("text/plain"), textTags),
                            imagePart
                    );
                    // Exécutez la demande
                    requestCreateProfil(call);

                } else {
                    Toast.makeText(context, "Erreur de création du fichier temporaire", Toast.LENGTH_SHORT).show();
                }
                // N'oubliez pas de fermer l'InputStream lorsque vous avez terminé
                inputStream.close();
            } else {
                Toast.makeText(context, "Erreur de récupération de l'image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void loadUSer(){
        RetrofitService retrofitService = new RetrofitService(new EncryptedPreferencesService(getContext()).getAuthToken());
        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        Call<Utilisateur> call = utilisateurApi.loadUser();
        call.enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if(response.isSuccessful()) {
                    Utilisateur utilisateur = response.body();
                    appService.getUtilisateurSecurity().setUtilisateur(utilisateur);
                    Log.d("UserLoader","userLoaded");
                } else {
                    Log.d("help",response.toString());
                }
            }
            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {
                Log.d("Erreur : ", t.getLocalizedMessage());
                Toast.makeText(context, "Erreur : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }


    public void requestCreateProfil(Call call){
        call.enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if (response.isSuccessful()) {
                    Utilisateur utilisateur = response.body();
                    UtilisateurSecurity utilisateurSecurity = appService.getUtilisateurSecurity();
                    utilisateurSecurity.setUtilisateur(utilisateur);
                    Log.d("show me UserS", utilisateurSecurity.toString());
                    Log.d("UserProfile",utilisateur.toString());
                    // Inscription réussie, redirigez l'utilisateur vers l'activité suivante
                    appService.setUtilisateurSecurity(utilisateurSecurity);
                    loadUSer();
                    Intent intent = new Intent(context, Accueil_fypActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
            }
            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {
                Log.d("Erreur : ", t.getLocalizedMessage());
                Toast.makeText(context, "Erreur : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public File createTempImageFile() {
        try {
            // Créez un fichier temporaire dans le répertoire de cache de l'application
            File cacheDir = context.getCacheDir();
            if (cacheDir != null) {
                return File.createTempFile("temp_image", ".jpg", cacheDir);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void writeInputStreamToFile(InputStream inputStream, File outputFile) throws IOException {
        OutputStream outputStream = Files.newOutputStream(outputFile.toPath());
        byte[] buffer = new byte[4 * 1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

}
