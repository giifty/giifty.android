package dk.android.giifty.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class Giftcard implements Parcelable {
    private int value;
    private int price;
    private DateTime expirationDate;
    private DateTime entryDate;
    private String description;
    private List<Image> images;
    private String company;
    private int companyId;
    private int giftcardTypeId;
    private int giftcardId;
    private int buyerId;
    private int sellerId;
    private User seller;
    private double savingsInPercentage;
    private String cvc;

    public int getValue() {
        return value;
    }

    public int getPrice() {
        return price;
    }

    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public DateTime getEntryDate() {
        return entryDate;
    }

    public String getDescription() {
        return description;
    }

    public List<Image> getImages() {
        return images;
    }

    public String getCompany() {
        return company;
    }

    public int getCompanyId() {
        return companyId;
    }

    public int getGiftcardTypeId() {
        return giftcardTypeId;
    }

    public int getGiftcardId() {
        return giftcardId;
    }

    public int getBuyerId() {
        return buyerId;
    }


    public int getSellerId() {
        return sellerId;
    }

    public User getSeller() {
        return seller;
    }

    public double getSavingsInPercentage() {
        return savingsInPercentage;
    }

    public boolean isOnSale() {
        return getBuyerId() == 0;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.value);
        dest.writeInt(this.price);
        dest.writeSerializable(this.expirationDate);
        dest.writeSerializable(this.entryDate);
        dest.writeString(this.description);
        dest.writeList(this.images);
        dest.writeString(this.company);
        dest.writeInt(this.companyId);
        dest.writeInt(this.giftcardTypeId);
        dest.writeInt(this.giftcardId);
        dest.writeInt(this.buyerId);
        dest.writeInt(this.sellerId);
        dest.writeParcelable(this.seller, flags);
        dest.writeDouble(this.savingsInPercentage);
        dest.writeString(this.cvc);
    }

    public Giftcard() {
    }

    protected Giftcard(Parcel in) {
        this.value = in.readInt();
        this.price = in.readInt();
        this.expirationDate = (DateTime) in.readSerializable();
        this.entryDate = (DateTime) in.readSerializable();
        this.description = in.readString();
        this.images = new ArrayList<Image>();
        in.readList(this.images, Image.class.getClassLoader());
        this.company = in.readString();
        this.companyId = in.readInt();
        this.giftcardTypeId = in.readInt();
        this.giftcardId = in.readInt();
        this.buyerId = in.readInt();
        this.sellerId = in.readInt();
        this.seller = in.readParcelable(User.class.getClassLoader());
        this.savingsInPercentage = in.readDouble();
        this.cvc = in.readString();
    }

    public static final Creator<Giftcard> CREATOR = new Creator<Giftcard>() {
        @Override
        public Giftcard createFromParcel(Parcel source) {
            return new Giftcard(source);
        }

        @Override
        public Giftcard[] newArray(int size) {
            return new Giftcard[size];
        }
    };
}
