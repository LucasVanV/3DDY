package fr.uphf.a3ddy.service.posts;

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
import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.posts.Page;
import fr.uphf.a3ddy.model.posts.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Page posts = new Page();

    public PostAdapter() {
        // Constructeur vide
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

        String baseUrl = "http://192.168.56.1:8080/"; // Remplacez cela par la base de l'URL de votre serveur
        String imageUrl = baseUrl + post.getImage();

        Log.d("url image", imageUrl);

        Glide.with(holder.itemView.getContext())
                .load(post.getUtilisateurPost().getPhotoProfil())
                .placeholder(R.drawable.default_user)
                .into(holder.userImage);

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.default_post)
                .into(holder.postImage);

        holder.userName.setText(post.getUtilisateurPost().getPseudo());
        holder.date.setText(Post.formatLocalDateTime(LocalDateTime.parse(post.getDatePost())));
        holder.title.setText(post.getTitre());
    }

    @Override
    public int getItemCount() {
        if (posts != null && posts.getPostList() != null) {
            return posts.getPostList().size();
        } else {
            return 0;
        }
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
