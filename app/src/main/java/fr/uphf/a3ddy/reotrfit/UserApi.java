package fr.uphf.a3ddy.reotrfit;

import fr.uphf.a3ddy.model.Utilisateur;
import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApi {

    @GET("/connexion")
    Call<Utilisateur> connexion();

}
