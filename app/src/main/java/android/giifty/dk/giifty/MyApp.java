package android.giifty.dk.giifty;

import android.app.Application;
import android.content.Context;
import android.giifty.dk.giifty.giftcard.GiftcardRepository;
import android.giifty.dk.giifty.user.UserController;
import android.giifty.dk.giifty.utils.MyPreferences;

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
    }


    public static Context getMyApplicationContext() {
        return applicationContext;
    }
}
