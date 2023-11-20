package fr.uphf.a3ddy.model;

import com.google.gson.annotations.SerializedName;

public class ModifRequest {
    @SerializedName("utilisateurSecurity")
    UtilisateurSecurity utilisateurSecurity;
    @SerializedName("newUtilisateurSecurity")
    UtilisateurSecurity newUtilisateurSecurity;

    public ModifRequest(UtilisateurSecurity utilisateurSecurity, UtilisateurSecurity newUtilisateurSecurity) {
        this.utilisateurSecurity = utilisateurSecurity;
        this.newUtilisateurSecurity = newUtilisateurSecurity;
    }
}
