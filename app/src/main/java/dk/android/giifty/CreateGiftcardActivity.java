package dk.android.giifty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import dk.android.giifty.barcode.BarcodeGenerator;
import dk.android.giifty.barcode.ScanResult;
import dk.android.giifty.utils.Constants;

public class CreateGiftcardActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 77;
    private ImageView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_giftcard);
        int companyId = getIntent().getIntExtra(Constants.EKSTRA_COMPANY_ID, -1);
        barcodeView = (ImageView) findViewById(R.id.barcode_view_id);
        Button startScan = (Button) findViewById(R.id.start_scan_id);
        startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   startActivityForResult(new Intent(CreateGiftcardActivity.this, ScannerActivity.class), REQUEST_CODE);
                showResult(new ScanResult("mm", "1234567890123"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                ScanResult result = data.getParcelableExtra(Constants.EKSTRA_SCAN_RESULT);
                showResult(result);
            }
        }
    }

    private void showResult(ScanResult result) {
        try {
            Bitmap bitmap = BarcodeGenerator.createEAN13(result.barcodeNumber);
            if(bitmap != null){
                barcodeView.setImageBitmap(bitmap);
            }else{
                barcodeView.setImageResource(R.drawable.avatar);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
