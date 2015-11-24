package com.ashomok.eNumbers.activities;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.ocr.OCREngineImpl;

/**
 * Created by Iuliia on 19.11.2015.
 */
public class CaptureImageAsyncTask extends AsyncTask<Void, Integer, String> {

    private String img_path;
    private AssetManager assetMgr;
    private MainFragment parentFragment;
    private ProgressBar progressBar;
    private LinearLayout progressBarLayout;

    public CaptureImageAsyncTask(MainFragment parentFragment, String img_path) {
        this.parentFragment = parentFragment;
        this.img_path = img_path;

        progressBar = (ProgressBar) parentFragment.getView().findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        progressBarLayout = (LinearLayout) parentFragment.getView().findViewById(R.id.progressBarLayout);
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        assetMgr = parentFragment.getActivity().getAssets();

        progressBarLayout.setVisibility( View.VISIBLE);

        parentFragment.getView().findViewById(R.id.data_layout).setVisibility(View.INVISIBLE);
    }


    @Override
    protected String doInBackground(Void... params) {

        OCREngineImpl ocrEngine = new OCREngineImpl();

        String result = ocrEngine.RetrieveText(assetMgr, img_path);

        return result;
    }


    @Override
    protected void onPostExecute(String result) {
         super.onPostExecute(result);

        parentFragment.scAdapter.changeCursor(null);
        parentFragment.outputWarning.setText(result);

        progressBarLayout.setVisibility(View.INVISIBLE);

        parentFragment.getView().findViewById(R.id.data_layout).setVisibility(View.VISIBLE);

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}

