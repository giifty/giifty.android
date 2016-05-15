package dk.android.giifty.busevents;

import android.support.annotation.Nullable;

import java.util.List;

import dk.android.giifty.model.Company;

/**
 * Created by mak on 15-05-2016.
 */
public class CompaniesFetchedEvent {
    @Nullable
    public final List<Company> companyList;
    public final boolean isSuccessful;

    public CompaniesFetchedEvent(List<Company> companyList, boolean isSuccessful) {
        this.companyList = companyList;
        this.isSuccessful = isSuccessful;
    }
}
