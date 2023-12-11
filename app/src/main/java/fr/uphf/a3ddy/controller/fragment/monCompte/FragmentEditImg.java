package fr.uphf.a3ddy.controller.fragment.monCompte;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.uphf.a3ddy.service.AppService;
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.Accueil_fypActivity;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.LoadFragmentService;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.UserApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEditImg extends Fragment {

    View view;
    Context context;
    private static final int REQUEST_IMAGE_PICK = 1;

    private UtilisateurSecurity userS;
    private ImageButton buttonRetour;
    private Button enregister;
    private Button selectImg;
    private AppService appService;
    private LoadFragmentService loadFragmentService;


    public void iniUI() {
        buttonRetour = view.findViewById(R.id.retour);
        enregister = view.findViewById(R.id.enregistrer);
        selectImg = view.findViewById(R.id.buttonChangeProfilePhoto);
    }

    public void setListeners() {
        buttonRetour.setOnClickListener(v -> loadFragmentService.loadFragment(new FragmentParamatres(),R.id.bloc_fragment_accueil));
        selectImg.setOnClickListener(this::choisirImage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        loadFragmentService = new LoadFragmentService(this);
        view = inflater.inflate(R.layout.fragment_edit_imagebio, container, false);
        appService = (AppService) getActivity().getApplication();
        userS = appService.getUtilisateurSecurity();
        iniUI();
        setListeners();
        return view;
    }


    public void choisirImage(View view) {
        // Créez un Intent pour ouvrir la galerie d'images
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");  // Filtre pour afficher uniquement les images
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // L'utilisateur a choisi une image
            Uri imageUri = data.getData();
            enregister.setOnClickListener(v -> {
                if (imageUri == null) {
                    Toast.makeText(context, "Vous devez choisir une photo de profil",
                            Toast.LENGTH_SHORT).show();
                }
                modificationImgProfil(imageUri);
            });
        }
    }

    public void modificationImgProfil(Uri imageUri) {

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
                    Call<Utilisateur> call = utilisateurApi.modificationProfilImg(
                            userS.getUtilisateur().getId(),
                           imagePart
                    );
                    // Exécutez la demande
                    requestModifImg(call);
                } else {
                    Toast.makeText(context, "Erreur de création du fichier temporaire", Toast.LENGTH_SHORT).show();
                }
                // N'oubliez pas de fermer l'InputStream lorsque vous avez terminé
                inputStream.close();
            } else {
                Toast.makeText(context, "Erreur de récupération de l'image", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Erreur : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void requestModifImg(Call call){
        call.enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) { // TODO a verifier
                if (response.isSuccessful()) {
                    Utilisateur utilisateur = response.body();
                    UtilisateurSecurity utilisateurSecurity = appService.getUtilisateurSecurity();
                    utilisateurSecurity.setUtilisateur(utilisateur);

                    // Inscription réussie, redirigez l'utilisateur vers l'activité suivante
                    appService.setUtilisateurSecurity(utilisateurSecurity);

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
        OutputStream outputStream = new FileOutputStream(outputFile);
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
