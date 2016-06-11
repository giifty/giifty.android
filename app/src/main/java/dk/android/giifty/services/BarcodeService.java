package dk.android.giifty.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.barcode.ScanResult;
import dk.android.giifty.busevents.BarcodeReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BarcodeService extends IntentService {


    private static final String EXTRA_SCAN_RESULT = "scanResult";
    private static final String IMAGE_SIZE = "400";
    private final OkHttpClient client;

    public BarcodeService() {
        super("testSetrvice");
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public static void createEAN13(Context context, ScanResult scanResult) {
        Intent intent = new Intent(context, BarcodeService.class);
        intent.putExtra(EXTRA_SCAN_RESULT, scanResult);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ScanResult scanResult = intent.getParcelableExtra(EXTRA_SCAN_RESULT);

        String url = new StringBuilder()
                .append("http://www.scandit.com/barcode-generator/?symbology=")
                .append(scanResult.symbologyName)
                .append("&valueDataBinding=")
                .append(scanResult.barcodeNumber)
                .append("&ec=L&size=")
                .append(IMAGE_SIZE).toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Bitmap barcodeImage;
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                barcodeImage = BitmapFactory.decodeStream(response.body().byteStream());
                post(new BarcodeReceivedEvent(barcodeImage, true));
            } else {
                post(new BarcodeReceivedEvent( null, true));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void post(final BarcodeReceivedEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                GiiftyApplication.getBus().post(event);
            }
        });
    }
}
