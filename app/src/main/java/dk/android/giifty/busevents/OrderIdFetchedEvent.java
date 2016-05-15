package dk.android.giifty.busevents;

import android.support.annotation.Nullable;

/**
 * Created by mak on 15-05-2016.
 */
public class OrderIdFetchedEvent {
    @Nullable
    public final String orderId;
    public final boolean isSuccessful;

    public OrderIdFetchedEvent(String orderId, boolean isSuccessful) {
        this.orderId = orderId;
        this.isSuccessful = isSuccessful;
    }
}
