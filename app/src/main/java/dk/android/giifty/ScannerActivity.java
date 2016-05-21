package dk.android.giifty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.scandit.barcodepicker.BarcodePicker;
import com.scandit.barcodepicker.OnScanListener;
import com.scandit.barcodepicker.ProcessFrameListener;
import com.scandit.barcodepicker.ScanSession;
import com.scandit.barcodepicker.ScanSettings;
import com.scandit.barcodepicker.ScanditLicense;
import com.scandit.recognition.Barcode;

import dk.android.giifty.barcode.ScanResult;
import dk.android.giifty.utils.Constants;

public class ScannerActivity extends AppCompatActivity implements OnScanListener {
    private final static String SCANNER_KEY = "/PTB+gZpM3Oy3So4hx6azp0VhvqdVOD/Jmb6C+2EIxM";
    private BarcodePicker scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initScanner();
        setContentView(scanner);
    }

    private void initScanner() {
        ScanditLicense.setAppKey(SCANNER_KEY);
        ScanSettings settings = ScanSettings.create();
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_EAN13, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_UPCA, true);
// Instantiate the barcode scanner by using the settings defined above.
        scanner = new BarcodePicker(this, settings);
// Set the on scan listener to receive barcode scan events.
        scanner.setOnScanListener(this);
        scanner.setProcessFrameListener(new ProcessFrameListener() {
            @Override
            public void didProcess(byte[] bytes, int i, int i1, ScanSession scanSession) {
            }
        });
    }


    @Override
    public void didScan(ScanSession scanSession) {
        scanSession.pauseScanning();

        if (scanSession.getAllRecognizedCodes().size() > 0) {
            Barcode barcode = scanSession.getAllRecognizedCodes().get(0);
            if (barcode.isRecognized()) {
                String eanType = barcode.getSymbologyName();
                String barcodeNumber = barcode.getData();

                Log.d("!!!", "type:" + eanType + " number:" + barcodeNumber);
                setResult(RESULT_OK, new Intent().putExtra(Constants.EKSTRA_SCAN_RESULT, new ScanResult(eanType, barcodeNumber)));
                finish();
            }
        }
    }


    @Override
    protected void onResume() {
        scanner.startScanning();
        super.onResume();
    }

    @Override
    protected void onPause() {
        scanner.stopScanning();
        super.onPause();
    }


}
