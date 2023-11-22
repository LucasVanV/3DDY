package fr.uphf.a3ddy.controller.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.FragmentInscription;

public class ChoixAuthentificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choix_authentification);

        loadFragment(new Fragment());
        findViewById(R.id.inscription).setOnClickListener(view -> {
            loadFragment(new FragmentInscription());
        });
    }

    // Fonction pour charger un fragment dans le conteneur
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);  // Permet d'ajouter la transaction à la pile de retour
        transaction.commit();
    }
}
