package dk.android.giifty.components;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageCreator {

    private static final String TAG = ImageCreator.class.getSimpleName();

    public static String getFilePathFromContentUri(Uri originalUri, Context context) {
        String filePath = "";
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(originalUri, filePathColumn, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        } else {
            filePath = originalUri.getPath();
        }
        return filePath;
    }

    public static File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HHmmss", new Locale("da_DK")).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return imageFile;
    }

    public static String saveBitmap(Bitmap barcodeImage) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HHmmss", new Locale("da_DK")).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        try {
            FileOutputStream outStream = new FileOutputStream(imageFile);
            barcodeImage.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "imagePath:" + imageFile.getAbsolutePath());
        return imageFile.getAbsolutePath();
    }
}
