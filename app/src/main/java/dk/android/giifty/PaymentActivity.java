package dk.android.giifty;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dk.android.giifty.PurchaseComponents.CardPaymentFrag;
import dk.android.giifty.PurchaseComponents.MobilepayFrag;
import dk.android.giifty.PurchaseComponents.PurchaseFragment;
import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.utils.Utils;
import dk.android.giifty.web.RequestHandler;
import dk.android.giifty.web.ServiceCreator;
import dk.android.giifty.web.SignInHandler;
import dk.android.giifty.web.WebApi;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PaymentActivity extends BaseActivity implements View.OnClickListener, PurchaseFragment.OnPurchaseFragmentInteraction, Callback<String> {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    private Giftcard giftcard;

    private TextView value, salesPrice, expiryDate, ownerName, cardPayButton, mobilepayButton;
    private ImageView ownerImage;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        value = (TextView) findViewById(R.id.value_id);
        salesPrice = (TextView) findViewById(R.id.sales_price_id);
        expiryDate = (TextView) findViewById(R.id.expiry_date_id);

        ownerImage = (ImageView) findViewById(R.id.user_image_id);
        ownerName = (TextView) findViewById(R.id.user_name_id);

        cardPayButton = (TextView) findViewById(R.id.pay_with_card_id);
        cardPayButton.setOnClickListener(this);
        mobilepayButton = (TextView) findViewById(R.id.pay_with_mp_id);
        mobilepayButton.setOnClickListener(this);

        giftcard = GiftcardRepository.getInstance().
                getGiftcard(getIntent().getIntExtra(Constants.EKSTRA_GIFTCARD_ID, -1));

        RequestHandler requestHandler = new RequestHandler(this);

        WebApi webService = ServiceCreator.creatServiceWithAuthenticator();
        requestHandler.enqueueRequest(webService.getTransactionOrderId(SignInHandler.getServerToken(), giftcard.getGiftcardId()), null);

        //must be after giftcard is set
        cardPayButton.callOnClick();
        setValues();
    }

    private void showFragment(PurchaseFragment fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container_id, fragment, fragment.getClass().getSimpleName());
            transaction.commit();
    }

    private void setValues() {
        if (giftcard != null) {
            salesPrice.setText(String.valueOf(giftcard.getPrice()));
            value.setText(String.valueOf(giftcard.getValue()));
            expiryDate.setText(Utils.calculateTime(giftcard.getExpirationDate()));
            ownerName.setText(giftcard.getSeller().getName());
            Utils.setUserImage(this, ownerImage, giftcard.getSeller().getFacebookProfileImageUrl());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (giftcard != null) {
            if (id == R.id.pay_with_card_id) {
                cardPayButton.setSelected(true);
                mobilepayButton.setSelected(false);
                showFragment(CardPaymentFrag.newInstance(giftcard.getGiftcardId(), giftcard.getPrice()));
            } else if (id == R.id.pay_with_mp_id) {
                cardPayButton.setSelected(false);
                mobilepayButton.setSelected(true);
                showFragment(MobilepayFrag.newInstance(giftcard.getGiftcardId(), giftcard.getPrice()));
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void onResponse(Response<String> response, Retrofit retrofit) {

        if (response.isSuccess()) {
            orderId = response.body();
        }else{
            orderId = "testOnly";
        }
    }

    private void fireOrderIdCollected(){
     //   Broadcasts.fireOrderIdCollected
    }
    @Override
    public void onFailure(Throwable t) {

    }
}
