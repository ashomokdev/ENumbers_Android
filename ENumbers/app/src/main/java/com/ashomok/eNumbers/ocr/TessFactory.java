package com.ashomok.eNumbers.ocr;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.ashomok.eNumbers.tools.LogHelper;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TessFactory {
    public String ASSETS_PATH;
    private static final String TESSDATA = "tessdata";
    private static final String TAG = TessFactory.class.getSimpleName();
    private final Context context;

    public TessFactory(Context context) {
        this.context = context;
        ASSETS_PATH = context.getExternalFilesDir(null).getPath() + "/ENumbers/";
    }

    public void initTessdata() throws IOException {
        prepareDirectories(new String[]{ASSETS_PATH + TESSDATA});
        copyFiles(TESSDATA);
    }

    private void copyFiles(String source) {
        try {
            String[] fileList = context.getAssets().list(source);
            for (String fileName : fileList) {
                // open file within the assets folder, if it is not already there copy it to the sdcard
                String pathToDataFile = ASSETS_PATH + source + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {
                    InputStream in = context.getAssets().open(source + "/" + fileName);
                    OutputStream out = new FileOutputStream(pathToDataFile);
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    LogHelper.v(TAG, "Copied " + fileName + "to tessdata");
                }
                if (!(new File(pathToDataFile)).exists()) {
                    LogHelper.e(TAG, fileName + "was not copied to tessdata");
                }
            }
        } catch (IOException e) {
            LogHelper.e(TAG, "Was unable to copy files to tessdata " + e);
        }
    }

    public void prepareDirectories(String[] paths) throws IOException {
        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    LogHelper.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    throw new IOException("Could not create folders");
                } else {
                    LogHelper.v(TAG, "Created directory " + path + " on sdcard");
                }
            }
        }
    }
}
