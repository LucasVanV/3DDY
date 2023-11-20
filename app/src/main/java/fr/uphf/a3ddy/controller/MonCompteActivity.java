package fr.uphf.a3ddy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.ModifRequest;
import fr.uphf.a3ddy.retrofit.RetrofitService;
import fr.uphf.a3ddy.retrofit.api.UserApi;
import java.security.Key;import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        Button buttonValider = findViewById(R.id.button_valider);
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout email = findViewById(R.id.TextInputLayout_email);
                TextInputLayout password = findViewById(R.id.TextInputLayout_mdp_confirm);

                String emailText = email.getEditText().getText().toString();
                String passwordText = password.getEditText().getText().toString();

                modificationCompte(emailText, passwordText);
            }
        });
    }

    private void modificationCompte(String emailText, String passwordText) {
        // Obtenez le token de votre emplacement de stockage sécurisé
        String authToken = "eyJhbGciOiJIUzI1NiJ9" +
                ".eyJzdWIiOiJmZXpmQHRlc3QuY29tIiwiaWF0IjoxNzAwNDc4Njg0LCJleHAiOjE3MDA1NjUwODR9.SyyCcU0O6SrJUcAZ5OXTfsCm6q97Z5crSv5YcbMrGIk";

        // Appel Retrofit
        RetrofitService retrofitService = new RetrofitService(authToken);
        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        Call<ModifRequest> call = utilisateurApi.modificationCompte("Bearer " + authToken,
                emailText, passwordText
        );

        call.enqueue(new Callback<ModifRequest>() {
            @Override
            public void onResponse(Call<ModifRequest> call, Response<ModifRequest> response) {
                if (response.isSuccessful()) {
                    ModifRequest modifRequest = response.body();
                    Toast.makeText(MonCompteActivity.this, "Modification réussie ", Toast.LENGTH_LONG).show();
                    // Modification réussie, redirigez l'utilisateur vers l'activité suivante
                    Intent intent = new Intent(MonCompteActivity.this, Accueil_fypActivity.class);
                    startActivity(intent);
                } else {
                    // Gestion des erreurs en fonction du code de réponse HTTP
                    if (response.code() == 400) {
                        // Erreur de validation côté serveur
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d("Erreur d'inscription", errorBody); // Enregistrez le message d'erreur dans les logs
                            Toast.makeText(MonCompteActivity.this, "Erreur lors de la modification : " + errorBody,
                                    Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("Erreur d'inscription", "Erreur lors de l'inscription"); // Enregistrez le message d'erreur dans les logs
                            Toast.makeText(MonCompteActivity.this, "Erreur lors de la modification", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Gérez d'autres erreurs ici
                        Log.d("Erreur d'inscription", "Erreur inattendue : " + response.code());
                        Toast.makeText(MonCompteActivity.this, "Erreur lors de la modification : " + response.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModifRequest> call, Throwable t) {
                // Gérez les erreurs de modification de compte, etc.
                Log.d("Erreur : ", t.getLocalizedMessage());
                Toast.makeText(MonCompteActivity.this, "Erreur : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }
}
