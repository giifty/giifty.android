package dk.android.giifty.purchase;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Utils;
import dk.android.giifty.web.RequestHandler;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.SignInHandler;
import dk.android.giifty.web.WebApi;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class PurchaseFragment extends Fragment implements Callback<Integer> {
    public static final String GIFTCARD_ID = "param1";
    public static final String PRICE = "param2";
    private static final String TAG = PurchaseFragment.class.getSimpleName();
    protected OnPurchaseFragmentInteraction parentInteraction;
    protected WebApi webService;
    protected RequestHandler requestHandler;

    public PurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestHandler = new RequestHandler(this);
        webService = ServiceCreator.creatServiceWithAuthenticator();
    }

    public void startTransaction() {
        Log.d(TAG, "startTransaction()");
    }

    public void commitPurchaseOnServer(int giftcardId, String transactionId) {
        requestHandler.enqueueRequest(webService.buyGiftcard(SignInHandler.getServerToken(), giftcardId), getContext());
    }

    @Override
    public void onResponse(Response<Integer> response, Retrofit retrofit) {

        if (response.isSuccess()) {
            //TODO what value is returned, i want the giftcard id
            onPurchaseSuccess(response.body());
            // if (response.body()) {
            //    onPurchaseSuccess();
            //   } else {
            // handler error
            //     onPurchaseFailed();
            //  }
        } else {
            // handler error
            onPurchaseFailed();
        }
    }

    protected void onPurchaseSuccess(int gifcardId) {
        ActivityStarter
                .startPurchaseSuccessAct(getActivity(), gifcardId);
    }

    protected void onPurchaseFailed() {
        Utils.makeToast("Purchase failed");
    }

    protected String getOrderId() {
        return parentInteraction.getOrderId();
    }

    protected boolean hasOrderId() {
        return parentInteraction.getOrderId() != null;
    }

    @Override
    public void onFailure(Throwable t) {
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
