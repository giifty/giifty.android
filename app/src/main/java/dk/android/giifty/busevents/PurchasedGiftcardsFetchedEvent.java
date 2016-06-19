package dk.android.giifty.busevents;

import java.util.List;

import dk.android.giifty.model.Giftcard;

public class PurchasedGiftcardsFetchedEvent {
    public final List<Giftcard> giftcard;
    public final boolean isSuccessFul;

    public PurchasedGiftcardsFetchedEvent(List<Giftcard> giftcard, boolean isSuccessFul) {
        this.giftcard = giftcard;
        this.isSuccessFul = isSuccessFul;
    }
}
