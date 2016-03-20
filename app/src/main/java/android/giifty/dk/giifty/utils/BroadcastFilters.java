package android.giifty.dk.giifty.utils;

import android.content.Context;
import android.content.Intent;
import android.giifty.dk.giifty.Constants;

/**
 * Created by mak on 20-03-2016.
 */
public class BroadcastFilters {
    public static final String NEW_DOWNLOADS_FILTER = "newDls";
    public static final String ON_SIGNED_IN_FILTER = "signedInfilter";

    public static void fireOnSignedInEvent(Context context){
        context.sendBroadcast(new Intent(ON_SIGNED_IN_FILTER));
    }

    public static void fireNewDataEvent(Context context, boolean isSucces){
        context.sendBroadcast(new Intent(NEW_DOWNLOADS_FILTER).putExtra(Constants.BOOLEAN_EXSTRA, isSucces));
    }
}
