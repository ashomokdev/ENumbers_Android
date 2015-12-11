package com.ashomok.eNumbers.activities.ocr_task;

import android.app.Fragment;
import android.content.res.AssetManager;

import com.ashomok.eNumbers.activities.TaskDelegate;
import com.ashomok.eNumbers.ocr.OCREngine;
import com.ashomok.eNumbers.ocr.OCREngineImpl;

/**
 * Created by Iuliia on 09.12.2015.
 */
public final class RecognizeImageAsyncTaskStandalone extends RecognizeImageAsyncTask {

    private String img_path;
    private Fragment parentFragment;

    public RecognizeImageAsyncTaskStandalone(Fragment parentFragment, String img_path, TaskDelegate delegate) {
        super(parentFragment, delegate);
        this.img_path = img_path;
        this.parentFragment = parentFragment;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        OCREngine ocrEngine = new OCREngineImpl();

        AssetManager assetMgr = parentFragment.getActivity().getAssets();
        String text = ocrEngine.RetrieveText(assetMgr, img_path);

        if (!text.isEmpty()) {
            String[] result = ocrEngine.parseResult(text);
            return result;
        }

        return new String[0];
    }


}
