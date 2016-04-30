package dk.android.giifty.giftcard;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dk.android.giifty.MyApp;
import dk.android.giifty.broadcastreceivers.MyBroadcastReceiver;
import dk.android.giifty.model.Company;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.utils.Broadcasts;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.WebApi;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mak on 16-01-2016.
 */
public class GiftcardRepository {

    private static final String TAG = GiftcardRepository.class.getSimpleName();
    private static GiftcardRepository instance;
    private WebApi webService;
    private GiiftyPreferences giiftyPreferences;
    private HashMap<Integer, Giftcard> giftcardsToSale;
    private HashMap<Integer, Giftcard> giftcardsPurchased;
    private List<Company> companyList;
    private HashMap<Integer, List<Giftcard>> map;

    public static GiftcardRepository getInstance() {
        return instance == null ? (instance = new GiftcardRepository()) : instance;
    }

    public void initController() {
        LocalBroadcastManager.getInstance(MyApp.getMyApplicationContext())
                .registerReceiver(new MyReceiver(), new IntentFilter(Broadcasts.SIGN_IN_FILTER));
        webService = ServiceCreator.creatServiceWithAuthenticator();
        giiftyPreferences = GiiftyPreferences.getInstance();
        companyList = new ArrayList<>();
        map = new HashMap<>();
        downloadMainView();
        downloadGiftcards();
        giftcardsToSale = giiftyPreferences.getGiftcardsToSale();
        if(giftcardsToSale == null){
            //create from serverList
            giftcardsToSale = new HashMap<>();
        }

        giftcardsPurchased = giiftyPreferences.getPurchasedGiftcards();
        if(giftcardsPurchased == null){
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

    public Giftcard getPurchasedGiftcard(int id) {
        return giftcardsPurchased.get(id);
    }

    public void addPurchased(Giftcard giftcard) {
        giftcardsPurchased.put(giftcard.getGiftcardId(), giftcard);
        giiftyPreferences.persistPurchasedGiftcards(giftcardsPurchased);
    }

    public void addGiftCardOnSale(Giftcard giftcard) {
        giftcardsToSale.put(giftcard.getGiftcardId(), giftcard);
        giiftyPreferences.persistGiftcardsToSale(giftcardsToSale);
    }

    private void downloadMainView() {
        Log.d(TAG, "downloadMainView()");
        webService.getMainView().enqueue(new Callback<List<Company>>() {
            @Override
            public void onResponse(Response<List<Company>> response, Retrofit retrofit) {
                boolean isSuccess = response.isSuccess();
                if (isSuccess) {
                    companyList = response.body();
                }
                fireDownloadEvent(isSuccess);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void downloadGiftcards() {

        Log.d(TAG, "downloadGiftcards()");
        webService.getAllGiftCards().enqueue(new Callback<List<Giftcard>>() {
            @Override
            public void onResponse(Response<List<Giftcard>> response, Retrofit retrofit) {
                boolean isSucces = response.isSuccess();
                if (isSucces) {
                    sortGiftcardsByCompany(response.body());
                }
                fireDownloadEvent(isSucces);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void sortGiftcardsByCompany(List<Giftcard> listToSort) {

        for (Giftcard g : listToSort) {
            if (g.isOnSale()) {
                if (!map.containsKey(g.getCompanyId())) {
                    map.put(g.getCompanyId(), new ArrayList<Giftcard>());
                }
                map.get(g.getCompanyId()).add(g);
            }else {


            }
        }
    }

    private void fireDownloadEvent(boolean isSuccess) {
        Broadcasts.fireNewDataEvent(isSuccess);
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    class MyReceiver extends MyBroadcastReceiver {

        @Override
        public void onSignIn(boolean isSuccess) {

        }
    }

}
