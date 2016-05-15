package dk.android.giifty.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.busevents.UserUpdateEvent;
import dk.android.giifty.model.User;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.WebApi;
import retrofit2.Response;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UserService extends IntentService {

    private static final String EXTRA_USER = "dk.android.giifty.extra.PARAM1";
    private static final String ACTION_UPDATE_USER = "updtaeUser";
    private final WebApi api;
    private GiiftyPreferences prefs;

    public UserService() {
        super("UpdateUserService");
        api = ServiceCreator.createServiceWithAuthenticator();
        prefs = GiiftyPreferences.getInstance();
    }

    public static void updateUser(Context context, User user) {
        Intent intent = new Intent();
        intent.setClass(context, UserService.class);
        intent.setAction(ACTION_UPDATE_USER);
        intent.putExtra(EXTRA_USER, user);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
                User user = intent.getParcelableExtra(EXTRA_USER);
                updateUser(user);
        } catch (IOException e) {
            e.printStackTrace();
            GiiftyApplication.getBus().post(new UserUpdateEvent(null, false));
        }
    }

    private void updateUser(User user) throws IOException {
        String newAccount = user.getAccountNumber(), newPassword = user.getPassword();
        //TODO should we check for naything else that that a user is persisted before updating
        Response<User> response = prefs.getUser() != null ? api.updateUser(SignInHandler.getServerToken(), user).execute() : api.createUser(user).execute();

        if (response.isSuccessful()) {
            User userUpdated = response.body();
            userUpdated.setAccountNumber(newAccount);
            userUpdated.setPassword(newPassword);
            persistUser(userUpdated);
        } else {
            GiiftyApplication.getBus().post(new UserUpdateEvent(null, false));
        }
    }

    private void persistUser(final User user) {
        prefs.persistUser(user);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GiiftyApplication.getBus().post(new UserUpdateEvent(user, true));
            }
        });
    }
}
