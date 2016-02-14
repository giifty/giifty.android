package android.giifty.dk.giifty;

import android.app.Application;
import android.giifty.dk.giifty.user.UserController;
import android.giifty.dk.giifty.utils.MyPrefrences;

/**
 * Created by mak on 14-02-2016.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyPrefrences.getInstance().setContext(this);
        UserController.getInstance().initController(this);
    }
}
