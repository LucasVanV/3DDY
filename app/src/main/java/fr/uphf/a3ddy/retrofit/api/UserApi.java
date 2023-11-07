package fr.uphf.a3ddy.retrofit.api;

import fr.uphf.a3ddy.controller.InscriptionRequest;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

    @GET("/connexion")
    Call<Utilisateur> connexion();

    @FormUrlEncoded
    @POST("/api/utilisateur/inscription")
    Call<UtilisateurSecurity> inscription(
            @Field("email") String nom,
            @Field("password") String password,
            @Field("isAdmin") boolean idAdmin,
            @Field("utilisateur") Utilisateur utilisateur
    );
}
