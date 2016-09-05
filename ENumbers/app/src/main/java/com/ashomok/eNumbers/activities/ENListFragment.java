package com.ashomok.eNumbers.activities;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.data_load.EN;
import com.ashomok.eNumbers.data_load.ENAsyncLoader;
import com.ashomok.eNumbers.keyboard.CustomKeyboard;
import com.ashomok.eNumbers.ocr.OCREngine;
import com.ashomok.eNumbers.ocr.OCREngineImpl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * Created by Iuliia on 29.08.2015.
 */
public abstract class ENListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<EN>> {

    private ImageButton closeBtn;
    private EditText inputEditText;
    private ENumberListAdapter scAdapter;
    private ListView listView;
    private TextView outputWarning;

    private static final String TAG = ENListFragment.class.getSimpleName();


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);

            inputEditText = (EditText) view.findViewById(R.id.inputE);

//            //hide default keyboard
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                inputEditText.setShowSoftInputOnFocus(false);
//            } else {
//                try {
//                    final Method method = EditText.class.getMethod(
//                            "setShowSoftInputOnFocus", boolean.class);
//                    method.setAccessible(true);
//                    method.invoke(inputEditText, false);
//                } catch (Exception e) {
//                    Log.e(TAG, e.getMessage());
//
//                }
//            }

            closeBtn = (ImageButton) view.findViewById(R.id.ic_close);
            closeBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    inputEditText.setText("");

                    showAllData();
                }
            });

            listView = (ListView) view.findViewById(R.id.ENumberList);

            outputWarning = (TextView) view.findViewById(R.id.warning);
            listView.setEmptyView(outputWarning);

            listView.setAdapter(scAdapter);

        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        scAdapter = new ENumberListAdapter(getActivity(), 0);

        listView.setAdapter(scAdapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);

        //todo call keyboard insted. implement fassade pattern here
        CustomKeyboard keyboard = new CustomKeyboard();
        keyboard.init(getActivity());
        keyboard.setOnSubmitListener(new CustomKeyboard.OnSubmitListener() {
            @Override
            public void onSubmit() {
                GetInfoFromInputting(inputEditText.getText().toString());
            }
        });
    }


    void GetInfoFromInputting(String input) {

        OCREngine parser = new OCREngineImpl();
        Set<String> enumbers = parser.parseResult(input);

        GetInfoByENumbersArray(enumbers.toArray(new String[enumbers.size()]));
    }

    void GetInfoByENumbersArray(String[] enumbers) {
        Bundle b = new Bundle();
        b.putStringArray("codes_array", enumbers);
        try {

            getLoaderManager().restartLoader(0, b, this);

        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }

    private void showAllData() {
        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public Loader<List<EN>> onCreateLoader(int i, Bundle bundle) {
        Log.d(TAG, "onCreateLoader(int i, Bundle bundle)");
        // Prepare the loader
        return new ENAsyncLoader(getActivity(), bundle);

    }


    //use data here
    @Override
    public void onLoadFinished(Loader<List<EN>> loader, List<EN> data) {
        Log.d(TAG, "onLoadFinished(Loader<List<EN>> loader, List<EN> data)");
        try {
            // Set the new data in the adapter.
            scAdapter.setData(data);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {

                    EN item = (EN) parent.getAdapter().getItem(position);

                    Intent intent = new Intent(getActivity(), ENDetailsActivity.class);

                    intent.putExtra(EN.TAG, item);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }

    @Override
    public void onLoaderReset(Loader<List<EN>> loader) {
        Log.d(TAG, "onLoaderReset(Loader<List<EN>> loader) ");
        scAdapter.setData(null);
    }

}