package fr.uphf.a3ddy.controller.fragment.monCompte;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import fr.uphf.a3ddy.R;

public class FragmentModifProfil extends Fragment {

    View view;
    Context context;

    public void iniUI() {
    }
    public void setListeners() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        view = inflater.inflate(R.layout.fragment_modif_mon_profil, container, false);
        iniUI();
        setListeners();


        return view;
    }

}
