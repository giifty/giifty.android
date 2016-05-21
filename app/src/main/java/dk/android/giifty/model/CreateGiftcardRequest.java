package dk.android.giifty.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mak on 21-05-2016.
 */
public class CreateGiftcardRequest implements Parcelable {
    public int userId;
    public int compnayId;
    public String type = "Giftcard";
    public int value;
    public int price;
    public String expirationDate;
    public String description;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeInt(this.compnayId);
        dest.writeString(this.type);
        dest.writeInt(this.value);
        dest.writeInt(this.price);
        dest.writeString(this.expirationDate);
        dest.writeString(this.description);
    }

    public CreateGiftcardRequest() {
    }

    protected CreateGiftcardRequest(Parcel in) {
        this.userId = in.readInt();
        this.compnayId = in.readInt();
        this.type = in.readString();
        this.value = in.readInt();
        this.price = in.readInt();
        this.expirationDate = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<CreateGiftcardRequest> CREATOR = new Parcelable.Creator<CreateGiftcardRequest>() {
        @Override
        public CreateGiftcardRequest createFromParcel(Parcel source) {
            return new CreateGiftcardRequest(source);
        }

        @Override
        public CreateGiftcardRequest[] newArray(int size) {
            return new CreateGiftcardRequest[size];
        }
    };
}
