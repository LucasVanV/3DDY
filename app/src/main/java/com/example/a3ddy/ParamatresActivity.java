package com.example.a3ddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class ParamatresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paramatres);

        Button boutonMonCompte = findViewById(R.id.button);

        boutonMonCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_mon_compte = new Intent(ParamatresActivity.this, MonCompteActivity.class);
                startActivity(intent_mon_compte);
            }
        });
    }


}
