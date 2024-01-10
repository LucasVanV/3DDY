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
import fr.uphf.a3ddy.service.AppService;
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.ChoixAuthentificationActivity;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.LoadFragmentService;
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
    private Button buttonTags;
    private Button supresionCompte;
    private AppService appService;
    private UtilisateurSecurity utilisateurSecurity;

    private LoadFragmentService loadFragmentService;

    public void iniUI(){
        boutonMonCompte = view.findViewById(R.id.monComptebutton);
        boutonRetour = view.findViewById(R.id.retour);
        buttonDeconnexion = view.findViewById(R.id.button_deconnexion);
        buttonTags = view.findViewById(R.id.button_tags);
        supresionCompte = view.findViewById(R.id.button_supprimer);
    }

    private void setListener() {
        boutonMonCompte.setOnClickListener(v-> loadFragmentService.loadFragment(
                new FragmentModifMonCompte(),
                R.id.bloc_fragment_accueil)
        );
        boutonRetour.setOnClickListener(v -> loadFragmentService.loadFragment(
                new FragmentProfil(),
                R.id.bloc_fragment_accueil)
        );
        buttonTags.setOnClickListener(v -> loadFragmentService.loadFragment(
                new FragmentTags(),
                R.id.bloc_fragment_accueil)
        );

        buttonDeconnexion.setOnClickListener(v -> deconnection());
        supresionCompte.setOnClickListener(v -> suppresioncompteValidation());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_paramatres, container, false);
        context = getContext();
        loadFragmentService = new LoadFragmentService(this);
        appService = (AppService) getActivity().getApplication();
        utilisateurSecurity = appService.getUtilisateurSecurity();
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

    public void suppresioncompteValidation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Suppresion Compte");
        builder.setMessage("Voulez vous supprimer votre compte");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Action pour le bouton "OK
                suppresioncompte();
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

    public UserApi loadingUserApi(){
        EncryptedPreferencesService encryptedPreferencesService =
                new EncryptedPreferencesService(getContext());
        String authToken =  encryptedPreferencesService.getAuthToken();
        // Appel Retrofit
        RetrofitService retrofitService = new RetrofitService(authToken);
        return retrofitService.getRetrofit().create(UserApi.class);
    }

    public void suppresioncompte(){

        UserApi utilisateurApi = loadingUserApi();
        Call<String> call = utilisateurApi.deleteProfil();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Log.d("Suppresion compte", "Compte supprimer avec succes " + response.body());
                }
                else {
                    Log.d("Suppresion compte", "Echec compte supprimer " + response.body());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Logout", "Erreur de requête: " + t.getMessage());
            }
        });
    }


    public void logout() {
        UserApi utilisateurApi = loadingUserApi();
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
}
