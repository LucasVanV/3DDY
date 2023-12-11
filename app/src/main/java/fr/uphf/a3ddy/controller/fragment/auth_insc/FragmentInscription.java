package fr.uphf.a3ddy.controller.fragment.auth_insc;

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
import java.util.Objects;

import fr.uphf.a3ddy.service.AppService;
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.LoadFragmentService;
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
    Context context;
    private AppService appService;
    private LoadFragmentService loadFragmentService;


    public void iniUI(){
        imageButton = view.findViewById(R.id.imageButton);
        email = view.findViewById(R.id.TextInputLayout_email);
        mdp = view.findViewById(R.id.TextInputLayout_mdp);
        confirmerMDP = view.findViewById(R.id.TextInputLayout_mdp_confirm);
        boutonInscription = view.findViewById(R.id.bouton_inscription);
    }

    public void setListeners() {
        imageButton.setOnClickListener(v -> loadFragmentService.loadFragment(
                new FragmentChoixAuthentification()
                ,R.id.main)
        );
        boutonInscription.setOnClickListener(v -> inscription());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        loadFragmentService = new LoadFragmentService(this);
        view = inflater.inflate(R.layout.fragment_inscription, container, false);
        iniUI();
        setListeners();
        appService = (AppService) getActivity().getApplication();
        return view;
    }

    //Fonction permettant de vérifier le regex pour le mot de passe
    private boolean motDePasseValidation(String password) {
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[.#?!@$%^&*-]).{8,}$";
        return password.matches(regex);
    }

    private void inscription() {
        email = view.findViewById(R.id.TextInputLayout_email);
        mdp = view.findViewById(R.id.TextInputLayout_mdp);
        confirmerMDP = view.findViewById(R.id.TextInputLayout_mdp_confirm);

        String emailText = Objects.requireNonNull(email.getEditText()).getText().toString();
        String mdpText = Objects.requireNonNull(mdp.getEditText()).getText().toString();
        String mdpConfirmText = Objects.requireNonNull(confirmerMDP.getEditText()).getText().toString();

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
            public void onResponse(
                    Call<UtilisateurSecurity> call,
                    Response<UtilisateurSecurity> response
            ) {
                if (response.isSuccessful()) {
                    UtilisateurSecurity utilisateurSecurity = response.body();
                    utilisateurSecurity.setEmail(emailText);
                    Log.d("Utilisateur.tostring", utilisateurSecurity.toString());
                    String token = utilisateurSecurity.getToken();
                
                    EncryptedPreferencesService encryptedPreferencesService =
                            new EncryptedPreferencesService(getContext());
                    encryptedPreferencesService.saveAuthToken(token);
                    appService.setUtilisateurSecurity(utilisateurSecurity);
                    loadFragmentService.loadFragment(
                            new FragmentCreationProfil(),
                            R.id.main
                    );


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
