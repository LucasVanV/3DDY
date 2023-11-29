package fr.uphf.a3ddy.controller.fragment.monCompte;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.UserApi;
import retrofit2.Call;

public class FragmentEditPseudo extends Fragment {

    View view;
    Context context;

    private UtilisateurSecurity userS;
    private ImageButton buttonRetour;
    private Button enregister;
    private TextInputEditText textInputEditTextpseudo;


    public void iniUI(){
        buttonRetour = view.findViewById(R.id.retour);
        enregister = view.findViewById(R.id.enregistrer);
        textInputEditTextpseudo = view.findViewById(R.id.TextInputEdit_pseudo);
    }

    public void setListeners() {
        buttonRetour.setOnClickListener(v -> loadFragment(new FragmentParamatres()));
        enregister.setOnClickListener(v-> modificationPseudo(textInputEditTextpseudo.getEditableText().toString()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //userS =  requireActivity().(UtilisateurSecurity.class);
        context = getContext();
        view = inflater.inflate(R.layout.fragment_modif_mon_profil, container, false);
        iniUI();
        setListeners();
        return view;
    }


    public void modificationPseudo(String newPseudo) {
        // Obtenez le token de votre emplacement de stockage sécurisé
        EncryptedPreferencesService encryptedPreferencesService =
                new EncryptedPreferencesService(getContext());
        String authToken = encryptedPreferencesService.getAuthToken();

        // Appel Retrofit
        RetrofitService retrofitService = new RetrofitService(authToken);
        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        /* TODO
        Call<Utilisateur> call = utilisateurApi.modificationProfilPseudo("Bearer " + authToken ,
                );


         */
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