package fr.uphf.a3ddy.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Set;

import retrofit2.Response;

public class Utilisateur {

    @SerializedName("id")
    private Long id;
    @SerializedName("pseudo")
    private String pseudo;
    @SerializedName("photoProfil")
    private String photoProfil;
    @SerializedName("bio")
    private String bio;
    //private Set<Tag> tagsPrefere; // Assurez-vous que la classe 'Tag' soit également adaptée


    // Constructeur par défaut
    public Utilisateur() {
    }

    public Utilisateur(String pseudo, String photoProfil, String bio) {
        this.pseudo = pseudo;
        this.photoProfil = photoProfil;
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
