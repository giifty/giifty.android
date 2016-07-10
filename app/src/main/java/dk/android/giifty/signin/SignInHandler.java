package dk.android.giifty.signin;

import android.util.Log;

import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.io.IOException;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.busevents.SignedInEvent;
import dk.android.giifty.busevents.UserUpdateEvent;
import dk.android.giifty.model.User;
import dk.android.giifty.services.SignInService;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.WebApi;
import retrofit2.Response;


public class SignInHandler {
    private static final String TAG = SignInHandler.class.getSimpleName();
    private WebApi webService;
    public static SignInHandler instance;
    private User currentUser;
    private static ServerToken serverToken;

    public static SignInHandler getInstance() {
        return instance == null ? (instance = new SignInHandler()) : instance;
    }

    public SignInHandler() {
        webService = ServiceCreator.createServiceNoAuthenticator();
        currentUser = GiiftyPreferences.getInstance().getUser();
        GiiftyApplication.getBus().register(this);
    }

    public void refreshTokenAsync()  {
        Log.d(TAG, "refreshTokenAsync()");
        if (!isTokenExpired()) {
            fireSignInEvent(true, 200);
        } else {
            if(currentUser != null){
                SignInService.signIn(GiiftyApplication.getMyApplicationContext(),
                        new SignInParams(currentUser.getEmail(), currentUser.getPassword()));
            }
        }
    }

    public void refreshWithParams(String email, String password) {
        Log.d(TAG, "refreshWithParams()");
        SignInService.signIn(GiiftyApplication.getMyApplicationContext(),
                new SignInParams(email, password));
    }


    public boolean refreshTokenSynchronous() throws IOException {
        Log.d(TAG, "refreshTokenSynchronous()");
        if (currentUser != null) {
            String auth = new SignInParams(currentUser.getEmail(), currentUser.getPassword()).createAuthenticationHeader();
            Response<String> response = webService.authenticateUser(auth).execute();
            if (response.isSuccessful()) {
                setServerToken(new ServerToken(response.headers().get("Token"),
                        new DateTime(response.headers().get("TokenExpiry"))));
                return true;
            }
        }
        return false;
    }

    private void fireSignInEvent(boolean isSuccess, int responseCode) {
        GiiftyApplication.getBus().post(new SignedInEvent(isSuccess, responseCode));
    }

    public static String getServerToken() {
        return serverToken != null ? serverToken.getToken() : "dummy_token";
    }

    public static void setServerToken(ServerToken serverToken) {
        SignInHandler.serverToken = serverToken;
    }

    public boolean isTokenExpired() {
        return serverToken == null || serverToken.getExpirationTime().isBeforeNow();
    }

    @Subscribe
    public void onUserUpdated(UserUpdateEvent event) {
        if(event.isSuccessful){
            currentUser = event.user;
        }
    }
}