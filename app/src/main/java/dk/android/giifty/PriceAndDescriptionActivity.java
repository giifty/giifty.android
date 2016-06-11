package dk.android.giifty;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.databinding.ActivityPriceAndDescriptionBinding;
import dk.android.giifty.model.GiftcardRequest;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Constants;


public class PriceAndDescriptionActivity extends BaseActivity {

    private GiftcardRequest giftcardRequest;
    private ActivityPriceAndDescriptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_price_and_description);
        giftcardRequest = (GiftcardRequest) getIntent().getSerializableExtra(Constants.EXTRA_GC_REQUEST);
        binding.setProperties(giftcardRequest.getProperties());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_gc_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_done) {
            if (!binding.giftcardInformationId.validateInput()) {
                return true;
            }
            ActivityStarter.startReviewActivity(this, giftcardRequest);
        }
        return super.onOptionsItemSelected(item);
    }
}
