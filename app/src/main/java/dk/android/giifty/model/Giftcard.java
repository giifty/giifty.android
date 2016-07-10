package dk.android.giifty.model;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

public class Giftcard implements Serializable {
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

}
