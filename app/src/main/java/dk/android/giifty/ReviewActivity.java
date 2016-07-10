package dk.android.giifty;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Constants;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.utils.ImageRotator;

public class ReviewActivity extends BaseActivity {

    private static final String TAG = ReviewActivity.class.getSimpleName();
    private GiftcardRequest giftcardRequest;
    private ObservableBoolean isBusy = new ObservableBoolean(false);
    private ObservableBoolean isLoadingImage = new ObservableBoolean(false);
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
        binding.setNextPageText(getString(R.string.create_giftcard));
        binding.setPageNumber("4/4");
        binding.setCanGoToNext(new ObservableBoolean(true));
        binding.accountId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "accountId()");
            }
        });

        binding.nextId.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGiftcard();
            }
        });
    }

    private void createGiftcard() {
        if (binding.giftcardInformationId.validateInput()) {
            isBusy.set(true);
            CreateGiftcardService.createGiftcard(ReviewActivity.this, giftcardRequest);
        }
    }

    public void changePicture(View v) {
        Snackbar.make(binding.imageBarcodeId, "Skift billede?", Snackbar.LENGTH_SHORT).setAction("GO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giftcardRequest.setGcImagePath(null);
                ActivityStarter.startCreateImageActClearTop(ReviewActivity.this, giftcardRequest);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GiiftyApplication.getBus().register(this);
        new ImageRotator(giftcardRequest.getGcImagePath(), binding.imageId, isLoadingImage);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GiiftyApplication.getBus().unregister(this);
    }

    @Subscribe
    public void onGiftcardCreated(GiftcardCreatedEvent event) {
        Log.d(TAG, "onGiftcardCreated() isSuccessFul:" + event.isSuccessFul);
        isBusy.set(false);

        if(event.isSuccessFul){
            navigateUpTo(new Intent(this, FrontPageActivity.class));
            return;
        }

        Snackbar.make(binding.barcodeTextId, R.string.msg_error_create_gc, Snackbar.LENGTH_LONG).setAction("Prøv igen", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGiftcard();
            }
        });
    }
}


