package dk.android.giifty;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import dk.android.giifty.busevents.GiftcardCreatedEvent;
import dk.android.giifty.components.GiftcardInformationView;
import dk.android.giifty.components.SpinnerLayout;
import dk.android.giifty.databinding.ActivityReviewBinding;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Company;
import dk.android.giifty.model.GiftcardRequest;
import dk.android.giifty.model.User;
import dk.android.giifty.services.CreateGiftcardService;
import dk.android.giifty.utils.Constants;
import dk.android.giifty.utils.GiiftyPreferences;

public class ReviewActivity extends AppCompatActivity {

    private GiftcardInformationView giftcardInfoView;
    private ImageView gcImage, barcodeImage;
    private GiftcardRequest giftcardRequest;

    private ObservableBoolean isBusy = new ObservableBoolean(false);
    private SpinnerLayout spinner;
    private ActivityReviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review);

        spinner = (SpinnerLayout) findViewById(R.id.spinner_layout_id);
        giftcardRequest = (GiftcardRequest) getIntent().getSerializableExtra(Constants.EXTRA_GC_REQUEST);

        Company company = GiftcardRepository.getInstance().getCompany(giftcardRequest.getProperties().getCompanyId());
        TextView companyName = (TextView) findViewById(R.id.company_name_id);
        assert companyName != null;
        companyName.setText(company.getName());

        User user = GiiftyPreferences.getInstance().getUser();
        EditText accountNr = (EditText) findViewById(R.id.account_id);
        if (user.getAccountNumber() != null) {
            assert accountNr != null;
            accountNr.setText(user.getAccountNumber());
        } else {
            //TODO add account number
            accountNr.setText("9859389589");
        }
        giftcardRequest.getProperties().sellerId = user.getUserId();

        binding.setCompany(company);
        binding.setUser(user);
        binding.giftcardInformationId.setBindingProperties(giftcardRequest.getProperties());
//    TODO     gcImage = (ImageView) findViewById(R.id.giftcard_image_id);
//    TODO    barcodeImage = (ImageView) findViewById(R.id.barcode_view_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_gc_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_done) {
//            if (!giftcardInfoView.validateInput()) {
//                return true;
//            }

            isBusy.set(true);
            spinner.showProgressBar();
            CreateGiftcardService.createGiftcard(this, giftcardRequest);
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onGiftcardCreated(GiftcardCreatedEvent event) {
        isBusy.set(false);
        spinner.hideProgressBar();
    }
}
