package com.ashomok.eNumbers.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ashomok.eNumbers.BuildConfig;
import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.activities.ocr_task.OCRAnimationActivity;
import com.ashomok.eNumbers.activities.ocr_task.OCRFirstRunDialogFragment;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTask;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageRESTClient;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageStandalone;
import com.ashomok.eNumbers.tools.RequestPermissionsTool;
import com.ashomok.eNumbers.tools.RequestPermissionsToolImpl;

import java.io.File;
import java.io.IOException;


/**
 * Created by Iuliia on 29.08.2015.
 */

public class SearchByCodeFragment extends ENListKeyboardFragment implements RecognizeImageAsyncTask.OnTaskCompletedListener, FragmentCompat.OnRequestPermissionsResultCallback {

    private static final int CaptureImage_REQUEST_CODE = 1;
    private static final int OCRAnimationActivity_REQUEST_CODE = 2;

    private static final String TAG = "SearchByCodeFragment";
    private String img_path;
    private RecognizeImageAsyncTask recognizeImageAsyncTask;
    private Activity context;
    private String[] permissions;
    private RequestPermissionsTool requestTool;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.start_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);

            FloatingActionButton fab = view.findViewById(R.id.fab);
            fab.setOnClickListener(new FabClickHandler());

            context = getActivity();
            requestTool = new RequestPermissionsToolImpl();

        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        //making photo
        if (requestCode == CaptureImage_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            startOCRtask(img_path); //todo sometimes null here
        }

        //ocr canceled
        else if (requestCode == OCRAnimationActivity_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            recognizeImageAsyncTask.cancel(true);
        }
    }

    private void startOCRtask(String img_path) {

        //run animation
        Intent intent = new Intent(context, OCRAnimationActivity.class);
        intent.putExtra("image", img_path);
        getActivity().startActivityForResult(intent, OCRAnimationActivity_REQUEST_CODE);

        try {
            //start ocr
            if (isNetworkAvailable(context)) {
                recognizeImageAsyncTask = new RecognizeImageRESTClient(img_path);
                //recognizeImageAsyncTask = new RecognizeImageGoogleVision(context, Uri.fromFile(new File(img_path)), null);

            } else {
                recognizeImageAsyncTask = new RecognizeImageStandalone(context, img_path);
            }
            recognizeImageAsyncTask.setOnTaskCompletedListener(this);
            recognizeImageAsyncTask.execute();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * to get high resolution image from camera
     */
    private void startBuildInCameraActivity() {

        for (String permission : permissions) {
            if ((ContextCompat.checkSelfPermission(getActivity(),
                    permission)
                    != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions();
                return;
            }
        }

        startCamera();

    }


    void startCamera() {
        try {
            dispatchTakePictureIntent();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    private File createImageFile() {
        Log.d(TAG, "in createImageFile");
        // Create an image file name
        String imageFileName = "ocr";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");

        File image = null;
        try {
            if (!storageDir.exists()) {
                prepareDirectory(storageDir.getPath());
            }

            image = new File(storageDir, imageFileName + ".jpg");


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        // Save a file: path for use with ACTION_VIEW intents
        if (image != null) {
            img_path = image.getAbsolutePath();
            Log.i(TAG, "img_path = " + img_path);
        } else {
            Log.e(TAG, "image == null");
        }
        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
                Log.e(TAG, "Error occurred while creating the File ");
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri outputFileUri;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //explanation https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat/en

                    outputFileUri = FileProvider.getUriForFile(context,
                            BuildConfig.APPLICATION_ID + ".provider",
                            createImageFile());
                } else {
                    outputFileUri = Uri.fromFile(createImageFile());
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(takePictureIntent, CaptureImage_REQUEST_CODE);
            }
        } else {
            Toast.makeText(context, getResources().getString(R.string.camera_not_found), Toast.LENGTH_SHORT).show();
        }
    }


    private void requestPermissions() {

        requestTool.requestPermissions(this, permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean grantedAllPermissions = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                grantedAllPermissions = false;
            }
        }

        if (grantResults.length != permissions.length || (!grantedAllPermissions)) {

            requestTool.onPermissionDenied();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            startCamera();
        }

    }

    @Override
    public void onTaskCompleted(String[] result) {
        getActivity().finishActivity(OCRAnimationActivity_REQUEST_CODE);
        GetInfoByENumbersArray(result);
    }

    private class FabClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean firstOpened = preferences.getBoolean("first_opened", true);
            if (firstOpened) {

                showWelcomeScreen();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("first_opened", false);
                editor.apply();
            } else {
                startBuildInCameraActivity();
            }
        }

        private void showWelcomeScreen() {
            OCRFirstRunDialogFragment dialog = new OCRFirstRunDialogFragment();
            dialog.show(getFragmentManager(), "dialog");
            dialog.setOnSubmitListener(() -> startBuildInCameraActivity());
        }
    }

    public static void prepareDirectory(String path) throws Exception {

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e(TAG, "ERROR: Creation of directory " + path
                        + " failed");
                throw new Exception(
                        "Could not create folder" + path);
            }
        } else {
            Log.d(TAG, "Created directory " + path);
        }
    }

    @Override
    protected void loadDefaultData() {
        showAllData();
    }
}