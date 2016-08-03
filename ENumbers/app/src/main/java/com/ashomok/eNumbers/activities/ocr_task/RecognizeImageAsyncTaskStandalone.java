package com.ashomok.eNumbers.activities.ocr_task;

import android.content.Context;
import android.content.res.AssetManager;

import com.ashomok.eNumbers.activities.TaskDelegate;
import com.ashomok.eNumbers.ocr.OCREngine;
import com.ashomok.eNumbers.ocr.OCREngineImpl;

import java.util.Set;

/**
 * Created by Iuliia on 09.12.2015.
 */
public final class RecognizeImageAsyncTaskStandalone extends RecognizeImageAsyncTask {

    private final String img_path;
    private final Context context;

    public RecognizeImageAsyncTaskStandalone(Context context, String img_path, TaskDelegate delegate) {
        super(delegate);
        this.img_path = img_path;
        this.context = context;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        OCREngine ocrEngine = new OCREngineImpl();

        AssetManager assetMgr = context.getAssets();
        String text = ocrEngine.RetrieveText(assetMgr, img_path);

        if (!text.isEmpty()) {
            Set<String> result = ocrEngine.parseResult(text);

            return result.toArray(new String[result.size()]) ;
        }

        return new String[0];
    }


}
