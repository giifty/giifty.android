package dk.android.giifty.giftcard;

import android.util.Log;

import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.busevents.CompaniesFetchedEvent;
import dk.android.giifty.busevents.GiftcardsFetchedEvent;
import dk.android.giifty.model.Company;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.services.GiftcardService;
import dk.android.giifty.utils.GiiftyPreferences;

/**
 * Created by mak on 16-01-2016.
 */
public class GiftcardRepository {

    private static final String TAG = GiftcardRepository.class.getSimpleName();
    private static GiftcardRepository instance;
    private GiiftyPreferences giiftyPreferences;
    private HashMap<Integer, Giftcard> giftcardsToSale;
    private HashMap<Integer, Giftcard> giftcardsPurchased;
    private List<Company> companyList;
    private HashMap<Integer, List<Giftcard>> map;

    public static GiftcardRepository getInstance() {
        return instance == null ? (instance = new GiftcardRepository()) : instance;
    }

    public void initController() {
        giiftyPreferences = GiiftyPreferences.getInstance();
        companyList = new ArrayList<>();
        map = new HashMap<>();

        GiiftyApplication.getBus().register(this);
        GiftcardService.startFecthMain(GiiftyApplication.getMyApplicationContext());
        GiftcardService.startFetchGiftcards(GiiftyApplication.getMyApplicationContext());

        giftcardsToSale = giiftyPreferences.getGiftcardsToSale();
        if (giftcardsToSale == null) {
            //create from serverList
            giftcardsToSale = new HashMap<>();
        }

        giftcardsPurchased = giiftyPreferences.getPurchasedGiftcards();
        if (giftcardsPurchased == null) {
            //create from serverList
            giftcardsPurchased = new HashMap<>();
        }
    }

    public Giftcard getGiftcard(int id) {
        for (List<Giftcard> listToSearch : map.values()) {
            for (Giftcard g : listToSearch) {
                if (g.getGiftcardId() == id) {
                    return g;
                }
            }
        }
        return null;
    }

    public boolean removeGiftcardFromCompanyList(Giftcard giftcard) {
        Log.d(TAG, "removeGiftcardFromCompanyList()  companyId:" + giftcard.getGiftcardId());
        return map.get(giftcard.getCompanyId()).remove(giftcard);
    }

    public List<Giftcard> getCompanyGiftcardsOnSale(int companyId) {
        Log.d(TAG, "getCompanyGiftcardsOnSale()  companyId:" + companyId);
        return Collections.unmodifiableList(map.containsKey(companyId) ? map.get(companyId) : new ArrayList<Giftcard>());
    }


    public Company getCompany(int id) {
        Log.d(TAG, "getCompany()  companyId:" + id);
        for (Company c : companyList) {
            if (c.getCompanyId() == id)
                return c;
        }
        return null;
    }


    public List<Company> getMainView() {
        return Collections.unmodifiableList(companyList);
    }


    public List<Giftcard> getMyGiftcardForSale() {
        return Collections.unmodifiableList(new ArrayList<>(giftcardsToSale.values()));
    }

    public List<Giftcard> getMyGiftcardPurchased() {
        return Collections.unmodifiableList(new ArrayList<>(giftcardsPurchased.values()));
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    public Giftcard getPurchasedGiftcard(int id) {
        return giftcardsPurchased.get(id);
    }

    public void addPurchased(Giftcard giftcard) {
        removeGiftcardFromCompanyList(giftcard);
        giftcardsPurchased.put(giftcard.getGiftcardId(), giftcard);
        giiftyPreferences.persistPurchasedGiftcards(giftcardsPurchased);
    }

    public void addGiftCardOnSale(Giftcard giftcard) {
        giftcardsToSale.put(giftcard.getGiftcardId(), giftcard);
        giiftyPreferences.persistGiftcardsToSale(giftcardsToSale);
    }

    @Subscribe
    public void onCompaniesFetched(CompaniesFetchedEvent fetchedEvent) {
        if (fetchedEvent.isSuccessful) {
            companyList = fetchedEvent.companyList;
        }
    }

    @Produce
    public CompaniesFetchedEvent produceLastCompaniesEvent() {
        return companyList != null ? new CompaniesFetchedEvent(companyList, true) : null;
    }

    @Subscribe
    public void onGiftcardsFetched(GiftcardsFetchedEvent fetchedEvent) {
        if (fetchedEvent.isSuccessful) {
            sortGiftcardsByCompany(fetchedEvent.companyList);
        }
    }

    private void sortGiftcardsByCompany(List<Giftcard> listToSort) {

        for (Giftcard g : listToSort) {
            if (g.isOnSale()) {
                if (!map.containsKey(g.getCompanyId())) {
                    map.put(g.getCompanyId(), new ArrayList<Giftcard>());
                }
                map.get(g.getCompanyId()).add(g);
            } else {
                //TODO ?
            }
        }
    }
}
