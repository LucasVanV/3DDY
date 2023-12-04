package fr.uphf.a3ddy;

import android.app.Application;

import fr.uphf.a3ddy.model.UtilisateurSecurity;

public class AppService extends Application {

    private UtilisateurSecurity utilisateurSecurity;

    // Méthode pour mettre à jour UtilisateurSecurity
    public void setUtilisateurSecurity(UtilisateurSecurity utilisateurSecurity) {
        this.utilisateurSecurity = utilisateurSecurity;
    }

    // Méthode pour obtenir UtilisateurSecurity
    public UtilisateurSecurity getUtilisateurSecurity() {
        return utilisateurSecurity;
    }

}
