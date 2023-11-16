package fr.uphf.a3ddy.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.RetrofitService;
import fr.uphf.a3ddy.retrofit.api.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choix_authentification);
        //recuperation_JSon();

        Button connexionButton = findViewById(R.id.connexion);
        Button inscriptionButton = findViewById(R.id.inscription);

        // Renvoie vers la page de connexion
        connexionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ConnexionActivity.class);
                startActivity(intent);
            }
        });

        //Renvoie vers la page d'inscription
        inscriptionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,InscriptionActivity.class);
                startActivity(intent);
            }
        });
    }



    /**
     *Exemple de requet pour la recuperation de depuis l'api
    */
    private void recuperation_JSon() {

        TextView textView = (TextView) findViewById(R.id.textJsonTest);

        // creation d'un object retrofit voir class fr.uphf.a3ddy.retrofit.RetrofitService
        RetrofitService retrofitService = new RetrofitService();

        //Utilisation d'une api faite a la mano pour les diferentes requete (VOir quentin pour plus d'explication)
        UserApi utilisateurAPi = retrofitService.getRetrofit().create(UserApi.class);

        // creation d'une requete assincro (Obligatoire sinon sa crask blocker par android(question de main Thread))
        // utilisation de l'ambda expresison
        //apel de votre objet
        utilisateurAPi.connexion().enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                //verification de reponse valide
                if (response.isSuccessful()) {
                    // Traitement du JsonArray (e.g. l'affichage des données)
                    Utilisateur utilisateur = response.body();// metre en place une methode de
                    textView.setText("Email utilisateur : " + utilisateur.getPseudo()); // Vous pouvez le traiter de manière plus sophistiquée
                } else {
                    textView.setText("Code :" + response.code());
                }
            }

            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur de recuperation", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Erreur: ", t);
            }
        });
    }
}
