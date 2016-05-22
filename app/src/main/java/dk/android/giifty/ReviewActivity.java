package dk.android.giifty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import dk.android.giifty.components.GiftcardInformationView;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Company;
import dk.android.giifty.model.Holder;
import dk.android.giifty.model.User;
import dk.android.giifty.utils.Constants;
import dk.android.giifty.utils.GiiftyPreferences;

public class ReviewActivity extends AppCompatActivity {

    private GiftcardInformationView giftcardInfoView;
    private ImageView gcImage, barcodeImage;
    private Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        holder = getIntent().getParcelableExtra(Constants.EXTRA_HOLDER);

        Company company = GiftcardRepository.getInstance().getCompany(holder.getRequest().getCompanyId());
        TextView companyName = (TextView) findViewById(R.id.company_name_id);
        companyName.setText(company.getName());

        User user = GiiftyPreferences.getInstance().getUser();
        TextView accountNr = (TextView) findViewById(R.id.account_id);
        if(user.getAccountNumber() != null){
            accountNr.setText(user.getAccountNumber());

        }
        gcImage = (ImageView) findViewById(R.id.giftcard_image_id);
        barcodeImage = (ImageView) findViewById(R.id.barcode_view_id);

        giftcardInfoView = (GiftcardInformationView) findViewById(R.id.giftcard_information_id);
        giftcardInfoView.setExpiryDate(holder.getRequest().getExpirationDate());
        giftcardInfoView.setPriceText(holder.getRequest().getPrice());
        giftcardInfoView.setValueText(holder.getRequest().getValue());
    }
}
