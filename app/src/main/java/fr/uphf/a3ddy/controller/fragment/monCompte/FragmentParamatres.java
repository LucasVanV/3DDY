package fr.uphf.a3ddy.controller.fragment.monCompte;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;


import java.io.IOException;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.ChoixAuthentificationActivity;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentParamatres extends Fragment {

    View view;
    Context context;
    private Button boutonMonCompte;
    private ImageButton boutonRetour;
    private Button buttonDeconnexion;


    public void iniUI(){
        boutonMonCompte = view.findViewById(R.id.monComptebutton);
        boutonRetour = view.findViewById(R.id.retour);
        buttonDeconnexion = view.findViewById(R.id.button_deconnexion);
    }

    private void setListener() {
        boutonMonCompte.setOnClickListener(v-> loadFragment(new FragmentModifMonCompte()));
        boutonRetour.setOnClickListener(v -> loadFragment(new FragmentProfil()));
        buttonDeconnexion.setOnClickListener(v -> deconnection());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_paramatres, container, false);
        context = getContext();
        iniUI();

        setListener();

        return view;
    }

    public void deconnection(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Deconnection");
        builder.setMessage("Voulez-vous vous deconecter");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Action pour le bouton "OK
                logout();
                Intent intent = new Intent(context, ChoixAuthentificationActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Action pour le bouton "Annuler"
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void logout() {
        // Obtenez le token de votre emplacement de stockage sécurisé
        EncryptedPreferencesService encryptedPreferencesService =
                new EncryptedPreferencesService(getContext());
        String authToken =  encryptedPreferencesService.getAuthToken();
        // Appel Retrofit
        RetrofitService retrofitService = new RetrofitService(authToken);
        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);

        Call<String> call = utilisateurApi.logout();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // La déconnexion a réussi
                    // Ici, vous pouvez par exemple effacer les données de l'utilisateur
                    // stockées localement ou naviguer vers l'écran de connexion
                    Log.d("Logout", "Déconnexion réussie: " + response.body());
                } else {
                    // Gérer la réponse en cas d'échec
                    Log.d("Logout", "Échec de la déconnexion: " + response.code());
                    // Afficher un message d'erreur à l'utilisateur, par exemple
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Gérer l'échec total de la requête (par exemple, pas de connexion Internet)
                Log.d("Logout", "Erreur de requête: " + t.getMessage());
                // Afficher un message d'erreur à l'utilisateur, par exemple
            }
        });
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
            transaction.add(R.id.bloc_fragment_accueil, fragment, fragment.getClass().getSimpleName());
        } else {
            transaction.show(fragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
