package dk.android.giifty;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

import dk.android.giifty.components.BaseActivity;
import dk.android.giifty.components.ImageCreator;
import dk.android.giifty.databinding.ActivityCreateImageBinding;
import dk.android.giifty.model.GiftcardRequest;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Constants;
import dk.android.giifty.utils.ImageRotator;

public class CreateImageActivity extends BaseActivity {

    private static final int REQUEST_CODE = 4477;
    private static final int REQUEST_IMAGE_CAPTURE = 7773;
    private static final String TAG = CreateImageActivity.class.getSimpleName();
    private GiftcardRequest giftcardRequest;
    private File imageFile;
    private ActivityCreateImageBinding binding;
    private ObservableBoolean canGoToNext = new ObservableBoolean();
    private ObservableBoolean isLoadingImage = new ObservableBoolean();
    private boolean dontAutoStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_image);

        giftcardRequest = (GiftcardRequest) getIntent().getSerializableExtra(Constants.EXTRA_GC_REQUEST);

        binding.setNextPageText(getString(R.string.price_and_description));
        binding.setPageNumber("2/4");
        binding.setCanGoToNext(canGoToNext);
        binding.setIsLoadingImage(isLoadingImage);

        binding.takePictureId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchCreateImageIntent();
            }
        });

        binding.nextId.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canGoToNext.get())
                    ActivityStarter.startPriceAndDescriptionActivity(CreateImageActivity.this, giftcardRequest);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkExternalStoragePermission() && !verifyPictureCreated()) {
            if (!dontAutoStart) {
                dontAutoStart = true;
                dispatchCreateImageIntent();
            }
        }
    }

    private void dispatchCreateImageIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageFile == null) {
            try {
                imageFile = ImageCreator.createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() resultCode:" + resultCode);
        dontAutoStart = true;
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            isLoadingImage.set(true);
            prepareImagePost();
        }
    }

    private void prepareImagePost() {
        new ImageRotator(imageFile.getAbsolutePath(), binding.imageContainerId, isLoadingImage);
        giftcardRequest.setGcImagePath(imageFile.getAbsolutePath());
        canGoToNext.set(true);
    }

    private boolean verifyPictureCreated() {
        return giftcardRequest.getGcImagePath() != null;
    }

    private boolean checkExternalStoragePermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            return true;
        }

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
