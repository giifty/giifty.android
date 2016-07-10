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
import dk.android.giifty.busevents.MyGiftcardsFetchedEvent;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.WebApi;
import retrofit2.Response;

public class MyGiftcardService extends IntentService {
    private static final String TAG = MyGiftcardService.class.getSimpleName();
    private final WebApi api;

    public MyGiftcardService() {
        super(TAG);
        api = ServiceCreator.createServiceNoAuthenticator();
    }

    public static void fetchMyGiftcards(Context context) {
        Intent intent = new Intent(context, MyGiftcardService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MyGiftcardsFetchedEvent event = new MyGiftcardsFetchedEvent(null, false);
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

    private MyGiftcardsFetchedEvent fetchMyGiftcards() throws IOException {
        Response<List<Giftcard>> response = api.getMyGiftcards(SignInHandler.getServerToken()).execute();

        Log.d(TAG, "isSuccessFul:" + response.isSuccessful());

        if (!response.isSuccessful()) {
            Log.d(TAG, "error msg:" + response.message());
            return new MyGiftcardsFetchedEvent(null, false);
        }

        GiiftyPreferences.getInstance().persistMyGiftcards(response.body());
        return new MyGiftcardsFetchedEvent(response.body(), true);
    }

    private void post(final MyGiftcardsFetchedEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GiiftyApplication.getBus().post(event);
            }
        });
    }
}
