package android.giifty.dk.giifty.model;

import com.google.gson.annotations.Expose;

import org.joda.time.DateTime;

/**
 * Created by mak on 11-01-2016.
 */
public class Giftcard {
    @Expose
    private int value;
    @Expose
    private int price;
    @Expose
    private DateTime expirationDate;
    @Expose
    private DateTime entryDate;
    @Expose
    private String description;
    @Expose
    private User owner;
    @Expose
    private String imageUrl;
    @Expose
    private int userID;
    @Expose
    private String companyName;
    @Expose
    private int companyId;
    @Expose
    private int giftcardTypeId;
    @Expose
    private GiftcardType giftcardType;
    @Expose
    private int giftcardId;


    public int getGiftcardId() {
        return giftcardId;
    }

    public GiftcardType getGiftcardType() {
        return giftcardType;
    }

    public int getGiftcardTypeId() {
        return giftcardTypeId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public int getUserID() {
        return userID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public User getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getEntryDate() {
        return entryDate;
    }

    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public int getPrice() {
        return price;
    }

    public int getValue() {
        return value;
    }

    public String getCompanyName() {
        return companyName;
    }
}
