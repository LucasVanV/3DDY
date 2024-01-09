package fr.uphf.a3ddy.controller.fragment.monCompte;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.fragment.monCompte.FragmentParamatres;
import fr.uphf.a3ddy.model.posts.Page;
import fr.uphf.a3ddy.service.AppService;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.LoadFragmentService;
import fr.uphf.a3ddy.service.posts.PostAdapterProfilUser;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.PostApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfil extends Fragment {
    View view;
    private Context context;
    private PostAdapterProfilUser postAdapter;
    private AppService appService;
    private int currentPage = 0;
    private boolean isLoading = false;

    private RecyclerView recyclerView;
    private ImageButton parametre;
    private TextView nom_utilisateur;
    private TextView bio;
    private TextView nb_publications;
    private TextView nb_abonnes;
    private TextView nb_abonnement;
    private ImageView photo_profil;
    private ImageButton bouton_parametre;
    private ImageButton bouton_favoris;
    private Button button_modifierProfil_ou_suivre;
    private Button button_partagerProfil_ou_message;
    private LoadFragmentService loadFragmentService;


    private void iniUI() {
        parametre = view.findViewById(R.id.bouton_parametre);
        nom_utilisateur = view.findViewById(R.id.nom_utilisateur);
        bio = view.findViewById(R.id.bio);
        nb_publications = view.findViewById(R.id.nb_publications);
        nb_abonnes = view.findViewById(R.id.nb_abonnes);
        nb_abonnement = view.findViewById(R.id.nb_abonnement);
        photo_profil = view.findViewById(R.id.photo_profil);
        bouton_parametre = view.findViewById(R.id.bouton_parametre);
        bouton_favoris = view.findViewById(R.id.bouton_favoris);
        button_modifierProfil_ou_suivre = view.findViewById(R.id.button_modifierProfil_ou_suivre);
        button_partagerProfil_ou_message = view.findViewById(R.id.button_partagerProfil_ou_message);
    }

    public void onSeeMoreClick(View view, int position) {
        // Handle the PopupMenu logic here
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_see_more_post, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.supprimer) {
                    Log.d("menuuuu", "delete");
                    return true;
                } else if (itemId == R.id.modifier) {
                    Log.d("menuuuu", "update");
                    return true;
                }
                return true;
            }
        });

        popupMenu.show();
    }

    private List<String> getUserBundle() {
        List<String> list = new ArrayList<>();
        try {
            Bundle arguments = getArguments();
            if (arguments != null) {
                //key == nom des arguments du loadFragment()
                List<String> keys = Arrays.asList("userId", "userPseudo", "userBio", "userPP", "userNbPublication", "userNbAbonnes", "userNbAbonnement");

                //on ajoute pour chaque clé la valeur correspondante dans une liste
                for (String key : keys) {
                    String value = arguments.getString(key);
                    if (value != null) {
                        list.add(value);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("FragmentProfil", "Error while getting user bundle", e);
        }

        return list;
    }


    private void setArguments() {
        List<String> userBundle = getUserBundle();

        //Si il y a des infos dans le bundle on rempli la page avec un utilisateur spécifique (pas l'utilisateur
        // connecté)
        if (userBundle.size() > 0) {
            bouton_parametre.setVisibility(View.GONE);
            bouton_favoris.setVisibility(View.GONE);
            button_modifierProfil_ou_suivre.setText("Suivre");
            button_partagerProfil_ou_message.setText("Envoyer un message");
        }

        if (userBundle.size() >= 4) {  // Vérifiez que l'URL de la photo de profil est présente
            String baseUrl = "http://192.168.56.1:8080/";
            String imageProfilUrl = baseUrl + "images/uploads/" + userBundle.get(3) +
                    "/profilPicture/PhotoProfil.jpg";

            Glide.with(context)
                    .load(imageProfilUrl)
                    .placeholder(R.drawable.default_user)
                    .apply(RequestOptions.circleCropTransform())
                    .into(photo_profil);
        }

        if (userBundle.size() >= 2) {
            nom_utilisateur.setText(userBundle.get(1));
            if (userBundle.size() >= 3) {
                bio.setText(userBundle.get(2));
                if (userBundle.size() >= 4) {
                    nb_publications.setText(userBundle.get(4));
                    if (userBundle.size() >= 5) {
                        nb_abonnes.setText(userBundle.get(5));
                        if (userBundle.size() >= 6) {
                            nb_abonnement.setText(userBundle.get(6));
                        }
                    }
                }
            }
        }

        //Si il y a rien dans le bundle on rempli les infos de l'utilisateur connecté
        if (userBundle.size() == 0) {
            button_modifierProfil_ou_suivre.setText("Modifier mon profil");
            button_partagerProfil_ou_message.setText("Partager mon profil");

            nom_utilisateur.setText(appService.getUtilisateurSecurity().getUtilisateur().getPseudo());
            bio.setText(appService.getUtilisateurSecurity().getUtilisateur().getBio());
            nb_publications.setText(String.valueOf(appService.getUtilisateurSecurity().getUtilisateur().getNbPublication()));
            nb_abonnes.setText(String.valueOf(appService.getUtilisateurSecurity().getUtilisateur().getNbAbonne()));
            nb_abonnement.setText(String.valueOf(appService.getUtilisateurSecurity().getUtilisateur().getNbSuivis()));

            String baseUrl = "http://192.168.56.1:8080/";
            String imageProfilUrl =
                    baseUrl + "images/uploads/" + appService.getUtilisateurSecurity().getUtilisateur().getDossierServer() +
                            "/profilPicture/PhotoProfil.jpg";

            Glide.with(context)
                    .load(imageProfilUrl)
                    .placeholder(R.drawable.default_user)
                    .apply(RequestOptions.circleCropTransform())
                    .into(photo_profil);

            button_modifierProfil_ou_suivre.setOnClickListener(v -> loadFragmentService.loadFragment(
                    new FragmentModifProfil(),
                    R.id.bloc_fragment_accueil)
            );
        }
    }


    private void setListeners() {
        parametre.setOnClickListener(v -> loadFragmentService.loadFragment(
                new FragmentParamatres(),
                R.id.bloc_fragment_accueil
        ));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profil, container, false);
        context = getContext();
        loadFragmentService = new LoadFragmentService(this);
        appService = (AppService) this.getActivity().getApplication();

        iniUI();
        setListeners();
        getUserBundle();
        setArguments();

        postAdapter = new PostAdapterProfilUser(this);

        recyclerView = view.findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(postAdapter);

        // Ajoutez un écouteur de défilement pour détecter le bas de la liste
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= 10) {  // Vérifiez si au moins 10 éléments pour déclencher le chargement suivant
                        currentPage++;

                    }
                }
            }
        });

        // Appel pour récupérer les posts d'un utilisateur en particulier (pas celui connecté)
        if (!getUserBundle().isEmpty()) {
            getListPost(currentPage, Long.valueOf(getUserBundle().get(0)));
        } else {
            // Appel pour récupérer les posts de l'utilisateur connecté
            getListPost(currentPage, appService.getUtilisateurSecurity().getUtilisateur().getId());
        }

        return view;
    }


    public void getListPost(int page, Long idUser) {
        isLoading = true;
        RetrofitService retrofitService = new RetrofitService(new EncryptedPreferencesService(context).getAuthToken());
        PostApi postApi = retrofitService.getRetrofit().create(PostApi.class);

        Call<Page> call = postApi.getUserPost(page, idUser);
        call.enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Log.d("erreur", "1");
                if (response.isSuccessful()) {
                    Page newPosts = response.body();
                    Log.d("post", newPosts.getPostList().toString());
                    if (newPosts != null && newPosts.getPostList() != null) {
                        // Vérifier si la liste actuelle est vide avant d'ajouter de nouveaux posts
                        if (postAdapter.getItemCount() == 0) {
                            postAdapter.setPosts(newPosts);
                        } else {
                            if (postAdapter != null) {
                                postAdapter.addPosts(newPosts);
                            }
                        }
                    } else {
                        Log.e("API Call", "Empty list of posts");
                    }
                } else {
                    Log.e("API Call", "Failed to get posts. HTTP status code: " + response.code());
                    Log.e("API Call", "Error body: " + response.errorBody().toString());
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                Log.e("Erreur", "Failed to get posts: " + t.getMessage(), t);
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                isLoading = false;
            }
        });
    }


}
