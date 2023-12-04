package fr.uphf.a3ddy.service.retrofit;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import fr.uphf.a3ddy.service.OkHttpClientService;
import fr.uphf.a3ddy.service.interceptor.AuthInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    // Méthode privée pour initialiser Retrofit avec un token d'authentification
    private void initializerRetrofit(String authToken)
    {
        // Création et configuration d'un client OkHttpClient
        // Ajoute l'AuthInterceptor pour gérer le token d'authentification
        OkHttpClientService okHttpClientService = new OkHttpClientService();

        // Construction de l'instance Retrofit
        // Définit l'URL de base pour les requêtes
        // Utilise le client OkHttpClient configuré ci-dessus
        // Ajoute un convertisseur Gson pour la sérialisation et la désérialisation des objets
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.56.1:8080/") // URL de base de l'API
                .client(okHttpClientService.initialyzer(authToken))                 // Ajout du client OkHttpClient
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
    public RetrofitService(String authToken)
    {
        initializerRetrofit(authToken);
    }
    public Retrofit getRetrofit()
    {
        return retrofit;
    }
}
