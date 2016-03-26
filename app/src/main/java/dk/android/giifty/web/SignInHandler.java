package dk.android.giifty.web;

import android.content.IntentFilter;
import android.util.Base64;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import dk.android.giifty.MyApp;
import dk.android.giifty.broadcastreceivers.MyBroadcastReceiver;
import dk.android.giifty.model.User;
import dk.android.giifty.user.UserController;
import dk.android.giifty.utils.Broadcasts;
import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mak on 20-02-2016.
 */
public class SignInHandler implements Callback {
    private final UserController userController;
    private WebApi webService;
    public static SignInHandler instance;
    private User currentUser;
    private static ServerToken serverToken;

    public static SignInHandler getInstance() {
        return instance == null ? (instance = new SignInHandler()) : instance;
    }

    public SignInHandler() {
        webService = ServiceCreator.createServiceNoAuthenticator();
        userController = UserController.getInstance();
        MyApp.getMyApplicationContext().registerReceiver(new MyReceiver(), new IntentFilter(Broadcasts.USER_UPDATED_FILTER));
    }


    @DebugLog
    public void refreshTokenAsync() throws IOException {

        if (!isTokenExpired()) {
            fireSigInEvent();
        } else {
            webService.signInUser(createAuthenticationHeader(createAuthText())).enqueue(this);
        }
    }

    @DebugLog
    public boolean refreshTokenSynchronous() throws IOException {
        if (currentUser != null) {
            String auth = createAuthenticationHeader(createAuthText());
            Response response = webService.signInUser(auth).execute();
            if (response.isSuccess()) {
                setServerToken(new ServerToken(response.headers().get("Token"),
                        new DateTime(response.headers().get("tokenExpiry"))));
                return true;
            }
        }
        return false;
    }


    @DebugLog
    @Override
    public void onResponse(Response response, Retrofit retrofit) {
        if (response.isSuccess()) {
            setServerToken(new ServerToken(response.headers().get("Token"),
                    new DateTime(response.headers().get("tokenExpiry"))));
            fireSigInEvent();
        }
    }

    private void fireSigInEvent() {
        Broadcasts.fireOnSignedInEvent();
    }

    @DebugLog
    @Override
    public void onFailure(Throwable t) {
    }


    public static String getServerToken() {
        return serverToken != null ? serverToken.getToken() : "dummy_token";
    }

    private static void setServerToken(ServerToken serverToken) {
        SignInHandler.serverToken = serverToken;
    }

    public boolean isTokenExpired() {
        return serverToken == null || serverToken.getExpirationTime().isBeforeNow();
    }

    @DebugLog
    public String createAuthenticationHeader(String text) {
        byte[] plain = new byte[0];
        try {
            plain = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format("Basic %s", Base64.encodeToString(plain, Base64.DEFAULT)).trim();
    }

    @DebugLog
    public String createAuthText() {
        return (currentUser.getEmail() + ":" + currentUser.getPassword()).trim();
    }

    class MyReceiver extends MyBroadcastReceiver {

        @Override
        public void onUserUpdated() {
            currentUser = userController.getUser();
        }
    }
}