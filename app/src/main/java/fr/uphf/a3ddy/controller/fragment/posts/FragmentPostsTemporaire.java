package fr.uphf.a3ddy.controller.fragment.posts;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.fragment.FragmentAccueilFyp;
import fr.uphf.a3ddy.model.posts.PostRequest;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.PostApi;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO code à intégrer sur la page de consultation d'un post
public class FragmentPostsTemporaire extends Fragment {

    View view;
    Context context;
    Button updatePostButton;
    Button deletePostButton;

    private void iniUI() {
        updatePostButton = view.findViewById(R.id.button_update_post);
        deletePostButton = view.findViewById(R.id.button_delete_post);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_posts_temporaire, container, false);
        iniUI();

        deletePostButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deletePost(252L);
                    }
                }
        );

        return view;
    }


    public void deletePost(Long id) {
        RetrofitService retrofitService = new RetrofitService("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGVvdmVzcXVlMUBnbWFpbC5jb20iLCJpYXQiOjE3MDEyNDY5NTIsImV4cCI6MTcwMTMzMzM1Mn0.5pnQKrwrKfBGuc_Ll3kfxKbZRO-uwDlpUF7wTfRElK8");
        PostApi postApi = retrofitService.getRetrofit().create(PostApi.class);


        Call<Long> call = postApi.deletePost(id);

        requestDeletePost(call);
    }

    public void requestDeletePost(Call call){
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    // Le post a été supprimé avec succès
                    // Vous pouvez gérer la suite ici (par exemple, actualiser l'interface utilisateur)
                } else {
                    // Gérer les erreurs de la requête
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                // Gérer les erreurs de connexion
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
            transaction.add(R.id.accueil, fragment, fragment.getClass().getSimpleName());
        } else {
            transaction.show(fragment);
        }

        transaction.addToBackStack(null);
        transaction.commit();
    }
}
