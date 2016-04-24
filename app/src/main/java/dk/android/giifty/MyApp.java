package dk.android.giifty;

import android.app.Application;
import android.content.Context;

import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.user.UserRepository;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.utils.Utils;
import dk.danskebank.mobilepay.sdk.Country;
import dk.danskebank.mobilepay.sdk.MobilePay;

/**
 * Created by mak on 14-02-2016.
 */
public class MyApp extends Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApp.applicationContext = getApplicationContext();

        GiiftyPreferences.getInstance().setContext(this);
        UserRepository.getInstance().initController();
        GiftcardRepository.getInstance().initController();
        Utils.printHasH(applicationContext);

        MobilePay.getInstance().init("APPDK0000000000", Country.DENMARK);
    }


    public static Context getMyApplicationContext() {
        return applicationContext;
    }
}
