package dk.android.giifty;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.databinding.ActivityGiftcardDetailsBinding;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.model.User;
import dk.android.giifty.signin.SignInDialogHandler;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Constants;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.utils.Utils;

public class GiftcardDetailsActivity extends BaseActivity {

    private static final String TAG = GiftcardDetailsActivity.class.getSimpleName();
    private Giftcard giftcard;
    private ActivityGiftcardDetailsBinding binding;
    private ObservableBoolean isUserGiftcard = new ObservableBoolean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.gc_details_tile));

        User user = GiiftyPreferences.getInstance().getUser();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_giftcard_details);

        binding.buyGiftcardId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase();
            }
        });

        giftcard = (Giftcard) getIntent().getSerializableExtra(Constants.EKSTRA_GIFTCARD);

        isUserGiftcard.set(isUserRelated(user));

        binding.setIsOwnerGiftcard(isUserGiftcard);
        binding.setGiftcard(giftcard);
        setValues();
    }

    private boolean isUserRelated(User user) {
        return user.getUserId() == giftcard.getSellerId() || giftcard.getBuyerId() == user.getUserId();
    }

    private void purchase() {
        if (GiiftyPreferences.getInstance().hasUser()) {
            ActivityStarter.startPaymentActivity(GiftcardDetailsActivity.this, giftcard.getGiftcardId());
        } else {
            new SignInDialogHandler().startDialog(this, giftcard.getGiftcardId());
        }
    }

    private void setValues() {
        if (giftcard != null) {
            if (giftcard.getImages() != null && !giftcard.getImages().isEmpty()) {
                Utils.setImage(this, binding.giftcardImageId, giftcard.getImages().get(0).getUrl());
            }
            //TODO implement databinding with userLayout
            Utils.setUserImage(this, (ImageView) binding.ownerLayoutId.getRootView().findViewById(R.id.user_image_id), giftcard.getSeller().getFacebookProfileImageUrl());
        }
    }
}
