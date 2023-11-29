package fr.uphf.a3ddy.model;

import com.google.gson.annotations.SerializedName;

public class UtilisateurSecurity{

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

    public UtilisateurSecurity(String emailText, String mdpText, Boolean aBoolean, Utilisateur utilisateur) {
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
}
