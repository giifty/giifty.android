package android.giifty.dk.giifty.giftcard;

import android.giifty.dk.giifty.components.DataUpdateListener;
import android.giifty.dk.giifty.model.Company;
import android.giifty.dk.giifty.model.Giftcard;
import android.giifty.dk.giifty.web.ServiceCreator;
import android.giifty.dk.giifty.web.WebApi;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mak on 16-01-2016.
 */
public class GiftcardController implements Callback {

    private static final String TAG = GiftcardController.class.getSimpleName();
    private static GiftcardController instance;
    private final WebApi webApi;
    private List<Company> companyList;
    private DataUpdateListener listener;
    private HashMap<Integer, Giftcard> map;

    public static GiftcardController getInstance() {
        return instance == null ? (instance = new GiftcardController()) : instance;
    }

    public GiftcardController() {
        webApi = ServiceCreator.creatService();
        companyList = new ArrayList<>();
        downloadMainView();
        map = new HashMap<>();
    }

    public void createGiftCard() {

    }

    public Giftcard getGiftcard(int id){
        return map.get(id);
    }

    public List<Giftcard> getAllGiftcards() {
      return new ArrayList<>(map.values());
    }

    public Company getCompany(int id) {
        for (Company c : companyList) {
            if (c.getCompanyId() == id)
                return c;
        }
        return null;
    }

    public List<Company> getMainView() {
        Log.d(TAG, " getMainView()");
        if (companyList.isEmpty()) {
            downloadMainView();
        }
        return Collections.unmodifiableList(companyList);
    }

    private void downloadMainView() {
        Log.d(TAG, " downloadMainView()");
        Call<List<Company>> request = webApi.getMainView();
        request.enqueue(this);
    }

    private void downloadAllGiftcards() {

    }

    public void setDataUpdateListener(DataUpdateListener listener) {
        this.listener = listener;
    }

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

    @Override
    public void onFailure(Throwable t) {
        Log.d(TAG, "onFailure() msg: " + t.getMessage());
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
