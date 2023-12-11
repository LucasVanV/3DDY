package fr.uphf.a3ddy.controller.fragment.auth_insc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.service.LoadFragmentService;

public class FragmentChoixAuthentification extends Fragment {

    View view;
    private LoadFragmentService loadFragmentService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadFragmentService = new LoadFragmentService(this);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }

            return view;
        }
        view = inflater.inflate(R.layout.choix_authentification, container, false);
        view.findViewById(R.id.inscription).setOnClickListener(v -> loadFragmentService.loadFragment(
                new FragmentInscription(),
                R.id.main)
        );
        view.findViewById(R.id.connexion).setOnClickListener(v -> loadFragmentService.loadFragment(
                new FragmentConnexion(),
                R.id.main)
        );
        return view;
    }

}
