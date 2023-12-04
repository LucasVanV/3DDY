package fr.uphf.a3ddy.controller.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import fr.uphf.a3ddy.AppService;
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.fragment.auth_insc.FragmentChoixAuthentification;
import fr.uphf.a3ddy.model.UtilisateurSecurity;

public class ChoixAuthentificationActivity extends AppCompatActivity {

    private UtilisateurSecurity utilisateurS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.main, FragmentChoixAuthentification.class,savedInstanceState)
                .commit();
    }
}
