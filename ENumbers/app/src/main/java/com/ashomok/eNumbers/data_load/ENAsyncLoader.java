package com.ashomok.eNumbers.data_load;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iuliia on 14.09.2015.
 */
public class ENAsyncLoader extends AsyncTaskLoader<List<EN>> {


    private static final String TAG = ENAsyncLoader.class.getSimpleName();
    private String[] codes;
    private int startValue = -1;
    private int endValue = -1;
    private String name;

    private Context context;
    private List<EN> data;

    public ENAsyncLoader(Context context, Bundle bundle) {
        super(context);

        this.context = context;

        if (bundle != null) {
            codes = bundle.getStringArray("codes_array");
            startValue = bundle.getInt("start_value");
            endValue = bundle.getInt("end_value");
            name = bundle.getString("name");
        }
    }

    @Override
    protected void onStartLoading() {
        try {
            if (data != null) {
                deliverResult(data);
            }
            if (takeContentChanged() || data == null) {
                forceLoad();
            }

            Log.d(TAG, "onStartLoading() ");
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void deliverResult(final List<EN> data) {
        Log.d(TAG, "deliverResult(final List<EN> data)");
        super.deliverResult(data);
    }

    @Override
    public List<EN> loadInBackground() {
        Log.d(TAG, "loadInBackground()");
        try {
            EN_ORM instance = EN_ORM.getInstance(context);
            data = new ArrayList<>();
            if (codes != null) {
                data = instance.getEnumbsByCodeArray(codes);
            } else if (name != null) {
                data = instance.getEnumbsByName(name);
            } else if (startValue > 0 && endValue > 0) {
                data = instance.getEnumbsByCodeRange(startValue, endValue);
            } else {
                data = instance.getAllEnumbs();
            }
            return data;

        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage() + Log.getStackTraceString(e));
        }
        return null;
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources if needed.
        if (data != null) {
            data = null;
        }
    }

}
