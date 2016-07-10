package dk.android.giifty.purchase;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import dk.android.giifty.model.Giftcard;
import dk.android.giifty.services.PurchaseService;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class PurchaseFragment extends Fragment {
    public static final String GIFTCARD_ID = "giftcardId";
    public static final String PRICE = "price";
    private static final String TAG = PurchaseFragment.class.getSimpleName();
    protected OnPurchaseFragmentInteraction parentInteraction;

    public PurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void startTransaction() {
        Log.d(TAG, "startTransaction()");
    }

    public void commitPurchaseOnServer(int giftcardId, String transactionId) {
         PurchaseService.purchaseGiftcard(getContext(), giftcardId);
    }

    public void onPurchaseSuccess(Giftcard giftcard) {
        ActivityStarter
                .startPurchaseSuccessAct(getActivity(), giftcard);
    }

    protected void onPurchaseFailed() {
        Utils.makeToast("Købet fejlet");
    }

    protected String getOrderId() {
        return parentInteraction.getOrderId();
    }

    protected boolean hasOrderId() {
        return parentInteraction.getOrderId() != null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPurchaseFragmentInteraction) {
            parentInteraction = (OnPurchaseFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPurchaseFragmentInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentInteraction = null;
    }

    public interface OnPurchaseFragmentInteraction {
        String getOrderId();
    }
}
