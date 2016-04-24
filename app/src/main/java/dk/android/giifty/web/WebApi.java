package dk.android.giifty.web;

import java.util.List;

import dk.android.giifty.model.Company;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.model.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;

/**
 * Created by mak on 16-01-2016.
 */
public interface WebApi {

    //User-related
    @Multipart
    @POST("Users/User/Verify/id/code")
    Call<Boolean> verifyUser(@Part("id") String id, @Part("code") String code);

    @POST("Users/User/Update")
    Call<User> updateUser(@Header("Token") String authHeader, @Body User userToUpdate);

    @Headers("Authorization: Basic QVBQOnNvOFpvcnJv")
    @POST("Users/User/Create")
    Call<User> createUser(@Body User userToUpdate);


    //Authentication
    @POST("Authenticate/Login")
    Call<String> signInUser(@Header("Authorization") String authHeader);


    //v1.0/Giftcards/GetAllGiftcards
    @GET("Giftcards/GetAllGiftcards")
    Call<List<Giftcard>> getAllGiftCards();

    @GET("Giftcards/GetAllGiftcards/id")
    Call<Giftcard> getSpecificGiftCard();

    @GET("Giftcards/GetMainView")
    Call<List<Company>> getMainView();

    @POST("Order/PreOrder/{giftcardId}")
    Call<String> getTransactionOrderId(@Header("Token") String authHeader, @Path("giftcardId") int giftcardId);

    @POST("Order/Buy/{giftcardId}")
    Call<Giftcard> buyGiftcard(@Header("Token") String authHeader, @Path("giftcardId") int giftcardId);
}
