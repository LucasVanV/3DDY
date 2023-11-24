package fr.uphf.a3ddy.controller.fragment.monCompte;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import fr.uphf.a3ddy.R;

public class FragmentProfil extends Fragment {
    View view;
    private Context context;
    private ImageButton parametre;

    private void iniUI(){
        parametre = view.findViewById(R.id.bouton_parametre);
        //TODO les autre bouton
    }

    private void setListeners() {
        parametre.setOnClickListener(v -> loadFragment(new FragmentParamatres()));
        //TODO les autres action de bouton
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profil, container, false);
        context = getContext();
        iniUI();
        setListeners();
        return view;
    }


    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Masquer le fragment actuel s'il y en a un
        Fragment currentFragment = getFragmentManager().findFragmentById(getId());

        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        // Remplacer le fragment ou l'ajouter s'il n'y en a pas
        if (getChildFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName()) == null) {
            transaction.add(R.id.bloc_fragment_accueil, fragment, fragment.getClass().getSimpleName());
        }
        else {
            transaction.show(fragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
