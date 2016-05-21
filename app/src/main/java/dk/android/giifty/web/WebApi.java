package dk.android.giifty.web;

import java.util.List;

import dk.android.giifty.model.Company;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by mak on 16-01-2016.
 */
public interface WebApi {

    //User-related
    @POST("Users/User/Verify/{id}/{code}")
    Call<Boolean> verifyUser(@Path("id") String id, @Path("code") String code);

    @POST("Users/User/Update")
    Call<User> updateUser(@Header("Token") String authHeader, @Body User userToUpdate);

    @GET("Users/User/GetUser")
    Call<User> getUser(@Header("Token") String authHeader);

    @Headers("Authorization: Basic QVBQOnNvOFpvcnJv")
    @POST("Users/User/Create")
    Call<User> createUser(@Body User userToUpdate);


    //Authentication
    @POST("Authenticate/Login")
    Call<String> authenticateUser(@Header("Authorization") String authHeader);


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
