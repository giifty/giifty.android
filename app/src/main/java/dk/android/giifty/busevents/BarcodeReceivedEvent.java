package dk.android.giifty.busevents;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

/**
 * Created by mak on 21-05-2016.
 */
public class BarcodeReceivedEvent {

    public Bitmap bitmap;
    public boolean isSuccessFul;

    public BarcodeReceivedEvent(@Nullable Bitmap bitmap, boolean isSuccessFul) {
        this.bitmap = bitmap;
        this.isSuccessFul = isSuccessFul;
    }
}
