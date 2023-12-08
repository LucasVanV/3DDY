package fr.uphf.a3ddy.model.posts;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import fr.uphf.a3ddy.model.Tag;
import fr.uphf.a3ddy.model.Utilisateur;

public class Post {

    @SerializedName("id")
    private Long id;

    @SerializedName("datePost")
    private String datePost;

    @SerializedName("image")
    private String image;

    @SerializedName("titre")
    private String titre;

    @SerializedName("description")
    private String description;

    @SerializedName("commentaires")
    private String commentaires;

    @SerializedName("nbTelechargement")
    private String nbTelechargement;

    @SerializedName("utilisateurPost")
    private Utilisateur utilisateurPost;

    @SerializedName("tagsReferences")
    private Set<Tag> tagsReferences;

    public Post(Long id, String image, String titre, String description, String commentaires, String nbTelechargement, Utilisateur utilisateurPost, Set<Tag> tagsReferences) {
        this.id = id;
        this.image = image;
        this.titre = titre;
        this.description = description;
        this.commentaires = commentaires;
        this.nbTelechargement = nbTelechargement;
        this.utilisateurPost = utilisateurPost;
        this.tagsReferences = tagsReferences;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public String getNbTelechargement() {
        return nbTelechargement;
    }

    public void setNbTelechargement(String nbTelechargement) {
        this.nbTelechargement = nbTelechargement;
    }

    public Utilisateur getUtilisateurPost() {
        return utilisateurPost;
    }

    public void setUtilisateurPost(Utilisateur utilisateurPost) {
        this.utilisateurPost = utilisateurPost;
    }

    public Set<Tag> getTagsReferences() {
        return tagsReferences;
    }

    public void setTagsReferences(Set<Tag> tagsReferences) {
        this.tagsReferences = tagsReferences;
    }
}
