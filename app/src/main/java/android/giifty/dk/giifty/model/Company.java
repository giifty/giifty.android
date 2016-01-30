package android.giifty.dk.giifty.model;

import android.giifty.dk.giifty.model.Giftcard;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by mak on 16-01-2016.
 */
public class Company {

    @Expose
    private int CompanyId;
    @Expose
    private String Name;
    @Expose
    private String Description;
    @Expose
    private String Shop; // ?
    @Expose
    private String imageUrl;
    @Expose
    private List<Giftcard> Giftcard;

    public String getImageUrl() {
        return imageUrl;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getShop() {
        return Shop;
    }

    public List<android.giifty.dk.giifty.model.Giftcard> getGiftcard() {
        return Giftcard;
    }

    public int getNumberOfCards(){
        return getGiftcard().size();
    }
}
