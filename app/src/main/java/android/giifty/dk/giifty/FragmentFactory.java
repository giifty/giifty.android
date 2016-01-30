package android.giifty.dk.giifty;

import android.support.v4.app.Fragment;

/**
 * Created by mak on 30-01-2016.
 */
public class FragmentFactory {

    public static Fragment getFragmentByID(int id) {

        switch (id) {
            case R.id.nav_buy_giftcards:
                return BuyGiftcardFrag.newInstance("4", "4");
            case R.id.nav_create_giftcards:
                return BuyGiftcardFrag.newInstance("4", "4");
            case R.id.nav_bought_giftcards:
                return BuyGiftcardFrag.newInstance("4", "4");
            case R.id.nav_my_giftcards:
                return BuyGiftcardFrag.newInstance("4", "4");
            case R.id.nav_nav_settings:
                return BuyGiftcardFrag.newInstance("4", "4");
            default:
                return BuyGiftcardFrag.newInstance("4", "4");
        }
    }
}
