package android.giifty.dk.giifty.user;

import android.content.Context;
import android.giifty.dk.giifty.model.NullResponse;
import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.utils.Broadcasts;
import android.giifty.dk.giifty.utils.MyPreferences;
import android.giifty.dk.giifty.web.RequestHandler;
import android.giifty.dk.giifty.web.ServiceCreator;
import android.giifty.dk.giifty.web.SignInHandler;
import android.giifty.dk.giifty.web.WebApi;
import android.util.Log;

import org.json.JSONException;

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
    private String newAccount, newPassword;

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
        user = myPreferences.getUser();
    }

    public boolean hasUser() {
        return user != null;
    }

    public User getUser() {
        return user;
    }


    public void updateUser(Context context, User userToUpdate) throws JSONException {
        newPassword = userToUpdate.getPassword();
        newAccount = userToUpdate.getAccountNumber();

        if (hasUser()) {
            requestHandler.enqueueRequest(webService.updateUser(SignInHandler.getServerToken(),
                    user.getUserId(), userToUpdate), context);
        } else {
            requestHandler.enqueueRequest(webService.createUser(userToUpdate), context);

        }
    }

    public void deleteUser() {
        user = null;
        myPreferences.clearUser();
        Broadcasts.fireUserUpdated();
    }


    @Override
    public void onResponse(Response response, Retrofit retrofit) {
        Log.d(TAG, "onResponse() state:" + response.isSuccess() + "  code:" + response.code() + "  msg:" + response.message());
        if (response.code() == 200) {
            Object responseBody = response.body();
            boolean isVerified = false;
            if (User.class.isInstance(responseBody)) {
                User userUpdated = (User) response.body();
                userUpdated.setAccountNumber(newAccount);
                userUpdated.setPassword(newPassword);
                persistUser(userUpdated);
            } else if (Boolean.class.isInstance(responseBody)) {
                isVerified = (boolean) responseBody;
            } else if (NullResponse.class.isInstance(responseBody)) {
                isVerified = (boolean) responseBody;
            }
            if (isVerified) {
                //TODO what?
            }
        }
        Broadcasts.fireUserUpdated();
    }


    @Override
    public void onFailure(Throwable t) {
        Broadcasts.fireUserUpdated();
    }

    private void persistUser(User user) {
        myPreferences.persistUser(user);
        setUser(user);
    }

    private void setUser(User user) {
        this.user = user;
    }

}
