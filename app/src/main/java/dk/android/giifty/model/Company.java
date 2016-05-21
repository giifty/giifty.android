package dk.android.giifty.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by mak on 16-01-2016.
 */
public class Company implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.companyId);
        dest.writeInt(this.categoryId);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.companyImageUrl);
        dest.writeInt(this.numberOfGiftcards);
        dest.writeDouble(this.savingInProcent);
    }

    public Company() {
    }

    protected Company(Parcel in) {
        this.companyId = in.readInt();
        this.categoryId = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.companyImageUrl = in.readString();
        this.numberOfGiftcards = in.readInt();
        this.savingInProcent = in.readDouble();
    }

    public static final Parcelable.Creator<Company> CREATOR = new Parcelable.Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel source) {
            return new Company(source);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };
}

