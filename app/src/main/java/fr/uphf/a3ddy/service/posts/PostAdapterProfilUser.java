package fr.uphf.a3ddy.service.posts;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.Accueil_fypActivity;
import fr.uphf.a3ddy.controller.fragment.monCompte.FragmentProfil;
import fr.uphf.a3ddy.controller.fragment.posts.FragmentPostDetail;
import fr.uphf.a3ddy.model.posts.Page;
import fr.uphf.a3ddy.model.posts.Post;

public class PostAdapterProfilUser extends RecyclerView.Adapter<PostAdapterProfilUser.PostViewHolder> {
    private Page posts = new Page();
    private Accueil_fypActivity mActivity;  // Ajoutez cette ligne

    public PostAdapterProfilUser(Fragment fragment) {
        if (fragment.getActivity() instanceof Accueil_fypActivity) {
            mActivity = (Accueil_fypActivity) fragment.getActivity();
        } else {
            throw new IllegalArgumentException("Fragment must be attached to an instance of Accueil_fypActivity");
        }
    }




    public PostAdapterProfilUser() {
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
        Log.d("setPosts", "Posts set: " + newPosts.getPostList().size() + " posts");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visualisation_list_post_profil, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Log.d("onBindViewHolder", "Position: " + position);

        // Vérifier si la position est valide
        if (position < posts.getPostList().size()) {
            Post post = posts.getPostList().get(position);
            Log.d("aaaaaaaaaaaaaaaaa", post.toString());

            // Vérifier si l'objet Post et sa propriété image ne sont pas null
            if (post != null && post.getImage() != null) {
                String baseUrl = "http://192.168.56.1:8080/"; // Remplacez cela par la base de l'URL du serveur
                String imageUrl = baseUrl + post.getImage();

                int firstImagePosition = position * 2;
                int secondImagePosition = position * 2 + 1;

                // Assurez-vous que la position est valide pour le premier élément
                if (firstImagePosition < posts.getPostList().size()) {
                    Post firstPost = posts.getPostList().get(firstImagePosition);
                    String firstImageUrl = baseUrl + firstPost.getImage();
                    Glide.with(holder.itemView.getContext())
                            .load(firstImageUrl)
                            .placeholder(R.drawable.default_post)
                            .into(holder.postImage2);

                    //Permet d'ouvrir un post pour le consulter en détail
                    holder.postImage2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("post", firstPost.getId().toString());
                            //on envoie les données dans le loadFragment()
                            setBundleArgs(firstPost);
                        }
                    });
                } else {
                    // Si la position est invalide, cachez l'ImageView correspondant
                    holder.postImage2.setVisibility(View.GONE);
                    holder.postImage3.setVisibility(View.GONE);
                }

                // Assurez-vous que la position est valide pour le deuxième élément
                if (secondImagePosition < posts.getPostList().size()) {
                    Post secondPost = posts.getPostList().get(secondImagePosition);
                    String secondImageUrl = baseUrl + secondPost.getImage();
                    Glide.with(holder.itemView.getContext())
                            .load(secondImageUrl)
                            .placeholder(R.drawable.default_post)
                            .into(holder.postImage3);

                    //Permet d'ouvrir un post pour le consulter en détail
                    holder.postImage3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("post", secondPost.getId().toString());
                            //on envoie les données dans le loadFragment()
                            setBundleArgs(secondPost);
                        }
                    });
                } else {
                    // Si la position est invalide, cachez l'ImageView correspondant
                    holder.postImage3.setVisibility(View.GONE);
                }
            } else {
                // Si l'objet Post ou sa propriété image est null, cachez les ImageView correspondants
                holder.postImage2.setVisibility(View.GONE);
                holder.postImage3.setVisibility(View.GONE);
            }
        } else {
            // Si la position est invalide, cachez les ImageView correspondants
            holder.postImage2.setVisibility(View.GONE);
            holder.postImage3.setVisibility(View.GONE);
        }
    }


    private void setBundleArgs(Post post) {
        Bundle arguments = new Bundle();
        //envoyer les infos de l'utilisateur du post sur le fragment profil
        arguments.putString("postId", String.valueOf(post.getId()));
        arguments.putString("datePost", post.getDatePost());
        arguments.putString("descriptionPost", post.getDescription());
        arguments.putString("imagePost", post.getImage());
        arguments.putString("nbTelechargementPost", post.getNbTelechargement());
        arguments.putString("titrePost", post.getTitre());
        arguments.putString("pseudoUtilisateurPost", post.getUtilisateurPost().getPseudo());
        arguments.putString("ppUtilisateurPost", post.getUtilisateurPost().getDossierServer());

        mActivity.loadFragmentWithBundle(new FragmentPostDetail(), arguments);
    }


    @Override
    public int getItemCount() {
        if (posts != null && posts.getPostList() != null) {
            int itemCount = posts.getPostList().size();
            Log.d("ItemCount", "Count: " + itemCount);
            return itemCount;
        } else {
            return 0;
        }
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage2, postImage3;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage2 = itemView.findViewById(R.id.post_image2);
            postImage3 = itemView.findViewById(R.id.post_image3);
        }
    }
}
