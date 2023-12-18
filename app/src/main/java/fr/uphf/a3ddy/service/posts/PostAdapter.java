package fr.uphf.a3ddy.service.posts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.Accueil_fypActivity;
import fr.uphf.a3ddy.controller.fragment.monCompte.FragmentProfil;
import fr.uphf.a3ddy.model.posts.Page;
import fr.uphf.a3ddy.model.posts.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Page posts = new Page();
    private Accueil_fypActivity mActivity;  // Ajoutez cette ligne

    public PostAdapter(Accueil_fypActivity activity) {
        mActivity = activity;  // Modifiez cette ligne
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

        String baseUrl = "http://192.168.56.1:8080/"; // Remplacez cela par la base de l'URL du serveur

        String imageUrl = baseUrl + post.getImage();
        String imageProfilUrl = baseUrl + "images/uploads/" + post.getUtilisateurPost().getDossierServer() +
                "/profilPicture/PhotoProfil.jpg";

        Log.d("utilisateur", post.getUtilisateurPost().toString());
        //Log.d("url image", imageUrl);

        Glide.with(holder.itemView.getContext())
                .load(imageProfilUrl)
                .placeholder(R.drawable.default_user)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.userImage);

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.default_post)
                .into(holder.postImage);

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
