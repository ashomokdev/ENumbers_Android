package com.ashomok.eNumbers.activities;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ashomok.eNumbers.R;;
import com.ashomok.eNumbers.data_load.EN;
import com.ashomok.eNumbers.data_load.ENAsyncLoader;
import com.ashomok.eNumbers.keyboard.KeyboardFacade;
import com.ashomok.eNumbers.keyboard.OnSubmitListener;
import com.ashomok.eNumbers.ocr.OCREngine;
import com.ashomok.eNumbers.ocr.OCREngineImpl;

import java.util.List;
import java.util.Set;

/**
 * Created by Iuliia on 29.08.2015.
 */
public abstract class ENListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<EN>> {

    private static final String IS_KEYBOARD_SWOWN_ARG = "IS_KEYBOARD_SWOWN";
    private static final String IS_DEFAULT_KEYBOARD_ARG = "IS_DEFAULT_KEYBOARD";
    private EditText inputEditText;
    private ENumberListAdapter scAdapter;
    private ListView listView;

    private static final String TAG = ENListFragment.class.getSimpleName();
    private boolean isKeyboardShown;
    private boolean isDefaultKeyboard;
    private KeyboardFacade keyboard;
    private static String startChar;
    private  ImageButton closeBtn;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);

            startChar = getString(R.string.startChar);

            inputEditText = (EditText) view.findViewById(R.id.inputE);
            TextWatcher watcher = new StartCharKeeper();
            inputEditText.addTextChangedListener(watcher);

            listView = (ListView) view.findViewById(R.id.ENumberList);
            TextView outputWarning = (TextView) view.findViewById(R.id.warning);
            listView.setEmptyView(outputWarning);

            closeBtn = (ImageButton) view.findViewById(R.id.ic_close);

        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);

        closeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                inputEditText.setText(getString(R.string.startChar));
                showAllData();
            }
        });

        scAdapter = new ENumberListAdapter(getActivity(), 0);
        listView.setAdapter(scAdapter);


        if (savedInstanceState != null) {
            isKeyboardShown = savedInstanceState.getBoolean(IS_KEYBOARD_SWOWN_ARG);
            if (isKeyboardShown) {
                isDefaultKeyboard = savedInstanceState.getBoolean(IS_DEFAULT_KEYBOARD_ARG);
            }
        }

        keyboard = new KeyboardFacade(getActivity());
        keyboard.init();

        inputEditText.clearFocus();

        //inputedit text never lose focus - this code will run only once.
        //EXPLANATION: By its nature the first time you touch an EditText it receives focus with OnFocusChangeListener so that the user can type. The action is consumed here therefor OnClick is not called. Each successive touch doesn't change the focus so the event trickles down to the OnClickListener.
        inputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Log.d(TAG, "hasFocus");
                    if (isKeyboardShown) {
                        if (isDefaultKeyboard) {
                            keyboard.showDefaultKeyboard();
                        } else {
                            keyboard.showCustomKeyboard();
                        }
                    } else {
                        keyboard.show();
                    }
                } else {
                    Log.d(TAG, "lose focus");
                }
            }
        });


        inputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyboard.show();
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();

        GetInfoFromInputting(inputEditText.getText().toString());

        keyboard.setOnSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmit() {
                GetInfoFromInputting(inputEditText.getText().toString());
            }
        });
    }


    void GetInfoFromInputting(String input) {
        if (input.contains(getString(R.string.startChar)) && (getString(R.string.startChar)).contains(input)) {
            showAllData();
        } else {

            OCREngine parser = new OCREngineImpl();
            Set<String> enumbers = parser.parseResult(input);

            GetInfoByENumbersArray(enumbers.toArray(new String[enumbers.size()]));
        }
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


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(IS_KEYBOARD_SWOWN_ARG, keyboard.isShown());
        outState.putBoolean(IS_DEFAULT_KEYBOARD_ARG, keyboard.isDefaultKeyboardShown());

        super.onSaveInstanceState(outState);
    }

    private class StartCharKeeper implements TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (keyboard.isShown() && keyboard.isDefaultKeyboardShown()) {
                //DO NOTHING
            } else {
                if (!charSequence.toString().startsWith(startChar)) {

                    inputEditText.setText(startChar);
                }
                inputEditText.setSelection(inputEditText.getText().length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

}