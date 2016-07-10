package dk.android.giifty;

import android.content.Context;
import android.support.v4.app.Fragment;

import dk.android.giifty.drawer.drawerfragments.BuyGiftcardFrag;
import dk.android.giifty.drawer.drawerfragments.CreateNewGiftcardFrag;
import dk.android.giifty.drawer.drawerfragments.MyGiftcardsFrag;
import dk.android.giifty.drawer.drawerfragments.PurchasedGiftcardsFrag;
import dk.android.giifty.drawer.drawerfragments.SettingsFrag;


public class FragmentFactory {

    public static Fragment createFragment(int id, Context context) {

        switch (id) {
            case R.id.nav_buy_giftcards:
                return new BuyGiftcardFrag();
            case R.id.nav_create_giftcards:
                return Fragment.instantiate(context, CreateNewGiftcardFrag.class.getName());
            case R.id.nav_bought_giftcards:
                return Fragment.instantiate(context, PurchasedGiftcardsFrag.class.getName());
            case R.id.nav_my_giftcards:
                return Fragment.instantiate(context, MyGiftcardsFrag.class.getName());
            case R.id.nav_nav_settings:
                return Fragment.instantiate(context, SettingsFrag.class.getName());
            default:
                return new BuyGiftcardFrag();
        }
    }
}
