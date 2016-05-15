package dk.android.giifty.web;


import java.io.IOException;
import java.net.Proxy;

import dk.android.giifty.signin.SignInHandler;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by mak on 21-02-2016.
 */
public class MyAuthenticator implements Authenticator {

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (SignInHandler.getInstance().refreshTokenSynchronous()) {
            return response
                    .request()
                    .newBuilder()
                    .header("Authorization", SignInHandler.getServerToken())
                    .build();
        }
        return null;
    }
}
