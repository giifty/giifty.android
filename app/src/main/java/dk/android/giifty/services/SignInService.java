package dk.android.giifty.services;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SignInService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "dk.android.giifty.services.action.FOO";
    public static final String ACTION_BAZ = "dk.android.giifty.services.action.BAZ";

    public static final String EXTRA_PARAM1 = "dk.android.giifty.services.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "dk.android.giifty.services.extra.PARAM2";

    public SignInService() {
        super("SignInService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


}
