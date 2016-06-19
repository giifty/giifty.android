package dk.android.giifty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.List;

import dk.android.giifty.components.ImageFrag;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.model.Image;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Constants;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.utils.Utils;

public class PurchaseSuccessActivity extends AppCompatActivity {

    private TextView infoText, valueText, expiryDate, reportProblem;
    private Giftcard giftcard;
    private final static String TAG = PurchaseSuccessActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_success);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        infoText = (TextView) findViewById(R.id.info_text_id);
        valueText = (TextView) findViewById(R.id.value_id);
        expiryDate = (TextView) findViewById(R.id.expiry_date_id);
        reportProblem = (TextView) findViewById(R.id.report_problem_id);
        reportProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO report problem action
            }
        });

        int id = getIntent().getIntExtra(Constants.EKSTRA_GIFTCARD_ID, -1);

        giftcard = GiiftyPreferences.getInstance().getPurchasedGiftcard(id);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_id);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),
                giftcard != null ? giftcard.getImages() : new ArrayList<Image>());
        viewPager.setAdapter(myPagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        goToFrontpageActivity();
    }

    private void goToFrontpageActivity() {
        ActivityStarter.startFrontPageActivityAsBackPressed(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setValues();
    }

    private void setValues() {
        if (giftcard != null) {
            infoText.setText(getInfoText());
            valueText.setText(getValueText());
            expiryDate.setText(Utils.formatTime(giftcard.getExpirationDate()));
        }
    }

    private Spanned getInfoText() {
        String companyName = "Navn";
        String body = getString(R.string.you_have_bought) + " " + companyName + " " + getString(R.string.with_value);
        return getHtmlText(body);
    }

    private Spanned getValueText() {
        String body = getString(R.string.your_price) + " " + giftcard.getPrice() + " " + getString(R.string.kr);
        return getHtmlText(body);
    }

    private Spanned getHtmlText(String body) {
        String htmlText = "<body>" + body + "</body>";
        return Html.fromHtml(htmlText, null, new MyTagHandler());
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        private List<Image> imageList;

        public MyPagerAdapter(FragmentManager fm, List<Image> imageList) {
            super(fm);
            this.imageList = imageList;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "pos:" + position);
            return ImageFrag.newInstance(imageList.get(position).getUrl());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return imageList.size();
        }
    }

    private class MyTagHandler implements Html.TagHandler {
        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            Log.d(TAG, "unknown html tag:" + tag);
        }
    }
}
