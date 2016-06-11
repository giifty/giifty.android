package dk.android.giifty.model;

import java.io.Serializable;

public class GiftcardRequest implements Serializable {
    private GiftcardProperties properties = new GiftcardProperties();
    private String barcodeImagePath = "/storage/emulated/0/Pictures/JPEG_2016_06_11_121248_347669736.jpg"; //TODO only for testing
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
