package dk.android.giifty;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.components.GiftcardInformationView;
import dk.android.giifty.model.GiftcardRequest;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Constants;


public class PriceAndDescriptionActivity extends BaseActivity {

    private GiftcardRequest giftcardRequest;
    private GiftcardInformationView giftcardInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_and_description);
        giftcardRequest = (GiftcardRequest) getIntent().getSerializableExtra(Constants.EXTRA_GC_REQUEST);
        giftcardInfoView = (GiftcardInformationView) findViewById(R.id.giftcard_information_id);
        giftcardInfoView.setBindingProperties(giftcardRequest.getProperties());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_gc_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_done) {
            if (!giftcardInfoView.validateInput()) {
                return true;
            }
            giftcardRequest.getProperties().setValue(giftcardInfoView.getValue());
            giftcardRequest.getProperties().setPrice(giftcardInfoView.getPrice());
            //  request.setExpirationDate(giftcardInfoView.getSelectedExpiryTime());
            ActivityStarter.startReviewActivity(this, giftcardRequest);
        }
        return super.onOptionsItemSelected(item);
    }
}
