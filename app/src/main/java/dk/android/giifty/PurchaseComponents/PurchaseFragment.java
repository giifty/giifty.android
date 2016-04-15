package dk.android.giifty.PurchaseComponents;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

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
public abstract class PurchaseFragment extends Fragment implements Callback<Boolean> {
    protected static final String GIFTCARD_ID = "param1";
    protected static final String PRICE = "param2";
    protected static final String ORDER_ID = "orderId";
    protected OnFragmentInteractionListener mListener;

    private WebApi webService;
    private RequestHandler requestHandler;

    public PurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestHandler = new RequestHandler(this);
        webService = ServiceCreator.creatServiceWithAuthenticator();
    }

    public void getOrderId(int giftcardId){
        requestHandler.enqueueRequest(webService.getTransactionOrderId(SignInHandler.getServerToken(), giftcardId), null);
    }

    public void commitPurchaseOnServer(int giftcardId, String transactionId) {
        requestHandler.enqueueRequest(webService.buyGiftcard(SignInHandler.getServerToken(), giftcardId), getContext());
    }

    @Override
    public void onResponse(Response<Boolean> response, Retrofit retrofit) {
        if (response.isSuccess()) {
            if (response.body()) {
                onPurchaseSucces();
            } else {
                // handler error
                onPurchaseFailed();
            }
        } else {
            // handler error
            onPurchaseFailed();
        }
    }

    protected void onPurchaseSucces() {
    }

    protected void onPurchaseFailed() {
    }

    @Override
    public void onFailure(Throwable t) {
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
