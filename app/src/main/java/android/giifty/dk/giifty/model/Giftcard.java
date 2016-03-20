package android.giifty.dk.giifty.model;

import com.google.gson.annotations.Expose;

import org.joda.time.DateTime;

import java.util.List;

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
    private List<Image> image;
    @Expose
    private String company;
    @Expose
    private int companyId;
    @Expose
    private int giftcardTypeId;
    @Expose
    private GiftcardType giftcardType;
    @Expose
    private int giftcardId;
    @Expose
    private int buyerId;
    @Expose
    private User buyer;
    @Expose
    private int sellerId;
    @Expose
    private User seller;
    @Expose
    private double savingInProcent;

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

    public List<Image> getImage() {
        return image;
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

    public GiftcardType getGiftcardType() {
        return giftcardType;
    }

    public int getGiftcardId() {
        return giftcardId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public User getBuyer() {
        return buyer;
    }

    public int getSellerId() {
        return sellerId;
    }

    public User getSeller() {
        return seller;
    }

    public double getSavingInProcent() {
        return savingInProcent;
    }


}
