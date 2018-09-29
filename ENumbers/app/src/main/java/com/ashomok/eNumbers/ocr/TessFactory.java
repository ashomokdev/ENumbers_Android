package com.ashomok.eNumbers.ocr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

class TessFactory {

    public static final String lang = "eng";

    public static final String ASSETS_PATH = Environment.getExternalStorageDirectory().toString() + "/ENumbers/";
    private static final String TESSDATA = "tessdata";

    private static final String TAG = "TessFactory";

    private final AssetManager context;
    private TessBaseAPI tessBaseApi;

    public TessFactory(AssetManager context) {
        this.context = context;
    }

    public TessBaseAPI getTess() throws TesseractNotInitializedException,
            DirectoryNotCreatedException {
        if (tessBaseApi == null) {
            initTessdata();
            tessBaseApi = new TessBaseAPI();
        }

        Log.i(TAG, "TessBaseAPI initialized...");

        return tessBaseApi;
    }

    private void initTessdata() throws TesseractNotInitializedException,
            DirectoryNotCreatedException {

        prepareDirectories(
                new String[]{
                        ASSETS_PATH + TESSDATA
                });

        copyFiles(TESSDATA);
    }

    private void copyFiles(String source) throws TesseractNotInitializedException {
        try {

            String fileList[] = context.list(source);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = ASSETS_PATH + source + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = context.open(source + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.v(TAG, "Copied " + fileName + "to tessdata");
                }
                if (!(new File(pathToDataFile)).exists()) {
                    Log.e(TAG, fileName + "was not copied to tessdata");
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Was unable to copy files to tessdata " + e.toString());
            throw new TesseractNotInitializedException("Could not copy language files... Unable to initialize Tesseract library");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void prepareDirectories(String[] paths) throws DirectoryNotCreatedException {
        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path
                            + " on sdcard failed");
                    throw new DirectoryNotCreatedException(
                            "Could not create folders for tesseract training data...s");
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }
        }
    }
}
