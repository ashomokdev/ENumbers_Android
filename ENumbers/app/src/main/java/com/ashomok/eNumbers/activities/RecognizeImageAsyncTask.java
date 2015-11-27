package com.ashomok.eNumbers.activities;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.ocr.OCREngine;
import com.ashomok.eNumbers.ocr.OCREngineImpl;

/**
 * Created by Iuliia on 19.11.2015.
 */
public class RecognizeImageAsyncTask extends AsyncTask<Void, Integer, String[]> {

    private TaskDelegate delegate;
    private String img_path;
    private AssetManager assetMgr;
    private MainFragment parentFragment;
    private ProgressBar progressBar;
    private LinearLayout progressBarLayout;

    public RecognizeImageAsyncTask(MainFragment parentFragment, String img_path, TaskDelegate delegate) {
        this.parentFragment = parentFragment;
        this.img_path = img_path;
        this.delegate = delegate;

        progressBar = (ProgressBar) parentFragment.getView().findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        progressBarLayout = (LinearLayout) parentFragment.getView().findViewById(R.id.progressBarLayout);
    }

    @Override
    protected void onPreExecute() {
        assetMgr = parentFragment.getActivity().getAssets();

        progressBarLayout.setVisibility( View.VISIBLE);

        parentFragment.getView().findViewById(R.id.data_layout).setVisibility(View.INVISIBLE);
    }


    @Override
    protected String[] doInBackground(Void... params) {

        OCREngine ocrEngine = new OCREngineImpl();

        String text = ocrEngine.RetrieveText(assetMgr, img_path);

        if (!text.isEmpty())
        {
            String[] result = ocrEngine.parseResult(text);
            return  result;
        }

        return new String[0];
    }

//TODO
    @Override
    protected void onPostExecute(String[] result) {

        delegate.TaskCompletionResult(result);

//        parentFragment.scAdapter.changeCursor(null);
//        parentFragment.outputWarning.setText(result);

        progressBarLayout.setVisibility(View.INVISIBLE);

        parentFragment.getView().findViewById(R.id.data_layout).setVisibility(View.VISIBLE);

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}

