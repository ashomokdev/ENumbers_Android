package com.ashomok.eNumbers.ocr;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Iuliia on 10.11.2015.
 */
public class OCREngineImpl implements OCREngine {

    private static final String TAG = "OCREngineImpl.java";

    private static final String REGEX_ENUMB = "E[ ]{0,2}[0-9]{3,4}[a-j]?";

    @Override
    public String RetrieveText(AssetManager assetMgr, String _path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

        try {
            ExifInterface exif = new ExifInterface(_path);
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
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        Log.v(TAG, "Before baseApi");

        TessExtractor tessExtractor = new TessExtractor(assetMgr, bitmap);

        long startTime = System.currentTimeMillis();
        String text = tessExtractor.getText();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;

        Log.i(TAG, "String text = tessExtractor.getText(); elapsed" + elapsedTime);

        return text;
    }

    @Override
    public Set<String> parseResult(String input) {
        final String E = "E";
        final int lengthOfWord = 8;

        Set<String> words =  new HashSet<>();
        if (input.contains(E)) {
            //get possible E-numbers



            int fromIndex = 0;
            while (fromIndex < input.length()) {

                int wordStart = input.indexOf(E, fromIndex);
                if (wordStart >= 0) { //if E - numbers exist

                    int wordEnd = input.indexOf(E, fromIndex) + lengthOfWord;
                    if (wordEnd > input.length() - 1) {
                        wordEnd = input.length();
                    }
                    String word = input.substring(wordStart, wordEnd);

                    String result = parseWord(word);
                    if (result != null) {
                        words.add(result);
                    }
                    fromIndex = wordStart + 1;
                }
                else
                {
                    fromIndex = input.length();
                }
            }
        }
        return words;
    }

    @Nullable
    private String parseWord(String word) {
        Pattern pattern = Pattern.compile(REGEX_ENUMB);
        Matcher matcher = pattern.matcher(word);
        if (matcher.find()) {
            String result = matcher.group(0).replaceAll("\\s", "");
            return result;
        } else {
            return null;
        }
    }
}
