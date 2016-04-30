package dk.android.giifty.user;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import dk.android.giifty.model.NullResponse;
import dk.android.giifty.model.User;
import dk.android.giifty.utils.Broadcasts;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.web.RequestHandler;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.web.WebApi;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mak on 16-01-2016.
 */
public class UserRepository implements Callback {

    private static final String TAG = UserRepository.class.getSimpleName();
    private static UserRepository instance;
    private WebApi webService;
    private GiiftyPreferences giiftyPreferences;
    private RequestHandler requestHandler;
    private User user;
    private String newAccount, newPassword;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public UserRepository() {
    }

    public void initController() {
        Log.d(TAG, "initController()");
        requestHandler = new RequestHandler(this);
        giiftyPreferences = GiiftyPreferences.getInstance();
        webService = ServiceCreator.creatServiceWithAuthenticator();
        user = giiftyPreferences.getUser();
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
                   userToUpdate), context);
        } else {
            requestHandler.enqueueRequest(webService.createUser(userToUpdate), context);
        }
    }

    public void fetchUserFromServer(){
        requestHandler.enqueueRequest(webService.getUser(SignInHandler.getServerToken()), null);
    }

    public void deleteUser() {
        user = null;
        giiftyPreferences.clearUser();
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
        giiftyPreferences.persistUser(user);
        setUser(user);
    }

    private void setUser(User user) {
        this.user = user;
    }

}
