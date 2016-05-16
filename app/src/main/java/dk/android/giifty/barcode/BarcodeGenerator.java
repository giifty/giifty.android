package dk.android.giifty.barcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.onbarcode.barcode.android.AndroidColor;
import com.onbarcode.barcode.android.AndroidFont;
import com.onbarcode.barcode.android.EAN13;
import com.onbarcode.barcode.android.IBarcode;

/**
 * Created by mak on 16-05-2016.
 */
public class BarcodeGenerator {

    public static Bitmap createEAN13(String data) throws Exception {
        EAN13 barcode = new EAN13();


        //  EAN 13 Valid data length: 12 digits only, excluding the last checksum digit
        StringBuilder s = new StringBuilder(data);
        s.deleteCharAt(s.length() - 1);
        barcode.setData(s.toString());

        // for EAN13 with supplement data (2 or 5 digits)
    /*
    barcode.setSupData("12");
	// supplement bar height vs bar height ratio
	barcode.setSupHeight(0.8f);
	// space between barcode and supplement barcode (in pixel)
	barcode.setSupSpace(15);
	*/

        // Unit of Measure, pixel, cm, or inch
        barcode.setUom(IBarcode.UOM_PIXEL);
        // barcode bar module width (X) in pixel
        barcode.setX(1f);
        // barcode bar module height (Y) in pixel
        barcode.setY(45f);
        barcode.setAutoResize(true);
        //   barcode.setBarcodeHeight(100);
        // barcode.setBarcodeWidth(500);

        // barcode image margins
        barcode.setLeftMargin(10f);
        barcode.setRightMargin(10f);
        barcode.setTopMargin(10f);
        barcode.setBottomMargin(10f);

        // barcode image resolution in dpi
        barcode.setResolution(512);

        // display barcode encoding data below the barcode
        barcode.setShowText(true);
        // barcode encoding data font style
        barcode.setTextFont(new AndroidFont("Arial", Typeface.NORMAL, 10));
        // space between barcode and barcode encoding data
        barcode.setTextMargin(6);
        barcode.setTextColor(AndroidColor.black);

        // barcode bar color and background color in Android device
        barcode.setForeColor(AndroidColor.black);
        barcode.setBackColor(AndroidColor.white);


        Bitmap b = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        RectF bounds = new RectF(30, 30, 0, 0);
        barcode.drawBarcode(canvas, bounds);

        return b;

    }
}
