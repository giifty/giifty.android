package dk.android.giifty.web;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import dk.android.giifty.signin.SignInHandler;
import hugo.weaving.DebugLog;

/**
 * Created by mak on 21-02-2016.
 */
public class MyAuthenticator implements Authenticator {

    @Override
    public Request authenticate(Proxy proxy, com.squareup.okhttp.Response response) throws IOException {
        if (SignInHandler.getInstance().refreshTokenSynchronous()) {
            return response
                    .request()
                    .newBuilder()
                    .header("Authorization", SignInHandler.getServerToken())
                    .build();
        }
        return null;
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        return null;
    }
}
