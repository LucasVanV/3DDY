package com.example.a3ddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilActivity extends AppCompatActivity {
    private static final int ACCUEIL = 1;
    private static final int RECHERCHE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ImageButton bouton_parametre = findViewById(R.id.bouton_parametre);

        bouton_parametre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_parametre = new Intent(ProfilActivity.this, ParamatresActivity.class);
                startActivity(intent_parametre);
            }
        });


        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case ACCUEIL:
                        // Réagir au clic sur l'élément 1
                        return true;
                    case RECHERCHE:
                        // Réagir au clic sur l'élément 2
                        return true;
                    default:
                        return false;
                }
            }
        });

    }
}
