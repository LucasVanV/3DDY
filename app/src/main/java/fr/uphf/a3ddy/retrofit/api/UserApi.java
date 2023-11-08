package fr.uphf.a3ddy.retrofit.api;

import fr.uphf.a3ddy.controller.InscriptionRequest;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public interface UserApi {

    @GET("/connexion")
    Call<Utilisateur> connexion();

    @FormUrlEncoded
    @POST("/api/utilisateur/inscription")
    Call<UtilisateurSecurity> inscription(
            @Field("email") String nom,
            @Field("password") String password,
            @Field("isAdmin") boolean idAdmin
    );

    @Multipart
    @POST("/api/utilisateur/creationProfil")
    Call<Utilisateur> creationProfil(
            @Part("pseudo") RequestBody pseudo,
            @Part("bio") RequestBody bio,
            @Part MultipartBody.Part photoProfil
    );

}
