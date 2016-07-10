package dk.android.giifty.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.busevents.PurchasedGiftcardsFetchedEvent;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.WebApi;
import retrofit2.Response;

public class MyPurchasedGiftcardService extends IntentService {
    private static final String TAG = MyPurchasedGiftcardService.class.getSimpleName();
    private final WebApi api;

    public MyPurchasedGiftcardService() {
        super(TAG);
        api = ServiceCreator.createServiceWithAuthenticator();
    }

    public static void fetchMyPurchasedGiftcards(Context context) {
        Intent intent = new Intent(context, MyPurchasedGiftcardService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        PurchasedGiftcardsFetchedEvent event = new PurchasedGiftcardsFetchedEvent(null, false);
        try {
            if (intent != null) {
                event = fetchMyGiftcards();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            post(event);
        }
    }

    private PurchasedGiftcardsFetchedEvent fetchMyGiftcards() throws IOException {
        Response<List<Giftcard>> response = api.getMyPurchasedGiftcards(SignInHandler.getServerToken()).execute();

        Log.d(TAG, "isSuccessFul:" + response.isSuccessful());

        if (!response.isSuccessful()) {
            Log.d(TAG, "error msg:" + response.message());
            return new PurchasedGiftcardsFetchedEvent(null, false);
        }

        GiiftyPreferences.getInstance().persistPurchasedGiftcards(response.body());
        return new PurchasedGiftcardsFetchedEvent(response.body(), true);
    }

    private void post(final PurchasedGiftcardsFetchedEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GiiftyApplication.getBus().post(event);
            }
        });
    }
}
