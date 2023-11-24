package fr.uphf.a3ddy.controller.fragment.auth_insc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.uphf.a3ddy.R;

public class FragmentChoixAuthentification extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.choix_authentification, container, false);
        view.findViewById(R.id.inscription).setOnClickListener(v -> loadFragment(new FragmentInscription()));//TODO a corriger
        view.findViewById(R.id.connexion).setOnClickListener(v -> loadFragment(new FragmentConnexion()));//TODO a corriger

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main,fragment)
                .addToBackStack(null)
                .commit();
    }
}
