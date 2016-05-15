package dk.android.giifty.purchase;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.squareup.otto.Subscribe;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.busevents.GiftcardPurchasedEvent;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.services.PurchaseService;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class PurchaseFragment extends Fragment  {
    public static final String GIFTCARD_ID = "param1";
    public static final String PRICE = "param2";
    private static final String TAG = PurchaseFragment.class.getSimpleName();
    protected OnPurchaseFragmentInteraction parentInteraction;

    public PurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        GiiftyApplication.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GiiftyApplication.getBus().unregister(this);
    }

    public void startTransaction() {
        Log.d(TAG, "startTransaction()");
    }

    public void commitPurchaseOnServer(int giftcardId, String transactionId) {
        PurchaseService.purchaseGiftcard(getContext(), giftcardId);
    }

    @Subscribe
    public void onGiftcardPurchased(GiftcardPurchasedEvent event) {
        if (event.isSuccessFul) {
            GiftcardRepository gcRepo = GiftcardRepository.getInstance();
            Giftcard giftcard = event.giftcard;
            gcRepo.removeGiftcardFromCompanyList(giftcard);
            gcRepo.addPurchased(giftcard);
            onPurchaseSuccess(giftcard.getGiftcardId());
        } else {
            // handler error
            onPurchaseFailed();
        }
    }

    protected void onPurchaseSuccess(int giftcardId) {
        ActivityStarter
                .startPurchaseSuccessAct(getActivity(), giftcardId);
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

        void onFragmentInteraction(Uri uri);

        String getOrderId();
    }
}
