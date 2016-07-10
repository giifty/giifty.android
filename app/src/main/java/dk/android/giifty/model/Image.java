package dk.android.giifty.model;


import java.io.Serializable;

public class Image implements Serializable {
    private int imageId;
    private int giftcardId;
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
