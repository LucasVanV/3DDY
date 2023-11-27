package fr.uphf.a3ddy.model.posts;

import com.google.gson.annotations.SerializedName;

public class Modele3D {

    @SerializedName("id")
    private Long id;

    @SerializedName("modele")
    private String modele;

    @SerializedName("post")
    private Post post;

    public Modele3D(Long id, String modele, Post post) {
        this.id = id;
        this.modele = modele;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
