package dk.android.giifty.purchase.purchasefragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.math.BigDecimal;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.R;
import dk.android.giifty.busevents.GiftcardPurchasedEvent;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.purchase.PurchaseFragment;
import dk.android.giifty.utils.Utils;
import dk.danskebank.mobilepay.sdk.MobilePay;
import dk.danskebank.mobilepay.sdk.ResultCallback;
import dk.danskebank.mobilepay.sdk.model.FailureResult;
import dk.danskebank.mobilepay.sdk.model.Payment;
import dk.danskebank.mobilepay.sdk.model.SuccessResult;

public class MobilepayFrag extends PurchaseFragment {

    private static final String TAG = MobilepayFrag.class.getSimpleName();
    private int giftcardId, price;
    private static final int MOBILEPAY_PAYMENT_REQUEST_CODE = 9889;
    private MobilePay mobilePay;

    public MobilepayFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            giftcardId = getArguments().getInt(GIFTCARD_ID);
            price = getArguments().getInt(PRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mobilepay, container, false);
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

    @Subscribe
    public void onGiftcardPurchased(GiftcardPurchasedEvent event) {
        if (event.isSuccessFul) {
            Giftcard giftcard = event.giftcard;
            onPurchaseSuccess(giftcard);
        } else {
            // handler error
            onPurchaseFailed();
        }
    }

    @Override
    public void startTransaction() {
        super.startTransaction();
        checkMobilepay();
    }

    private void checkMobilepay() {
        mobilePay = MobilePay.getInstance();
        if (mobilePay.isMobilePayInstalled(getContext())) {
            if (hasOrderId()) {
                startMobilepay(getOrderId());
            } else {
                Utils.makeToast("Beklager, Prøv igen :)");
            }
        } else {
            // MobilePay is not installed. Use the SDK to create an Intent to take the user to Google Play and download MobilePay.
            Intent intent = mobilePay.createDownloadMobilePayIntent(getContext());
            startActivity(intent);
        }
    }

    private void startMobilepay(String orderId) {
        Log.d(TAG, "startMobilepay() orderId:" + orderId);
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        BigDecimal p = BigDecimal.valueOf(price);
        payment.setProductPrice(p);

        // Create a payment Intent using the Payment object from above.
        Intent paymentIntent = MobilePay.getInstance().createPaymentIntent(payment);
        startActivityForResult(paymentIntent, MOBILEPAY_PAYMENT_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() resultCode:" + resultCode);
        if (requestCode == MOBILEPAY_PAYMENT_REQUEST_CODE) {
            mobilePay.handleResult(resultCode, data, new ResultCallback() {
                @Override
                public void onSuccess(SuccessResult successResult) {
                    commitPurchaseOnServer(giftcardId, successResult.getTransactionId());
                }

                @Override
                public void onFailure(FailureResult failureResult) {
                    errorHandling(failureResult);
                }

                @Override
                public void onCancel() {
                    //user cancelled transaction
                }
            });
        }
    }

    // https://github.com/DanskeBank/MobilePay-AppSwitch-SDK/wiki/Error-handling
    private void errorHandling(FailureResult failureResult) {
        int errorCode = failureResult.getErrorCode();
        Log.d(TAG, "errorHandling code:" + errorCode + "msg:" + failureResult.getErrorMessage());

        switch (errorCode) {
            case MobilepayErrors.MOBILEPAY_MUST_BE_UPDATED:
                Utils.makeToast(getString(R.string.msg_mobilepay_needs_update));
            case MobilepayErrors.MOBILEPAY_MAX_AMOUNT_REACHED:
                Utils.makeToast(getString(R.string.msg_mobilepay_max_amount_reached));
                ;
            case MobilepayErrors.MOBILEPAY_TIMEOUT:
                Utils.makeToast(getString(R.string.msg_transaction_timeout));
            default:
                Utils.makeToast(getString(R.string.msg_try_again));// retry purchase?
        }

    }


    private class MobilepayErrors {
        private static final int MOBILEPAY_MUST_BE_UPDATED = 3;
        public static final int MOBILEPAY_MAX_AMOUNT_REACHED = 7;
        public static final int MOBILEPAY_TIMEOUT = 6;
    }
}
