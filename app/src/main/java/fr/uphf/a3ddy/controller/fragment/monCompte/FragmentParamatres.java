package fr.uphf.a3ddy.controller.fragment.monCompte;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageButton;


import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.fragment.FragmentAccueilFyp;

public class FragmentParamatres extends Fragment {

    View view;
    Context context;
    private Button boutonMonCompte;
    private ImageButton boutonRetour;


    public void iniUI(){
        boutonMonCompte = view.findViewById(R.id.monComptebutton);
        boutonRetour = view.findViewById(R.id.retour);
    }

    private void setListener() {
        boutonMonCompte.setOnClickListener(v-> loadFragment(new FragmentModifMonCompte()));//TODO
        boutonRetour.setOnClickListener(v -> loadFragment(new FragmentProfil()));//TODO
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_paramatres, container, false);
        context = getContext();
        iniUI();

        setListener();

        return view;
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
