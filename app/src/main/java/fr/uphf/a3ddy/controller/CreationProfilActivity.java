package fr.uphf.a3ddy.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.RetrofitService;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.retrofit.api.UserApi;
import okhttp3.MediaType;
import retrofit2.Call;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;

public class CreationProfilActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private TextInputLayout nomUtilisateur;
    private TextInputLayout bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_profil);

        nomUtilisateur = findViewById(R.id.TextInputLayout_nomUtilisateur);
        bio = findViewById(R.id.TextInputLayout_bio);

        /* Exemple pour la récupération des chips sélectionnés :

        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        List<Integer> checkedChipIds = chipGroup.getCheckedChipIds();

        for (Integer chipId : checkedChipIds) {
            Chip selectedChip = findViewById(chipId);
            String chipText = selectedChip.getText().toString();

            // Traitez chaque chip sélectionné individuellement
        }

        */
    }

    //Fonction permettant de choisir une image sur le téléphone
    public void choisirImage(View view) {
        // Créez un Intent pour ouvrir la galerie d'images
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");  // Filtre pour afficher uniquement les images
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // L'utilisateur a choisi une image
            Uri imageUri = data.getData();

            String nomUtilisateurText = nomUtilisateur.getEditText().getText().toString();
            String bioText = bio.getEditText().getText().toString();

            // Lancer la fonction de création de profil
            creationProfil(nomUtilisateurText, bioText, imageUri);
        }
    }

    public void creationProfil(String nomUtilisateurText, String bioText, Uri imageUri) {
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
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("photoProfil", imageFile.getName(), imageRequestBody);

                    // Envoie de la demande avec l'image
                    Call<Utilisateur> call = utilisateurApi.creationProfil(
                            RequestBody.create(MediaType.parse("text/plain"), nomUtilisateurText),
                            RequestBody.create(MediaType.parse("text/plain"), bioText),
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


