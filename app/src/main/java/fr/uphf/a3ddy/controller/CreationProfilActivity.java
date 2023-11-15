package fr.uphf.a3ddy.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.RetrofitService;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.retrofit.api.UserApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreationProfilActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private TextInputLayout nomUtilisateur;
    private TextInputLayout bio;
    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_profil);

        nomUtilisateur = findViewById(R.id.TextInputLayout_nomUtilisateur);
        bio = findViewById(R.id.TextInputLayout_bio);
        chipGroup = findViewById(R.id.chipGroup);

        ImageButton boutonRetour = findViewById(R.id.arrowButton);

        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_inscription = new Intent(CreationProfilActivity.this, InscriptionActivity.class);
                startActivity(intent_inscription);
            }
        });

        // Liste de tags
        // TODO : Mettre directement les données de la table "tags" dans la list au lieu de rentrer à la main
        List<String> tagList = Arrays.asList("Automobile", "Sport", "Objet");

        // Ajouter les chips à partir de la liste
        for (String tag : tagList) {
            Chip chip = new Chip(this);
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
    }

    // Fonction permettant de choisir une image sur le téléphone
    public void choisirImage(View view) {
        // Créez un Intent pour ouvrir la galerie d'images
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");  // Filtre pour afficher uniquement les images
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

            Log.d("tags1845515154145", selectedChipsText.toString());

            // Lancer la fonction de création de profil
            Button buttonValider  = findViewById(R.id.validateButton);
            buttonValider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    creationProfil(nomUtilisateurText, bioText, selectedChipsText.toString(), imageUri);
                }
            });
        }
    }

    public void creationProfil(String nomUtilisateurText, String bioText, String textTags, Uri imageUri) {
        // Logs pour déboguer
        System.out.println("Pseudo : " + nomUtilisateurText);
        System.out.println("Bio : " + bioText);
        System.out.println("Tags : " + textTags);
        System.out.println("Image : " + imageUri);

        // Appel Retrofit
        RetrofitService retrofitService = new RetrofitService();
        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        try {
            // Utilisez ContentResolver pour ouvrir un InputStream à partir de l'URI
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
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
                    call.enqueue(new Callback<Utilisateur>() {
                        @Override
                        public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                            if (response.isSuccessful()) {
                                Utilisateur utilisateur = response.body();
                                // Inscription réussie, redirigez l'utilisateur vers l'activité suivante
                                Intent intent = new Intent(CreationProfilActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Utilisateur> call, Throwable t) {
                            Log.d("Erreur : ", t.getLocalizedMessage());
                            Toast.makeText(CreationProfilActivity.this, "Erreur : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });
                } else {
                    Toast.makeText(CreationProfilActivity.this, "Erreur de création du fichier temporaire", Toast.LENGTH_SHORT).show();
                }
                // N'oubliez pas de fermer l'InputStream lorsque vous avez terminé
                inputStream.close();
            } else {
                Toast.makeText(CreationProfilActivity.this, "Erreur de récupération de l'image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(CreationProfilActivity.this, "Erreur : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File createTempImageFile() {
        try {
            // Créez un fichier temporaire dans le répertoire de cache de l'application
            File cacheDir = getCacheDir();
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

    private void writeInputStreamToFile(InputStream inputStream, File outputFile) throws IOException {
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
