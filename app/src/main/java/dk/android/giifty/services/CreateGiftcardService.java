package dk.android.giifty.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.busevents.GiftcardCreatedEvent;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.model.GiftcardRequest;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.WebApi;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

public class CreateGiftcardService extends IntentService {

    private static final String EXTRA_GC_REQUEST = "dk.android.giifty.services.CreateGiftcardService.extra.PARAM1";
    private static final String TAG = CreateGiftcardService.class.getSimpleName();
    private final WebApi api;

    public CreateGiftcardService() {
        super("GiftcardService");
        api = ServiceCreator.createServiceWithAuthenticator();
    }

    public static void createGiftcard(Context context, GiftcardRequest request) {
        Intent intent = new Intent(context, CreateGiftcardService.class);
        intent.putExtra(EXTRA_GC_REQUEST, request);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent()");
        if (intent != null) {
            GiftcardRequest request = (GiftcardRequest) intent.getSerializableExtra(EXTRA_GC_REQUEST);
            GiftcardCreatedEvent event = new GiftcardCreatedEvent(null, false);
            RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), new File(request.getGcImagePath()));
            RequestBody barcode = RequestBody.create(MediaType.parse("image/jpg"), new File(request.getBarcodeImagePath()));
            try {
                Response<Giftcard> response = api.createGiftcardWithImage(SignInHandler.getServerToken(), image, barcode, request.getProperties()).execute();
                Log.d(TAG, "isSuccessFul:" + response.isSuccessful());
                if (!response.isSuccessful()) {
                    //TODO stop sequence
                }
                event = new GiftcardCreatedEvent(response.body(), response.isSuccessful());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                post(event);
            }
        }
    }

    private void post(final GiftcardCreatedEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GiiftyApplication.getBus().post(event);
            }
        });
    }

}