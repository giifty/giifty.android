package android.giifty.dk.giifty.web;

import android.giifty.dk.giifty.utils.GlobalObserver;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import hugo.weaving.DebugLog;

/**
 * Created by mak on 21-02-2016.
 */
public class MyAuthenticator implements Authenticator {

    @DebugLog
    @Override
    public Request authenticate(Proxy proxy, com.squareup.okhttp.Response response) throws IOException {
        if (SignInHandler.getInstance().refreshToken()) {
            return response
                    .request()
                    .newBuilder()
                    .header("Authorization", GlobalObserver.getServerToken())
                    .build();
        }
        return null;
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        return null;
    }
}
