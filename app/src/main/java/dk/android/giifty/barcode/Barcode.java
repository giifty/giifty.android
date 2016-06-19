package dk.android.giifty.barcode;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class Barcode implements Serializable {

    public final String symbologyName;
    public final String barcodeNumber;

    public Barcode(String symbologyName, String barcodeNumber) {
        this.symbologyName = symbologyName;
        this.barcodeNumber = barcodeNumber;
    }
}
