package fr.uphf.a3ddy.model;


import android.net.Uri;

import com.google.gson.annotations.Expose;
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
    @SerializedName("dossierServer")
    private String dossierServer;
    @SerializedName("bio")
    private String bio;
    //private Set<Tag> tagsPrefere; // Assurez-vous que la classe 'Tag' soit également adaptée

    @SerializedName("Tags")
    private Set<Tag> tags;

    //@Expose permet d'exclure les attributs transitoires lors de la sérialisation
    @Expose(serialize = false, deserialize = false)
    private int nbPublication;

    @Expose(serialize = false, deserialize = false)
    private int nbAbonne;

    @Expose(serialize = false, deserialize = false)
    private int nbSuivis;

    // Constructeur par défaut
    public Utilisateur() {
    }

    public Utilisateur(String pseudo, String dossierServer , String bio) {
        this.pseudo = pseudo;
        this.dossierServer = dossierServer;
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
        return dossierServer;
    }

    public void setDossierServer(String dossier_server) {
        this.dossierServer = dossier_server;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public int getNbPublication() {
        return nbPublication;
    }

    public void setNbPublication(int nbPublication) {
        this.nbPublication = nbPublication;
    }

    public int getNbAbonne() {
        return nbAbonne;
    }

    public void setNbAbonne(int nbAbonne) {
        this.nbAbonne = nbAbonne;
    }

    public int getNbSuivis() {
        return nbSuivis;
    }

    public void setNbSuivis(int nbSuivis) {
        this.nbSuivis = nbSuivis;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", pseudo='" + pseudo + "'" +
                ", bio='" + bio + "'" +
                ", dossier_server='" + dossierServer + "'" +
                '}';
    }
}
