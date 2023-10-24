package com.example.a3ddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PosterActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1; //définir une valeur unique pour la demande d'upload des models
    private static final int PICK_IMAGE_REQUEST = 2; //définir une vvaleur unique pour la demande d'upload de l'image du model

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        Button bouton_ajouter_model = findViewById(R.id.bouton_ajouter_model_3d);
        Button bouton_photo_model = findViewById(R.id.bouton_ajouter_photo);
        // Bouton qui sert à sélectionner les modèles 3D
        bouton_ajouter_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Permet de sélectionner tous les types de fichiers
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Autorise la sélection de plusieurs fichiers
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // Bouton qui sert à selectionner la photo du model
        bouton_photo_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); // Restreint la sélection aux images
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Récupération et traitement des fichiers selectionnés pour les models 3D
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ClipData clipData = data.getClipData(); // Pour obtenir plusieurs fichiers
                if (clipData != null) {
                    int nb_model_3d = clipData.getItemCount();
                    if (nb_model_3d > 2) {
                        // Affichez un message d'erreur pour dire que seuls 20 fichiers sont autorisés.
                        LayoutInflater inflater = getLayoutInflater();
                        //On ajoute le toast personnalisé dans le layout
                        View layout = inflater.inflate(R.layout.toast_erreur_upload_models_3d, (ViewGroup) findViewById(R.id.custom_toast_container));
                        TextView text = (TextView) layout.findViewById(R.id.text_toast_erreur_upload_models_3d);
                        text.setText("Vous ne pouvez importer que 20 modèles maximum");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    } else {
                        for (int i = 0; i < nb_model_3d; i++) {
                            Uri fileUri = clipData.getItemAt(i).getUri();
                            // Traitez le fichier 3D ici
                        }
                    }
                } else {
                    Uri fileUri = data.getData(); // Pour obtenir un seul fichier 3D
                    // Traitez le fichier 3D ici
                }
            }
            //Récupération et traitement de l'image du model
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData(); // Récupère l'URI de l'image sélectionnée
                // Traitez l'image ici
            }
        }
    }
}


