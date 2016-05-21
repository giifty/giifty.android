package dk.android.giifty.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by mak on 16-01-2016.
 */
public class ServiceCreator {
    private static final String BASE_URL = "http://giifty-test.azurewebsites.net/v1.0/";
    private static final String BARCODE_BASE_URL = "http://scandit.com/";

    private ServiceCreator() {
    }

    public static WebApi createServiceWithAuthenticator() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new TypeAdapterYodaTime())
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder().authenticator(new MyAuthenticator()).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
               .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(WebApi.class);
    }

    public static WebApi createServiceNoAuthenticator(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new TypeAdapterYodaTime())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(WebApi.class);
    }
}
