package fr.uphf.a3ddy.controller;

import com.google.gson.annotations.SerializedName;

import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;

public class InscriptionRequest {
    @SerializedName("utilisateur")
    private Utilisateur utilisateur;
    @SerializedName("utilisateurSecurity")
    private UtilisateurSecurity utilisateurSecurity;

    public InscriptionRequest(Utilisateur utilisateur, UtilisateurSecurity utilisateurSecurity) {
        this.utilisateur = utilisateur;
        this.utilisateurSecurity = utilisateurSecurity;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public UtilisateurSecurity getUtilisateurSecurity() {
        return utilisateurSecurity;
    }

    public void setUtilisateurSecurity(UtilisateurSecurity utilisateurSecurity) {
        this.utilisateurSecurity = utilisateurSecurity;
    }
}
