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
public class ENCursorLoader extends AsyncTaskLoader<List<EN>> {


    private String[] codes;
    private int startValue = -1;
    private int endValue = -1;

    private Context context;
    private boolean hasResult;
    private List<EN> mData;

    public ENCursorLoader(Context context, Bundle bundle) {
        super(context);

        this.context = context;

        if (bundle != null) {
            codes = bundle.getStringArray("codes_array");
            startValue = bundle.getInt("start_value");
            endValue = bundle.getInt("end_value");
        }
    }

    @Override
    protected void onStartLoading() {

            forceLoad();
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final List<EN> data) {
        mData = data;
        hasResult = true;
        super.deliverResult(data);
    }

    //todo never called
    @Override
    public List<EN> loadInBackground() {
        try {
            EN_ORM instance = EN_ORM.getInstance(context);
            List<EN> data = new ArrayList<>();
            if (codes != null) {
                data = instance.getEnumbsByCodeArray(codes);
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
}
