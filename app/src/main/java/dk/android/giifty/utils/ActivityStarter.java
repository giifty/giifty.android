package dk.android.giifty.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;

import dk.android.giifty.CreateBarcodeActivity;
import dk.android.giifty.CreateImageActivity;
import dk.android.giifty.CreateUserActivity;
import dk.android.giifty.FrontPageActivity;
import dk.android.giifty.GiftcardDetailsActivity;
import dk.android.giifty.PaymentActivity;
import dk.android.giifty.PriceAndDescriptionActivity;
import dk.android.giifty.PaymentSuccessActivity;
import dk.android.giifty.R;
import dk.android.giifty.ReviewActivity;
import dk.android.giifty.UpdateUserActivity;
import dk.android.giifty.model.Company;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.model.GiftcardRequest;

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

    public static void startCreateImageAct(Activity activity, GiftcardRequest giftcardRequest) {
        Intent intent = new Intent(activity, CreateImageActivity.class);
        intent.putExtra(Constants.EXTRA_GC_REQUEST, giftcardRequest);
        startActivityWithSlideIn(activity, intent);
    }

    public static void startCreateImageActClearTop(Activity activity, GiftcardRequest giftcardRequest) {
        Intent intent = new Intent(activity, CreateImageActivity.class);
        intent.putExtra(Constants.EXTRA_GC_REQUEST, giftcardRequest);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityWithSlideIn(activity, intent);
    }

    public static void startPriceAndDescriptionActivity(Activity activity, GiftcardRequest giftcardRequest) {
        Intent intent = new Intent(activity, PriceAndDescriptionActivity.class);
        intent.putExtra(Constants.EXTRA_GC_REQUEST, giftcardRequest);
        startActivityWithSlideIn(activity, intent);
    }

    public static void startReviewActivity(Activity activity, GiftcardRequest giftcardRequest) {
        Intent intent = new Intent(activity, ReviewActivity.class);
        intent.putExtra(Constants.EXTRA_GC_REQUEST, giftcardRequest);
        startActivityWithSlideIn(activity, intent);
    }

    public static void startGiftCardDetails(Activity activity, View transitionView, int giftcardId) {
        Intent intent = new Intent(activity, GiftcardDetailsActivity.class);
        intent.putExtra(Constants.EKSTRA_GIFTCARD_ID, giftcardId);
        startActivityWithSlideIn(activity, intent);
        //  startActivityWithHero(activity, transitionView, intent, activity.getString(R.string.hero_transition_giftcard));
    }

    public static void startCreateGiftcardActivity(Activity activity, Company company) {
        Intent intent = new Intent(activity, CreateBarcodeActivity.class);
        intent.putExtra(Constants.EKSTRA_COMPANY, company);
        startActivityWithSlideIn(activity, intent);
    }

    public static void startPaymentActivity(Activity activity, int giftcardId) {
        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(Constants.EKSTRA_GIFTCARD_ID, giftcardId);
        startActivityWithSlideIn(activity, intent);
    }

    public static void startPurchaseSuccessAct(Activity activity, Giftcard giftcard) {
        Intent intent = new Intent(activity, PaymentSuccessActivity.class);
        intent.putExtra(Constants.EKSTRA_GIFTCARD, giftcard);
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
