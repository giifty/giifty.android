package dk.android.giifty;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.signin.SignInDialogHandler;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Utils;

public class GiftcardDetailsActivity extends BaseActivity  {

    private static final String TAG = GiftcardDetailsActivity.class.getSimpleName();
    private TextView value, salesPrice, date, expiryDate, description, ownerName;
    private ImageView image, ownerImage;
    private Giftcard giftcard;

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

        ownerImage = (ImageView) findViewById(R.id.user_image_id);
        ownerName = (TextView) findViewById(R.id.user_name_id);

        Button buyGc = (Button) findViewById(R.id.buy_giftcard_id);
        buyGc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase();
            }
        });

        giftcard = GiftcardRepository.getInstance().
                getGiftcard(getIntent().getIntExtra(Constants.EKSTRA_GIFTCARD_ID, -1));
        setValues();
    }

    private void purchase(){
        if (UserRepository.getInstance().hasUser()) {
            ActivityStarter.startPaymentActivity(GiftcardDetailsActivity.this, giftcard.getGiftcardId());
        }else {
            new SignInDialogHandler().startDialog(this, giftcard.getGiftcardId());
        }
    }

    private void setValues() {
        if (giftcard != null) {
            salesPrice.setText(String.valueOf(giftcard.getPrice()));
            value.setText(String.valueOf(giftcard.getValue()));
            date.setText(Utils.formatTime(giftcard.getEntryDate()));
            expiryDate.setText(Utils.calculateTime(giftcard.getExpirationDate()));
            description.setText(giftcard.getDescription());
            if (!giftcard.getImages().isEmpty()) {
                Utils.setImage(this, image, giftcard.getImages().get(0).getUrl());
            }
            ownerName.setText(giftcard.getSeller().getName());
            Utils.setUserImage(this, ownerImage, giftcard.getSeller().getFacebookProfileImageUrl());
        }
    }
}
