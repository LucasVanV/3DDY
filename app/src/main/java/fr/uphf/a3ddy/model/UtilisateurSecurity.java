package fr.uphf.a3ddy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UtilisateurSecurity implements Serializable {

    @SerializedName("id")
    private Long id;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("utilisateur")
    private Utilisateur utilisateur;
    @SerializedName("acces_token")
    private String token;

    // Constructeur par default
    public UtilisateurSecurity() {}

    // Constructeur par parametrique 1
    public UtilisateurSecurity(String emailText, String mdpText) {}

    // Constructeur paramétrique 2
    public UtilisateurSecurity(String email, String password, Utilisateur utilisateur) {
        this.email = email;
        this.password = password;
        this.utilisateur = utilisateur;
        this.token="";
    }

    public Long getId() {
        return id;
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

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UtilisateurSecurity{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", token=" + token +
                ", utilisateur=" + utilisateur +
                '}';
    }

}
