package android.giifty.dk.giifty.web;

import android.giifty.dk.giifty.model.Company;
import android.giifty.dk.giifty.model.Giftcard;
import android.giifty.dk.giifty.model.NullResponse;
import android.giifty.dk.giifty.model.User;

import java.util.List;

import retrofit.Call;
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
    Call<Boolean> deleteUser(@Part("id") int id);


    @Multipart
    @POST("Users/User/Update/id")
    Call<User> updateUser(@Header("Authorization") String authorization, @Part("id") int id, @Part("user") User userToUpdate);

    @Headers("Authorization: Basic QVBQOnNvOFpvcnJv")
    @POST("Users/User/Create")
    Call<User> createUser(User userToUpdate);

    @POST("Authenticate/Login")
    Call<NullResponse> signInUser(@Header("Authorization") String authorization);


    //v1.0/Giftcards/GetAllGiftcards
    @GET("Giftcards/GetAllGiftcards")
    Call<List<Giftcard>> getAllGiftCards();

    @GET("Giftcards/GetAllGiftcards/id")
    Call<Giftcard> getSpecificGiftCard();

    @GET("Giftcards/GetMainView")
    Call<List<Company>> getMainView();
}
