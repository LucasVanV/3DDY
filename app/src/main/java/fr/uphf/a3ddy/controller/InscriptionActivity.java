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
                TextInputLayout email = findViewById(R.id.TextInputLayout_email);
                TextInputLayout mdp = findViewById(R.id.TextInputLayout_mdp);
                TextInputLayout mdpConfirm = findViewById(R.id.TextInputLayout_mdp_confirm);

                String emailText = email.getEditText().getText().toString();
                String mdpText = mdp.getEditText().getText().toString();
                String mdpConfirmText = mdpConfirm.getEditText().getText().toString();

                if (!mdpText.equals(mdpConfirmText)) {
                    Toast.makeText(InscriptionActivity.this, "Les mots de passe sont différents", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (motDePasseValidation(mdpText) == false) {
                    Toast.makeText(InscriptionActivity.this, "Le mot de passe doit contenir au minimum 8 caractères, dont 1 majuscule, 1 minuscule, 1 chiffre et 1 symbole", Toast.LENGTH_SHORT).show();
                    return;
                }

                inscriptionUtilisateur(emailText, mdpText, false);
            }
        });
    }

    //Fonction permettant de vérifier le regex pour le mot de passe
    private boolean motDePasseValidation(String password) {
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[.#?!@$%^&*-]).{8,}$";
        return password.matches(regex);
    }

    //Fonction permettant l'inscription d'un utilisateur
    private void inscriptionUtilisateur(String emailText, String mdpText, boolean idAdmin) {
        // Appel Retrofit
        RetrofitService retrofitService = new RetrofitService();
        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        Call<UtilisateurSecurity> call = utilisateurApi.inscription(
                emailText, mdpText, idAdmin
        );

        call.enqueue(new Callback<UtilisateurSecurity>() {
            @Override
            public void onResponse(Call<UtilisateurSecurity> call, Response<UtilisateurSecurity> response) {
                if (response.isSuccessful()) {
                    UtilisateurSecurity utilisateurSecurity1 = response.body();
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
            public void onFailure(Call<UtilisateurSecurity> call, Throwable t) {
                // Gérez les erreurs de connexion, etc.
                Log.d("Erreur : ", t.getLocalizedMessage());
                Toast.makeText(InscriptionActivity.this, "Erreur : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }
}
