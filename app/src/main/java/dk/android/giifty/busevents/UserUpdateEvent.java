package dk.android.giifty.busevents;

import android.support.annotation.Nullable;

import dk.android.giifty.model.User;

/**
 * Created by mak on 15-05-2016.
 */
public class UserUpdateEvent {
    @Nullable
    public final User user;
    public final boolean isSuccessful;

    public UserUpdateEvent(User user, boolean isSuccessful) {
        this.user = user;
        this.isSuccessful = isSuccessful;
    }
}
