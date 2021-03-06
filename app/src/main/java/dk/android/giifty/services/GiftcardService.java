package dk.android.giifty.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.List;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.busevents.CompaniesFetchedEvent;
import dk.android.giifty.busevents.GiftcardsFetchedEvent;
import dk.android.giifty.model.Company;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.WebApi;
import retrofit2.Response;

public class GiftcardService extends IntentService {

    private static final String ACTION_FETCH_MAIN = "dk.android.giifty.services.action.FETCH_MAIN";
    private static final String ACTION_FETCH_GIFTCARDS = "dk.android.giifty.services.action.FETCH_GIFTCARDS";
    private static final String TAG = GiftcardService.class.getSimpleName();

    private final WebApi api;

    public GiftcardService() {
        super(TAG);
        api = ServiceCreator.createServiceNoAuthenticator();
    }

    public static void fetchMainView(Context context) {
        Intent intent = new Intent(context, GiftcardService.class);
        intent.setAction(ACTION_FETCH_MAIN);
        context.startService(intent);
    }
    public static void fetchGiftcards(Context context) {
        Intent intent = new Intent(context, GiftcardService.class);
        intent.setAction(ACTION_FETCH_GIFTCARDS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            if (intent != null) {
                final String action = intent.getAction();
                if (ACTION_FETCH_MAIN.equals(action)) {
                    fetchMainView();
                } else if (ACTION_FETCH_GIFTCARDS.equals(action)) {
                    fetchGiftcardsForSale();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchGiftcardsForSale() throws IOException {
        Response<List<Giftcard>> response = api.getAllGiftCards().execute();
        if (response.isSuccessful()) {
            post(new GiftcardsFetchedEvent(response.body(), true));
        } else {
            post(new GiftcardsFetchedEvent(null, false));
        }
    }
    private void fetchMainView() throws IOException {

        Response<List<Company>> response = api.getMainView().execute();
        if (response.isSuccessful()) {
            post(new CompaniesFetchedEvent(response.body(), true));
        } else {
            post(new CompaniesFetchedEvent(null, false));
        }
    }

    private void post(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GiiftyApplication.getBus().post(event);
            }
        });
    }

}
