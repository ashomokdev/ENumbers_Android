package com.ashomok.eNumbers.activities;

import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.ashomok.eNumbers.ocr.OCREngine;

/**
 * Created by Iuliia on 19.11.2015.
 */
public class CaptureImageAsyncTask extends AsyncTask<Void, Integer, String> {

    private String img_path;
    private AssetManager assetMgr;
    private MainFragment parentFragment;

    public CaptureImageAsyncTask(MainFragment parentFragment, String img_path) {
        this.parentFragment = parentFragment;
        this.img_path = img_path;
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        assetMgr = parentFragment.getActivity().getAssets();

    }


    @Override
    protected String doInBackground(Void... params) {

        OCREngine ocrEngine = new OCREngine();

        String result = ocrEngine.RetrieveText(assetMgr, img_path);

        return result;
    }


    @Override
    protected void onPostExecute(String result) {
        //  super.onPostExecute(s);

        parentFragment.scAdapter.changeCursor(null);
        parentFragment.outputWarning.setText(result);
    }
}

