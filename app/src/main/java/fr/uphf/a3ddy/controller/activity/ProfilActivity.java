package fr.uphf.a3ddy.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.uphf.a3ddy.R;

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


        //Permet de mettre en surbrillance la page Mon compte dans le menu
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.compte);


    }
}
