package com.ashomok.eNumbers;

import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;

import com.ashomok.eNumbers.activities.MainActivity;
import com.ashomok.eNumbers.activities.SearchByCodeFragment;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTask;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageRESTClient;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageStandalone;
import com.ashomok.eNumbers.ocr.OCREngineImpl;
import com.ashomok.eNumbers.tools.RequestPermissionsTool;
import com.ashomok.eNumbers.tools.RequestPermissionsToolImpl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static java.lang.Thread.sleep;

/**
 * Created by iuliia on 10/25/16.
 */

@RunWith(AndroidJUnit4.class)
public class RecognizeImageTest {

    private static final String TAG = RecognizeImageTest.class.getSimpleName();
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/ENumbers/";
    private static final String TEST_IMGS = "test_imgs";

    SearchByCodeFragment fragment;
    private ArrayList<String> files;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Before
    public void setup() {
        fragment = new SearchByCodeFragment();
        mActivityRule.getActivity().getFragmentManager().beginTransaction().add(R.id.titles, fragment, null).commitAllowingStateLoss();

        files = getTestImages();

    }


    /**
     * test for Standalone recognition using tesseract (builded in android app).
     *
     */
    @Test
    public void testOCREngineRecognize() {

        ArrayList<String> files = getTestImages();
        if (files.size() < 1) {
            throw new AssertionError("Images for test not found.");
        }

        OCREngineImpl ocrEngine = new OCREngineImpl();

        for (String file : files) {
            mActivityRule.getActivity().getAssets();

            AssetManager assetManager = mActivityRule.getActivity().getAssets();

            String result = ocrEngine.RetrieveText(assetManager,
                    file);

            Log.i(TAG, "Result from " + file + result);
            appendLog("Result from " + file + result);
        }
    }

    /**
     * time a method's execution for RecognizeImageRESTClient and RecognizeImageStandalone
     * And comparing
     */
    @Test
    public void testRecognizeImageRESTClient() {

        //results of timing RecognizeImageRESTClient
        //URL  41098071129  40289779933  42650368734
        //URL2 191964505082 203698485182 193867012634 67070300688 71843954948
        //URL1 123115701776 129641047762 131110172450 53560201043 42370121722 195613520967

        long startTime = System.nanoTime();
        for (String path : files) {

            final CountDownLatch signal = new CountDownLatch(1);
            TaskDelegateImpl delegate = new TaskDelegateImpl(path, signal);

            RecognizeImageAsyncTask task = new RecognizeImageRESTClient(path);
            task.setOnTaskCompletedListener(delegate);
            executeTask(signal, task);
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        Log.v(TAG, "RecognizeImageRESTClient: " + duration + "/n");
    }

    @Test
    public void testRecognizeImageStandalone() {

        long startTime = System.nanoTime();
        for (String path : files) {

            final CountDownLatch signal = new CountDownLatch(1);
            TaskDelegateImpl delegate = new TaskDelegateImpl(path, signal);

            RecognizeImageAsyncTask task = new RecognizeImageStandalone(mActivityRule.getActivity(), path);
            task.setOnTaskCompletedListener(delegate);
            executeTask(signal, task);
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        Log.v(TAG, "RecognizeImageStandalone: " + duration + "/n");
    }


    @Test
    public void testDurationRecognizeImageGoogleVision() {

        long startTime = System.nanoTime();

        String path = files.get(10); //3mb file

        final CountDownLatch signal = new CountDownLatch(1);
        TaskDelegateImpl delegate = new TaskDelegateImpl(path, signal);

        RecognizeImageAsyncTask task = new RecognizeImageRESTClient(path);
        task.setOnTaskCompletedListener(delegate);
        executeTask(signal, task);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        Log.v(TAG, "RecognizeImageRESTClient: " + duration + "/n");
    }


    private void executeTask(CountDownLatch signal, RecognizeImageAsyncTask task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
        try {
            signal.await();// wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeSampledBitmap(String pathName,
                                       int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    //I added this to have a good approximation of the screen size:
    private Bitmap decodeSampledBitmap(String pathName) {
        Display display = mActivityRule.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return decodeSampledBitmap(pathName, width, height);
    }

    private void prepareDirectories(String[] paths) {
        if (ContextCompat.checkSelfPermission(mActivityRule.getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            RequestPermissionsTool requestTool = new RequestPermissionsToolImpl();
            requestTool.requestPermissions(fragment, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE});

            final long PERMISSIONS_DIALOG_DELAY = 10000;
            try {
                sleep(PERMISSIONS_DIALOG_DELAY);
            } catch (Exception e) {
                throw new AssertionError("Unexpected error");
            }
        }

        if (ContextCompat.checkSelfPermission(mActivityRule.getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            throw new AssertionError("Test not failed, but needs permission");
        } else {
            for (String path : paths) {
                File dir = new File(path);
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        Log.e(TAG, "ERROR: Creation of directory " + path
                                + " on sdcard failed");
                        Assert.fail("ERROR: Creation of directory " + path
                                + " on sdcard failed");
                    } else {
                        Log.v(TAG, "Created directory " + path + " on sdcard");
                    }
                }
            }
        }
    }

    private ArrayList<String> getTestImages() {
        //create folders for tessdata files
        prepareDirectories(
                new String[]{DATA_PATH + TEST_IMGS});

        ArrayList<String> files = new ArrayList<String>();

        try {

            AssetManager assetManager = getInstrumentation().getContext().getAssets();
            String fileList[] = assetManager.list(TEST_IMGS);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + TEST_IMGS + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = assetManager.open(TEST_IMGS + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.v(TAG, "Copied " + fileName + "to test_imgs");
                }
                if (!(new File(pathToDataFile)).exists()) {

                    throw new AssertionError("Can not copy file.");
                }
                files.add(pathToDataFile);
            }
        } catch (IOException e) {
            Log.e(TAG, "Was unable to copy files to test_imgs " + e.toString());
            Assert.fail("Was unable to copy files to test_imgs " + e.toString());

        }
        return files;
    }

    private void appendLog(String text) {
        File logFile = new File("sdcard/log.file");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class TaskDelegateImpl implements RecognizeImageAsyncTask.OnTaskCompletedListener {
        private CountDownLatch signal;
        private String filePath;

        TaskDelegateImpl(String path, CountDownLatch signal) {
            this.signal = signal;
            filePath = path;
        }

        @Override
        public void onTaskCompleted(String[] result) {
            StringBuilder builder = new StringBuilder();
            for (String s : result) {
                builder.append(s);
            }
            Log.d(TAG, " result from " + filePath + ": " + builder.toString());

            signal.countDown();// notify the count down latch
        }
    }
}
