package dk.android.giifty.model;

import com.google.gson.annotations.Expose;

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
