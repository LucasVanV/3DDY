package fr.uphf.a3ddy.service.retrofit.api;

import java.util.List;

import fr.uphf.a3ddy.model.posts.Page;
import fr.uphf.a3ddy.model.posts.Post;
import fr.uphf.a3ddy.model.posts.PostRequest;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PostApi {

    /**
     * Appel Retrofit pour modifier un post à partir de son ID
     *
     * @Param titre - Titre du post - String
     * @Param description - Description du post - String
     * @Param tagsRegerence - Tags du post - String
     * @Param commentaires - Lien vers le fichier JSON des commentaires du post - String
     * @Param idUpdatedPost - ID du post à modifier - Long
     * @Param imagePost - Image du post - MultipartFile
     * @Param modele3d - Modele 3d du post - MultipartFile
     */
    @Multipart
    @PUT("/api/v1/posts/updatePost")
    Call<PostRequest> updatePost(
            @Part("titre") RequestBody titre,
            @Part("description") RequestBody description,
            @Part("tagsReference") RequestBody tagsReference,
            @Part("commentaires") RequestBody commentaires,
            @Part("tagsPrefere") RequestBody tagsPrefere,
            @Part("idUpdatedPost") RequestBody idUpdatedPost,
            @Part MultipartBody.Part imagePost,
            @Part MultipartBody.Part modele3d
    );

    /**
     * Appel Retrofit pour ajouter un post
     *
     * @Param titre - Titre du post - String
     * @Param description - Description du post - String
     * @Param tagsRegerence - Tags du post - String
     * @Param commentaires - Lien vers le fichier JSON des commentaires du post - String
     * @Param tagsPrefere - Tags du post - String
     * @Param imagePost - Image du post - MultipartFile
     * @Param modele3d - Modele 3d du post - MultipartFile
     */
    @Multipart
    @POST("/api/v1/posts/addPost")
    Call<PostRequest> addPost(
            @Part("titre") RequestBody titre,
            @Part("description") RequestBody description,
            @Part("tagsReference") RequestBody tagsReference,
            @Part("commentaires") RequestBody commentaires,
            @Part("tagsPrefere") RequestBody tagsPrefere,
            @Part MultipartBody.Part imagePost,
            @Part MultipartBody.Part modele3d
    );


    /**
     * Appel retrofit pour supprimer un post à partir de son ID
     *
     * @param idPost - ID du post à supprimer - Long
     */
    @DELETE("/api/v1/posts/deletePost")
    Call<Long> deletePost(
            @Query("idPost") Long idPost
    );

    /**
     * Appel retrofit pour récupérer la page d'accueil d'un utilisateur
     *
     * @param page - Paramètre comprenant une liste de 10 post actualisée au fur et à mesure - Page
     */
    @GET("/api/v1/posts/getForYouPage")
    Call<Page> getForYouPage(
            @Query("page") int page);

    /**
     * Appel retrofit pour récupérer les posts d'un utilisateur
     *
     * @param page - Paramètre comprenant une liste de 10 post actualisée au fur et à mesure - Page
     * @param userId - ID de l'utilisateur souhaité - Long
     */
    @GET("/api/v1/posts/getUserPost")
    Call<Page> getUserPost(
            @Query("page") int page,
            @Query("userId") Long userId
    );
}
