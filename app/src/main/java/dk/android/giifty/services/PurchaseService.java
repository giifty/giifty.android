package dk.android.giifty.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.busevents.GiftcardPurchasedEvent;
import dk.android.giifty.busevents.OrderIdFetchedEvent;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.WebApi;
import retrofit2.Response;


public class PurchaseService extends IntentService {
    public static final String EXTRA_GIFTCARD_ID = "dk.android.giifty.services.extra.GIFTCARD_ID";
    public static final String ACTION_RESERVE_GC = "reserveGc";
    public static final String ACTION_PURCHASE_GC = "purchaseGc";
    private static final String TAG = PurchaseService.class.getSimpleName();
    private WebApi api;

    public static void purchaseGiftcard(Context context, int giftcardId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_GIFTCARD_ID, giftcardId);
        intent.setAction(ACTION_PURCHASE_GC);
        intent.setClass(context, PurchaseService.class);
        context.startService(intent);
    }

    public static void reserveGiftcard(Context context, int giftcardId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_GIFTCARD_ID, giftcardId);
        intent.setAction(ACTION_RESERVE_GC);
        intent.setClass(context, PurchaseService.class);
        context.startService(intent);
    }

    public PurchaseService() {
        super("PurchaseService");
        api = ServiceCreator.createServiceWithAuthenticator();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int id = intent.getIntExtra(EXTRA_GIFTCARD_ID, -1);
        try {
            if (intent.getAction().contentEquals(ACTION_RESERVE_GC)) {
                reserveGiftcard(id);
            } else if (intent.getAction().contentEquals(ACTION_PURCHASE_GC)) {
                purchaseGiftcard(id);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reserveGiftcard(int giftcardId) throws IOException {
        Log.d(TAG, "reserveGiftcard() giftcardId:" + giftcardId);
        Response<String> response = api.getTransactionOrderId(SignInHandler.getServerToken(), giftcardId).execute();
        if (response.isSuccessful()) {
            postReservationEvent(new OrderIdFetchedEvent(response.body(), true));
        } else {
            postReservationEvent(new OrderIdFetchedEvent(null, false));
        }
    }

    private void purchaseGiftcard(int giftcardId) throws IOException {
        Log.d(TAG, "purchaseGiftcard() giftcardId:" + giftcardId);
        Response<Giftcard> response = api.buyGiftcard(SignInHandler.getServerToken(), giftcardId).execute();

        if (!response.isSuccessful()) {
            postPurchaseEvent(new GiftcardPurchasedEvent(null, false));
            return;
        }

        Giftcard giftcard = response.body();
        GiiftyPreferences.getInstance().addPurchased(giftcard);
        postPurchaseEvent(new GiftcardPurchasedEvent(giftcard, true));
    }

    private void postPurchaseEvent(final GiftcardPurchasedEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GiiftyApplication.getBus().post(event);
            }
        });
    }

    private void postReservationEvent(final OrderIdFetchedEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GiiftyApplication.getBus().post(event);
            }
        });
    }
}
