package com.clpstudio.bsocial.presentation.conversation.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.utils.IOUtils;
import com.clpstudio.bsocial.presentation.general.mvp.BaseMvpPresenter;
import com.clpstudio.bsocial.presentation.general.mvp.IBaseMvpPresenter;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.clpstudio.bsocial.presentation.profile.EditAvatarFragment.FILE_PROVIDER_AUTHORITY;

/**
 * Created by clapalucian on 28/05/2017.
 */

public class TakePhotoPresenter extends BaseMvpPresenter<TakePhotoPresenter.View> {

    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CODE_PICK_IMAGE = 2;
    private static final int PERMISSION_REQUEST_CODE_CAMERA = 2;
    private static final String PICTURES_DIR_NAME = "pictures";
    private static final String IMAGE_TYPE_WILDCARD = "image/*";
    private static final String JPG = ".jpg";

    private Uri imageUri;
    private String filename;

    @Inject
    Context context;

    @Inject
    public TakePhotoPresenter() {
    }

    public void takePhoto(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_CAMERA);
            } else {
                startCameraActivity(activity);
            }
        } else {
            startCameraActivity(activity);
        }
    }

    public void selectPhoto(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(IMAGE_TYPE_WILDCARD);
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void startCameraActivity(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getImageFile(activity);
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            finishWithError(R.string.unknown_error);
            return;
        }
        imageUri = FileProvider.getUriForFile(activity, FILE_PROVIDER_AUTHORITY, file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, REQUEST_CODE_IMAGE_CAPTURE);
    }

    private File getImageFile(Activity activity) {
        File picturesDir = new File(activity.getExternalCacheDir(), PICTURES_DIR_NAME);
        filename = UUID.randomUUID().toString() + JPG;
        return new File(picturesDir, filename);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                Log.d("luci", "REQUEST_CODE_IMAGE_CAPTURE file uri = " + imageUri.toString());
                view().imagePhotoTaken(filename, imageUri);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finishWithMessage(R.string.edit_avatar_canceled);
            } else {
                finishWithError(R.string.unknown_error);
            }
        } else if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == AppCompatActivity.RESULT_OK && data != null && data.getData() != null) {
                Uri uri = data.getData();
                Log.d("luci", "REQUEST_CODE_PICK_IMAGE file uri = " + uri);
                copyImageFromPickup(activity, uri);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finishWithMessage(R.string.edit_avatar_canceled);
            } else {
                finishWithError(R.string.unknown_error);
            }
        }
    }

    private void copyImageFromPickup(Activity activity, Uri uri) {
        File file = getImageFile(activity);
        Completable.fromAction(() -> IOUtils.copyUriToFile(context, uri, file))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            view().imagePhotoTaken(filename ,Uri.fromFile(file));
                            Log.d("luci", "Copy from pick success!");
                        },
                        t -> {
                            Log.d("luci", "Copy from pick failed!");
                            finishWithError(R.string.unknown_error);
                        }
                );
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraActivity(activity);
            } else {
                finishWithError(R.string.edit_avatar_camera_permission_error);
            }
        }
    }

    private void finishWithMessage(int messageRes) {
        view().showError(context.getString(messageRes));
    }

    private void finishWithError(int errorMessageRes) {
        view().showError(context.getString(errorMessageRes));
    }

    private void finishWithError(String errorMessage) {
        view().showError(errorMessage);
    }


    public interface View extends IBaseMvpPresenter.View {
        void imagePhotoTaken(String filename, Uri path);

        void showError(String message);
    }

}
