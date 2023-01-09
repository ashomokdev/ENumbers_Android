package com.ashomok.eNumbers;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.ashomok.eNumbers.activities.MainActivity;
import com.ashomok.eNumbers.activities.SearchByCodeFragment;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTask;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageStandalone;
import com.ashomok.eNumbers.ocr.OCREngineImpl;
import com.ashomok.eNumbers.ocr.TessFactory;

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
import java.util.concurrent.ExecutionException;

/**
 * Created by iuliia on 10/25/16.
 */

@RunWith(AndroidJUnit4.class)
public class RecognizeImageTest {

    private static final String TAG = RecognizeImageTest.class.getSimpleName();
    private static final String TEST_IMGS = "test_imgs";
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);
    SearchByCodeFragment fragment;
    private ArrayList<String> files;

    @Before
    public void setup() throws IOException {
        fragment = new SearchByCodeFragment();
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.titles, fragment, null).commitAllowingStateLoss();
        files = getTestImages();
    }

    /**
     * test for Standalone recognition using tesseract (builded in android app).
     */
    @Test
    public void testOCREngineRecognize() throws IOException {
        if (files.size() < 1) {
            throw new AssertionError("Images for test not found.");
        }
        OCREngineImpl ocrEngine = new OCREngineImpl();
        for (String file : files) {
            mActivityRule.getActivity().getAssets();
            String result = ocrEngine.retrieveText(mActivityRule.getActivity(), file);
            Log.i(TAG, "Result from " + file + result);
            appendLog("Result from " + file + result);
        }
    }

    @Test
    public void testRecognizeImageStandalone() throws ExecutionException, InterruptedException {
        for (String path : files) {
            final CountDownLatch signal = new CountDownLatch(1);
            TaskDelegateImpl delegate = new TaskDelegateImpl(path, signal);
            RecognizeImageAsyncTask task = new RecognizeImageStandalone(mActivityRule.getActivity(), path);
            task.setOnTaskCompletedListener(delegate);
            String[] enumbers = task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            assertTrue(enumbers.length > 0);
            try {
                signal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<String> getTestImages() throws IOException {
        //create folders for tessdata files
        TessFactory tessFactory = new TessFactory(mActivityRule.getActivity());
        tessFactory.prepareDirectories(new String[]{tessFactory.ASSETS_PATH + TEST_IMGS});
        ArrayList<String> files = new ArrayList<>();
        try {
            AssetManager assetManager = getInstrumentation().getContext().getAssets();
            String[] fileList = assetManager.list(TEST_IMGS);
            for (String fileName : fileList) {
                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = tessFactory.ASSETS_PATH + TEST_IMGS + "/" + fileName;
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

    private static class TaskDelegateImpl implements RecognizeImageAsyncTask.OnTaskCompletedListener {
        private final CountDownLatch signal;
        private final String filePath;

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
            signal.countDown();
        }
    }
}
