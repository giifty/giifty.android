package android.giifty.dk.giifty;

import android.giifty.dk.giifty.components.BaseActivity;
import android.giifty.dk.giifty.giftcard.GiftcardRepository;
import android.giifty.dk.giifty.model.Giftcard;
import android.giifty.dk.giifty.utils.Utils;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GiftcardDetailsActivity extends BaseActivity {

    private TextView value, salesPrice, date, expiryDate, description;
    private ImageView image;

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

        View ownerLayout = findViewById(R.id.owner_layout_id);

        ImageView ownerImage = (ImageView) ownerLayout.findViewById(R.id.user_image_id);

        TextView ownerName = (TextView) ownerLayout.findViewById(R.id.user_name_id);

        Giftcard giftcard = GiftcardRepository.getInstance().
                getGiftcard(getIntent().getIntExtra(Constants.EKSTRA_GIFTCARD_ID, -1));

        if (giftcard != null) {
            salesPrice.setText(String.valueOf(giftcard.getPrice()));
            value.setText(String.valueOf(giftcard.getValue()));
            date.setText(Utils.calculateTime(giftcard.getEntryDate()));
            expiryDate.setText(Utils.calculateTime(giftcard.getExpirationDate()));
            description.setText(giftcard.getDescription());
            Utils.setImage(this, image, giftcard.getImage().get(0).getUrl());
            ownerName.setText(giftcard.getSeller().getName());
            Utils.setUserImage(this, ownerImage, giftcard.getSeller().getFacebookProfileImageUrl());
        }
    }
}
