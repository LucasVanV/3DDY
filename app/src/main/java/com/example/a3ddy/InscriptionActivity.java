package com.example.a3ddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        ImageView imageView = findViewById(R.id.imageView);

        /*
        Exemple de récupération des données pour les edittext de material component :

            TextInputEditText textInputEditText = findViewById(R.id.TextInputEditText_nom_utilisateur);
            String utilisateurNom = textInputEditText.getText().toString();
        */

    }
}
