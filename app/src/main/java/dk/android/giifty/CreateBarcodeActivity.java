package dk.android.giifty;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;

import dk.android.giifty.barcode.Barcode;
import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.databinding.ActivityCreateBarcodeBinding;
import dk.android.giifty.model.Company;
import dk.android.giifty.model.GiftcardRequest;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Constants;

public class CreateBarcodeActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 77;
    private Company company;
    private GiftcardRequest giftcardRequest = new GiftcardRequest();
    private ObservableBoolean canGoToNext = new ObservableBoolean();
    private ActivityCreateBarcodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_barcode);
        company = getIntent().getParcelableExtra(Constants.EKSTRA_COMPANY);

        String title = getString(R.string.create_giftcard_for) + " " + company.getName();
        setTitle(title);

        binding.startScanId.setOnClickListener(this);
        binding.nextId.getRoot().setOnClickListener(this);

        binding.setNextPageText(getString(R.string.take_picture));
        binding.setPageNumber("1/4");
        binding.setCanGoToNext(canGoToNext);
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
                Barcode result = (Barcode) data.getSerializableExtra(Constants.EKSTRA_SCAN_RESULT);
                binding.barcodeViewId.setImageResource(R.drawable.barcode_image);
                binding.barcodeTextId.setText(String.valueOf(result.barcodeNumber));
                giftcardRequest.getProperties().setBarcode(result);
                canGoToNext.set(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item_done) {
            giftcardRequest.getProperties().companyId = company.getCompanyId();
            ActivityStarter.startCreateImageAct(this, giftcardRequest);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.startScanId.getId()) {
            startActivityForResult(new Intent(CreateBarcodeActivity.this, ScannerActivity.class), REQUEST_CODE);
        } else if (v.getId() == binding.nextId.getRoot().getId()) {
            giftcardRequest.getProperties().companyId = company.getCompanyId();
            ActivityStarter.startCreateImageAct(this, giftcardRequest);
        }
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
