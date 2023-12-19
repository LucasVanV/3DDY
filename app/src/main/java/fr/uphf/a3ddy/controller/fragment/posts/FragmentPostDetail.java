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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        List<String> postBundle = getPostBundle();
        if (postBundle.size() > 0) {
        }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        view = inflater.inflate(R.layout.visualisation_post, container, false);
        iniUI(); // récupère le xml
        setArguments();

        return view;
    }
}
