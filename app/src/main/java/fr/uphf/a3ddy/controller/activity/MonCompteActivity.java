package fr.uphf.a3ddy.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import fr.uphf.a3ddy.R;

public class MonCompteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_compte);

        ImageButton boutonRetour = findViewById(R.id.retour);

        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_mon_compte = new Intent(MonCompteActivity.this, ParamatresActivity.class);
                startActivity(intent_mon_compte);
            }
        });
    }
}
