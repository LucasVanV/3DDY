package fr.uphf.a3ddy.service.glide;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import java.io.InputStream;
import fr.uphf.a3ddy.service.OkHttpClientService;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Custom Glide module to integrate Glide with OkHttp and add authorization token.
 * This class extends AppGlideModule and is annotated with @GlideModule, which
 * allows Glide to discover this module and use it to configure Glide's image loading
 * with OkHttp and authorization headers.
 */
@GlideModule
public final class GlideService extends AppGlideModule {

    // Authorization token to be used in HTTP requests.
    private String authToken;

    /**
     * Registers components with Glide, setting up a custom OkHttp client that includes
     * an authorization token in requests.
     *
     * @param context The context for Glide.
     * @param glide The Glide instance.
     * @param registry The registry to replace Glide's default loading mechanisms.
     */
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        // Initialize OkHttpClientService with the authorization token.
        OkHttpClientService okHttpClientService = new OkHttpClientService();
        OkHttpClient client = okHttpClientService.initialyzer("Bearer " + this.authToken);

        // Replace Glide's default loader with the custom OkHttp loader.
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory((Call.Factory) client));
    }

    /**
     * Sets the authorization token to be used in network requests.
     *
     * @param authToken The authorization token to be set.
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
