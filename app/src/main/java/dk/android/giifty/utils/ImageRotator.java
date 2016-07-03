package dk.android.giifty.utils;

import android.databinding.ObservableBoolean;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;

public class ImageRotator {

    //called when the source is image file
    public ImageRotator(final String photoPath, final ImageView view, final ObservableBoolean isLoading) {
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {

                try {
                    return createPicture(createBitmap(params[0]), photoPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap != null) {
                    view.setImageBitmap(bitmap);
                }
                isLoading.set(false);
            }
        }.execute(photoPath);

    }

    private Bitmap createPicture(Bitmap source, String filePath) throws IOException {
        ExifInterface ei = new ExifInterface(filePath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(source, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(source, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(source, 270);
            default:
                return source;
        }
    }

    private Bitmap createBitmap(String photoPath) {
        return BitmapFactory.decodeFile(photoPath);
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        source = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        return source;
    }
}
