package dk.android.giifty;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.services.MyGiftcardService;
import dk.android.giifty.services.MyPurchasedGiftcardService;
import dk.android.giifty.utils.Utils;
import dk.danskebank.mobilepay.sdk.Country;
import dk.danskebank.mobilepay.sdk.MobilePay;

public class GiiftyApplication extends Application {

    private static Context applicationContext;
    private static Bus bus = new Bus(ThreadEnforcer.MAIN);

    @Override
    public void onCreate() {
        super.onCreate();
        GiiftyApplication.applicationContext = getApplicationContext();

        GiftcardRepository.getInstance().initController();
        Utils.printHasH(applicationContext);

        //TODO get real licence for MP
        MobilePay.getInstance().init("APPDK0000000000", Country.DENMARK);

        MyGiftcardService.fetchMyGiftcards(applicationContext);
        MyPurchasedGiftcardService.fetchMyPurchasedGiftcards(applicationContext);

    }

    public static Bus getBus() {
        return bus;
    }

    public static Context getMyApplicationContext() {
        return applicationContext;
    }
}
