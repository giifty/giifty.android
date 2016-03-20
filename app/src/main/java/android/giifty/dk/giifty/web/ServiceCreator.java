package android.giifty.dk.giifty.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.DateTime;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by mak on 16-01-2016.
 */
public class ServiceCreator {
    private static final String BASE_URL = "http://giifty-test.azurewebsites.net/v1.0/";

    private ServiceCreator() {
    }

    public static WebApi creatServiceWithAuthenticator() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new TypeAdapterYodaTime())
                .create();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setAuthenticator(new MyAuthenticator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(WebApi.class);
    }

    public static  WebApi createServiceNoAuthenticator(){
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
