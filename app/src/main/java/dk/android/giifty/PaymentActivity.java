package dk.android.giifty;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import dk.android.giifty.busevents.OrderIdFetchedEvent;
import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.purchase.PurchaseFragment;
import dk.android.giifty.purchase.PurchaseFragmentHandler;
import dk.android.giifty.purchase.purchasefragments.CardPaymentFrag;
import dk.android.giifty.purchase.purchasefragments.MobilepayFrag;
import dk.android.giifty.services.PurchaseService;
import dk.android.giifty.utils.Utils;

public class PaymentActivity extends BaseActivity implements View.OnClickListener, PurchaseFragment.OnPurchaseFragmentInteraction {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    private Giftcard giftcard;

    private TextView value, salesPrice, expiryDate, ownerName, cardPayButton, mobilepayButton;
    private ImageView ownerImage;
    private String orderId;
    private Button payButton;
    private PurchaseFragmentHandler fragmentHandler;
    private ProgressBar progressBar;
    private TextView cantPurchaseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        value = (TextView) findViewById(R.id.value_id);
        salesPrice = (TextView) findViewById(R.id.sales_price_id);
        expiryDate = (TextView) findViewById(R.id.expiry_date_id);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_id);
        cantPurchaseText = (TextView) findViewById(R.id.gc_already_purchased_msg);
        ownerImage = (ImageView) findViewById(R.id.user_image_id);
        ownerName = (TextView) findViewById(R.id.user_name_id);
        payButton = (Button) findViewById(R.id.pay_button_id);
        payButton.setOnClickListener(this);

        cardPayButton = (TextView) findViewById(R.id.pay_with_card_id);
        cardPayButton.setOnClickListener(this);
        mobilepayButton = (TextView) findViewById(R.id.pay_with_mp_id);
        mobilepayButton.setOnClickListener(this);

        giftcard = GiftcardRepository.getInstance().
                getGiftcard(getIntent().getIntExtra(Constants.EKSTRA_GIFTCARD_ID, -1));

        PurchaseService.reserveGiftcard(this, giftcard.getGiftcardId());
        fragmentHandler = new PurchaseFragmentHandler(getSupportFragmentManager(), this);
        setValues();
    }

    private void setValues() {
        if (giftcard != null) {
            salesPrice.setText(String.valueOf(giftcard.getPrice()));
            value.setText(String.valueOf(giftcard.getValue()));
            expiryDate.setText(Utils.calculateTime(giftcard.getExpirationDate()));
            ownerName.setText(giftcard.getSeller().getName());
            Utils.setUserImage(this, ownerImage, giftcard.getSeller().getFacebookProfileImageUrl());
            String text = getString(R.string.pay) + giftcard.getPrice() + " " + getString(R.string.kr);
            payButton.setText(text);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (giftcard != null) {
            if (id == R.id.pay_with_card_id) {
                cardPayButton.setSelected(true);
                mobilepayButton.setSelected(false);
                showFragment(CardPaymentFrag.class.getName());
            } else if (id == R.id.pay_with_mp_id) {
                cardPayButton.setSelected(false);
                mobilepayButton.setSelected(true);
                showFragment(MobilepayFrag.class.getName());
            } else if (id == R.id.pay_button_id) {
                PurchaseFragment fragment = fragmentHandler.getCurrentFragment();
                if (fragment != null)
                    fragment.startTransaction();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GiiftyApplication.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GiiftyApplication.getBus().unregister(this);
    }

    private void showFragment(String tag) {
        if (orderId != null)
            fragmentHandler.showFragment(tag, giftcard.getGiftcardId(), giftcard.getPrice());
    }

    @Subscribe
    public void onOrderIdFetched(OrderIdFetchedEvent event){
        Log.d(TAG, "onResponse()");
        if (event.isSuccessful) {
            orderId = event.orderId;
            setReadyToPurchase();
        } else  {
            setCantPurchase();
            hideProgressBar();
            Toast.makeText(this, getString(R.string.generel_error_msg), Toast.LENGTH_LONG).show();
        }
    }

    private void setReadyToPurchase() {
        Log.d(TAG, "setReadyToPurchase()");
        payButton.setVisibility(View.VISIBLE);
        hideProgressBar();
        mobilepayButton.callOnClick();
    }

    private void setCantPurchase() {
        Log.d(TAG, "setCantPurchase()");
        hideProgressBar();
        cantPurchaseText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public String getOrderId() {
        return orderId;
    }



    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }



}
