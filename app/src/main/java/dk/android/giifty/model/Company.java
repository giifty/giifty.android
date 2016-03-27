package dk.android.giifty.model;

import com.google.gson.annotations.Expose;

/**
 * Created by mak on 16-01-2016.
 */
public class Company {

    @Expose
    private int companyId;
    @Expose
    private int categoryId;
    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private String companyImageUrl;
    @Expose
    private int numberOfGiftcards;
    @Expose
    private double savingInProcent;

    public int getCompanyId() {
        return companyId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCompanyImageUrl() {
        return companyImageUrl;
    }

    public int getNumberOfGiftcards() {
        return numberOfGiftcards;
    }

    public double getSavingInProcent() {
        return savingInProcent;
    }
}

