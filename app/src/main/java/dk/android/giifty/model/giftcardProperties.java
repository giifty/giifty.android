package dk.android.giifty.model;

import org.joda.time.DateTime;

import java.io.Serializable;

import dk.android.giifty.utils.Constants;

public class GiftcardProperties implements Serializable {

    public int sellerId;
    public int companyId;
    public int giftcardTypeId = Constants.TYPE_GIFTCARD;
    public String valueDataBinding;
    public String priceDataBinding;
    private int value;
    private int price;
    public DateTime expirationDateUtc;
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

    public String getValueDataBinding() {
        return valueDataBinding;
    }

    public void setValueDataBinding(String valueDataBinding) {
        this.valueDataBinding = valueDataBinding;
    }

    public String getPriceDataBinding() {
        return priceDataBinding;
    }

    public void setPriceDataBinding(String priceDataBinding) {
        this.priceDataBinding = priceDataBinding;
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
