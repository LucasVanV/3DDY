package fr.uphf.a3ddy.controller.fragment.posts;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.Accueil_fypActivity;
import fr.uphf.a3ddy.controller.fragment.FragmentAccueilFyp;
import fr.uphf.a3ddy.controller.fragment.monCompte.FragmentProfil;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.posts.Post;
import fr.uphf.a3ddy.model.posts.PostRequest;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.PostApi;
import fr.uphf.a3ddy.service.retrofit.api.UserApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPostDetail extends Fragment {
    View view;
    Context context;

    private TextView user2;
    private TextView textView_date;
    private TextView textView2;
    private TextView textView4;
    private ImageView post_image2;
    private ImageView user_image2;
    private ImageButton buttonSeeMore;


    private List<String> getPostBundle() {
        List<String> list = new ArrayList<>();
        try {
            Bundle arguments = getArguments();
            if (arguments != null) {
                //key == nom des arguments du loadFragment()
                List<String> keys = Arrays.asList("postId", "datePost", "descriptionPost", "imagePost",
                        "nbTelechargementPost", "titrePost", "pseudoUtilisateurPost", "ppUtilisateurPost");

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
        //On récupère les infos du post
        List<String> postBundle = getPostBundle();

        //Date
        if (postBundle.size() >= 1) {
            textView_date.setText(postBundle.get(1));

            //Description
            if (postBundle.size() >= 2) {
                textView2.setText(postBundle.get(2));

                //Image
                if (postBundle.size() >= 3) {
                    String baseUrl = "http://192.168.56.1:8080/";
                    String imageUrl = baseUrl + postBundle.get(3);
                    Glide.with(context)
                            .load(imageUrl)
                            .placeholder(R.drawable.default_post)
                            .into(post_image2);

                    //Nb téléchargements
                    if (postBundle.size() >= 4) {
                        textView4.setText(postBundle.get(4));

                        //Pseudo user
                        if (postBundle.size() >= 6) {
                            user2.setText(postBundle.get(6));

                            //PP user
                            if (postBundle.size() >= 7) {
                                String imageProfilUrl = baseUrl + "images/uploads/" + postBundle.get(7) +
                                        "/profilPicture/PhotoProfil.jpg";
                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(RequestOptions.circleCropTransform())
                                        .placeholder(R.drawable.default_user)
                                        .into(user_image2);
                            }
                        }
                    }
                }
            }
        }


    }

    private void iniUI() {
        user2 = view.findViewById(R.id.user2);
        textView_date = view.findViewById(R.id.textView_date);
        textView2 = view.findViewById(R.id.textView2);
        post_image2 = view.findViewById(R.id.post_image2);
        textView4 = view.findViewById(R.id.textView4);
        user_image2 = view.findViewById(R.id.user_image2);
        buttonSeeMore = view.findViewById(R.id.button_see_more);
        if (buttonSeeMore != null) {
            buttonSeeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Ajoutez la logique de votre menu ici
                    onSeeMoreClick(view);
                }
            });
        }
    }

    private void onSeeMoreClick(View view) {
        // Ajoutez la logique de votre menu ici
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_see_more_post, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                List<String> postBundle = getPostBundle();
                int itemId = item.getItemId();
                if (itemId == R.id.supprimer) {
                    Log.d("menu", "delete");
                    //Delete post
                    if (postBundle.size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("postId", Long.parseLong(postBundle.get(0)));
                        deletePost(Long.valueOf(postBundle.get(0)));
                    }
                    return true;
                } else if (itemId == R.id.modifier) {
                    //On récupère l'id du post à modifier
                    Log.d("menu", "update");

                    //Update post
                    if (postBundle.size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("postId", Long.parseLong(postBundle.get(0)));
                        loadFragment(new FragmentPoster(), bundle);
                    }
                    return true;
                }
                return true;
            }
        });

        popupMenu.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        view = inflater.inflate(R.layout.visualisation_post, container, false);
        iniUI(); // récupère le xml
        setArguments();

        return view;
    }


    //Fonction pour delete un post
    public void deletePost(Long id) {
        // Appel Retrofit
        RetrofitService retrofitService = new RetrofitService(new EncryptedPreferencesService(context).getAuthToken());
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
                    loadFragmentWithoutBundle(new FragmentProfil());
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

    //Méthode pour changer de fragment
    public void loadFragmentWithoutBundle(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Masquer le fragment actuel s'il y en a un
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.bloc_fragment_accueil);
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
