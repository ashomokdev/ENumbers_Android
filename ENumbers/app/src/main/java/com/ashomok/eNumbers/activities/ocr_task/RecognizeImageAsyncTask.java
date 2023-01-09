package com.ashomok.eNumbers.activities.ocr_task;

import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by Iuliia on 19.11.2015.
 */
public abstract class RecognizeImageAsyncTask extends AsyncTask<Void, Integer, String[]> {
    private static final String TAG = RecognizeImageAsyncTask.class.getSimpleName();
    private OnTaskCompletedListener onTaskCompletedListener;

    @Override
    protected abstract String[] doInBackground(Void... params);

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String[] result) {
        StringBuilder resultString = new StringBuilder();
        for (String aResult : result) {
            resultString.append(", ").append(aResult);
        }
        Log.d(TAG, resultString.toString());
        onTaskCompletedListener.onTaskCompleted(result);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    public void setOnTaskCompletedListener(OnTaskCompletedListener onTaskCompletedListener) {
        this.onTaskCompletedListener = onTaskCompletedListener;
    }

    public interface OnTaskCompletedListener {
        void onTaskCompleted(String[] result);
    }
}

