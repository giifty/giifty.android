package android.giifty.dk.giifty.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.giifty.dk.giifty.Constants;
import android.giifty.dk.giifty.utils.BroadcastFilters;
import android.util.Log;

/**
 * Created by mak on 20-03-2016.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = MyBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action:" + action);
        if (action.contentEquals(BroadcastFilters.ON_SIGNED_IN_FILTER)){
            onSignIn();
        }else if(action.contentEquals(BroadcastFilters.NEW_DOWNLOADS_FILTER)){
            downloadCompleted(intent.getBooleanExtra(Constants.BOOLEAN_EXSTRA, false));
        }
    }

    public void downloadCompleted(boolean isSucces) {

    }

    public void onSignIn() {

    }
}
