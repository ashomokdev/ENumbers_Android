package com.ashomok.eNumbers.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.activities.capture_image.CaptureImageActivity;
import com.ashomok.eNumbers.activities.ocr_task.OCRAnimationActivity;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTask;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTaskRESTClient;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTaskStandalone;

import java.io.File;

/**
 * Created by Iuliia on 29.08.2015.
 */
public class MainFragment extends ENListFragment implements TaskDelegate {

    private static final int CaptureImage_REQUEST_CODE = 1;
    private static final int OCRAnimationActivity_REQUEST_CODE = 2;

    private String img_path;
    private Uri outputFileUri;
    private FloatingActionButton fab;
    private RecognizeImageAsyncTask recognizeImageAsyncTask;

    private static final String TAG = "MainFragment";
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.start_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);

            fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new FabClickHandler());

            context = view.getContext();

        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {

        //making photo
        if (requestCode == CaptureImage_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                img_path = outputFileUri.getPath();
            } else {
                img_path = data.getStringExtra("file");
            }
            startOCRtask(img_path);
        }

        //ocr canceled
        if (requestCode == OCRAnimationActivity_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            recognizeImageAsyncTask.cancel(true);
        }
    }

    private void startOCRtask(String img_path) {

        //run animation
        Intent intent = new Intent(context, OCRAnimationActivity.class);
        intent.putExtra("image", img_path);
        getActivity().startActivityForResult(intent, OCRAnimationActivity_REQUEST_CODE);

        //start ocr
        if (isNetworkAvailable(context)) {
            recognizeImageAsyncTask = new RecognizeImageAsyncTaskRESTClient(img_path, this);

        } else {
            recognizeImageAsyncTask = new RecognizeImageAsyncTaskStandalone(context, img_path, this);
        }
        recognizeImageAsyncTask.execute();

    }

    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }



    @Override
    public void TaskCompletionResult(String[] result) {
        getActivity().finishActivity(OCRAnimationActivity_REQUEST_CODE);
        GetInfoByENumbersArray(result);
    }

    private class FabClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                startBuildInCameraActivity(v);
            } else {

                Intent intent = new Intent(v.getContext(), CaptureImageActivity.class);
                startActivityForResult(intent, CaptureImage_REQUEST_CODE);

            }
        }
    }

    /**
     * to get high resolution image from camera
     *
     * @param v
     */
    private void startBuildInCameraActivity(View v) {
        try {
            String IMGS_PATH = Environment.getExternalStorageDirectory().toString() + "/ENumbers/imgs";
            prepareDirectory(IMGS_PATH);

            img_path = IMGS_PATH + "/ocr.jpg";

            File file = new File(img_path);
            outputFileUri = Uri.fromFile(file);

            final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            if (takePictureIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, CaptureImage_REQUEST_CODE);
            }
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }

    private void prepareDirectory(String path) throws Exception {

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.v(TAG, "ERROR: Creation of directory " + path
                        + " on sdcard failed");
                throw new Exception(
                        "Could not create folder" + path);
            }
        } else {
            Log.v(TAG, "Created directory " + path + " on sdcard");
        }
    }
}