package dk.android.giifty.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.busevents.UserUpdateEvent;
import dk.android.giifty.model.User;
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
public class CreateUserService extends IntentService {

    private static final String EXTRA_USER = "dk.android.giifty.extra.PARAM1";
    private final WebApi api;

    public CreateUserService() {
        super("UpdateUserService");
        api = ServiceCreator.createServiceWithAuthenticator();
    }

    public static void startService(Context context, User user) {
        Intent intent = new Intent();
        intent.setClass(context, CreateUserService.class);
        intent.putExtra(EXTRA_USER, user);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        User user = intent.getParcelableExtra(EXTRA_USER);
        String newAccount = user.getAccountNumber(), newPassword = user.getPassword();
        try {
            Response<User> response = api.createUser(user).execute();

            if (response.isSuccessful()) {
                User userUpdated = response.body();
                userUpdated.setAccountNumber(newAccount);
                userUpdated.setPassword(newPassword);
                persistUser(userUpdated);
            }
        } catch (IOException e) {
            e.printStackTrace();
            GiiftyApplication.getBus().post(new UserUpdateEvent(null, true));
        }
    }

    private void persistUser(User user) {
        GiiftyPreferences.getInstance().persistUser(user);
        GiiftyApplication.getBus().post(new UserUpdateEvent(user, true));
    }
}
