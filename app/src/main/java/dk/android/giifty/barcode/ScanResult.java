package dk.android.giifty.barcode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mak on 16-05-2016.
 */
public class ScanResult implements Parcelable {

    public final String eanType;
    public final String barcodeNumber;

    public ScanResult(String eanType, String barcodeNumber) {
        this.eanType = eanType;
        this.barcodeNumber = barcodeNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eanType);
        dest.writeString(this.barcodeNumber);
    }

    protected ScanResult(Parcel in) {
        this.eanType = in.readString();
        this.barcodeNumber = in.readString();
    }

    public static final Parcelable.Creator<ScanResult> CREATOR = new Parcelable.Creator<ScanResult>() {
        @Override
        public ScanResult createFromParcel(Parcel source) {
            return new ScanResult(source);
        }

        @Override
        public ScanResult[] newArray(int size) {
            return new ScanResult[size];
        }
    };
}
