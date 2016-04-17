package dk.android.giifty.Purchase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import dk.android.giifty.R;

/**
 * Created by mak on 17-04-2016.
 */
public class PurchaseFragmentHandler {

    private FragmentManager manager;
    private String currentFragmentTag = "";
    private Context context;

    public PurchaseFragmentHandler(FragmentManager manager, Context context) {
        this.manager = manager;
        this.context = context;
    }

    public void showFragment(String tag, int giftcardId, int price) {
        if (!currentFragmentTag.contentEquals(tag)) {
            PurchaseFragment fragment = getFragment(tag);
            if (fragment == null) {
                Bundle b = new Bundle();
                b.putInt(PurchaseFragment.GIFTCARD_ID, giftcardId);
                b.putInt(PurchaseFragment.PRICE, price);
                fragment = getNewInstance(tag, b);
            }
            showFragment(fragment);
        }
    }

    private PurchaseFragment getNewInstance(String tag, Bundle bundle) {
        return (PurchaseFragment) Fragment.instantiate(context, tag, bundle);
//        if (tag.contentEquals(MobilepayFrag.class.getName())) {
//            return MobilepayFrag.newInstance(3, 3);
//        } else if (tag.contentEquals(CardPaymentFrag.class.getName())) {
//            return CardPaymentFrag.newInstance(3, 3);
//        }
//        return CardPaymentFrag.newInstance(3, 3);
    }

    private void showFragment(PurchaseFragment fragment) {
        currentFragmentTag = fragment.getClass().getName();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container_id, fragment, currentFragmentTag);
        transaction.commit();
    }

    public PurchaseFragment getCurrentFragment() {
        return getFragment(currentFragmentTag);
    }

    private PurchaseFragment getFragment(String tag) {
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment != null) {
            if (fragment instanceof PurchaseFragment) {
                return (PurchaseFragment) fragment;
            }
        }
        return null;
    }
}
