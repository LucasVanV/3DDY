package fr.uphf.a3ddy.controller.fragment.monCompte;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import fr.uphf.a3ddy.AppService;
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEditPseudo extends Fragment {

    View view;
    Context context;

    private UtilisateurSecurity userS;
    private ImageButton buttonRetour;
    private Button enregistrer;
    private TextInputEditText textInputEditTextpseudo;
    private AppService appService;


    public void iniUI(){
        buttonRetour = view.findViewById(R.id.retour);
        enregistrer = view.findViewById(R.id.enregistrer);
        textInputEditTextpseudo = view.findViewById(R.id.TextInputEdit_pseudo);
    }

    public void setListeners() {
        buttonRetour.setOnClickListener(v -> loadFragment(new FragmentParamatres()));
        enregistrer.setOnClickListener(v-> modificationPseudo());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        appService = (AppService) getActivity().getApplication();

        view = inflater.inflate(R.layout.fragment_edit_pseudo, container, false);
        userS = appService.getUtilisateurSecurity();
        iniUI();
        setListeners();
        return view;
    }


    public void modificationPseudo() {

        if(textInputEditTextpseudo.getEditableText().toString() != ""){
            // Obtenez le token de votre emplacement de stockage sécurisé
            RetrofitService retrofitService = new RetrofitService(new EncryptedPreferencesService(context).getAuthToken());

            UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

            Call<Utilisateur> call = utilisateurApi.modificationProfilPseudo(
                    userS.getUtilisateur().getId(),
                    textInputEditTextpseudo.getEditableText().toString()
                    );
            call.enqueue(new Callback<Utilisateur>() {
                @Override
                public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                    if (response.isSuccessful()) {
                        userS.setUtilisateur(response.body());
                        loadFragment(new FragmentModifProfil());
                    }
                    else{
                        String errorBody = null;
                        try {
                            errorBody = response.errorBody().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("Erreur d'inscription", errorBody); // Enregistrez le message d'erreur dans les logs
                        Toast.makeText(getActivity(), "Erreur lors de la modification : " + errorBody,
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Utilisateur> call, Throwable t) {
                    // Gérez les erreurs de modification de compte, etc.
                    Log.d("Erreur : ", t.getLocalizedMessage());
                    Toast.makeText(getActivity(), "Erreur : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Masquer le fragment actuel s'il y en a un
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        // Remplacer le fragment ou l'ajouter s'il n'y en a pas
        if (getChildFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName()) == null) {
            transaction.add(R.id.bloc_fragment_accueil, fragment, fragment.getClass().getSimpleName());
        } else {
            transaction.show(fragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}