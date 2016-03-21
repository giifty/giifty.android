package android.giifty.dk.giifty.user;

import android.content.Context;
import android.giifty.dk.giifty.Constants;
import android.giifty.dk.giifty.model.NullResponse;
import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.utils.Broadcasts;
import android.giifty.dk.giifty.utils.MyPreferences;
import android.giifty.dk.giifty.utils.Utils;
import android.giifty.dk.giifty.web.RequestHandler;
import android.giifty.dk.giifty.web.ServiceCreator;
import android.giifty.dk.giifty.web.SignInHandler;
import android.giifty.dk.giifty.web.WebApi;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

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
    private WebApi webService;
    private User userToUpdate;
    private MyPreferences myPreferences;
    private RequestHandler requestHandler;
    private User user;
    private Context applicationContext;

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public UserController() {
    }

    public void initController(Context applicationContext) {
        this.applicationContext = applicationContext;
        Log.d(TAG, "initController()");
        requestHandler = new RequestHandler(this);
        myPreferences = MyPreferences.getInstance();
        webService = ServiceCreator.creatServiceWithAuthenticator();
        if (myPreferences.hasKey(Constants.KEY_USER)) {
            user = myPreferences.getObject(Constants.KEY_USER, new TypeToken<User>() {
            });
        }
    }

    public boolean hasUser() {
        return user != null;
    }

    public User getUser() {
        return user;
    }

    public void createUser(Context context, String name, String phone, String email, String password, String accountNumber) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("password", password);
        json.put("name", name);
        json.put("email", email);
        json.put("phone", phone);
        json.put("accountNumber", accountNumber);
      //  userToUpdate = newUser;
        requestHandler.enqueueRequest(webService.createUser(Utils.createRequestBodyFromJson(json)), context);
    }

    //TODO remove
    public void createUser(Context context, User newUser) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("password", newUser.getPassword());
        json.put("name", newUser.getName());
        json.put("email", newUser.getEmail());
        json.put("phone", newUser.getPhone());
        json.put("accountNumber", newUser.getAccountNumber());
        userToUpdate = newUser;
        requestHandler.enqueueRequest(webService.createUser(Utils.createRequestBodyFromJson(json)), context);
    }

    public void updateUser(Context context, UpdatedUser updatedUser) throws JSONException {
        requestHandler.enqueueRequest(webService.updateUser(SignInHandler.getServerToken(),
                user.getUserId(), updatedUser.createUpdateRequest(user)), context);
    }

    public void deleteUser(Context context) {
        requestHandler.enqueueRequest(webService.deleteUser(user.getUserId()), context);
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
                userToUpdate = null;
                Broadcasts.fireUserUpdated(applicationContext);
            } else if (Boolean.class.isInstance(responseBody)) {
                isVerified = (boolean) responseBody;
            } else if (NullResponse.class.isInstance(responseBody)) {
                isVerified = (boolean) responseBody;
            }
            if (isVerified) {
                //TODO what?
            }
        }
    }


    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
    }

    private void persistUser(User user) {
        myPreferences.persistObject(Constants.KEY_USER, user);
    }

}
