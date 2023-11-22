package fr.uphf.a3ddy.controller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import fr.uphf.a3ddy.R;

public class FragmentConnection extends Fragment {

    private View view;
    private ImageButton imageButton;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button boutonLogin;


    private void iniUI(){
        imageButton = view.findViewById(R.id.imageButton);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextMDP);
        boutonLogin = view.findViewById(R.id.connectionButton);
    }


    private void setListeners() {
        imageButton.setOnClickListener(v -> loadFragment(new FragmentChoixAuthentification()));
        boutonLogin.setOnClickListener(v -> connection());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_inscription, container, false);
        iniUI();
        setListeners();
        return view;
    }


    private void connection() {
        //TODO
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
            transaction.add(R.id.main, fragment, fragment.getClass().getSimpleName());
        } else {
            transaction.show(fragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
