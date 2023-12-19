package fr.uphf.a3ddy.controller.fragment.posts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.Model3dFragment;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.PostApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO code à intégrer sur la page de consultation d'un post
public class FragmentPostsTemporaire extends Fragment {

    View view;
    Context context;
    Button updatePostButton;
    Button deletePostButton;
    Button showModel;

    private void iniUI() {
        updatePostButton = view.findViewById(R.id.button_update_post);
        deletePostButton = view.findViewById(R.id.button_delete_post);
        showModel = view.findViewById(R.id.button_show_model);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_posts_temporaire, container, false);
        iniUI();

        context = getContext();

        updatePostButton.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putLong("postId",1103L);
            loadFragment(new FragmentPoster(), bundle);
        });
        deletePostButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deletePost(1103L);
                    }
                }
        );
        showModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Model3dFragment.class);
                startActivity(intent);
            }
        });


        return view;
    }


    public void deletePost(Long id) {
        RetrofitService retrofitService = new RetrofitService("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGVvb29vb3YxMjNAZ21haWwuY29tIiwiaWF0IjoxNzAyODk3MDU3LCJleHAiOjE3MDI5ODM0NTd9.SUOxE3GDVwZZBvtDpmlB-woOqQ79CeV8hkfPYXmCmvs");
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
                    // rediriger vers la page d'accueil
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


    public void loadFragment(Fragment fragment, Bundle bundle) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Masquer le fragment actuel s'il y en a un
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        // Remplacer le fragment ou l'ajouter s'il n'y en a pas
        if (getChildFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName()) == null) {
            fragment.setArguments(bundle); // Passer le bundle au fragment
            transaction.add(R.id.accueil, fragment, fragment.getClass().getSimpleName());
        } else {
            transaction.show(fragment);
        }

        transaction.addToBackStack(null);
        transaction.commit();
    }
}
