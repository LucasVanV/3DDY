package fr.uphf.a3ddy.controller.fragment.monCompte;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.service.LoadFragmentService;

public class FragmentProfil extends Fragment {
    View view;
    private Context context;
    private ImageButton parametre;
    private LoadFragmentService loadFragmentService;

    private void iniUI(){
        parametre = view.findViewById(R.id.bouton_parametre);
        //TODO les autre bouton
    }

    private void setListeners() {
        parametre.setOnClickListener(v -> loadFragmentService.loadFragment(
                new FragmentParamatres(),
                R.id.bloc_fragment_accueil
                ));
        //TODO les autres action de bouton
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profil, container, false);
        context = getContext();
        loadFragmentService = new LoadFragmentService(this);
        iniUI();
        setListeners();
        return view;
    }
}
