package android.giifty.dk.giifty.giftcard;

import android.content.Context;
import android.giifty.dk.giifty.Constants;
import android.giifty.dk.giifty.components.DataUpdateListener;
import android.giifty.dk.giifty.model.Company;
import android.giifty.dk.giifty.model.Giftcard;
import android.giifty.dk.giifty.utils.MyPrefrences;
import android.giifty.dk.giifty.web.RequestHandler;
import android.giifty.dk.giifty.web.ServiceCreator;
import android.giifty.dk.giifty.web.WebApi;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mak on 16-01-2016.
 */
public class GiftcardController implements Callback {

    private static final String TAG = GiftcardController.class.getSimpleName();
    private static GiftcardController instance;
    private final WebApi webService;
    private final MyPrefrences myPreferences;
    private final List<Giftcard> giftcardsOnSale, giftcardsPurchased;
    private final RequestHandler requestHandler;
    private List<Company> companyList;
    private DataUpdateListener listener;
    private HashMap<Integer, Giftcard> map;

    public static GiftcardController getInstance() {
        return instance == null ? (instance = new GiftcardController()) : instance;
    }

    public GiftcardController() {
        webService = ServiceCreator.creatService();
        myPreferences = MyPrefrences.getInstance();
        companyList = new ArrayList<>();
        requestHandler = new RequestHandler(this);
        downloadMainView(null);
        map = new HashMap<>();
        giftcardsOnSale = myPreferences.hasKey(Constants.KEY_MY_GC_ON_SALE) ?
                (List<Giftcard>) myPreferences.getObject(Constants.KEY_MY_GC_ON_SALE, new TypeToken<List<Giftcard>>() {
                }) : new ArrayList<Giftcard>();

        giftcardsPurchased = myPreferences.hasKey(Constants.KEY_MY_GC_PURCHASED) ?
                (List<Giftcard>) myPreferences.getObject(Constants.KEY_MY_GC_PURCHASED, new TypeToken<List<Giftcard>>() {
                }) : new ArrayList<Giftcard>();
    }

    public Giftcard getGiftcard(int id) {
        return map.get(id);
    }

    public List<Giftcard> getAllGiftcards() {
        return new ArrayList<>(map.values());
    }

    @DebugLog
    public Company getCompany(int id) {
        for (Company c : companyList) {
            if (c.getCompanyId() == id)
                return c;
        }
        return null;
    }

    @DebugLog
    public List<Company> getMainView(Context context) {
        if (companyList.isEmpty()) {
            downloadMainView(context);
        }
        return Collections.unmodifiableList(companyList);
    }


    private void downloadMainView(Context context) {
        requestHandler.enqueueRequest(webService.getMainView(), context);
    }

    @DebugLog
    public List<Giftcard> getMyGiftcardForSale() {
        return Collections.unmodifiableList(giftcardsOnSale);
    }

    @DebugLog
    public List<Giftcard> getMyGiftcardPurchased() {
        return Collections.unmodifiableList(giftcardsPurchased);
    }

    @DebugLog
    public void addPurchased(Giftcard giftcard) {
        giftcardsPurchased.add(giftcard);
        myPreferences.persistObject(Constants.KEY_MY_GC_PURCHASED, giftcardsPurchased);
    }

    @DebugLog
    public void addGiftCardOnSale(Giftcard giftcard) {
        giftcardsOnSale.add(giftcard);
        myPreferences.persistObject(Constants.KEY_MY_GC_ON_SALE, giftcardsOnSale);
    }

    public void setDataUpdateListener(DataUpdateListener listener) {
        this.listener = listener;
    }

    @DebugLog
    @Override
    public void onResponse(Response response, Retrofit retrofit) {
        Log.d(TAG, "onResponse() state:" + response.isSuccess() + "  code:" + response.code() + "  msg:" + response.message());
        if (response.isSuccess()) {
            companyList = (List<Company>) response.body();

            if (listener != null) {
                listener.onNewDataAvailable();
            }
            createmapAsync();
        }
    }

    @DebugLog
    @Override
    public void onFailure(Throwable t) {
        if (listener != null) {
            listener.onNewUpdateFailed();
        }
    }

    private void createmapAsync() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                for (Company c : companyList) {
                    for (Giftcard g : c.getGiftcard()) {
                        map.put(g.getGiftcardId(), g);
                    }
                }
                return null;
            }
        }.execute();
    }

}
