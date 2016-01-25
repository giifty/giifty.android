package android.giifty.dk.giifty.Giftcards;

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
    private String shop; // ?
    @Expose
    private Category Category;
    @Expose
    private List<Giftcard> Giftcard;


}
