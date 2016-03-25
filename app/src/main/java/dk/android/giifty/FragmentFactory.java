package dk.android.giifty;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by mak on 30-01-2016.
 */
public class FragmentFactory {

    public static Fragment createFragment(int id, Context context) {

        switch (id) {
            case R.id.nav_buy_giftcards:
                return BuyGiftcardFrag.newInstance("4", "4");
            case R.id.nav_create_giftcards:
                return BuyGiftcardFrag.newInstance("4", "4");
            case R.id.nav_bought_giftcards:
                return Fragment.instantiate(context, PurchasedGiftcardsFrag.class.getName());
            case R.id.nav_my_giftcards:
                return Fragment.instantiate(context, MyGiftcardsFrag.class.getName());
            case R.id.nav_nav_settings:
                return Fragment.instantiate(context, SettingsFrag.class.getName());
            default:
                return BuyGiftcardFrag.newInstance("4", "4");
        }
    }
}
