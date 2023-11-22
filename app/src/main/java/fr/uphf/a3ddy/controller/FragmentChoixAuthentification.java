package fr.uphf.a3ddy.controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

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
        view.findViewById(R.id.inscription).setOnClickListener(v -> loadFragment(new FragmentInscription()));
        //TODO connexion

        return view;
    }

    public void loadFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.choix_authe, fragment)
                .addToBackStack(null)
                .commit();
    }
}
