package dk.android.giifty;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.components.GiftcardInformationView;
import dk.android.giifty.model.CreateGiftcardRequest;
import dk.android.giifty.model.Holder;
import dk.android.giifty.utils.ActivityStarter;


public class PriceAndDescriptionActivity extends BaseActivity {

    private Holder holder;
    private CreateGiftcardRequest request = new CreateGiftcardRequest();
    private GiftcardInformationView giftcardInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_and_description);
     //   holder = getIntent().getParcelableExtra(Constants.EXTRA_HOLDER);
        holder = new Holder();
        holder.setRequest(request);
        giftcardInfoView = (GiftcardInformationView) findViewById(R.id.giftcard_information_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_gc_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_done) {
            if (!giftcardInfoView.validateTextFields()) {
                return true;
            }
            request.setValue(giftcardInfoView.getValue());
            request.setPrice(giftcardInfoView.getPrice());
            request.setExpirationDate(giftcardInfoView.getSelectedExpiryTime());
            ActivityStarter.startReviewActivity(this, holder);
        }
        return super.onOptionsItemSelected(item);
    }
}
