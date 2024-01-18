package fr.uphf.a3ddy.service.posts;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;

import org.jetbrains.annotations.NotNull;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.Accueil_fypActivity;
import fr.uphf.a3ddy.controller.fragment.monCompte.FragmentProfil;
import fr.uphf.a3ddy.model.posts.Page;
import fr.uphf.a3ddy.model.posts.Post;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Page posts = new Page();
    private Accueil_fypActivity mActivity;

    public PostAdapter(Accueil_fypActivity activity) {
        mActivity = activity;
    }

    public void addPosts(Page newPosts) {
        if (this.posts == null) {
            this.posts = new Page();
        }

        if (newPosts != null && newPosts.getPostList() != null) {
            this.posts.getPostList().addAll(newPosts.getPostList());
            notifyDataSetChanged();
        }
    }


    public void setPosts(Page newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visualisation_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.getPostList().get(position);

        String baseUrl = "http://192.168.56.1:8080/";

        String imageUrl = baseUrl + post.getImage();
        String imageProfilUrl = baseUrl + "images/uploads/" + post.getUtilisateurPost().getDossierServer() +
                "/profilPicture/PhotoProfil.jpg";

        Log.d("utilisateur", post.getUtilisateurPost().toString());
        Log.d("url image", imageUrl);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String authToken = new EncryptedPreferencesService(mActivity).getAuthToken();
                Log.d("AuthToken", authToken);

                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + authToken)
                        .build();
                return chain.proceed(newRequest);
            }
        });

        OkHttpClient okHttpClient = clientBuilder.build();

        try {
            Log.d("token", new EncryptedPreferencesService(mActivity).getAuthToken());
            Glide.with(holder.itemView.getContext())
                    .load(imageProfilUrl)
                    .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.default_user))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("Glide", "Error loading imageProfilUrl", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.userImage);


            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.default_post)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("Glide", "Error loading imageUrl", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.postImage);
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }

        holder.userName.setText(post.getUtilisateurPost().getPseudo());
        holder.date.setText(Post.formatLocalDateTime(LocalDateTime.parse(post.getDatePost())));
        holder.title.setText(post.getDescription());

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActivity != null) {
                    setBundleArgs(post);
                }
            }
        });

        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActivity != null) {
                    setBundleArgs(post);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        if (posts != null && posts.getPostList() != null) {
            return posts.getPostList().size();
        } else {
            return 0;
        }
    }

    private void setBundleArgs(Post post) {
        Bundle arguments = new Bundle();
        //envoyer les infos de l'utilisateur du post sur le fragment profil
        arguments.putString("userId", String.valueOf(post.getUtilisateurPost().getId()));
        arguments.putString("userPseudo", post.getUtilisateurPost().getPseudo());
        arguments.putString("userBio", post.getUtilisateurPost().getBio());
        arguments.putString("userPP", post.getUtilisateurPost().getDossierServer());
        arguments.putString("userNbPublication", String.valueOf(post.getUtilisateurPost().getNbPublication()));
        arguments.putString("userNbAbonnes", String.valueOf(post.getUtilisateurPost().getNbAbonne()));
        arguments.putString("userNbAbonnement",
                String.valueOf(post.getUtilisateurPost().getNbSuivis()));
        mActivity.loadFragmentWithBundle(new FragmentProfil(), arguments);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage, postImage;
        TextView userName, date, title;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image2);
            postImage = itemView.findViewById(R.id.post_image2);
            userName = itemView.findViewById(R.id.user2);
            date = itemView.findViewById(R.id.textView_date);
            title = itemView.findViewById(R.id.textView2);
        }
    }
}
