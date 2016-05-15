package dk.android.giifty.utils;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import dk.android.giifty.Constants;
import dk.android.giifty.GiiftyApplication;

/**
 * Created by mak on 20-03-2016.
 */
public class Broadcasts {
    public static final String NEW_DOWNLOADS_FILTER = "newDls";
    public static final String SIGN_IN_FILTER = "signedInfilter";
    public static final String USER_UPDATED_FILTER = "userUpdated";
    private static final String TAG = Broadcasts.class.getSimpleName();

    public static void fireOnSignedInEvent(boolean isSuccess){
        LocalBroadcastManager.getInstance(GiiftyApplication.getMyApplicationContext())
                .sendBroadcast(new Intent(SIGN_IN_FILTER)
                        .putExtra(Constants.BOOLEAN_EXSTRA, isSuccess));
    }

    public static void fireNewDataEvent(boolean isSuccess){
        LocalBroadcastManager.getInstance(GiiftyApplication.getMyApplicationContext())
                .sendBroadcast(new Intent(NEW_DOWNLOADS_FILTER).putExtra(Constants.BOOLEAN_EXSTRA, isSuccess));
    }

    public static void fireUserUpdated(){
        Log.d(TAG, "fireUserUpdated");
        LocalBroadcastManager.getInstance(GiiftyApplication.getMyApplicationContext()).sendBroadcast(new Intent(USER_UPDATED_FILTER));
    }
}
