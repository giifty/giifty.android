package android.giifty.dk.giifty.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.giifty.dk.giifty.R;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by mak on 11-01-2016.
 */
public class HelperMethods {


    private static final String TAG = HelperMethods.class.getSimpleName();

    public static void setImage(Context context, ImageView imageView, String imageUrl) {
        Picasso.with(context).load(imageUrl).into(imageView);
    }

    public static void startActivityWithSlideIn(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.animator.slide_from_left, R.animator.fade_out);
    }

    public static void startActivityWithHero(Activity activity, View transitionView, Intent intent, String transitionName) {
        Log.d(TAG, "startActivityWithHero()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, transitionView, transitionName);
            activity.startActivity(intent, optionsCompat.toBundle());
        } else {
            activity.startActivity(intent);
        }
    }
}
