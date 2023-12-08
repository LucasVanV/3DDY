package fr.uphf.a3ddy.service;


import android.app.Activity;
import android.app.Application;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fr.uphf.a3ddy.R;


public class LoadFragmentService {

    private Fragment currentFragment;

    public LoadFragmentService(Fragment currentFragment) {
        this.currentFragment = currentFragment;

    }

    /**
     * Remplace un fragment existant par un nouveau fragment dans une activité.
     *
     * @param newFragment     Nouveau fragment à afficher.
     * @param containerId     ID du conteneur de fragment dans le layout de l'activité.
     */
    public void loadFragment(
            Fragment newFragment,
            int containerId
    ) {
        FragmentManager fragmentManager = currentFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Cache le fragment actuel s'il est présent
        if (this.currentFragment != null) {
            transaction.hide(this.currentFragment);
        }

        // Ajoute le nouveau fragment ou le montre s'il est déjà ajouté
        String tag = newFragment.getClass().getSimpleName();
        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);
        if (existingFragment == null) {
            transaction.add(containerId, newFragment, tag);
        } else {
            transaction.show(existingFragment);
        }

        transaction.addToBackStack(null); // Optionnel, pour ajouter la transaction à la pile arrière
        transaction.commit();
    }
}
