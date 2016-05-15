package dk.android.giifty.busevents;

import android.support.annotation.Nullable;

import java.util.List;

import dk.android.giifty.model.Giftcard;

/**
 * Created by mak on 15-05-2016.
 */
public class GiftcardsFetchedEvent {
    @Nullable
    public final List<Giftcard> companyList;
    public final boolean isSuccessful;

    public GiftcardsFetchedEvent(List<Giftcard> companyList, boolean isSuccessful) {
        this.companyList = companyList;
        this.isSuccessful = isSuccessful;
    }
}
