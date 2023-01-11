package com.ashomok.eNumbers.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.data_load.EN;
import com.ashomok.eNumbers.data_load.ENAsyncLoader;
import com.ashomok.eNumbers.ocr.OCREngine;
import com.ashomok.eNumbers.ocr.OCREngineImpl;
import com.ashomok.eNumbers.tools.LogHelper;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Iuliia on 29.08.2015.
 */
public abstract class ENListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<EN>> {

    private static final String TAG = ENListFragment.class.getSimpleName();
    private EditText editText;
    private ENumberListAdapter scAdapter;
    private ListView listView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            editText = view.findViewById(R.id.inputE);
            listView = view.findViewById(R.id.enumber_list);
            TextView outputWarning = view.findViewById(R.id.warning);
            listView.setEmptyView(outputWarning);

            // Prepare the loader.  Either re-connect with an existing one, or start a new one.
            getLoaderManager().initLoader(0, null, this);

            scAdapter = new ENumberListAdapter(getActivity(), 0);
            listView.setAdapter(scAdapter);

            editText.setOnEditorActionListener((v, actionId, event) -> {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loadRequestedData(editText.getText().toString());
                    handled = true;
                }
                return handled;
            });

            ImageButton closeBtn = view.findViewById(R.id.ic_close);
            closeBtn.setOnClickListener(view1 -> {
                editText.setText("");
                loadData();
            });

        } catch (Exception e) {
            LogHelper.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        editText.clearFocus();
    }

    void loadRequestedData(String input) {
        Pattern p = Pattern.compile("[0-9]");
        if (input.equals("") || input.equals(getString(R.string.startChar))) {
            showAllData();
        } else if (input.startsWith(getString(R.string.startChar)) &&
                p.matcher(String.valueOf(input.charAt(1))).matches()) { //second char is number
            OCREngine parser = new OCREngineImpl();
            Set<String> enumbers = parser.parseResult(input);
            getInfoByENumbersArray(enumbers.toArray(new String[enumbers.size()]));
        } else {
            GetInfoByName(input);
        }
    }

    void getInfoByENumbersArray(String[] enumbers) {
        Bundle b = new Bundle();
        b.putStringArray("codes_array", enumbers);
        try {
            getLoaderManager().restartLoader(0, b, this);

        } catch (Exception e) {
            LogHelper.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }

    void GetInfoByName(String name) {
        if (name.isEmpty()) {
            Log.w(TAG, "GetInfoByName with empty name called");
        }
        Bundle b = new Bundle();
        b.putString("name", name);
        try {

            getLoaderManager().restartLoader(0, b, this);

        } catch (Exception e) {
            LogHelper.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void showAllData() {
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public Loader<List<EN>> onCreateLoader(int i, Bundle bundle) {
        LogHelper.d(TAG, "onCreateLoader(int i, Bundle bundle)");
        // Prepare the loader
        return new ENAsyncLoader(getActivity(), bundle);
    }


    //use data here
    @Override
    public void onLoadFinished(Loader<List<EN>> loader, List<EN> data) {
        LogHelper.d(TAG, "onLoadFinished(Loader<List<EN>> loader, List<EN> data)");
        try {
            // Set the new data in the adapter.
            scAdapter.setData(data);

            listView.setOnItemClickListener((parent, arg1, position, arg3) -> {

                EN item = (EN) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), ENDetailsActivity.class);

                intent.putExtra(EN.TAG, item);
                startActivity(intent);
            });
        } catch (Exception e) {
            LogHelper.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }

    @Override
    public void onLoaderReset(Loader<List<EN>> loader) {
        LogHelper.d(TAG, "onLoaderReset(Loader<List<EN>> loader) ");
        scAdapter.setData(null);
    }

    protected abstract void loadData();
}