package fr.uphf.a3ddy.service;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * A service class to handle encrypted shared preferences in an Android application.
 * This class uses EncryptedSharedPreferences for storing sensitive information such as
 * authentication tokens in a secure manner.
 */
public class EncryptedPreferencesService {

    // Constants for the encrypted preferences file name and the key for the auth token.
    private static final String FILE_NAME = "encrypted_prefs";
    private static final String AUTH_TOKEN_KEY = "authToken";

    // Instance of SharedPreferences to store and retrieve data.
    private SharedPreferences sharedPreferences;

    /**
     * Constructor that initializes the EncryptedSharedPreferences instance.
     *
     * @param context The context from which the preferences are being accessed.
     */
    public EncryptedPreferencesService(Context context) {
        try {
            // Creating a MasterKey for encryption, using AES256_GCM for the key scheme.
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Initializing EncryptedSharedPreferences with AES256 encryption schemes for keys and values.
            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    FILE_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            // Exception handling for errors during encrypted preferences setup.
            e.printStackTrace();
        }
    }

    /**
     * Saves the authentication token in the encrypted shared preferences.
     *
     * @param token The authentication token to be saved.
     */
    public void saveAuthToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_TOKEN_KEY, token);
        editor.apply();
    }

    /**
     * Retrieves the authentication token from the encrypted shared preferences.
     *
     * @return The stored authentication token, or null if it doesn't exist.
     */
    public String getAuthToken() {
        return sharedPreferences.getString(AUTH_TOKEN_KEY, null);
    }
}
