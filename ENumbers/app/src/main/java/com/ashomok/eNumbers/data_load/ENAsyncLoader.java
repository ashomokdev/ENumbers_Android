package com.ashomok.eNumbers.data_load;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import com.ashomok.eNumbers.tools.LogHelper;

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
        LogHelper.d(TAG, "ENAsyncLoader created.");
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

            LogHelper.d(TAG, "onStartLoading() ");
        } catch (Exception e) {
            LogHelper.d(TAG, e.getMessage());
        }
    }

    @Override
    public void deliverResult(final List<EN> data) {
        LogHelper.d(TAG, "deliverResult(final List<EN> data)");
        super.deliverResult(data);
    }

    @Override
    public List<EN> loadInBackground() {
        LogHelper.d(TAG, "loadInBackground()");
        try {
            EN_ORM instance = EN_ORM.getInstance(context);
            data = new ArrayList<>();
            if (codes != null) {
                LogHelper.d(TAG, "getEnumbsByCodeArray request");
                data = instance.getEnumbsByCodeArray(codes);
            } else if (name != null) {
                LogHelper.d(TAG, "getEnumbsByName request");
                data = instance.getEnumbsByName(name);
            } else if (startValue > 0 && endValue > 0) {
                LogHelper.d(TAG, "getEnumbsByCodeRange request");
                data = instance.getEnumbsByCodeRange(startValue, endValue);
            } else {
                LogHelper.d(TAG, "getAllEnumbs request");
                data = instance.getAllEnumbs();
            }
            LogHelper.d(TAG, "data size = " + data.size() + " obtained from DB.");
            return data;

        } catch (Exception e) {
            LogHelper.e(this.getClass().getCanonicalName(), e.getMessage() + Log.getStackTraceString(e));
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
