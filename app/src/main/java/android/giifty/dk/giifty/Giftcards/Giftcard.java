package android.giifty.dk.giifty.Giftcards;

import android.giifty.dk.giifty.User.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by mak on 11-01-2016.
 */
public class Giftcard {
    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("ExpirationDate")
    @Expose
    private DateTime expirationDate;
    @SerializedName("CreatedDate")
    @Expose
    private DateTime createdDate;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("SalesPerson")
    @Expose
    private User salesPerson;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("GiftcardTypeId")
    @Expose
    private int giftcardTypeId;
    @SerializedName("UserID")
    @Expose
    private int userID;
    @SerializedName("CompanyId")
    @Expose
    private int companyId;
    @SerializedName("Company")
    @Expose
    private Company company;
    @SerializedName("GiftcardType")
    @Expose
    private GiftcardType giftcardType;


}
