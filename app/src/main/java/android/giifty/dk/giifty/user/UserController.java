package android.giifty.dk.giifty.user;

import android.content.Context;
import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.web.ServiceCreator;
import android.giifty.dk.giifty.web.WebApi;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mak on 16-01-2016.
 */
public class UserController implements Callback {

    private static final String TAG = UserController.class.getSimpleName();
    private static UserController instance;
    private Context context;
    private WebApi webApi;
    private User user;

    public static UserController getInstance() {
        return instance == null ? new UserController() : instance;
    }

    public UserController() {
        webApi = ServiceCreator.creatService();
    }

    public void initController(Context context) {
        this.context = context;
    }

    public void createUser(User user) {
       String toEncode = "App:so8Zorro";
        Log.d(TAG, "createUser(), user:" + user + ", encodeString:" + encodeBAse64(toEncode));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), user.toString());
        Call<Integer> request = webApi.createUser(encodeBAse64(toEncode), requestBody);
        request.enqueue(this);
    }

    public void deleteUser() {
        Call<Boolean> request = webApi.deleteUser("");
        request.enqueue(this);
    }

    public void loginWithUser(){

    }
    public User getUser() {
        return user;
    }

    @Override
    public void onResponse(Response response, Retrofit retrofit) {
        Log.d(TAG, "onResponse() state:" + response.isSuccess() + "  code:" + response.code() + "  msg:" + response.message());
      String tokenResponse =  response.headers().get("token");
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d(TAG, "onFailure() msg: " + t.getMessage());

    }

    private String encodeBAse64(String body){
     //   return Base64.encodeToString(body.getBytes(), Base64.NO_PADDING).trim();
        return  "Basic QVBQOnNvOFpvcnJv";
    }
}
