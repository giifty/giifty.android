package android.giifty.dk.giifty.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by mak on 11-01-2016.
 */
public class HelperMethods {


    public static void setImage(Context context, ImageView imageView, String imageUrl) {
        Picasso.with(context).load(imageUrl).into(imageView);
    }

    public static void startActivityWithHero(Activity activity, View transitionView, Intent intent, String transitionName) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, transitionView, transitionName);
            activity.startActivity(intent, optionsCompat.toBundle());
        }else {
            activity.startActivity(intent);
        }
    }
}
