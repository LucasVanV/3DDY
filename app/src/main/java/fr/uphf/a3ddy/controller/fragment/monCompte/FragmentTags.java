package fr.uphf.a3ddy.controller.fragment.monCompte;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.uphf.a3ddy.AppService;
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.Tag;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTags extends Fragment {

    View view;
    Context context;
    private ImageButton buttonRetour;
    private ChipGroup chipTag;

    private UtilisateurSecurity userS;
    private AppService appService;

    private Button buttonValider;

    public void iniUI() {
        buttonRetour = view.findViewById(R.id.retour);
        chipTag = view.findViewById(R.id.chipGroup);
        buttonValider = view.findViewById(R.id.button_valider);

        List<String> tagList = Arrays.asList("Automobile", "Sport", "Objet");

        // Ajouter les chips à partir de la liste
        for (String tag : tagList) {
            Chip chip = new Chip(context);
            chip.setText(tag);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Réagir aux changements de sélection du chips
                if (isChecked) {
                    // Chip sélectionnée
                    Log.d("ChipSelection", "Selected: " + tag);
                } else {
                    // Chip désélectionnée
                    Log.d("ChipSelection", "Deselected: " + tag);
                }
            });

            chipTag.addView(chip);
        }
    }

    public void setListener() {
        buttonRetour.setOnClickListener(v -> new FragmentParamatres());
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder selectedChipsText = new StringBuilder();
                Set<Tag> tagSet = new HashSet<>();

                for (int i = 0; i < chipTag.getChildCount(); i++) {
                    Chip chip = (Chip) chipTag.getChildAt(i);
                    if (chip.isChecked()) {
                        tagSet.add(new Tag(chip.toString()));
                        String chipText = chip.getText().toString();
                        selectedChipsText.append(chipText).append("/");
                    }
                }

                // Supprimer le dernier "/" si nécessaire
                if (selectedChipsText.length() > 0 && selectedChipsText.charAt(selectedChipsText.length() - 1) == '/') {
                    selectedChipsText.deleteCharAt(selectedChipsText.length() - 1);
                }
                modificationTags(tagSet);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_tags, container, false);
        context = getContext();
        appService = (AppService) getActivity().getApplication();
        userS = appService.getUtilisateurSecurity();
        iniUI();

        setListener();

        return view;
    }

    public void modificationTags(Set<Tag> tags) {
        // Obtenez le token de votre emplacement de stockage sécurisé

        EncryptedPreferencesService encryptedPreferencesService =
                new EncryptedPreferencesService(getContext());
        String authToken =  encryptedPreferencesService.getAuthToken();

        // Appel Retrofit
        RetrofitService retrofitService = new RetrofitService(authToken);
        UserApi utilisateurApi = retrofitService.getRetrofit().create(UserApi.class);
        Log.d("Token : " , authToken );
        Call<Utilisateur> call = utilisateurApi.modificationTags("Bearer " + authToken,
                userS.getId(),
                userS.getUtilisateur().getPseudo(),
                tags
        );

        call.enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if (response.isSuccessful()) {
                    Utilisateur modifRequest = response.body();
                    Toast.makeText(getActivity(), "Modification réussie ", Toast.LENGTH_LONG).show();
                    // Modification réussie, redirigez l'utilisateur vers l'activité suivante
                    loadFragment( new FragmentParamatres());
                } else {
                    // Gestion des erreurs en fonction du code de réponse HTTP
                    if (response.code() == 400) {
                        // Erreur de validation côté serveur
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d("Erreur d'inscription", errorBody); // Enregistrez le message d'erreur dans les logs
                            Toast.makeText(getActivity(), "Erreur lors de la modification : " + errorBody,
                                    Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("Erreur d'inscription", "Erreur lors de l'inscription"); // Enregistrez le message d'erreur dans les logs
                            Toast.makeText(getActivity(), "Erreur lors de la modification", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Gérez d'autres erreurs ici
                        Log.d("Erreur d'inscription", "Erreur inattendue : " + response.code());
                        Toast.makeText(getActivity(), "Erreur lors de la modification : " + response.body(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {
                // Gérez les erreurs de modification de compte, etc.
                Log.d("Erreur : ", t.getLocalizedMessage());
                Toast.makeText(getActivity(), "Erreur : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
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
