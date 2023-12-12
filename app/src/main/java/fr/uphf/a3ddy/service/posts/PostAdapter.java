package fr.uphf.a3ddy.service.posts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.posts.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts = new ArrayList<>();

    public PostAdapter() {
        // Constructeur vide
    }

    public void addPosts(List<Post> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    public void setPosts(List<Post> newPosts) {
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
        Post post = posts.get(position);

        Glide.with(holder.itemView.getContext())
                .load(post.getUtilisateurPost().getPhotoProfil())
                .placeholder(R.drawable.default_user)
                .into(holder.userImage);

        Glide.with(holder.itemView.getContext())
                .load(post.getImage())
                .placeholder(R.drawable.default_post)
                .into(holder.postImage);

        holder.userName.setText(post.getUtilisateurPost().getPseudo());
        holder.date.setText(post.getDatePost());
        holder.title.setText(post.getTitre());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage, postImage;
        TextView userName, date, title;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image2);
            postImage = itemView.findViewById(R.id.post_image2);
            userName = itemView.findViewById(R.id.user2);
            date = itemView.findViewById(R.id.textView);
            title = itemView.findViewById(R.id.textView2);
        }
    }
}
