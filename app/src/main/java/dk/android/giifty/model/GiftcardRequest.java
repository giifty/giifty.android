package dk.android.giifty.model;

import java.io.Serializable;

public class GiftcardRequest implements Serializable {
    private GiftcardProperties properties = new GiftcardProperties();
    private String barcodeImagePath;
    private String gcImagePath;

    public GiftcardRequest() {
    }

    public GiftcardProperties getProperties() {
        return properties;
    }

    public void setProperties(GiftcardProperties properties) {
        this.properties = properties;
    }

    public String getBarcodeImagePath() {
        return barcodeImagePath;
    }

    public void setBarcodeImagePath(String barcodeImagePath) {
        this.barcodeImagePath = barcodeImagePath;
    }

    public String getGcImagePath() {
        return gcImagePath;
    }

    public void setGcImagePath(String gcImagePath) {
        this.gcImagePath = gcImagePath;
    }

}
