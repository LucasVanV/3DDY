package fr.uphf.a3ddy.service.posts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.posts.Page;
import fr.uphf.a3ddy.model.posts.Post;

public class PostAdapterProfilUser extends RecyclerView.Adapter<PostAdapterProfilUser.PostViewHolder> {
    private Page posts = new Page();

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
