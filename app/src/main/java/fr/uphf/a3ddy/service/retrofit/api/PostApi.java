package fr.uphf.a3ddy.service.retrofit.api;

import fr.uphf.a3ddy.model.posts.PostRequest;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
}
