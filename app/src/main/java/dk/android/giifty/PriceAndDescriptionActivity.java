package dk.android.giifty;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.view.View;

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

        giftcardRequest = (GiftcardRequest) getIntent().getSerializableExtra(Constants.EXTRA_GC_REQUEST);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_price_and_description);

        binding.setProperties(giftcardRequest.getProperties());
        binding.setNextPageText(getString(R.string.walk_trough_overview));
        binding.setPageNumber("3/4");
        binding.setCanGoToNext(new ObservableBoolean(true));

        binding.nextId.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.giftcardInformationId.validateInput())
                    ActivityStarter.startReviewActivity(PriceAndDescriptionActivity.this, giftcardRequest);
            }
        });
    }
}
