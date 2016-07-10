package dk.android.giifty.web;


import android.util.Log;

import java.io.IOException;

import dk.android.giifty.signin.SignInHandler;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class MyAuthenticator implements Authenticator {

    private boolean hasRetried;

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (!hasRetried) {
            SignInHandler.getInstance().refreshTokenSynchronous();
            hasRetried = true;
            return response
                    .request()
                    .newBuilder()
                    .header("Token", SignInHandler.getServerToken())
                    .build();

        }

        hasRetried = false;
        return null;
    }
}
