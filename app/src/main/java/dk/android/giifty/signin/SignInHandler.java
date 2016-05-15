package dk.android.giifty.signin;

import android.util.Base64;
import android.util.Log;

import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.busevents.SignedInEvent;
import dk.android.giifty.busevents.UserUpdateEvent;
import dk.android.giifty.model.User;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.WebApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mak on 20-02-2016.
 */
public class SignInHandler implements Callback {
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


    public void refreshTokenAsync() throws IOException {
        Log.d(TAG, "refreshTokenAsync()");
        if (!isTokenExpired()) {
            fireSigInEvent(true, 200);
        } else {
            if(currentUser != null){
                webService.signInUser(createAuthenticationHeader(createAuthText())).enqueue(this);
            }
        }
    }

    public void refreshWithParams(String email, String password) {
        Log.d(TAG, "refreshWithParams()");
        webService.signInUser(createAuthenticationHeader(createAuthText(email, password))).enqueue(this);
    }


    public boolean refreshTokenSynchronous() throws IOException {
        Log.d(TAG, "refreshTokenSynchronous()");
        if (currentUser != null) {
            String auth = createAuthenticationHeader(createAuthText());
            Response<String> response = webService.signInUser(auth).execute();
            if (response.isSuccessful()) {
                setServerToken(new ServerToken(response.headers().get("Token"),
                        new DateTime(response.headers().get("TokenExpiry"))));
                return true;
            }
        }
        return false;
    }


    @Override
    public void onResponse(Call call, Response response) {
        boolean isSuccessFul = response.isSuccessful();
        Log.d(TAG, "onResponse() succes:" + isSuccessFul);
        if (isSuccessFul) {
            setServerToken(new ServerToken(response.headers().get("token"),
                    new DateTime(response.headers().get("tokenExpiry"))));
        }

        fireSigInEvent(isSuccessFul, response.code());
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        fireSigInEvent(false, -1);
        t.printStackTrace();
    }

    private void fireSigInEvent(boolean isSuccess, int responseCode) {
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

    public String createAuthenticationHeader(String text) {
        byte[] plain = new byte[0];
        try {
            plain = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format("Basic %s", Base64.encodeToString(plain, Base64.DEFAULT)).trim();
    }

    public String createAuthText() {
        return (currentUser.getEmail() + ":" + currentUser.getPassword()).trim();
    }

    public String createAuthText(String email, String password) {
        return (email + ":" + password.trim());
    }

    @Subscribe
    public void onUserUpdated(UserUpdateEvent event) {
        if(event.isSuccessful){
            currentUser = event.user;
        }
    }
}