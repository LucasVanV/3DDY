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
    @SerializedName("dossier_server")
    private String dossier_server;

    @SerializedName("bio")
    private String bio;
    //private Set<Tag> tagsPrefere; // Assurez-vous que la classe 'Tag' soit également adaptée

    @SerializedName("Tags")
    private Set<Tag> tags;

    // Constructeur par défaut
    public Utilisateur() {
    }

    public Utilisateur(String pseudo, String dossier_server , String bio) {
        this.pseudo = pseudo;
        this.dossier_server = dossier_server;

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

    public String getDossierServer() {
        return dossier_server;
    }

    public void setDossierServer(String dossier_server) {
        this.dossier_server = dossier_server;

    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


    public String getDossier_server() {
        return dossier_server;
    }

    public void setDossier_server(String dossier_server) {
        this.dossier_server = dossier_server;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", pseudo='" + pseudo + "'" +
                ", bio='" + bio + "'" +
                ", dossier_server='" + dossier_server + "'" +
                '}';
    }
}
