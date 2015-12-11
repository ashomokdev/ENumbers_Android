package com.ashomok.eNumbers.activities.ocr_task;

import android.app.Fragment;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.activities.TaskDelegate;


/**
 * Created by Iuliia on 19.11.2015.
 */
public abstract class RecognizeImageAsyncTask extends AsyncTask<Void, Integer, String[]> {

    private TaskDelegate delegate;
    private Fragment parentFragment;
    private ProgressBar progressBar;
    private LinearLayout progressBarLayout;

    protected RecognizeImageAsyncTask(Fragment parentFragment, TaskDelegate delegate) {
        this.parentFragment = parentFragment;
        this.delegate = delegate;
        try {
            progressBar = (ProgressBar) parentFragment.getView().findViewById(R.id.progressBar);

            progressBar.setIndeterminate(true);

            progressBarLayout = (LinearLayout) parentFragment.getView().findViewById(R.id.progressBarLayout);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected abstract String[] doInBackground(Void... params);

    @Override
    protected void onPreExecute() {
        progressBarLayout.setVisibility(View.VISIBLE);
        try {
            parentFragment.getView().findViewById(R.id.data_layout).setVisibility(View.INVISIBLE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPostExecute(String[] result) {

        delegate.TaskCompletionResult(result);

        progressBarLayout.setVisibility(View.INVISIBLE);
        try {
            parentFragment.getView().findViewById(R.id.data_layout).setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}

