package dk.android.giifty.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mak on 21-05-2016.
 */
public class Holder implements Parcelable {
    CreateGiftcardRequest request;
    String barcodeImagePath;
    String gcImagePath;

    public Holder() {
    }

    public CreateGiftcardRequest getRequest() {
        return request;
    }

    public void setRequest(CreateGiftcardRequest request) {
        this.request = request;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.request, flags);
        dest.writeString(this.barcodeImagePath);
        dest.writeString(this.gcImagePath);
    }



    protected Holder(Parcel in) {
        this.request = in.readParcelable(CreateGiftcardRequest.class.getClassLoader());
        this.barcodeImagePath = in.readString();
        this.gcImagePath = in.readString();
    }

    public static final Parcelable.Creator<Holder> CREATOR = new Parcelable.Creator<Holder>() {
        @Override
        public Holder createFromParcel(Parcel source) {
            return new Holder(source);
        }

        @Override
        public Holder[] newArray(int size) {
            return new Holder[size];
        }
    };
}
