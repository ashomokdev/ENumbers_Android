package com.ashomok.eNumbers.ocr;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

/**
 * Created by Iuliia on 10.11.2015.
 */
public class OCREngineImpl extends OCREngine {

    private static final String TAG = OCREngineImpl.class.getSimpleName();

    @Override
    public String retrieveText(Context context, String path) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        //tesseract can not recognize unrotated image, so rotate firstly.
        try {
            ExifInterface exif = new ExifInterface(path);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            Log.v(TAG, "Orient: " + exifOrientation);
            int rotate = 0;
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }
            Log.v(TAG, "Rotation: " + rotate);
            if (rotate != 0) {
                // Getting width & height of the given image.
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.postRotate(rotate);
                // Rotating Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
            }
            // Convert to ARGB_8888, required by tess
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        } catch (IOException e) {
            Log.e(TAG, "Couldn't correct orientation: " + e.toString());
        }
        TessBaseAPI tess = new TessBaseAPI();
        // Given path must contain subdirectory `tessdata` where are `*.traineddata` language files
        String dataPath = context.getExternalFilesDir(null).getPath() + "/ENumbers/";
        // Initialize API for specified language (can be called multiple times during Tesseract lifetime)
        if (!tess.init(dataPath, "eng")) {
           throw new IOException("Error initializing Tesseract (wrong data path or language)");
        }
        // Specify image and then recognize it and get result (can be called multiple times during Tesseract lifetime)
        tess.setImage(bitmap);
        return tess.getUTF8Text();
    }
}
