package dk.android.giifty.busevents;

import android.support.annotation.Nullable;

import dk.android.giifty.model.Giftcard;

public class GiftcardCreatedEvent {

    public final Giftcard giftcard;
    public final boolean isSuccessFul;

    public GiftcardCreatedEvent(@Nullable Giftcard giftcard, boolean isSuccessFul) {
        this.giftcard = giftcard;
        this.isSuccessFul = isSuccessFul;
    }
}
