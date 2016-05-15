package dk.android.giifty.busevents;

import android.support.annotation.Nullable;

/**
 * Created by mak on 15-05-2016.
 */
public class SignedInEvent {
    @Nullable
    public final boolean isSuccessful;
    public int code;

    public SignedInEvent(boolean isSuccessful, int code) {
        this.isSuccessful = isSuccessful;
        this.code = code;
    }
}
