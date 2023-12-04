package fr.uphf.a3ddy.service.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import fr.uphf.a3ddy.service.OkHttpClientService;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@GlideModule
public final class GlideService extends AppGlideModule {

    private String authToken;

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

        OkHttpClientService okHttpClientService = new OkHttpClientService();
        OkHttpClient client  = okHttpClientService.initialyzer(this.authToken);

        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory((Call.Factory) client));
    }

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

}