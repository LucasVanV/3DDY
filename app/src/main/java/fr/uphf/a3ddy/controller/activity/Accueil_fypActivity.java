package fr.uphf.a3ddy.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.fragment.monCompte.FragmentProfil;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;

public class Accueil_fypActivity extends AppCompatActivity {

    private UtilisateurSecurity currentUserSecurity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);

        //On inclus le menu sur l'activité principale
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomAppBar);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Log.d("Menu activity", "Item selectionné" + item.getItemId());
            int itemId = item.getItemId();
            if (itemId == R.id.accueil) {
                // Redirection vers la page d'accueil
                Intent intentRecherche = new Intent(this, Accueil_fypActivity.class);
                startActivity(intentRecherche);
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
                loadFragment(new FragmentProfil());
                return true;
            } else {
                return false;
            }
        });
    }
/*
    public void ExtractionUserS(){
        Intent intent = getIntent();
        currentUserSecurity= intent.get(UtilisateurSecurity);
    }
    */

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bloc_fragment_accueil,fragment)
                .addToBackStack(null)
                .commit();
    }
}
