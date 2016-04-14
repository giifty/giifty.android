package dk.android.giifty;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.math.BigDecimal;

import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.user.UserRepository;
import dk.android.giifty.utils.Utils;
import dk.android.giifty.web.RequestHandler;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.WebApi;
import dk.danskebank.mobilepay.sdk.MobilePay;
import dk.danskebank.mobilepay.sdk.ResultCallback;
import dk.danskebank.mobilepay.sdk.model.FailureResult;
import dk.danskebank.mobilepay.sdk.model.Payment;
import dk.danskebank.mobilepay.sdk.model.SuccessResult;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class GiftcardDetailsActivity extends BaseActivity implements Callback<Boolean> {

    private static final int MOBILEPAY_PAYMENT_REQUEST_CODE = 9889;
    private static final String TAG = GiftcardDetailsActivity.class.getSimpleName();
    private TextView value, salesPrice, date, expiryDate, description, ownerName;
    private ImageView image, ownerImage;
    private Giftcard giftcard;
    private MobilePay mobilePay;
    private WebApi webService;
    private RequestHandler requestHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftcard_details);
        value = (TextView) findViewById(R.id.value_id);
        salesPrice = (TextView) findViewById(R.id.sales_price_id);
        date = (TextView) findViewById(R.id.created_date_id);
        expiryDate = (TextView) findViewById(R.id.expiry_date_id);
        description = (TextView) findViewById(R.id.description_id);
        image = (ImageView) findViewById(R.id.giftcard_image_id);

        //   View ownerLayout = findViewById(R.id.owner_layout_id);
        ownerImage = (ImageView) findViewById(R.id.user_image_id);
        ownerName = (TextView) findViewById(R.id.user_name_id);

        Button buyGc = (Button) findViewById(R.id.buy_giftcard_id);
        buyGc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (giftcard != null) {
                    buyGiftcard();
                }
            }
        });

        giftcard = GiftcardRepository.getInstance().
                getGiftcard(getIntent().getIntExtra(Constants.EKSTRA_GIFTCARD_ID, -1));

        requestHandler = new RequestHandler(this);
        webService = ServiceCreator.creatServiceWithAuthenticator();

        setValues();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!UserRepository.getInstance().hasUser()) {
            // Utils.makeToast();
        }
    }

    private void setValues() {
        if (giftcard != null) {
            salesPrice.setText(String.valueOf(giftcard.getPrice()));
            value.setText(String.valueOf(giftcard.getValue()));
            date.setText(Utils.calculateTime(giftcard.getEntryDate()));
            expiryDate.setText(Utils.calculateTime(giftcard.getExpirationDate()));
            description.setText(giftcard.getDescription());
            if (!giftcard.getImage().isEmpty()) {
                Utils.setImage(this, image, giftcard.getImage().get(0).getUrl());
            }
            ownerName.setText(giftcard.getSeller().getName());
            Utils.setUserImage(this, ownerImage, giftcard.getSeller().getFacebookProfileImageUrl());
        }
    }

    private void buyGiftcard() {

        if (!UserRepository.getInstance().hasUser()) {
            MyDialogBuilder.createNoUserDialog(this).show();
        } else {
            if (giftcard != null) {
                mobilePay = MobilePay.getInstance();
                if (mobilePay.isMobilePayInstalled(this)) {
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            String orderId = null;
                            try {
                                Response<String> response = webService.getTransactionOrderId(giftcard.getGiftcardId()).execute();
                                if (response.isSuccess()) {
                                    orderId = response.body();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return orderId;
                        }

                        @Override
                        protected void onPostExecute(String orderId) {
                            super.onPostExecute(orderId);
                            if (orderId != null) {
                                startMobilePay(orderId);
                            } else {
                                //TODO remove this, only for testing
                                startMobilePay("testing1234");
                            }
                        }
                    }.execute();
                } else {
                    // MobilePay is not installed. Use the SDK to create an Intent to take the user to Google Play and download MobilePay.
                    Intent intent = mobilePay.createDownloadMobilePayIntent(getApplicationContext());
                    startActivity(intent);
                }
            }
        }
    }

    private void startMobilePay(String orderId) {
        Log.d(TAG, "startMobilePay() orderId:" + orderId);
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        BigDecimal price = BigDecimal.valueOf(giftcard.getPrice());
        payment.setProductPrice(price);

        // Create a payment Intent using the Payment object from above.
        Intent paymentIntent = MobilePay.getInstance().createPaymentIntent(payment);
        startActivityForResult(paymentIntent, MOBILEPAY_PAYMENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() resultCode:" + resultCode);
        if (requestCode == MOBILEPAY_PAYMENT_REQUEST_CODE) {
            mobilePay.handleResult(resultCode, data, new ResultCallback() {
                @Override
                public void onSuccess(SuccessResult successResult) {
                    //TODO use transaction number
                    requestHandler.enqueueRequest(webService.buyGiftcard(giftcard.getGiftcardId()), GiftcardDetailsActivity.this);
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


    @Override
    public void onResponse(Response<Boolean> response, Retrofit retrofit) {

        if (response.isSuccess()) {
            if (!response.body()) {
                // handler error
            }
        } else {
            // handler error
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }

    private class MobilepayErrors {
        private static final int MOBILEPAY_MUST_BE_UPDATED = 3;
        public static final int MOBILEPAY_MAX_AMOUNT_REACHED = 7;
        public static final int MOBILEPAY_TIMEOUT = 6;
    }
}
