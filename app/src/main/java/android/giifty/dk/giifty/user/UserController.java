package android.giifty.dk.giifty.user;

import android.content.Context;
import android.giifty.dk.giifty.Constants;
import android.giifty.dk.giifty.model.NullResponse;
import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.utils.GlobalObserver;
import android.giifty.dk.giifty.utils.MyPrefrences;
import android.giifty.dk.giifty.utils.Utils;
import android.giifty.dk.giifty.web.RequestHandler;
import android.giifty.dk.giifty.web.ServiceCreator;
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
    private MyPrefrences myPrefrences;
    private RequestHandler requestHandler;

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
        requestHandler = new RequestHandler(this);
        myPrefrences = MyPrefrences.getInstance();
        webService = ServiceCreator.creatService();
        if (myPrefrences.hasKey(Constants.KEY_USER)) {
            GlobalObserver.setCurrentUser((User) myPrefrences.getObject(Constants.KEY_USER, new TypeToken<User>() {
            }));
        }
    }

    @DebugLog
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
        User current = GlobalObserver.getCurrentUser();
        requestHandler.enqueueRequest(webService.updateUser(GlobalObserver.getServerToken(),
                current.getUserId(), updatedUser.createUpdateRequest(current)), context);
    }

    public void deleteUser(Context context) {
        requestHandler.enqueueRequest(webService.deleteUser(GlobalObserver.getCurrentUser().getUserId()), context);
    }

    @DebugLog
    @Override
    public void onResponse(Response response, Retrofit retrofit) {
        Log.d(TAG, "onResponse() state:" + response.isSuccess() + "  code:" + response.code() + "  msg:" + response.message());
        if (response.code() == 200) {
            Object responseBody = response.body();
            boolean isVerified = false;
            if (User.class.isInstance(responseBody)) {
                GlobalObserver.setCurrentUser(userToUpdate);
                persistUser(userToUpdate);
                userToUpdate = null;
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
    }

    private void persistUser(User user) {
        myPrefrences.persistObject(Constants.KEY_USER, user);
    }
}
