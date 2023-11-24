package fr.uphf.a3ddy.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.Accueil_fypActivity;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
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


    private void iniUI(){
        imageButton = view.findViewById(R.id.imageButton);
        email = view.findViewById(R.id.TextInputLayout_email);
        mdp = view.findViewById(R.id.TextInputLayout_mdp);
        boutonLogin = view.findViewById(R.id.connectionButton);
    }

    private void setListeners() {
        imageButton.setOnClickListener(v -> loadFragment(new FragmentChoixAuthentification()));
        boutonLogin.setOnClickListener(v -> connection());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        view = inflater.inflate(R.layout.connexion_layout, container, false);
        iniUI();
        setListeners();
        return view;
    }


    private void connection() {
        email = view.findViewById(R.id.TextInputLayout_email);
        mdp = view.findViewById(R.id.TextInputLayout_mdp);

        String emailText = Objects.requireNonNull(email.getEditText()).getText().toString();
        String mdpText = Objects.requireNonNull(mdp.getEditText()).getText().toString();

        RetrofitService retrofitService = new RetrofitService("");
        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        Call<UtilisateurSecurity> call = utilisateurApi.connexion(
                emailText,
                mdpText
        );

        call.enqueue(new Callback<UtilisateurSecurity>() {
            @Override
            public void onResponse(Call<UtilisateurSecurity> call, Response<UtilisateurSecurity> response) {
                if(response.isSuccessful()) {
                    UtilisateurSecurity utilisateurSecurity1 = response.body();
                    Log.d("bite",response.toString());
                    String token = utilisateurSecurity1.getToken();
                    Log.d("token de l'utilisateur",token);

                    EncryptedPreferencesService encryptedPreferencesService =
                            new EncryptedPreferencesService(getContext());
                    encryptedPreferencesService.saveAuthToken(token);

                    Intent intent = new Intent(context, Accueil_fypActivity.class);
                    startActivity(intent);
                } else {
                    Log.d("help",response.toString());
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


    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        // Masquer le fragment actuel s'il y en a un
        Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        // Remplacer le fragment ou l'ajouter s'il n'y en a pas
        if (getChildFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName()) == null) {
            transaction.add(R.id.main, fragment, fragment.getClass().getSimpleName());
        } else {
            transaction.show(fragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
