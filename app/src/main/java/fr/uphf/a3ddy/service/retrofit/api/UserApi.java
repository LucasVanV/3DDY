package fr.uphf.a3ddy.service.retrofit.api;

import java.util.Set;

import fr.uphf.a3ddy.model.Tag;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import retrofit2.Call;
import retrofit2.http.DELETE;
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

    /**
     * Appel Retrofit pour l'inscription d'un utilisateur (1ere partie de l'inscription)
     *
     * @Param nom - Nom de l'utilisateur - String
     * @Param password - mot de passe de l'utilisateur - String
     */
    @FormUrlEncoded
    @POST("/api/v1/auth/register")
    Call<UtilisateurSecurity> inscription(
            @Field("email") String nom,
            @Field("password") String password
    );

    /**
     * Appel Retrofit pour l'enregistrement du profil de l'utilisateur pendant l'inscription (2ème partie de
     * l'inscription)
     *
     * @Param pseudo - Pseudo de l'utilisateur - String
     * @Param bio - Bio de l'utilisateur - String
     * @Param tahsPrefere - Tags préféres de l'utilisateur choisis parmi une liste - String
     * @Param imageProfil - Photo de profil de l'utilisateur - MultipartFile
     */
    @Multipart
    @POST("/api/v1/auth/buildprofil")
    Call<Utilisateur> creationProfil(
            @Part("pseudo") RequestBody pseudo,
            @Part("bio") RequestBody bio,
            @Part("tagsPrefere") RequestBody tagsPrefere,
            @Part MultipartBody.Part imageProfil
    );

    /**
     * Appel Retrofit pour modifier le compte d'un utilisateur
     *
     * @Param email - Email de l'utilisateur à modifier - String
     * @Param password - mot de passe de l'utilisateur - String
     */
    @FormUrlEncoded
    @PUT("/api/v1/modif/modificationCompte")
    Call<UtilisateurSecurity> modificationCompte(
            @Field("email") String email,
            @Field("password") String password
    );

    /**
     * Appel Retrofit pour modifier la bio d'un utilisateur
     *
     * @Param id - Id de l'utilisateur à modifier - Long
     * @Param bio - Nouvelle bio - String
     */
    @FormUrlEncoded
    @PUT("/api/v1/modif/modificationProfil/bio")
    Call<Utilisateur>modificationProfilBio(
        @Field("id") Long id,
        @Field("bio") String bio);

    /**
     * Appel Retrofit pour modifier le pseudo d'un utilisateur
     *
     * @Param id - Id de l'utilisateur à modifier - Long
     * @Param pseudo - Nouveau pseudo - String
     */
    @FormUrlEncoded
    @PUT("/api/v1/modif/modificationProfil/pseudo")
    Call<Utilisateur>modificationProfilPseudo(
            @Field("id") Long id,
            @Field("pseudo") String pseudo);

    /**
     * Appel Retrofit pour modifier la photo de profil d'un utilisateur
     *
     * @Param id - Id de l'utilisateur à modifier - Long
     * @Param photoProfil - Nouvelle photo de profil - MultipartFile
     */
    @Multipart
    @PUT("/api/v1/modif/modificationProfil/photoProfil")
    Call<Utilisateur>modificationProfilImg(
            @Part("id") Long id,
            @Part MultipartBody.Part photoProfil
    );

    /**
     * Appel Retrofit pour la connexion d'un utilisateur
     *
     * @Param email - Email de l'utilisateur - String
     * @Param password - Mot de passe de l'utilisateur - String
     */
    @FormUrlEncoded
    @POST("/api/v1/auth/authenticate")
    Call<UtilisateurSecurity> connexion(
            @Field("email") String email,
            @Field("password") String password
    );

    /**
     * Appel Retrofit pour modifier les tags d'un utilisateur
     *
     * @Param authorization - En tête de la requête - String
     * @Param id - id de l'utilisateur - Long
     * @Param pseudo - pseudo de l'utilisateur - String
     * @Param tags - nouveaux tags choisis par l'utilisateur - Set<Tag>
     */
    @FormUrlEncoded
    @PUT("/api/v1/modif/modificationTags")
    Call<Utilisateur> modificationTags(
            @Header("Authorization") String authorization,
            @Field("id") long id,
            @Field("pseudo") String pseudo,
            @Field("tags") Set<Tag> tags
    );

    /**
     * Appel Retrofit pour supprimer un utilisateur
     */
    @DELETE("/api/utilisateur/deleteUser")
    Call<String> deleteProfil();

    /**
     * Appel Retrofit pour charger l'utilisateur un utilisateur
     */
    @GET("/api/v1/auth/loadUser")
    Call<Utilisateur> loadUser();

    /**
     * Appel Retrofit pour déconnecter un utilisateur
     */
    @POST("/api/v1/auth/logout")
    Call<String> logout();

}
