package dk.android.giifty.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mak on 21-05-2016.
 */
public class CreateGiftcardRequest implements Parcelable {
    public int userId;
    public int companyId;
    public String type = "Giftcard";
    public int value;
    public int price;
    public String expirationDate;
    public String description;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeInt(this.companyId);
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
        this.companyId = in.readInt();
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
