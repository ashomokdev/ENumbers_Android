package com.ashomok.eNumbers;

import android.content.res.AssetManager;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.IsolatedContext;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.ashomok.eNumbers.activities.MainActivity;
import com.ashomok.eNumbers.ocr.OCREngine;
import com.ashomok.eNumbers.sql.ENumbersSQLiteAssetHelper;

import junit.framework.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/ENumbers/";
    public static final String DATA_DIR = "test_imgs";

    public static final String TAG = "MainActivityTest";

    public MainActivityTest() {

        super(MainActivity.class);
    }

    public void testSQLiteAssetHelper() {

        IsolatedContext _openHelperContext = new IsolatedContext(new MockContentResolver(), getActivity());

        ENumbersSQLiteAssetHelper helper = new ENumbersSQLiteAssetHelper(_openHelperContext);

        Assert.assertTrue(helper.selectRowsByCodes(new String[]{"E100"}).getCount() == 1);

        Assert.assertTrue(helper.selectRowsByCodes(new String[]{"E100", "E123"}).getCount() == 2);
    }

    public void testOCREngine() throws IOException {
        //create folders for tessdata files
        prepareDirectories(
                new String[]{DATA_PATH + DATA_DIR});

        ArrayList<String> files = copyFiles();

        OCREngine ocrEngine = new OCREngine();

        for (String file: files) {
            String result = ocrEngine.RetrieveText(getActivity().getApplicationContext(),
                    file);
            Log.v(TAG, "Result from " + file + "/n" + result);
        }

    }

    private void prepareDirectories(String[] paths) {
        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path
                            + " on sdcard failed");
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }
        }
    }

     private ArrayList<String> copyFiles() {

         ArrayList<String> files = new ArrayList<String>();

         try {
            AssetManager assetManager = getInstrumentation().getContext().getAssets();
            String fileList[] = assetManager.list(DATA_DIR);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + DATA_DIR + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = assetManager.open(DATA_DIR + "/" + fileName);

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
                files.add(pathToDataFile);
            }
        } catch (IOException e) {
            Log.e(TAG, "Was unable to copy files to test_imgs " + e.toString());

        }
         return files;
    }
}