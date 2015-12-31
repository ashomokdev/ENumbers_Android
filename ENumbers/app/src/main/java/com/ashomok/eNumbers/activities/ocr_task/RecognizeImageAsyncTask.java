package com.ashomok.eNumbers.activities.ocr_task;

import android.os.AsyncTask;
import com.ashomok.eNumbers.activities.TaskDelegate;


/**
 * Created by Iuliia on 19.11.2015.
 */
public abstract class RecognizeImageAsyncTask extends AsyncTask<Void, Integer, String[]> {

    private TaskDelegate delegate;

    protected RecognizeImageAsyncTask(TaskDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected abstract String[] doInBackground(Void... params);

    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onPostExecute(String[] result) {

        delegate.TaskCompletionResult(result);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}

