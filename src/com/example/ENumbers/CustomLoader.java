package com.example.eNumbers;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

/**
 * Created by Iuliia on 11.09.2015.
 */
public class CustomLoader extends CursorLoader implements LoaderManager.LoaderCallbacks {

    public CustomLoader(Context context) {
        super(context);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
