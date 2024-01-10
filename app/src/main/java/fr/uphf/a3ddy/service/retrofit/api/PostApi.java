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


    @DELETE("/api/v1/posts/deletePost")
    Call<Long> deletePost(
            @Query("idPost") Long idPost
    );

    @GET("/api/v1/posts/getForYouPage")
    Call<Page> getForYouPage(
            @Query("page") int page);

    @GET("/api/v1/posts/getUserPost")
    Call<Page> getUserPost(
            @Query("page") int page,
            @Query("userId") Long userId
    );
}
