package fr.uphf.a3ddy.service.posts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.Accueil_fypActivity;
import fr.uphf.a3ddy.controller.fragment.posts.FragmentPostDetail;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.model.posts.Page;
import fr.uphf.a3ddy.model.posts.Post;
import fr.uphf.a3ddy.service.AppService;

public class PostAdapterProfilUser extends RecyclerView.Adapter<PostAdapterProfilUser.PostViewHolder> {
    private Page posts = new Page();
    private Accueil_fypActivity mActivity;

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
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visualisation_list_post_profil, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visualisation_post, parent, false);
            // If viewType is not equal to 0, set up button_see_more
            setUpSeeMoreButton(view);
        }
        return new PostViewHolder(view);
    }

    private void setUpSeeMoreButton(View view) {
        ImageButton button_see_more = view.findViewById(R.id.button_see_more);
        if (button_see_more != null) {
            // Set up your button_see_more here
            button_see_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle button_see_more click
                    int position = (int) view.getTag();  // Get the position from the tag
                    showPopupMenu(view, position);
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public void showPopupMenu(View anchorView, int position) {
        if (mActivity != null) {
            mActivity.onSeeMoreClick(anchorView, position);
        } else {
            Log.d("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "a");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        // Set click listener for visible buttons
        if (holder.button_see_more != null) {
            setUpSeeMoreButton(holder.itemView);
            holder.itemView.setTag(position);  // Set the position as a tag


            holder.button_see_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle button_see_more click
                    int clickedPosition = (int) view.getTag();  // Get the position from the tag
                    showPopupMenu(view, clickedPosition);
                }
            });
        }


        if (position < posts.getPostList().size()) {
            Post post = posts.getPostList().get(position);

            //Si l'utilisateur a 1 seul post :
            if (posts.getPostList().get(0).getUtilisateurPost().getNbPublication() < 2) {
                if (holder.postImage3 != null) {
                    //On cache dans le xml l'emplacement du 2eme post
                    holder.postImage3.setVisibility(View.GONE);
                }
            }

            if (post != null && post.getImage() != null) {
                String baseUrl = "http://192.168.56.1:8080/";
                String imageUrl = baseUrl + post.getImage();

                int firstImagePosition = position * 2;
                int secondImagePosition = position * 2 + 1;

                if (firstImagePosition < posts.getPostList().size()) {
                    Post firstPost = posts.getPostList().get(firstImagePosition);
                    String firstImageUrl = baseUrl + firstPost.getImage();
                    Glide.with(holder.itemView.getContext())
                            .load(firstImageUrl)
                            .placeholder(R.drawable.default_post)
                            .into(holder.postImage2);

                    holder.postImage2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setBundleArgs(firstPost);
                        }
                    });
                } else {
                    if (holder.postImage2 != null) {
                        holder.postImage2.setVisibility(View.GONE);
                    }
                    if (holder.postImage3 != null) {
                        holder.postImage3.setVisibility(View.GONE);
                    }
                }

                // Set up the click listener for postImage3 if present
                if (holder.postImage3 != null) {
                    if (secondImagePosition < posts.getPostList().size()) {
                        Post secondPost = posts.getPostList().get(secondImagePosition);
                        String secondImageUrl = baseUrl + secondPost.getImage();
                        Glide.with(holder.itemView.getContext())
                                .load(secondImageUrl)
                                .placeholder(R.drawable.default_post)
                                .into(holder.postImage3);

                        holder.postImage3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setBundleArgs(secondPost);
                            }
                        });
                    }
                } else {
                    if (holder.postImage3 != null) {
                        holder.postImage3.setVisibility(View.GONE);
                    }
                }
            } else {
                holder.postImage2.setVisibility(View.GONE);
                if (holder.postImage3 != null) {
                    holder.postImage3.setVisibility(View.GONE);
                }
            }
        } else {
            holder.postImage2.setVisibility(View.GONE);
            if (holder.postImage3 != null) {
                holder.postImage3.setVisibility(View.GONE);
            }
        }
    }

    private void setBundleArgs(Post post) {
        Bundle arguments = new Bundle();
        arguments.putString("postId", String.valueOf(post.getId()));
        arguments.putString("datePost", post.getDatePost());
        arguments.putString("descriptionPost", post.getDescription());
        arguments.putString("imagePost", post.getImage());
        arguments.putString("nbTelechargementPost", post.getNbTelechargement());
        arguments.putString("titrePost", post.getTitre());
        arguments.putString("pseudoUtilisateurPost", post.getUtilisateurPost().getPseudo());
        arguments.putString("ppUtilisateurPost", post.getUtilisateurPost().getDossierServer());

        //AppService allow to get the connected user
        AppService appService = (AppService) mActivity.getApplication();
        UtilisateurSecurity utilisateurSecurity = appService.getUtilisateurSecurity();
        //If user is connected :
        if (utilisateurSecurity.getUtilisateur() != null) {
            //If connected user == user post :
            if (post.getUtilisateurPost().getDossierServer().equals(utilisateurSecurity.getUtilisateur().getDossierServer())) {
                //We set the bundle == true
                arguments.putString("postButtonSeeMore", "true");
            } else { //We set the bundle == false
                arguments.putString("postButtonSeeMore", "false");

            }
        } else {
            Log.d("erreur dans l'affichage du post", "utilisateur pas connecté");
        }
        mActivity.loadFragmentWithBundle(new FragmentPostDetail(), arguments);
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
        ImageView postImage2, postImage3;
        ImageButton button_see_more;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage2 = itemView.findViewById(R.id.post_image2);
            postImage3 = itemView.findViewById(R.id.post_image3);
            button_see_more = itemView.findViewById(R.id.button_see_more);

            // Check if postImage3 is present in the layout
            if (postImage3 == null) {
                Log.e("PostAdapter", "post_image3 is null in the layout");
            }
        }
    }
}
