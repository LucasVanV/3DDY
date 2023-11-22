package fr.uphf.a3ddy.controller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.uphf.a3ddy.R;

public class FragmentChoixAuthentification extends Fragment {

    View view;
    Button buttonInscription;
    Button buttonConnexion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.choix_authentification, container, false);
        buttonInscription = view.findViewById(R.id.inscription);
        buttonConnexion = view.findViewById(R.id.connexion);
        buttonInscription.setOnClickListener(view -> {

            Fragment fragment = new FragmentInscription();
            FragmentTransaction transaction = getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();

            transaction.replace(R.id.choix_authe, fragment)
                    .addToBackStack(null)
                    .commit();

            view.setVisibility(View.INVISIBLE);
            buttonConnexion.setVisibility(View.INVISIBLE);
        });

        return view;
    }
}