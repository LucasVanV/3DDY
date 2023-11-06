package fr.uphf.a3ddy.model;

import com.google.gson.annotations.SerializedName;

public class UtilisateurSecurity {

    @SerializedName("id")
    private Long id;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("isAdmin")
    private boolean isAdmin;
    @SerializedName("utilisateur")
    private Utilisateur utilisateur;

    // Constructeur par défaut
    public UtilisateurSecurity(String emailText, String mdpText) {}

    // Constructeur paramétrique
    public UtilisateurSecurity(String email, String password, boolean isAdmin, Utilisateur utilisateur) {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.utilisateur = utilisateur;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
