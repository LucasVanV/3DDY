package fr.uphf.a3ddy.service;

import java.util.concurrent.TimeUnit;
import fr.uphf.a3ddy.service.interceptor.AuthInterceptor;
import okhttp3.OkHttpClient;

/**
 * A subclass of OkHttpClient that includes custom configurations for network requests.
 * This class provides a method to initialize a new OkHttpClient instance with predefined
 * settings including a custom interceptor for authorization, and timeout settings.
 */
public class OkHttpClientService extends OkHttpClient {

    /**
     * Initializes and configures an OkHttpClient instance.
     * This method creates a new OkHttpClient with a custom interceptor and specific timeout settings.
     *
     * @param authToken The authorization token to be used in the AuthInterceptor for HTTP requests.
     *                  This token is typically a JWT or a similar token provided after successful authentication.
     * @return An OkHttpClient instance configured with a custom AuthInterceptor and timeout settings.
     */
    public OkHttpClient initialyzer(String authToken){
        return new OkHttpClient.Builder()
                // Adds an interceptor which appends the provided authToken in all HTTP requests' headers.
                .addInterceptor(new AuthInterceptor(authToken))
                // Sets the connection timeout. If the client doesn't establish a connection within 30 seconds, it will timeout.
                .connectTimeout(30, TimeUnit.SECONDS)
                // Sets the read timeout. If the client doesn't get a response within 30 seconds, it will timeout.
                .readTimeout(30, TimeUnit.SECONDS)
                // Sets the write timeout. If the client doesn't complete sending the request within 30 seconds, it will timeout.
                .writeTimeout(30, TimeUnit.SECONDS)
                // Builds and returns the OkHttpClient instance with these settings.
                .build();
    }
}