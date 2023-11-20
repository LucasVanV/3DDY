package fr.uphf.a3ddy.retrofit;

import com.google.gson.Gson;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;
    private String authToken; // Ajout du token comme attribut

    private void initializerRetrofit(){
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.56.1:8080/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
    public RetrofitService(){
        initializerRetrofit();
    }

    // Nouveau constructeur qui prend en compte le token
    public RetrofitService(String authToken) {
        initializerRetrofit(); // Appelle le constructeur sans token
        this.authToken = authToken;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    // Nouvelle méthode pour ajouter le token de manière conditionnelle
    public void addAuthToken(String authToken) {
        this.authToken = authToken;
    }

    // Nouvelle méthode pour obtenir le token actuel
    public String getAuthToken() {
        return authToken;
    }
}
