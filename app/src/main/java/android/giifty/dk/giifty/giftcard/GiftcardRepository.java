package android.giifty.dk.giifty.giftcard;

import android.content.Context;
import android.content.IntentFilter;
import android.giifty.dk.giifty.Constants;
import android.giifty.dk.giifty.broadcastreceivers.MyBroadcastReceiver;
import android.giifty.dk.giifty.model.Company;
import android.giifty.dk.giifty.model.Giftcard;
import android.giifty.dk.giifty.utils.Broadcasts;
import android.giifty.dk.giifty.utils.MyPreferences;
import android.giifty.dk.giifty.web.ServiceCreator;
import android.giifty.dk.giifty.web.WebApi;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
    private MyPreferences myPreferences;
    private List<Giftcard> giftcardsOnSale;
    private List<Giftcard> giftcardsPurchased;
    private List<Company> companyList;
    private HashMap<Integer, List<Giftcard>> map;
    private Context applicationContext;

    public static GiftcardRepository getInstance() {
        return instance == null ? (instance = new GiftcardRepository()) : instance;
    }

    public void initController(Context applicationContext) {
        this.applicationContext = applicationContext;
        applicationContext.registerReceiver(new MyReceiver(), new IntentFilter(Broadcasts.ON_SIGNED_IN_FILTER));
        webService = ServiceCreator.creatServiceWithAuthenticator();
        myPreferences = MyPreferences.getInstance();
        companyList = new ArrayList<>();
        map = new HashMap<>();
        downloadMainView();
        downloadGiftcards();
        giftcardsOnSale = myPreferences.hasKey(Constants.KEY_MY_GC_ON_SALE) ?
                (List<Giftcard>) myPreferences.getObject(Constants.KEY_MY_GC_ON_SALE, new TypeToken<List<Giftcard>>() {
                }) : new ArrayList<Giftcard>();

        giftcardsPurchased = myPreferences.hasKey(Constants.KEY_MY_GC_PURCHASED) ?
                (List<Giftcard>) myPreferences.getObject(Constants.KEY_MY_GC_PURCHASED, new TypeToken<List<Giftcard>>() {
                }) : new ArrayList<Giftcard>();
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

    public List<Giftcard> getCompanyGiftcards(int companyId) {
        Log.d(TAG, "getCompanyGiftcards()  companyId:" + companyId );
        return Collections.unmodifiableList(map.containsKey(companyId) ? map.get(companyId) : new ArrayList<Giftcard>());
    }


    public Company getCompany(int id) {
        Log.d(TAG, "getCompany()  companyId:" + id );
        for (Company c : companyList) {
            if (c.getCompanyId() == id)
                return c;
        }
        return null;
    }


    public List<Company> getMainView(Context context) {
        return Collections.unmodifiableList(companyList);
    }


    public List<Giftcard> getMyGiftcardForSale() {
        return Collections.unmodifiableList(giftcardsOnSale);
    }

    public List<Giftcard> getMyGiftcardPurchased() {
        return Collections.unmodifiableList(giftcardsPurchased);
    }

    public void addPurchased(Giftcard giftcard) {
        giftcardsPurchased.add(giftcard);
        myPreferences.persistObject(Constants.KEY_MY_GC_PURCHASED, giftcardsPurchased);
    }

    public void addGiftCardOnSale(Giftcard giftcard) {
        giftcardsOnSale.add(giftcard);
        myPreferences.persistObject(Constants.KEY_MY_GC_ON_SALE, giftcardsOnSale);
    }

    private void downloadMainView() {
        Log.d(TAG, "downloadMainView()");
        webService.getMainView().enqueue(new Callback<List<Company>>() {
            @Override
            public void onResponse(Response<List<Company>> response, Retrofit retrofit) {
                boolean isSucces = response.isSuccess();
                if (isSucces) {
                    companyList = response.body();
                }
                fireDownloadEvent(isSucces);
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
            if (!map.containsKey(g.getCompanyId())) {
                map.put(g.getCompanyId(), new ArrayList<Giftcard>());
            }
            map.get(g.getCompanyId()).add(g);
        }
    }

    private void fireDownloadEvent(boolean isSucces) {
        Broadcasts.fireNewDataEvent(isSucces);
    }

    class MyReceiver extends MyBroadcastReceiver {

        @Override
        public void onSignIn() {
            super.onSignIn();

        }
    }

}
