package fr.uphf.a3ddy.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.RetrofitService;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.retrofit.api.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);


        ImageView imageView = findViewById(R.id.imageView);

        //Récupération des données du formulaire
        TextInputLayout nom_utilisateur = findViewById(R.id.TextInputLayout_nom_utilisateur);
        TextInputLayout email = findViewById(R.id.TextInputLayout_email);
        TextInputLayout mdp = findViewById(R.id.TextInputLayout_mdp);
        TextInputLayout mdpConfirm = findViewById(R.id.TextInputLayout_mdp_confirm);
        Button bouton_inscription = findViewById(R.id.bouton_inscription);

        bouton_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nom_utilisateur.getEditText().toString().isEmpty() || email.getEditText().toString().isEmpty() || mdp.getEditText().toString().isEmpty() || mdpConfirm.getEditText().toString().isEmpty()) {
                    Toast.makeText(InscriptionActivity.this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                } else {
                    inscription();
                }
            }
        });
    }


    private void inscription() {
        //Récupération des données du formulaire
        TextInputEditText nom_utilisateur = findViewById(R.id.TextInputLayout_nom_utilisateur);
        TextInputEditText email = findViewById(R.id.TextInputLayout_email);
        TextInputEditText mdp = findViewById(R.id.TextInputLayout_mdp);
        TextInputEditText mdpConfirm = findViewById(R.id.TextInputLayout_mdp_confirm);
        Button bouton_inscription = findViewById(R.id.bouton_inscription);

        RetrofitService retrofitService = new RetrofitService();
        UserApi utilisateurAPi = retrofitService.getRetrofit().create(UserApi.class);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo(nom_utilisateur.getText().toString());
        utilisateur.setEmail(email.getText().toString());
        utilisateur.setPassword(mdp.getText().toString());


        utilisateurAPi.inscription(utilisateur).enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if (response.isSuccessful()) {
                    Intent intent_creation_profil = new Intent(InscriptionActivity.this, CreationProfilActivity.class);
                    startActivity(intent_creation_profil);
                } else {
                    Toast.makeText(InscriptionActivity.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {
                Toast.makeText(InscriptionActivity.this, "Throwable : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
