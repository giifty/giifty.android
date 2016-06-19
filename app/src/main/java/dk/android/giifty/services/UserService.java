package dk.android.giifty.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.busevents.UserUpdateEvent;
import dk.android.giifty.model.User;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.WebApi;
import retrofit2.Response;

public class UserService extends IntentService {

    private static final String EXTRA_USER = "dk.android.giifty.extra.USER";
    private static final String ACTION_UPDATE_USER = "updateUser";
    private static final String TAG = UserService.class.getSimpleName();
    private final WebApi api;
    private GiiftyPreferences prefs;

    public UserService() {
        super(TAG);
        api = ServiceCreator.createServiceWithAuthenticator();
        prefs = GiiftyPreferences.getInstance();
    }

    public static void updateUser(Context context, User user) {
        Log.d(TAG, "updateUser user:" + user);
        Intent intent = new Intent();
        intent.setClass(context, UserService.class);
        intent.setAction(ACTION_UPDATE_USER);
        intent.putExtra(EXTRA_USER, user);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        UserUpdateEvent event = new UserUpdateEvent(null, false);
        try {
            User user = intent.getParcelableExtra(EXTRA_USER);
            event = updateUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            postEvent(event);
        }
    }

    private UserUpdateEvent updateUser(User user) throws IOException {
        String newAccount = user.getAccountNumber(),
                newPassword = user.getPassword(),
                newCardholderName = user.getCardholderName();

        //TODO should we check for aything else that that a user is persisted before updating
        Response<User> response = prefs.getUser() != null
                ? api.updateUser(SignInHandler.getServerToken(), user).execute()
                : api.createUser(user).execute();

        Log.d(TAG, "response isSuccessFul:" + response.isSuccessful());

        if (!response.isSuccessful()) {
            Log.d(TAG, "error msg:" + response.message());
            return new UserUpdateEvent(null, false);
        }

        User userUpdated = response.body();
        userUpdated.setAccountNumber(newAccount);
        userUpdated.setPassword(newPassword);
        userUpdated.setCardholderName(newCardholderName);
        prefs.persistUser(userUpdated);
        return new UserUpdateEvent(user, true);
    }

    private void postEvent(final UserUpdateEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GiiftyApplication.getBus().post(event);
            }
        });
    }
}
