package dk.android.giifty.model;

import org.joda.time.DateTime;

import java.io.Serializable;

import dk.android.giifty.utils.Constants;

public class GiftcardProperties implements Serializable {

    public int sellerId;
    public int companyId;
    public int giftcardTypeId = Constants.TYPE_GIFTCARD;
    public int value;
    public int price;
    public DateTime expirationDateUtc;
    public DateTime entryDateUtc = new DateTime();
    public String description;

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getGiftcardTypeId() {
        return giftcardTypeId;
    }

    public void setGiftcardTypeId(int giftcardTypeId) {
        this.giftcardTypeId = giftcardTypeId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = Integer.parseInt(value);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public DateTime getExpirationDate() {
        return expirationDateUtc;
    }

    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDateUtc = expirationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
