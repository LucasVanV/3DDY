package fr.uphf.a3ddy;

import com.google.gson.Gson;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    private void initializerRetrofit(){
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.56.1:8080/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
    public RetrofitService(){
        initializerRetrofit();
    }
    public Retrofit getRetrofit() {
        return retrofit;
    }
}
