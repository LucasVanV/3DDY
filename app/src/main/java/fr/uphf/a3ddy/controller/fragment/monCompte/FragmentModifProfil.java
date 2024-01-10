package fr.uphf.a3ddy.controller.fragment.monCompte;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;



import fr.uphf.a3ddy.service.AppService;
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.LoadFragmentService;


public class FragmentModifProfil extends Fragment {

    View view;
    Context context;
    private ImageButton butonRetour;
    private Button editPseudo;
    private Button editBio;
    private Button editImgProfile;
    private LoadFragmentService loadFragmentService;

    public void iniUI() {
        butonRetour = view.findViewById(R.id.retour);
        editPseudo = view.findViewById(R.id.edit_pseudo);
        editBio = view.findViewById(R.id.edit_bio);
        editImgProfile = view.findViewById(R.id.change_photo);
    }
    public void setListeners() {
        butonRetour.setOnClickListener(v-> loadFragmentService.loadFragment(
                new FragmentProfil(),
                R.id.bloc_fragment_accueil)
        );
        editImgProfile.setOnClickListener(v-> loadFragmentService.loadFragment(
                new FragmentEditImg(),
                R.id.bloc_fragment_accueil)
        );
        editBio.setOnClickListener(v->loadFragmentService.loadFragment(
                new FragmentEditBio(),
                R.id.bloc_fragment_accueil)
        );
        editPseudo.setOnClickListener(v->loadFragmentService.loadFragment(
                new FragmentEditPseudo(),
                R.id.bloc_fragment_accueil)
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        loadFragmentService = new LoadFragmentService(this);
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
}
