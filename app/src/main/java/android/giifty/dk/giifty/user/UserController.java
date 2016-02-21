package android.giifty.dk.giifty.user;

import android.content.Context;
import android.giifty.dk.giifty.Constants;
import android.giifty.dk.giifty.model.NullResponse;
import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.utils.MyPrefrences;
import android.giifty.dk.giifty.utils.Utils;
import android.giifty.dk.giifty.web.RequestHandler;
import android.giifty.dk.giifty.web.ServiceCreator;
import android.giifty.dk.giifty.web.WebApi;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import hugo.weaving.DebugLog;
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
    private WebApi webService;
    private User user, userToUpdate;
    private MyPrefrences myPrefrences;
    private RequestHandler requestHandler;
    private String serverToken;

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public UserController() {
    }

    public void initController(Context applicationContext) {
        Log.d(TAG, "initController()");
        this.context = applicationContext;
        requestHandler = new RequestHandler(this);
        myPrefrences = MyPrefrences.getInstance();
        webService = ServiceCreator.creatService();
        if (myPrefrences.hasKey(Constants.KEY_USER)) {
            user = myPrefrences.getObject(Constants.KEY_USER, new TypeToken<User>() {
            });
        }
    }


    @DebugLog
    public void createUser(Context context, User user) throws JSONException {
        String auth = "APP:so8Zorro";
        JSONObject json = new JSONObject();
        json.put("password", user.getPassword());
        json.put("name", user.getName());
        json.put("email", user.getEmail());
        json.put("phone", user.getPhone());
        json.put("accountNumber", user.getAccountNumber());

        auth = Utils.createAuthenticationHeader(auth);

        userToUpdate = user;

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString());
        requestHandler.enqueueRequest(webService.createUser(auth, requestBody), context);
    }

    public void deleteUser(Context context) {
        requestHandler.enqueueRequest(webService.deleteUser(getUser().getUserId()), context);
    }


    public User getUser() {
        return user;
    }




    @DebugLog
    @Override
    public void onResponse(Response response, Retrofit retrofit) {
        Log.d(TAG, "onResponse() state:" + response.isSuccess() + "  code:" + response.code() + "  msg:" + response.message());
        if (response.code() == 200) {
            Object responseBody = response.body();
            boolean isVerified = false;
            if (User.class.isInstance(responseBody)) {
                persistUser(userToUpdate);
            } else if (Boolean.class.isInstance(responseBody)) {
                isVerified = (boolean) responseBody;
            } else if (NullResponse.class.isInstance(responseBody)) {
                isVerified = (boolean) responseBody;
            }
            if (isVerified) {
                serverToken = response.headers().get("token");
            }
        }
    }

    @DebugLog
    @Override
    public void onFailure(Throwable t) {
    }

    private void persistUser(User user) {
        myPrefrences.persistObject(Constants.KEY_USER, user);
    }

}
