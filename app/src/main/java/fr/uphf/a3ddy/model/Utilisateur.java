package fr.uphf.a3ddy.model;


import java.io.Serializable;
import java.util.Set;

import retrofit2.Response;

public class Utilisateur implements Serializable {

    private Long id;
    private String pseudo;
    private String email;
    private String password;
    private String photoProfil;
    private String bio;
    private boolean isAdmin;
    //private Set<Tag> tagsPrefere; // Assurez-vous que la classe 'Tag' soit également adaptée
    private int nbPublication;
    private int nbAbonne;
    private int nbSuivis;

    // Constructeur par défaut
    public Utilisateur() {
    }

    // Constructeur avec paramètres
    public Utilisateur(long id ,String pseudo, String email, String password, String photoProfil, String bio) {
        this.id = id ;
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
        this.photoProfil = photoProfil;
        this.bio = bio;
        this.isAdmin = false;
        //this.tagsPrefere = tagsPrefere;
        this.nbPublication = 0;
        this.nbAbonne = 0;
        this.nbSuivis = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    //public Set<Tag> getTagsPrefere() {
    //    return tagsPrefere;
    //}

    //public void setTagsPrefere(Set<Tag> tagsPrefere) {
    //    this.tagsPrefere = tagsPrefere;
    //}

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
}