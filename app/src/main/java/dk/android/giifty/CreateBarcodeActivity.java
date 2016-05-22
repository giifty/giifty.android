package dk.android.giifty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.io.IOException;

import dk.android.giifty.barcode.ScanResult;
import dk.android.giifty.busevents.BarcodeReceivedEvent;
import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.components.ImageCreator;
import dk.android.giifty.model.Company;
import dk.android.giifty.model.CreateGiftcardRequest;
import dk.android.giifty.model.Holder;
import dk.android.giifty.services.BarcodeService;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Constants;

public class CreateBarcodeActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 77;
    private ImageView barcodeView;
    private ProgressBar progressBar;
    private Bitmap barcodeImage;
    private Company company;
    private TextView barcodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_barcode);
        company = getIntent().getParcelableExtra(Constants.EKSTRA_COMPANY);

        String title = getString(R.string.create_giftcard_for) + " " + company.getName();
        setTitle(title);

        barcodeView = (ImageView) findViewById(R.id.barcode_view_id);
        Button startScan = (Button) findViewById(R.id.start_scan_id);
        assert startScan != null;
        startScan.setOnClickListener(this);

        barcodeText = (TextView) findViewById(R.id.barcode_text_id);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_id);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                showProgressBar();
                ScanResult result = data.getParcelableExtra(Constants.EKSTRA_SCAN_RESULT);
                BarcodeService.createEAN13(CreateBarcodeActivity.this, result);
                barcodeText.setText(String.valueOf(result.barcodeNumber));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_gc_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item_done) {
            Holder holder = new Holder();
            CreateGiftcardRequest request = new CreateGiftcardRequest();
            request.companyId = company.getCompanyId();
            try {
                holder.setBarcodeImagePath(ImageCreator.saveBitmap(barcodeImage));
                holder.setRequest(request);
                ActivityStarter.startCreateImageAct(this, holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onBitmapReceived(BarcodeReceivedEvent event) {
        if (event.isSuccessFul) {
            barcodeImage = event.bitmap;
            barcodeView.setImageBitmap(barcodeImage);
        } else {
            Snackbar.make(barcodeView, R.string.msg_error_barcode_creation, Snackbar.LENGTH_SHORT).setAction("Prøv igen", this).show();
        }
        hideProgressBar();
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(CreateBarcodeActivity.this, ScannerActivity.class), REQUEST_CODE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
