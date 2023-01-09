package com.ashomok.eNumbers.activities.ocr_task;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.ashomok.eNumbers.ocr.OCREngine;
import com.ashomok.eNumbers.ocr.OCREngineImpl;
import com.ashomok.eNumbers.ocr.TessFactory;

import java.io.File;
import java.util.Set;

/**
 * Created by Iuliia on 09.12.2015.
 */
public final class RecognizeImageStandalone extends RecognizeImageAsyncTask {

    private static final String TAG = RecognizeImageStandalone.class.getSimpleName();
    private final String img_path;
    private final Context context;

    public RecognizeImageStandalone(Context context, String img_path) {
        this.img_path = img_path;
        this.context = context;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        try {
            if (!(new File(img_path)).exists()) {
                Log.e(TAG, "File was not obtained.");
            }
            TessFactory tessFactory = new TessFactory(context);
            tessFactory.initTessdata();
            OCREngine ocrEngine = new OCREngineImpl();
            String text = ocrEngine.retrieveText(context, img_path);
            if (!text.isEmpty()) {
                Set<String> result = ocrEngine.parseResult(text);
                return result.toArray(new String[result.size()]);
            }
            return new String[0];
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return new String[0];
        }
    }
}
