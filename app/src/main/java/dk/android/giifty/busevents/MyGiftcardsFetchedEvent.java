package dk.android.giifty.busevents;

import java.util.List;

import dk.android.giifty.model.Giftcard;

public class MyGiftcardsFetchedEvent {
    public final List<Giftcard> giftcard;
    public final boolean isSuccessFul;

    public MyGiftcardsFetchedEvent(List<Giftcard> giftcard, boolean isSuccessFul) {
        this.giftcard = giftcard;
        this.isSuccessFul = isSuccessFul;
    }
}
