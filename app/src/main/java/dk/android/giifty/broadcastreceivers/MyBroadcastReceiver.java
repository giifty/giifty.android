package dk.android.giifty.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import dk.android.giifty.Constants;
import dk.android.giifty.utils.Broadcasts;

/**
 * Created by mak on 20-03-2016.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = MyBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action:" + action);
        if (action.contentEquals(Broadcasts.SIGN_IN_FILTER)) {
            onSignIn(intent.getBooleanExtra(Constants.BOOLEAN_EXSTRA, false));
        } else if (action.contentEquals(Broadcasts.NEW_DOWNLOADS_FILTER)) {
            downloadCompleted(intent.getBooleanExtra(Constants.BOOLEAN_EXSTRA, false));
        } else if (action.contentEquals(Broadcasts.USER_UPDATED_FILTER)) {
            onUserUpdated();
        }
    }

    public void downloadCompleted(boolean isSuccess) {
    }

    public void onSignIn(boolean isSuccess) {
    }

    public void onUserUpdated() {
    }
}
