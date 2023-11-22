package fr.uphf.a3ddy.controller;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInscription extends Fragment {

    private View view;
    private ImageButton imageButton;
    private TextInputLayout email;
    private TextInputLayout mdp;
    private TextInputLayout confirmerMDP;
    private Button boutonInscription;

    Context context = this.getContext();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_inscription, container, false);
        imageButton = view.findViewById(R.id.imageButton);
        email = view.findViewById(R.id.TextInputLayout_email);
        mdp = view.findViewById(R.id.TextInputLayout_mdp);
        confirmerMDP = view.findViewById(R.id.TextInputLayout_mdp_confirm);
        boutonInscription = view.findViewById(R.id.bouton_inscription);

        imageButton.setOnClickListener(view -> {
            Fragment fragment = new ChoixAuthentificationActivity();
            FragmentTransaction transaction = getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();

            transaction.replace(R.id.page_inscription, fragment)
                    .addToBackStack(null)
                    .commit();

            view.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
            email.setVisibility(View.INVISIBLE);
            mdp.setVisibility(View.INVISIBLE);
            confirmerMDP.setVisibility(View.INVISIBLE);
            boutonInscription.setVisibility(View.INVISIBLE);
        });

        boutonInscription.setOnClickListener(view -> inscription());

        return view;
    }

    //Fonction permettant de vérifier le regex pour le mot de passe
    private boolean motDePasseValidation(String password) {
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[.#?!@$%^&*-]).{8,}$";
        return password.matches(regex);
    }

    private void inscription() {

        String emailText = email.getEditText().getText().toString();
        String mdpText = mdp.getEditText().getText().toString();
        String mdpConfirmText = mdp.getEditText().getText().toString();

        if (!mdpText.equals(mdpConfirmText)) {
            Toast.makeText(context, "Les mots de passe sont différents", Toast.LENGTH_SHORT);
            return;
        }

        RetrofitService retrofitService = new RetrofitService("");
        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        Call<UtilisateurSecurity> call = utilisateurApi.inscription(
                emailText, mdpText
        );

        call.enqueue(new Callback<UtilisateurSecurity>() {
            @Override
            public void onResponse(Call<UtilisateurSecurity> call, Response<UtilisateurSecurity> response) {
                if (response.isSuccessful()) {
                    UtilisateurSecurity utilisateurSecurity1 = response.body();
                    Log.d("",response.toString());
                    String token = utilisateurSecurity1.getToken();
                    Log.d("token de l'utilisateur",token);

                    EncryptedPreferencesService encryptedPreferencesService =
                            new EncryptedPreferencesService(getContext());
                    encryptedPreferencesService.saveAuthToken(token);


                    // Inscription réussie, redirigez l'utilisateur vers l'activité suivante
                    Fragment fragment = new FragmentCreationProfil();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                    transaction.replace(R.id.page_inscription, fragment)
                            .addToBackStack(null)
                            .commit();

                    view.setVisibility(View.INVISIBLE);
                    imageButton.setVisibility(View.INVISIBLE);
                    email.setVisibility(View.INVISIBLE);
                    mdp.setVisibility(View.INVISIBLE);
                    confirmerMDP.setVisibility(View.INVISIBLE);
                    boutonInscription.setVisibility(View.INVISIBLE);
                } else {
                    // Gestion des erreurs en fonction du code de réponse HTTP
                    if (response.code() == 400) {
                        // Erreur de validation côté serveur
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d("Erreur d'inscription", errorBody); // Enregistrez le message d'erreur dans les logs
                            Toast.makeText(context, "Erreur lors de l'inscription : " + errorBody, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("Erreur d'inscription", "Erreur lors de l'inscription"); // Enregistrez le message d'erreur dans les logs
                            Toast.makeText(context, "Erreur lors de l'inscription", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Gérez d'autres erreurs ici
                        Log.d("Erreur d'inscription", "Erreur inattendue : " + response.code());
                        Toast.makeText(context, "Erreur lors de l'inscription : " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UtilisateurSecurity> call, Throwable t) {
                // Gérez les erreurs de connexion, etc.
                Log.d("Erreur : ", t.getLocalizedMessage());
                Toast.makeText(context, "Erreur : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }
}
