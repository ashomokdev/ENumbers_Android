package com.ashomok.eNumbers.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.data_load.EN;
import com.ashomok.eNumbers.data_load.ENCursorLoader;
import com.ashomok.eNumbers.keyboard.CustomKeyboardListener;
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
    private static String startChar;
    private ENumberListAdapter scAdapter;
    private ListView listView;
    private TextView outputWarning;
    private AudioManager audioManager;
    private Context context;

    private static final String TAG = ENListFragment.class.getSimpleName();
    private KeyboardView keyboardView;



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);

            startChar = getString(R.string.startChar);

            inputEditText = (EditText) view.findViewById(R.id.inputE);

            //hide default keyboard
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                inputEditText.setShowSoftInputOnFocus(false);
            } else {
                try {
                    final Method method = EditText.class.getMethod(
                            "setShowSoftInputOnFocus", boolean.class);
                    method.setAccessible(true);
                    method.invoke(inputEditText, false);
                } catch (Exception e) {
                    // ignore
                }
            }

            inputEditText.setSelection(inputEditText.getText().length()); //starts type after "E"
            inputEditText.addTextChangedListener(new StartCharKeeper());
            inputEditText.setOnEditorActionListener(new BtnDoneHandler());


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

            createCustomKeyboard(view);

            context = view.getContext();

        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // create Loader for data reading
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        inputEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    hideCustomKeyboard();
                    return true;
                }
                return false;
            }
        });
    }


    private void GetInfoFromInputting(String input) {

        OCREngine parser = new OCREngineImpl();
        Set<String> enumbers = parser.parseResult(input);

        GetInfoByENumbersArray(enumbers.toArray(new String[enumbers.size()]));
    }

    private void GetInfoByENumbersArray(String[] enumbers) {
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


    private class BtnDoneHandler implements EditText.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                GetInfoFromInputting(textView.getText().toString());

                //to hide the soft keyboard
                InputMethodManager imm = (InputMethodManager) context.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                return true;
            }

            return false;
        }
    }

    private class StartCharKeeper implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            float vol = 0.3f; //This will be half of the default system sound
            audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (!charSequence.toString().startsWith(startChar)) {

                inputEditText.setText(startChar);
            }
            inputEditText.setSelection(inputEditText.getText().length());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }


    private void createCustomKeyboard(View view) {
        keyboardView = (KeyboardView) view.findViewById(R.id.keyboard);
        Keyboard customKeyboard = new Keyboard(view.getContext(), R.xml.keyboard_keys);
        keyboardView.setKeyboard(customKeyboard);
        CustomKeyboardListener numbersListener = new CustomKeyboardListener(inputEditText);

        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getView() != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }

        numbersListener.setSubmitListener(new CustomKeyboardListener.SubmitListener() {
            @Override
            public void onSubmit() {
                GetInfoFromInputting(inputEditText.getText().toString());
                hideCustomKeyboard();
            }
        });
        keyboardView.setOnKeyboardActionListener(numbersListener);

        inputEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showCustomKeyboard();
                return false;
            }
        });

        inputEditText.setOnFocusChangeListener(focusChangeListener);

        audioManager = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    private void hideCustomKeyboard() {
        keyboardView.setVisibility(View.GONE);
    }

    private void showCustomKeyboard() {
        keyboardView.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).commit();
    }

    private void hideDefaultKeyboard(View v) {

        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(keyboardView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        showCustomKeyboard();
    }

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                hideDefaultKeyboard(v);
            } else {
                hideCustomKeyboard();
            }
        }
    };


    @Override
    public Loader<List<EN>> onCreateLoader(int i, Bundle bundle) {
        // Prepare the loader
        return new ENCursorLoader(getActivity(), bundle);

    }


    //use data here
    @Override
    public void onLoadFinished(Loader<List<EN>> loader, List<EN> data) {
        try {
            scAdapter = new ENumberListAdapter(getActivity(), 0);
            // Set the new data in the adapter.
            scAdapter.setData(data);

            listView.setAdapter(scAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {

                    EN item = (EN) parent.getAdapter().getItem(position);

                    Intent intent = new Intent(getActivity(), ENDetailsActivity.class);//todo update activity instead recreating

                    intent.putExtra(EN.TAG, item);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<EN>> loader) {
//        scAdapter.swapCursor(null);
    }

}