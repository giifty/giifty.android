package dk.android.giifty.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import org.joda.time.DateTime;

import java.io.IOException;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.busevents.SignedInEvent;
import dk.android.giifty.busevents.UserUpdateEvent;
import dk.android.giifty.model.User;
import dk.android.giifty.signin.ServerToken;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.signin.SignInParams;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.WebApi;
import retrofit2.Response;


public class SignInService extends IntentService {
    private static final String EXTRA_PARAMS = "params";
    private WebApi api;

    public static void signIn(Context context, SignInParams params){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAMS, params);
        intent.setClass(context, SignInService.class);
        context.startService(intent);
    }

    public SignInService() {
        super("SignInService");
        api = ServiceCreator.createServiceNoAuthenticator();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SignInParams params = intent.getParcelableExtra(EXTRA_PARAMS);
        try {
           Response<String> response = api.authenticateUser(params.createAuthenticationHeader()).execute();
            if(response.isSuccessful()){
                SignInHandler.setServerToken(new ServerToken(response.headers().get("token"),
                        new DateTime(response.headers().get("tokenExpiry"))));
                fetchUser(params);
            }else{
                post(new SignedInEvent(false, response.code()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void fetchUser( SignInParams param) throws IOException {
        Response<User> response = api.getUser(SignInHandler.getServerToken()).execute();
        if (response.isSuccessful()) {
            User userUpdated = response.body();
            userUpdated.setEmail(param.email);
            userUpdated.setPassword(param.password);
            persistUser(userUpdated);
        } else {
            GiiftyApplication.getBus().post(new UserUpdateEvent(null, false));
        }
    }
    private void persistUser(final User user) {
        GiiftyPreferences.getInstance().persistUser(user);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GiiftyApplication.getBus().post(new UserUpdateEvent(user, true));
                GiiftyApplication.getBus().post(new SignedInEvent(true, 200));
            }
        });
    }

    private void post(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GiiftyApplication.getBus().post(event);
            }
        });
    }
}
