package fr.uphf.a3ddy.controller.fragment.auth_insc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import fr.uphf.a3ddy.service.AppService;
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.Accueil_fypActivity;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.LoadFragmentService;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentConnexion extends Fragment {
    private Context context;
    private View view;
    private ImageButton imageButton;
    private TextInputLayout email;
    private TextInputLayout mdp;
    private Button boutonLogin;
    private AppService appService;
    private UserApi utilisateurApi;
    private LoadFragmentService loadFragmentService;

    private void iniUI(){
        imageButton = view.findViewById(R.id.imageButton);
        email = view.findViewById(R.id.TextInputLayout_email);
        mdp = view.findViewById(R.id.TextInputLayout_mdp);
        boutonLogin = view.findViewById(R.id.connectionButton);
    }

    private void setListeners() {
        imageButton.setOnClickListener(v -> loadFragmentService.loadFragment(
                new FragmentChoixAuthentification(),
                R.id.main)
        );
        boutonLogin.setOnClickListener(v -> connection());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        view = inflater.inflate(R.layout.connexion_layout, container, false);
        appService = (AppService) this.getActivity().getApplication();
        loadFragmentService = new LoadFragmentService(this);
        iniUI();
        setListeners();
        return view;
    }


    private void connection() {
        email = view.findViewById(R.id.TextInputLayout_email);
        mdp = view.findViewById(R.id.TextInputLayout_mdp);

        String emailText = Objects.requireNonNull(email.getEditText()).getText().toString();
        String mdpText = Objects.requireNonNull(mdp.getEditText()).getText().toString();

        Log.d("EmailText contenant avant request ;", emailText);
        Log.d("mdpText contenant avant request ;" ,mdpText);

        RetrofitService retrofitService = new RetrofitService("");
        utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        Call<UtilisateurSecurity> call = utilisateurApi.connexion(
                emailText,
                mdpText
        );

        call.enqueue(new Callback<UtilisateurSecurity>() {
            @Override
            public void onResponse(Call<UtilisateurSecurity> call, Response<UtilisateurSecurity> response) {
                Log.d("OnResponce code", String.valueOf(response.code()));

                if(response.code() == 200) {
                    Log.d("ECHO LOG CODE LODER USER", String.valueOf(response.code()));
                    Log.d("OnResponce", String.valueOf(response.isSuccessful()));
                    UtilisateurSecurity utilisateurSecurity = response.body();
                    utilisateurSecurity.setEmail(emailText);
                    // ajout de l'utilisateur security
                    appService.setUtilisateurSecurity(utilisateurSecurity);

                    String token = utilisateurSecurity.getToken();
                    EncryptedPreferencesService encryptedPreferencesService =
                            new EncryptedPreferencesService(getContext());
                    encryptedPreferencesService.saveAuthToken(token);

                    Log.d("TOKEN USER",token);

                    loadUSer();

                    Intent intent = new Intent(context, Accueil_fypActivity.class);
                    startActivity(intent);
                    requireActivity().finish();

                } else {
                    Log.d("code :", String.valueOf(response.code()));
                    Toast.makeText(context, "Erreur : " + response.code(), Toast.LENGTH_SHORT).show();
                    call.cancel();
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


    public void loadUSer(){
        RetrofitService retrofitService = new RetrofitService(
                new EncryptedPreferencesService(getContext()).getAuthToken()
        );
        utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        Call<Utilisateur> call = utilisateurApi.loadUser();
        call.enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if(response.isSuccessful()) {
                    Utilisateur utilisateur = response.body();
                    appService.getUtilisateurSecurity().setUtilisateur(utilisateur);
                    Log.d("UserLoader","userLoaded");
                } else {

                    Log.d("help",response.toString());
                }
            }
            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {
                Log.d("Erreur : ", t.getLocalizedMessage());
                Toast.makeText(context, "Erreur : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

}
