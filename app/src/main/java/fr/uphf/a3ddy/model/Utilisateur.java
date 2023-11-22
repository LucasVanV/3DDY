package fr.uphf.a3ddy.model;


import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;
import java.util.Set;

import retrofit2.Response;

public class Utilisateur {

    @SerializedName("id")
    private Long id;
    @SerializedName("pseudo")
    private String pseudo;
    @SerializedName("photoProfil")
    private File photoProfil;
    @SerializedName("bio")
    private String bio;
    //private Set<Tag> tagsPrefere; // Assurez-vous que la classe 'Tag' soit également adaptée


    // Constructeur par défaut
    public Utilisateur() {
    }

    public Utilisateur(String pseudo, File photoProfil, String bio) {
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

    public File getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(File photoProfil) {
        this.photoProfil = photoProfil;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
