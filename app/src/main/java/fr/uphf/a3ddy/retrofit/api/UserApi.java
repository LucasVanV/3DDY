package fr.uphf.a3ddy.retrofit.api;

import fr.uphf.a3ddy.model.Utilisateur;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

    @GET("/connexion")
    Call<Utilisateur> connexion();

    @POST("/inscription")
    Call<Utilisateur> inscription(@Body Utilisateur utilisateur);

}
