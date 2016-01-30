package android.giifty.dk.giifty.Web;

import android.giifty.dk.giifty.Giftcard.Company;
import android.giifty.dk.giifty.Giftcard.Giftcard;

import com.squareup.okhttp.RequestBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;

/**
 * Created by mak on 16-01-2016.
 */
public interface WebApi {

    //User-related
    @Multipart
    @POST("Users/User/Verify/id/code")
    Call<Boolean> verifyUser(@Part("id") String id, @Part("code") String code);

    @Multipart
    @PUT("Users/User/Delete/id")
    Call<Boolean> deleteUser(@Part("id") String id);

    @Multipart
    @POST("Users/User/Update/id")
    Call<Boolean> updateUser(@Part("id") String id, @Body String userEntity);

    @POST("Users/User/Create")
    Call<Integer> createUser(@Header("Authorization") String authorization, @Body RequestBody requestBody);


    //v1.0/Giftcards/GetAllGiftcards
    @GET("Giftcards/GetAllGiftcards")
    Call<Giftcard> getAllGiftCards();

    @GET("Giftcards/GetAllGiftcards/id")
    Call<Giftcard> getSpecifikGiftCard();

    @Headers("Authorization: Basic QVBQOnNvOFpvcnJv")
    @GET("Giftcards/GetMainView")
    Call<List<Company>> getMainView();
}
