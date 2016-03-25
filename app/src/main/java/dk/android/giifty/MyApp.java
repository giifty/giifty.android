package dk.android.giifty;

import android.app.Application;
import android.content.Context;

import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.user.UserController;
import dk.android.giifty.utils.MyPreferences;
import dk.android.giifty.utils.Utils;

/**
 * Created by mak on 14-02-2016.
 */
public class MyApp extends Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApp.applicationContext = getApplicationContext();

        MyPreferences.getInstance().setContext(this);
        UserController.getInstance().initController(this);
        GiftcardRepository.getInstance().initController(this);
        Utils.printHasH(applicationContext);
    }


    public static Context getMyApplicationContext() {
        return applicationContext;
    }
}
