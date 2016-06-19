package dk.android.giifty;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import dk.android.giifty.busevents.GiftcardCreatedEvent;
import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.components.ImagePagerAdapter;
import dk.android.giifty.databinding.ActivityReviewBinding;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Company;
import dk.android.giifty.model.GiftcardRequest;
import dk.android.giifty.model.User;
import dk.android.giifty.services.CreateGiftcardService;
import dk.android.giifty.utils.Constants;
import dk.android.giifty.utils.GiiftyPreferences;

public class ReviewActivity extends BaseActivity {

    private static final String TAG = ReviewActivity.class.getSimpleName();
    private GiftcardRequest giftcardRequest;
    private ObservableBoolean isBusy = new ObservableBoolean(false);
    private ActivityReviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review);

        giftcardRequest = (GiftcardRequest) getIntent().getSerializableExtra(Constants.EXTRA_GC_REQUEST);

        List<String> images = new ArrayList<>();
        images.add(giftcardRequest.getGcImagePath());

        binding.viewPagerId.setAdapter(new ImagePagerAdapter(this, images));

        Company company = GiftcardRepository.getInstance().getCompany(giftcardRequest.getProperties().getCompanyId());

        User user = GiiftyPreferences.getInstance().getUser();

        giftcardRequest.getProperties().sellerId = user.getUserId();

        binding.setCompany(company);
        binding.setUser(user);
        binding.setBusy(isBusy);
        binding.setRequest(giftcardRequest);

        binding.accountId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "accountId()");
            }
        });
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
            isBusy.set(true);
            CreateGiftcardService.createGiftcard(this, giftcardRequest);
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onGiftcardCreated(GiftcardCreatedEvent event) {
        Log.d(TAG, "onGiftcardCreated() isSuccessFul:" + event.isSuccessFul);
        isBusy.set(false);
        navigateUpTo(new Intent(this, FrontPageActivity.class));
    }
}
