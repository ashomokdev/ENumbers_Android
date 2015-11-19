package com.ashomok.eNumbers.ocr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

class TessFactory {

    public static final String lang = "eng";

    public static final String ASSETS_PATH = Environment.getExternalStorageDirectory().toString() + "/ENumbers/";
    public static final String TESSDATA = "tessdata";

    public static final String CONFIGS = "configs";
    public static final String TESSCONFIGS = "tessconfigs";

    public static final String TAG = "TessFactory";

    private AssetManager context;
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
        if (tessBaseApi == null) {
            Log.i(TAG, "TessBaseAPI still not initialized, just successfully copied");
        } else {
            Log.i(TAG, "TessBaseAPI initialized...");
        }
        return tessBaseApi;
    }

    private void initTessdata() throws TesseractNotInitializedException,
            DirectoryNotCreatedException {

        prepareDirectories(
                new String[]{
                        ASSETS_PATH + TESSDATA
                });

        copyFiles(TESSDATA);

        //create folders for tessdata files
//        prepareDirectories(
//                new String[]{
//                        ASSETS_PATH + TESSDATA,
//                        ASSETS_PATH + TESSDATA + "/" + CONFIGS,
//                        ASSETS_PATH + TESSDATA + "/" + TESSCONFIGS
//                });

//        copyFiles(TESSDATA + "/" + CONFIGS);
//        copyFiles(TESSDATA + "/" + TESSCONFIGS);
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
            }
        } catch (IOException e) {
            Log.e(TAG, "Was unable to copy files to tessdata " + e.toString());
            throw new TesseractNotInitializedException("Could not copy language files... Unable to initialize Tesseract library");
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
