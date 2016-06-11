package dk.android.giifty;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import dk.android.giifty.barcode.ScanResult;
import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.model.Company;
import dk.android.giifty.model.GiftcardRequest;
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        GiiftyApplication.getBus().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCameraPermission();
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
                ScanResult result = data.getParcelableExtra(Constants.EKSTRA_SCAN_RESULT);
                BarcodeService.createEAN13(CreateBarcodeActivity.this, result);
                barcodeView.setImageResource(R.drawable.barcode_image);
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
            GiftcardRequest giftcardRequest = new GiftcardRequest();
            giftcardRequest.getProperties().companyId = company.getCompanyId();
            ActivityStarter.startCreateImageAct(this, giftcardRequest);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(CreateBarcodeActivity.this, ScannerActivity.class), REQUEST_CODE);
    }

    private boolean checkForCameraPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setTitle("Tag dig sammen")
                        .setMessage("Tag dig sammen")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                askForPermission();
                            }
                        })
                        .show();
            } else {
                askForPermission();
            }
        } else {
            return true;
        }
        return false;
    }

    private void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                finish();
            }
        }
    }
}
