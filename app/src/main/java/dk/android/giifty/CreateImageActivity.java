package dk.android.giifty;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.components.ImageCreator;
import dk.android.giifty.model.Holder;
import dk.android.giifty.utils.ActivityStarter;

public class CreateImageActivity extends BaseActivity {

    private static final int REQUEST_CODE = 4477;
    private static final int REQUEST_IMAGE_CAPTURE = 7773;
    private static final String TAG = CreateImageActivity.class.getSimpleName();
    private Holder holder;
    private ImageView image;
    private File imageFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_image);
        //   holder = getIntent().getParcelableExtra(Constants.EXTRA_HOLDER);
        holder = new Holder();
        image = (ImageView) findViewById(R.id.image_container_id);
        ImageView cameraButton = (ImageView) findViewById(R.id.camera_button_id);
        assert cameraButton != null;
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageFile != null)
                    dispatchCreateContentIntent(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), imageFile, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            imageFile = ImageCreator.createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void dispatchCreateContentIntent(Intent intent, File filePath, int requestCode) {
        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePath));
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() resultCode:" + resultCode);
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            prepareImagePost();
        }
    }

    private void prepareImagePost() {
        image.setImageURI(Uri.fromFile(imageFile));
        holder.setGcImagePath(imageFile.getAbsolutePath());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_gc_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_done) {
            if (!verifyPictureCreated()) {
                Snackbar.make(image, "Du skal tage et billede før du kan forsætte", Snackbar.LENGTH_LONG).show();
                return true;
            }
            ActivityStarter.startPriceAndDescriptionActivity(this, holder);
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean verifyPictureCreated() {
        return holder.getGcImagePath() != null;
    }

    private boolean checkExternalStoragePermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
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
