package android.giifty.dk.giifty.model;

import com.google.gson.annotations.Expose;

/**
 * Created by mak on 16-01-2016.
 */
public class Image {
    @Expose
    private int imageId;
    @Expose
    private int giftcardId;
    @Expose
    private String url;

    public int getImageId() {
        return imageId;
    }

    public int getGiftcardId() {
        return giftcardId;
    }

    public String getUrl() {
        return url;
    }
}
