package fr.uphf.a3ddy.controller.fragment.monCompte;

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

public class FragmentModifProfil extends Fragment {

    View view;
    Context context;
    private ImageButton butonRetour;
    private Button editPseudo;
    private Button editBio;
    private Button editImgProfile;

    public void iniUI() {
        butonRetour = view.findViewById(R.id.retour);
        editPseudo = view.findViewById(R.id.edit_pseudo);
        editBio = view.findViewById(R.id.edit_bio);
        editImgProfile = view.findViewById(R.id.change_photo);
    }
    public void setListeners() {
        butonRetour.setOnClickListener(v-> loadFragment(new FragmentParamatres()));
        editImgProfile.setOnClickListener(v->loadFragment(new FragmentEditImg()));
        editBio.setOnClickListener(v->loadFragment(new FragmentEditBio()));
        editPseudo.setOnClickListener(v->loadFragment(new FragmentEditPseudo()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        view = inflater.inflate(R.layout.fragment_modif_mon_profil, container, false);
        iniUI();
        setListeners();
        setTextInView();
        return view;
    }

    public void setTextInView(){
        AppService appService = (AppService) getActivity().getApplication();
        UtilisateurSecurity utilisateurSecurity = appService.getUtilisateurSecurity();
        TextView textViewpseudo = view.findViewById(R.id.user_name);
        textViewpseudo.setText(utilisateurSecurity.getUtilisateur().getPseudo());
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
