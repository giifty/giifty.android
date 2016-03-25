package dk.android.giifty.utils;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import dk.android.giifty.Constants;
import dk.android.giifty.MyApp;

/**
 * Created by mak on 20-03-2016.
 */
public class Broadcasts {
    public static final String NEW_DOWNLOADS_FILTER = "newDls";
    public static final String ON_SIGNED_IN_FILTER = "signedInfilter";
    public static final String USER_UPDATED_FILTER = "userUpdated";

    public static void fireOnSignedInEvent(){
        LocalBroadcastManager.getInstance(MyApp.getMyApplicationContext()).sendBroadcast(new Intent(ON_SIGNED_IN_FILTER));
    }

    public static void fireNewDataEvent(boolean isSuccess){
        LocalBroadcastManager.getInstance(MyApp.getMyApplicationContext())
                .sendBroadcast(new Intent(NEW_DOWNLOADS_FILTER).putExtra(Constants.BOOLEAN_EXSTRA, isSuccess));
    }

    public static void fireUserUpdated(){
        LocalBroadcastManager.getInstance(MyApp.getMyApplicationContext()).sendBroadcast(new Intent(USER_UPDATED_FILTER));
    }
}
