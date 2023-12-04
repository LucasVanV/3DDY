package fr.uphf.a3ddy.service;

import java.util.concurrent.TimeUnit;

import fr.uphf.a3ddy.service.interceptor.AuthInterceptor;
import okhttp3.OkHttpClient;

public class OkHttpClientService extends OkHttpClient {

    public OkHttpClient initialyzer(String authToken){
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(authToken))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }
}
