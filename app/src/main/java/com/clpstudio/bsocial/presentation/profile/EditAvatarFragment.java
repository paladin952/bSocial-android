package com.clpstudio.bsocial.presentation.profile;

/**
 * Created by clapalucian on 5/4/17.
 */

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.clpstudio.bsocial.BuildConfig;
import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.service.ProfileService;
import com.clpstudio.bsocial.bussiness.utils.IOUtils;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EditAvatarFragment extends Fragment {

    private static final String FRAGMENT_TAG = "EditAvatarFragment";
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CODE_PICK_IMAGE = 2;
    private static final int PERMISSION_REQUEST_CODE_CAMERA = 2;
    private static final int AVATAR_MAX_SIZE = 210;
    private static final String IMAGE_TO_UPLOAD_FILE_NAME = "avatarToUpload.jpg";
    private static final String IMAGE_TO_CROP_FILE_NAME = "avatarToCrop.jpg";
    private static final String PICTURES_DIR_NAME = "pictures";
    private static final String IMAGE_TYPE_WILDCARD = "image/*";
    public static final String FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";

    @Inject
    ProfileService profileService;

    private Uri imageToCrop;
    private Uri imageToUpload;
    private OnUploadFinishedListener onUploadFinishedListener;

    public interface OnUploadFinishedListener {
        void refreshProfileImage();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUploadFinishedListener){
            onUploadFinishedListener = ((OnUploadFinishedListener)getActivity());
        } else {
            throw new RuntimeException("Activity not instance of OnUploadFinishedListener");
        }
    }

    public static void show(FragmentManager fragmentManager) {
        Fragment fragment = new EditAvatarFragment();
        fragmentManager.beginTransaction().add(fragment, FRAGMENT_TAG).commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BSocialApplication) getActivity().getApplication()).getDiComponent().inject(this);

        if (savedInstanceState == null) {
            EditAvatarDialogFragment.show(getChildFragmentManager());
        }
    }

    public void takePhoto() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_CAMERA);
        } else {
            startCameraActivity();
        }
    }

    public void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(IMAGE_TYPE_WILDCARD);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    public void removeAvatar() {
//        userService.removeAvatar().subscribe(
//                () -> {
//                    if (isAdded()) {
//                        finishWithMessage(R.string.edit_avatar_removed);
//                    }
//                },
//                t -> {
//                    exceptionTracker.track(t);
//                    if (isAdded()) {
//                        if (t instanceof ApiException && t.getMessage() != null) {
//                            finishWithError(t.getMessage());
//                        } else {
//                            finishWithError(R.string.edit_avatar_remove_error);
//                        }
//                    }
//                }
//        );
    }

    public void dialogCancelled() {
        finish();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void startCameraActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getImageToCropFile();
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            finishWithError(R.string.unknown_error);
            return;
        }
        imageToCrop = FileProvider.getUriForFile(getContext(), FILE_PROVIDER_AUTHORITY, file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageToCrop);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_CAPTURE);
    }

    private File getImageToCropFile() {
        File picturesDir = new File(getContext().getExternalCacheDir(), PICTURES_DIR_NAME);
        return new File(picturesDir, IMAGE_TO_CROP_FILE_NAME);
    }

    @SuppressWarnings("deprecation")
    private void startCropActivity() {
        // no idea why they implemented setting colors that way instead of using theme
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.light_bar_background));
        options.setStatusBarColor(getResources().getColor(R.color.dark_bar_background));
        options.setActiveWidgetColor(getResources().getColor(R.color.action_bar_background));
        UCrop.of(imageToCrop, Uri.fromFile(new File(getContext().getExternalCacheDir(), IMAGE_TO_UPLOAD_FILE_NAME)))
                .withAspectRatio(1, 1)
                .withMaxResultSize(AVATAR_MAX_SIZE, AVATAR_MAX_SIZE)
                .withOptions(options)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                startCropActivity();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finishWithMessage(R.string.edit_avatar_canceled);
            } else {
                finishWithError(R.string.unknown_error);
            }
        } else if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == AppCompatActivity.RESULT_OK && data != null && data.getData() != null) {
                copyImageAndStartCropActivity(data.getData());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finishWithMessage(R.string.edit_avatar_canceled);
            } else {
                finishWithError(R.string.unknown_error);
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                imageToUpload = UCrop.getOutput(data);
                uploadAvatar();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finishWithMessage(R.string.edit_avatar_canceled);
            } else {
                finishWithError(R.string.unknown_error);
            }
        }
    }

    private void copyImageAndStartCropActivity(Uri data) {
        File file = getImageToCropFile();
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), null, getString(R.string.edit_avatar_loading));
        Completable.fromAction(() -> IOUtils.copyUriToFile(getContext(), data, file))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            if (isAdded()) {
                                progressDialog.dismiss();
                                imageToCrop = Uri.fromFile(file);
                                startCropActivity();
                            }
                        },
                        t -> {
                            if (isAdded()) {
                                progressDialog.dismiss();
                                finishWithError(R.string.unknown_error);
                            }
                        }
                );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraActivity();
            } else {
                finishWithError(R.string.edit_avatar_camera_permission_error);
            }
        }
    }

    private void uploadAvatar() {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), null, getString(R.string.edit_avatar_uploading));
        if (!imageToUpload.getScheme().equals("file")) {
            //TODO: copy uri to file
        }

        profileService.upload(new File(imageToUpload.getPath())).subscribe(() -> {
            if (isAdded()) {
                progressDialog.dismiss();
                finishWithMessage(R.string.edit_avatar_uploaded);
                onUploadFinishedListener.refreshProfileImage();
            }
        }, err -> {
            if (isAdded()) {
                progressDialog.dismiss();
                finishWithError(R.string.edit_avatar_upload_error);
            }
        });
    }

    private void finishWithMessage(int messageRes) {
        Toast.makeText(getContext(), messageRes, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void finishWithError(int errorMessageRes) {
        if (isAdded()) {
            finishWithError(getString(errorMessageRes));
        }
    }

    private void finishWithError(String errorMessage) {
        if (!isAdded()) {
            return;
        }
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        finish();
    }

    private void finish() {
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }
}

