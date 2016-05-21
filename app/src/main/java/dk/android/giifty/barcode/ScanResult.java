package dk.android.giifty.barcode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mak on 16-05-2016.
 */
public class ScanResult implements Parcelable {

    public final String symbologyName;
    public final String barcodeNumber;

    public ScanResult(String symbologyName, String barcodeNumber) {
        this.symbologyName = symbologyName;
        this.barcodeNumber = barcodeNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.symbologyName);
        dest.writeString(this.barcodeNumber);
    }

    protected ScanResult(Parcel in) {
        this.symbologyName = in.readString();
        this.barcodeNumber = in.readString();
    }

    public static final Creator<ScanResult> CREATOR = new Creator<ScanResult>() {
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
