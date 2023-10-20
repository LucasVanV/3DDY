package com.example.a3ddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ProfilActivity extends AppCompatActivity {

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
    }
}
