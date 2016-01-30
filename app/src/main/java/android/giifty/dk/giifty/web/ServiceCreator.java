package android.giifty.dk.giifty.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public static WebApi creatService() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new TypeAdapterYodaTime())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(WebApi.class);
    }

}
