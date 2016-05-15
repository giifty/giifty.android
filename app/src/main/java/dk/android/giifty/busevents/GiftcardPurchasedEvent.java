package dk.android.giifty.busevents;

import dk.android.giifty.model.Giftcard;

/**
 * Created by mak on 15-05-2016.
 */
public class GiftcardPurchasedEvent {
    public final Giftcard giftcard;
    public final boolean isSuccessFul;

    public GiftcardPurchasedEvent(Giftcard giftcard, boolean isSuccessFul) {
        this.giftcard = giftcard;
        this.isSuccessFul = isSuccessFul;
    }
}
