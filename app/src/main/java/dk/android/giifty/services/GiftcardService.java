package dk.android.giifty.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GiftcardService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FECTH_MAIN = "dk.android.giifty.services.action.FOO";
    private static final String ACTION_FETCH_GIFTCARDS = "dk.android.giifty.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "dk.android.giifty.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "dk.android.giifty.services.extra.PARAM2";
    private final WebApi api;

    public GiftcardService() {
        super("GiftcardService");
        api = ServiceCreator.createServiceNoAuthenticator();
    }

    public static void startFecthMain(Context context) {
        Intent intent = new Intent(context, GiftcardService.class);
        intent.setAction(ACTION_FECTH_MAIN);
        context.startService(intent);
    }
    public static void startFetchGiftcards(Context context) {
        Intent intent = new Intent(context, GiftcardService.class);
        intent.setAction(ACTION_FETCH_GIFTCARDS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            if (intent != null) {
                final String action = intent.getAction();
                if (ACTION_FECTH_MAIN.equals(action)) {
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

    private void post(Object response) {
        GiiftyApplication.getBus().post(response);
    }

}
