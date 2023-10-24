package fr.uphf.a3ddy.retrofit.api;

import fr.uphf.a3ddy.model.Utilisateur;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApi {

    @GET("/connexion")
    Call<Utilisateur> connexion();

}
