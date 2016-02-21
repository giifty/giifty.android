package android.giifty.dk.giifty.user;

import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.web.ServerToken;
import android.giifty.dk.giifty.web.ServiceCreator;
import android.giifty.dk.giifty.web.WebApi;
import android.util.Base64;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mak on 20-02-2016.
 */
public class SignInHandler implements Callback, Authenticator, UserUpdatedListener {
    private WebApi webService;
    public static SignInHandler instance;
    private User currentUser;

    public static SignInHandler getInstance() {
        return instance == null ? (instance = new SignInHandler()) : instance;
    }

    public SignInHandler() {
        webService = ServiceCreator.creatService();
        GlobalObserver.registerUserUpdateListener(this);
    }

    @DebugLog
    public void loginWithUser() throws IOException {
        if (GlobalObserver.hasCurrentUser()) {
            currentUser = GlobalObserver.getCurrentUser();
            String auth = createAuthenticationHeader(createAuthText());
            webService.loginUser(auth).enqueue(this);
        }
    }

    @DebugLog
    public boolean refreshToken() throws IOException {
        if (currentUser != null && currentUser.hasSignedIn()) {
            String auth = createAuthenticationHeader(createAuthText());
            Response response = webService.loginUser(auth).execute();
            if (response.isSuccess()) {
                GlobalObserver.setServerToken(new ServerToken(response.headers().get("Token"),
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
            GlobalObserver.setServerToken(new ServerToken(response.headers().get("Token"),
                    new DateTime(response.headers().get("tokenExpiry"))));
            GlobalObserver.fireSignInEvent();
        }
    }

    @DebugLog
    @Override
    public void onFailure(Throwable t) {
    }

    @DebugLog
    @Override
    public Request authenticate(Proxy proxy, com.squareup.okhttp.Response response) throws IOException {
        if (refreshToken()) {
            return response
                    .request()
                    .newBuilder()
                    .header("Authorization", GlobalObserver.getServerToken())
                    .build();
        }
        return null;
    }

    @Override
    public Request authenticateProxy(Proxy proxy, com.squareup.okhttp.Response response) throws IOException {
        return null;
    }

    @DebugLog
    public String createAuthenticationHeader(String text) {
        byte[] plain = new byte[0];
        try {
            plain = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String auth = ("Basic " + Base64.encodeToString(plain, Base64.DEFAULT)).trim();
        return auth;
    }

    @DebugLog
    public String createAuthText() {
        return (currentUser.getEmail() + ":" + currentUser.getPassword()).trim();
    }

    @Override
    public void onUserUpdated() {
        currentUser = GlobalObserver.getCurrentUser();
    }
}
