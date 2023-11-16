package fr.uphf.a3ddy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.uphf.a3ddy.R;

public class menuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.accueil) {
                // Redirection vers la page d'accueil
                Intent intentAccueil = new Intent(getApplicationContext(), Accueil_fypActivity.class);
                Log.d("MenuActivity", "Before starting activity");
                try {
                    startActivity(intentAccueil);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("MenuActivity", "After starting activity");

                return true;
            } else if (itemId == R.id.recherche) {
                // Redirection vers la page de recherche
                // Intent intentRecherche = new Intent(menuActivity.this, RechercheActivity.class);
                // startActivity(intentRecherche);
                return true;
            } else if (itemId == R.id.publier) {
                // Redirection vers la page de publication
                // Intent intentPublier = new Intent(menuActivity.this, PublierActivity.class);
                // startActivity(intentPublier);
                return true;
            } else if (itemId == R.id.message) {
                // Redirection vers la page de messages
                // Intent intentMessage = new Intent(menuActivity.this, MessageActivity.class);
                // startActivity(intentMessage);
                return true;
            } else if (itemId == R.id.compte) {
                //Redirection vers la page de compte
                Intent intentCompte = new Intent(menuActivity.this, MonCompteActivity.class);
                startActivity(intentCompte);
                return true;
            } else {
                return false;
            }
        });
    }


}

