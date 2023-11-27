package fr.uphf.a3ddy.model.posts;

import com.google.gson.annotations.SerializedName;

public class PostRequest {
    @SerializedName("post")
    private Post post;

    @SerializedName("modele3D")
    private Modele3D modele3D;

    public PostRequest(Post post, Modele3D modele3D) {
        this.post = post;
        this.modele3D = modele3D;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Modele3D getModele3D() {
        return modele3D;
    }

    public void setModele3D(Modele3D modele3D) {
        this.modele3D = modele3D;
    }
}
