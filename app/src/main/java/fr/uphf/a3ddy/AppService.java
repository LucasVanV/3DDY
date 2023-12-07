package fr.uphf.a3ddy;

import android.app.Application;
import fr.uphf.a3ddy.model.UtilisateurSecurity;

/**
 * A custom Application class used to manage global application state.
 * This class extends Android's Application class and is used to hold
 * application-wide resources and data, in this case, an instance of UtilisateurSecurity.
 */
public class AppService extends Application {

    // A variable to store an instance of UtilisateurSecurity.
    private UtilisateurSecurity utilisateurSecurity;

    /**
     * Sets the current UtilisateurSecurity instance.
     * This method is used to update the UtilisateurSecurity instance
     * that is stored globally in the application.
     *
     * @param utilisateurSecurity The UtilisateurSecurity instance to be set.
     */
    public void setUtilisateurSecurity(UtilisateurSecurity utilisateurSecurity) {
        this.utilisateurSecurity = utilisateurSecurity;
    }

    /**
     * Gets the current UtilisateurSecurity instance.
     * This method is used to retrieve the UtilisateurSecurity instance
     * that is stored globally in the application.
     *
     * @return The current UtilisateurSecurity instance.
     */
    public UtilisateurSecurity getUtilisateurSecurity() {
        return utilisateurSecurity;
    }

}
