package dk.android.giifty.model;

import org.joda.time.DateTime;

import java.io.Serializable;

import dk.android.giifty.barcode.Barcode;
import dk.android.giifty.utils.Constants;

public class GiftcardProperties implements Serializable {

    public int sellerId;
    public int companyId;
    public int giftcardTypeId = Constants.TYPE_GIFTCARD;
    private int value;
    private int price;
    public DateTime expirationDateUtc;
    public String description;
    public Barcode barcode;

    public String getValue() {
        return String.valueOf(value);
    }

    public void setValue(String value) {
        this.value =  value.isEmpty() ? 0 : Integer.parseInt(value);
    }

    public String getPrice() {
        return String.valueOf(price);
    }

    public void setPrice(String price) {
        this.price = price.isEmpty() ? 0 :  Integer.parseInt(price);
    }

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

    public void setBarcode(Barcode barcode) {
        this.barcode = barcode;
    }
}
