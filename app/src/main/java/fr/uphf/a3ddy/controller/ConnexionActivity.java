package fr.uphf.a3ddy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import fr.uphf.a3ddy.R;

public class ConnexionActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connexion_layout);
        
        //boutton de retour
        ImageButton returnButton = findViewById(R.id.left_arrow_button);

        //Revenir à la page de choix d'authentification
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(ConnexionActivity.this,MainActivity.class);
                startActivity(returnIntent);
            }
        });
    }
}
