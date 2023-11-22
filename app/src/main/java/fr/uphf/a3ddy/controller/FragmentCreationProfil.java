package fr.uphf.a3ddy.controller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.uphf.a3ddy.R;

public class FragmentCreationProfil extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("FragmentCreationProfil", "onCreateView() called");
        view = inflater.inflate(R.layout.creation_profil, container, false);
        return view;
    }
}