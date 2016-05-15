package dk.android.giifty.signin;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.broadcastreceivers.MyBroadcastReceiver;
import dk.android.giifty.model.User;
import dk.android.giifty.user.UserRepository;
import dk.android.giifty.utils.Broadcasts;
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
    private final UserRepository userRepository;
    private WebApi webService;
    public static SignInHandler instance;
    private User currentUser;
    private static ServerToken serverToken;


    public static SignInHandler getInstance() {
        return instance == null ? (instance = new SignInHandler()) : instance;
    }

    public SignInHandler() {
        webService = ServiceCreator.createServiceNoAuthenticator();
        userRepository = UserRepository.getInstance();
        currentUser = userRepository.getUser();
        LocalBroadcastManager.getInstance(GiiftyApplication.getMyApplicationContext())
                .registerReceiver(new MyReceiver(), new IntentFilter(Broadcasts.USER_UPDATED_FILTER));
    }


    public void refreshTokenAsync() throws IOException {
        Log.d(TAG, "refreshTokenAsync()");
        if (!isTokenExpired()) {
            fireSigInEvent(true);
        } else {
            webService.signInUser(createAuthenticationHeader(createAuthText())).enqueue(this);
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
        Log.d(TAG, "onResponse() succes:" + response.isSuccessful());
        if (response.isSuccessful()) {
            setServerToken(new ServerToken(response.headers().get("token"),
                    new DateTime(response.headers().get("tokenExpiry"))));

            fireSigInEvent(true);
        } else {
            fireSigInEvent(false);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        fireSigInEvent(false);
        t.printStackTrace();
    }


    private void fireSigInEvent(boolean isSuccess) {
        Broadcasts.fireOnSignedInEvent(isSuccess);
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


    class MyReceiver extends MyBroadcastReceiver {
        @Override
        public void onUserUpdated() {
            Log.d(TAG, "onUserUpdated()");
            currentUser = userRepository.getUser();
        }
    }
}