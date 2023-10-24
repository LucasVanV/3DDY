package com.example.a3ddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParamatresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paramatres);

        Button boutonMonCompte = findViewById(R.id.button);
        ImageButton boutonRetour = findViewById(R.id.retour);

        boutonMonCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_mon_compte = new Intent(ParamatresActivity.this, MonCompteActivity.class);
                startActivity(intent_mon_compte);
            }
        });

        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_profil = new Intent(ParamatresActivity.this, ProfilActivity.class);
                startActivity(intent_profil);
            }
        });

        //Permet de mettre en surbrillance la page Mon compte dans le menu
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.compte);
    }


}
