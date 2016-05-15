package dk.android.giifty;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.utils.Utils;
import dk.danskebank.mobilepay.sdk.Country;
import dk.danskebank.mobilepay.sdk.MobilePay;

/**
 * Created by mak on 14-02-2016.
 */
public class GiiftyApplication extends Application {

    private static Context applicationContext;
    private static Bus bus = new Bus(ThreadEnforcer.MAIN);

    @Override
    public void onCreate() {
        super.onCreate();
        GiiftyApplication.applicationContext = getApplicationContext();

        GiiftyPreferences.getInstance().setContext(this);
        GiftcardRepository.getInstance().initController();
        Utils.printHasH(applicationContext);

        MobilePay.getInstance().init("APPDK0000000000", Country.DENMARK);
    }

    public static Bus getBus() {
        return bus;
    }

    public static Context getMyApplicationContext() {
        return applicationContext;
    }
}
