package fr.uphf.a3ddy.controller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.FragmentChoixAuthentification;
import fr.uphf.a3ddy.controller.FragmentInscription;

public class ChoixAuthentificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.main, FragmentChoixAuthentification.class,null)
                .commit();
    }

    // Fonction pour charger un fragment dans le conteneur
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);  // Permet d'ajouter la transaction à la pile de retour
        transaction.commit();
    }
}
