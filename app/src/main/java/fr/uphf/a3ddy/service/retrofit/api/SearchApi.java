package fr.uphf.a3ddy.service.retrofit.api;

import java.util.Set;
import fr.uphf.a3ddy.model.posts.Page;
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
import retrofit2.http.Query;

public interface SearchApi {
    @GET("/api/search/getUser")
    Call<Page> findUserByName(
            @Query("page") int page
    );
}
