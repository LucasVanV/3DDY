package fr.uphf.a3ddy.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import fr.uphf.a3ddy.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.RetrofitService;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.retrofit.api.UserApi;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.main, FragmentChoixAuthentification.class, null)
                .commit();

    }

    private void inscription() {
        TextInputLayout email = findViewById(R.id.TextInputLayout_email);
        TextInputLayout mdp = findViewById(R.id.TextInputLayout_mdp);
        TextInputLayout mdpConfirm = findViewById(R.id.TextInputLayout_mdp_confirm);

        String emailText = email.getEditText().getText().toString();
        String mdpText = mdp.getEditText().getText().toString();
        String mdpConfirmText = mdp.getEditText().getText().toString();

        if (!mdpText.equals(mdpConfirmText)) {
            Toast.makeText(InscriptionActivity.this, "Les mots de passe sont différents", Toast.LENGTH_SHORT);
            return;
        }

        // Appel Retrofit
        RetrofitService retrofitService = new RetrofitService();
        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        Call<UtilisateurSecurity> call = utilisateurApi.inscription(
                emailText, mdpText, false, null
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

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.choix_authe,fragment);
        fragmentTransaction.commit();
    }
}
