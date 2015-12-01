package com.ashomok.eNumbers.ocr;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Iuliia on 10.11.2015.
 */
public class OCREngineImpl implements OCREngine {

    private static final String TAG = "OCREngineImpl.java";

    private static final String REGEX_ENUMB = "E[ ]{0,2}[0-9]{3,4}[a-j]{0,1}";

//    public String detectText(Bitmap bitmap) {
//
//        TessBaseAPI tessBaseAPI = new TessBaseAPI();
//
//        String path = "/tessdata/eng.traineddata";
//
//        tessBaseAPI.setDebug(true);
//        tessBaseAPI.init(path, "eng"); //Init the Tess with the trained data file, with english language
//
//        //For example if we want to only detect numbers
//        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
//        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]}{POIU" +
//                "YTREWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?");
//
//
//        tessBaseAPI.setImage(bitmap);
//
//        String text = tessBaseAPI.getUTF8Text();
//
//        tessBaseAPI.end();
//
//        return text;
//    }

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


//        TessBaseAPI baseApi = new TessBaseAPI();
//        baseApi.setDebug(true);
//        baseApi.init(ASSETS_PATH, lang);
//        baseApi.setImage(bitmap);
//
//        String recognizedText = baseApi.getUTF8Text();
//
//        baseApi.end();
//
//        // You now have the text in recognizedText var, you can do anything with it.
//        // We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
//        // so that garbage doesn't make it to the display.
//
//        Log.v(TAG, "OCRED TEXT: " + recognizedText);
//
//        if ( lang.equalsIgnoreCase("eng") ) {
//            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
//        }
//
//        recognizedText = recognizedText.trim();
//
//        if ( recognizedText.length() != 0 ) {
//            _field.setText(_field.getText().toString().length() == 0 ? recognizedText : _field.getText() + " " + recognizedText);
//            _field.setSelection(_field.getText().toString().length());
//        }

        // Cycle done.
    }

    @Override
    public String[] parseResult(String input) {
        final String E = "E";
        final int lengthOfWord = 8;

        if (input.contains(E)) {
            //get possible E-numbers

            List<String> words = new ArrayList<>();

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
            return words.toArray(new String[words.size()]);
        } else {
            return new String[0];
        }
    }

    @Nullable
    public String parseWord(String word) {
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
