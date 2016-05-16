package dk.android.giifty.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;

import dk.android.giifty.CreateGiftcardActivity;
import dk.android.giifty.CreateUserActivity;
import dk.android.giifty.FrontPageActivity;
import dk.android.giifty.GiftcardDetailsActivity;
import dk.android.giifty.PaymentActivity;
import dk.android.giifty.PurchaseSuccessActivity;
import dk.android.giifty.R;
import dk.android.giifty.UpdateUserActivity;

/**
 * Created by mak on 13-02-2016.
 */
public class ActivityStarter {
    private static final String TAG = ActivityStarter.class.getSimpleName();


    public static void startUpdateUserActivity(Activity activity) {
        startActivityWithSlideIn(activity, new Intent(activity, UpdateUserActivity.class));
    }

    public static void startCreateUserActivity(Activity activity) {
        startActivityWithSlideIn(activity, new Intent(activity, CreateUserActivity.class));
    }

    public static void startCreateUserActivityNoAni(Context context, int giftcardId) {
        context.startActivity(new Intent(context, CreateUserActivity.class).putExtra(Constants.EKSTRA_GIFTCARD_ID, giftcardId));
    }
    public static void startCreateUserActivityNoAni(Context context) {
        context.startActivity(new Intent(context, CreateUserActivity.class));
    }

    public static void startGiftCardDetails(Activity activity, View transitionView, int giftcardId) {
        Intent intent = new Intent(activity, GiftcardDetailsActivity.class);
        intent.putExtra(Constants.EKSTRA_GIFTCARD_ID, giftcardId);
        startActivityWithSlideIn(activity, intent);
        //  startActivityWithHero(activity, transitionView, intent, activity.getString(R.string.hero_transition_giftcard));
    }

    public static void startCreateGiftcardActivity(Activity activity, int companyId) {
        Intent intent = new Intent(activity, CreateGiftcardActivity.class);
        intent.putExtra(Constants.EKSTRA_COMPANY_ID, companyId);
        startActivityWithSlideIn(activity, intent);
    }

    public static void startPaymentActivity(Activity activity, int giftcardId) {
        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(Constants.EKSTRA_GIFTCARD_ID, giftcardId);
        startActivityWithSlideIn(activity, intent);
    }

    public static void startPurchaseSuccessAct(Activity activity, int giftcardId) {
        Intent intent = new Intent(activity, PurchaseSuccessActivity.class);
        intent.putExtra(Constants.EKSTRA_GIFTCARD_ID, giftcardId);
        startActivityWithSlideIn(activity, intent);
    }

    public static void startFrontPageActivityAsBackPressed(Activity activity) {
        activity.startActivity(new Intent(activity, FrontPageActivity.class));
        activity.overridePendingTransition(R.animator.fade_in, R.animator.slide_out_right);
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
