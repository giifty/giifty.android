package android.giifty.dk.giifty.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by mak on 16-01-2016.
 */
public class Company {

    @Expose
    private int companyId;
    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private String shop; // ?
    @Expose
    private String imageUrl;
    @Expose
    private List<Giftcard> giftcard;

    public String getImageUrl() {
        return imageUrl;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getShop() {
        return shop;
    }

    public List<android.giifty.dk.giifty.model.Giftcard> getGiftcard() {
        return giftcard;
    }

    public int getNumberOfCards(){
        return getGiftcard().size();
    }
}
