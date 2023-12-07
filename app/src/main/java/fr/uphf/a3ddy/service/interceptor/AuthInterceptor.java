package fr.uphf.a3ddy.service.interceptor;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor for adding an authorization token to HTTP requests.
 * This class implements the Interceptor interface from the OkHttp library.
 * It is used to intercept outgoing HTTP requests and modify them to include
 * an authorization header with a bearer token.
 * 'Bearer ' already include
 */
public class AuthInterceptor implements Interceptor {
    // The authorization token to be added to the request.
    private String authToken;

    /**
     * Constructor for the AuthInterceptor.
     *
     * @param token The authorization token that will be used in the HTTP header.
     *              This token is typically a JWT or similar token provided after
     *              a successful authentication.
     */
    public AuthInterceptor(String token) {
        this.authToken = token;
    }

    /**
     * Method that intercepts the outgoing request and modifies it.
     *
     * @param chain The Interceptor.Chain provided by OkHttp which gives access to the
     *              request and the ability to proceed with the chain.
     * @return The HTTP response after the request has been processed by the server.
     * @throws IOException If an IO exception occurs while processing the request.
     */
    public Response intercept(Interceptor.Chain chain) throws IOException {
        // Original request before being modified.
        Request originalRequest = chain.request();

        // Builder to create a new request based on the original one.
        Request.Builder builder = originalRequest.newBuilder()
                // Adding the authorization header with the bearer token.
                .header("Authorization", "Bearer " + authToken);

        // Building the new request with the added header.
        Request newRequest = builder.build();

        // Proceeding with the chain and returning the response from the server.
        return chain.proceed(newRequest);
    }
}