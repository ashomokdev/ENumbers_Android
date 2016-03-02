package com.ashomok.eNumbers.data_load;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.ashomok.eNumbers.data_load.ENumbersSQLiteAssetHelper;

/**
 * Created by Iuliia on 14.09.2015.
 */
public class ENCursorLoader extends CursorLoader {

    private ENumbersSQLiteAssetHelper db;

    private String[] codes;

    public ENCursorLoader(Context context, ENumbersSQLiteAssetHelper db) {
        super(context);
        this.db = db;
    }

    public ENCursorLoader(Context context, ENumbersSQLiteAssetHelper db, Bundle bundle) {
        super(context);
        this.db = db;
        if (bundle != null) {
            codes = bundle.getStringArray("codes");
        }
    }

    @Override
    public Cursor loadInBackground() {
        try {
            Cursor cursor;
            if (codes == null) {
                cursor = db.selectAllData();
            } else {
                cursor = db.selectRowsByCodes(codes);
            }
            return cursor;

        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage() + Log.getStackTraceString(e));
        }
        return null;
    }
}
