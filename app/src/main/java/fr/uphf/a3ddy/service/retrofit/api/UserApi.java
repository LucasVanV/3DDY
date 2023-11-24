package fr.uphf.a3ddy.service.retrofit.api;

import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public interface UserApi {


    @FormUrlEncoded
    @POST("/api/v1/auth/register")
    Call<UtilisateurSecurity> inscription(
            @Field("email") String nom,
            @Field("password") String password
    );

    @Multipart
    @POST("/api/v1/auth/buildprofil")
    Call<Utilisateur> creationProfil(
            @Part("pseudo") RequestBody pseudo,
            @Part("bio") RequestBody bio,
            @Part("tagsPrefere") RequestBody tagsPrefere,
            @Part MultipartBody.Part imageProfil
    );

    @FormUrlEncoded
    @PUT("/api/v1/modif/modificationCompte")
    Call<UtilisateurSecurity> modificationCompte(
            @Header("Authorization") String authorization,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/api/v1/auth/authenticate")
    Call<UtilisateurSecurity> connexion(
            @Field("email") String email,
            @Field("password") String password
    );
}
