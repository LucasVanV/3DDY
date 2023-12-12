package fr.uphf.a3ddy.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.fragment.monCompte.FragmentProfil;
import fr.uphf.a3ddy.controller.fragment.posts.FragmentPoster;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.posts.Post;
import fr.uphf.a3ddy.model.posts.PostRequest;
import fr.uphf.a3ddy.service.posts.PostAdapter;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.PostApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Accueil_fypActivity extends AppCompatActivity {
    private PostAdapter postAdapter;
    private int currentPage = 0;
    private boolean isLoading = false;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);

        // Initialiser votre adapter
        postAdapter = new PostAdapter();
        getFyp(currentPage);
        recyclerView = findViewById(R.id.recyclerViewPosts);

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
                        getFyp(currentPage);
                    }
                }
            }
        });


        //On inclus le menu sur l'activité principale
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomAppBar);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Log.d("Menu activity", "Item selectionné" + item.getItemId());
            int itemId = item.getItemId();
            if (itemId == R.id.accueil) {
                // Redirection vers la page d'accueil
                Intent intentRecherche = new Intent(this, Accueil_fypActivity.class);
                startActivity(intentRecherche);
                return true;
            } else if (itemId == R.id.recherche) {
                // Redirection vers la page de recherche
                // Intent intentRecherche = new Intent(menuActivity.this, RechercheActivity.class);
                // startActivity(intentRecherche);
                return true;
            } else if (itemId == R.id.publier) {
                // Redirection vers la page de publication
                loadFragment(new FragmentPoster());
                return true;
            } else if (itemId == R.id.message) {
                // Redirection vers la page de messages
                // Intent intentMessage = new Intent(menuActivity.this, MessageActivity.class);
                // startActivity(intentMessage);
                return true;
            } else if (itemId == R.id.compte) {
                //Redirection vers la page de compte
                loadFragment(new FragmentProfil());
                return true;
            } else {
                return false;
            }
        });
    }

    private void displayPosts(List<Post> newPosts) {
        // Ajoutez les nouveaux posts à l'adaptateur
        postAdapter.addPosts(newPosts);
    }

    public void getFyp(int page) {
        isLoading = true;
        RetrofitService retrofitService = new RetrofitService("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGVvMTIzNDVAZ21haWwuY29tIiwiaWF0IjoxNzAyMzg3MjE3LCJleHAiOjE3MDI0NzM2MTd9.XEws5QQWSt2ymLnJcFm11fKz_VSFb3oYRNagI9Q8aMU");
        PostApi postApi = retrofitService.getRetrofit().create(PostApi.class);

        Call<List<Post>> call = postApi.getForYouPage(page);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d("erreur", "1");
                if (response.isSuccessful()) {
                    List<Post> newPosts = response.body();
                    if (newPosts != null ) {
                        displayPosts(newPosts);
                    } else {
                        Log.e("API Call", "Empty list of posts");
                    }
                } else {
                    Log.e("API Call", "Failed to get posts. HTTP status code: " + response.code());
                    Log.e("API Call",
                            "Error body: " + response.errorBody().toString());
                }
                isLoading = false;
            }


            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("Erreur", "Failed to get posts: " + t.getMessage(), t);
                Toast.makeText(Accueil_fypActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                isLoading = false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bloc_fragment_accueil, fragment)
                .addToBackStack(null)
                .commit();
    }
}
