package fr.uphf.a3ddy.controller.fragment.monCompte;

import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.UtilisateurSecurity;

import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.LoadFragmentService;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentModifMonCompte extends Fragment {

    View view;
    Context context;
    private ImageButton boutonRetour;
    private Button buttonValider;

    private LoadFragmentService loadFragmentService;


    private void iniUI(){
        boutonRetour = view.findViewById(R.id.retour);
        buttonValider = view.findViewById(R.id.button_valider);
    }

    private void setListeners() {
        boutonRetour.setOnClickListener(v-> loadFragmentService.loadFragment(
                new FragmentParamatres(),
                R.id.bloc_fragment_accueil
                )
        );
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout email = view.findViewById(R.id.TextInputLayout_email);
                TextInputLayout password = view.findViewById(R.id.TextInputLayout_mdp_confirm);
                String emailText = email.getEditText().getText().toString();
                String passwordText = password.getEditText().getText().toString();
                modificationCompte(emailText, passwordText);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        loadFragmentService = new LoadFragmentService(this);
        view = inflater.inflate(R.layout.fragment_modif_mon_compte, container, false);
        iniUI();
        setListeners();
        return view;
    }

    public void modificationCompte(String emailText, String passwordText) {
        // Obtenez le token de votre emplacement de stockage sécurisé
        EncryptedPreferencesService encryptedPreferencesService =
                new EncryptedPreferencesService(getContext());

        String authToken =  encryptedPreferencesService.getAuthToken();

        // Appel Retrofit
        RetrofitService retrofitService = new RetrofitService(authToken);
        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        Call<UtilisateurSecurity> call = utilisateurApi.modificationCompte(
                emailText,
                passwordText
        );

        call.enqueue(new Callback<UtilisateurSecurity>() {
            @Override
            public void onResponse(Call<UtilisateurSecurity> call, Response<UtilisateurSecurity> response) {
                if (response.isSuccessful()) {
                    UtilisateurSecurity modifRequest = response.body();
                    Toast.makeText(getActivity(), "Modification réussie ", Toast.LENGTH_LONG).show();
                    // Modification réussie, redirigez l'utilisateur vers l'activité suivante
                    loadFragmentService.loadFragment(
                            new FragmentModifMonCompte(),
                            R.id.main
                    );

                } else {
                    // Gestion des erreurs en fonction du code de réponse HTTP
                    if (response.code() == 400) {
                        // Erreur de validation côté serveur
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d("Erreur d'inscription", errorBody); // Enregistrez le message d'erreur dans les logs
                            Toast.makeText(getActivity(), "Erreur lors de la modification : " + errorBody,
                                    Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("Erreur d'inscription", "Erreur lors de l'inscription"); // Enregistrez le message d'erreur dans les logs
                            Toast.makeText(getActivity(), "Erreur lors de la modification", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Gérez d'autres erreurs ici
                        Log.d("Erreur d'inscription", "Erreur inattendue : " + response.code());
                        Toast.makeText(getActivity(), "Erreur lors de la modification : " + response.body(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UtilisateurSecurity> call, Throwable t) {
                // Gérez les erreurs de modification de compte, etc.
                Log.d("Erreur : ", t.getLocalizedMessage());
                Toast.makeText(getActivity(), "Erreur : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }
}
