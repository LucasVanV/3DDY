package fr.uphf.a3ddy.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.RetrofitService;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.retrofit.api.UserApi;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Button boutonInscription = findViewById(R.id.bouton_inscription);

        boutonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inscription();
            }
        });
    }

    private void inscription() {
        TextInputLayout nomUtilisateur = findViewById(R.id.TextInputLayout_nom_utilisateur);
        TextInputLayout email = findViewById(R.id.TextInputLayout_email);
        TextInputLayout mdp = findViewById(R.id.TextInputLayout_mdp);

        String nomUtilisateurText = nomUtilisateur.getEditText().getText().toString();
        String emailText = email.getEditText().getText().toString();
        String mdpText = mdp.getEditText().getText().toString();

        Utilisateur utilisateur = new Utilisateur(nomUtilisateurText, "app/src/main/res/drawable/default_user.png", "");
        UtilisateurSecurity utilisateurSecurity = new UtilisateurSecurity(emailText, mdpText, false, null);
        utilisateurSecurity.setUtilisateur(utilisateur);

        // Appel Retrofit
        RetrofitService retrofitService = new RetrofitService();
        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        Call<InscriptionRequest> call = utilisateurApi.inscription(
                emailText, mdpText, false, null
        );

        call.enqueue(new Callback<InscriptionRequest>() {
            @Override
            public void onResponse(Call<InscriptionRequest> call, Response<InscriptionRequest> response) {
                if (response.isSuccessful()) {
                    InscriptionRequest inscriptionRequestResponse = response.body();
                    // Inscription réussie, redirigez l'utilisateur vers l'activité suivante
                    Intent intent = new Intent(InscriptionActivity.this, CreationProfilActivity.class);
                    startActivity(intent);
                } else {
                    // Gestion des erreurs en fonction du code de réponse HTTP
                    if (response.code() == 400) {
                        // Erreur de validation côté serveur
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d("Erreur d'inscription", errorBody); // Enregistrez le message d'erreur dans les logs
                            Toast.makeText(InscriptionActivity.this, "Erreur lors de l'inscription : " + errorBody, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("Erreur d'inscription", "Erreur lors de l'inscription"); // Enregistrez le message d'erreur dans les logs
                            Toast.makeText(InscriptionActivity.this, "Erreur lors de l'inscription", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Gérez d'autres erreurs ici
                        Log.d("Erreur d'inscription", "Erreur inattendue : " + response.code());
                        Toast.makeText(InscriptionActivity.this, "Erreur lors de l'inscription : " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<InscriptionRequest> call, Throwable t) {
                // Gérez les erreurs de connexion, etc.
                Log.d("Erreur : ", t.getLocalizedMessage());
                Toast.makeText(InscriptionActivity.this, "Erreur : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }
}
