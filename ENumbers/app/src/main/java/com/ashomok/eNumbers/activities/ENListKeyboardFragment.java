package com.ashomok.eNumbers.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.keyboard.CustomKeyboardFacade;

/**
 * Created by iuliia on 11/16/16.
 */

public abstract class ENListKeyboardFragment extends ENListFragment {
    private static final String IS_KEYBOARD_SWOWN_ARG = "IS_KEYBOARD_SHOWN";
    private static String startChar;
    private CustomKeyboardFacade keyboard;
    private boolean isKeyboardShown;
    private EditText inputEditText;

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        keyboard = new CustomKeyboardFacade(getActivity());
        keyboard.init();

        //inputedit text never lose focus - this code will run only once.
        //EXPLANATION: By its nature the first time you touch an EditText it receives focus with
        // OnFocusChangeListener so that the user can type. The action is consumed here therefor
        // OnClick is not called. Each successive touch doesn't change the focus so the event
        // trickles down to the OnClickListener.
        inputEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                if (savedInstanceState == null) {
                    //first run (not recreation after screen rotation)
                    keyboard.show();
                } else {
                    if (isKeyboardShown) {
                        keyboard.showCustomKeyboard();
                    }
                }
            }
        });


        inputEditText.setOnClickListener(view -> keyboard.showCustomKeyboard());
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);

            if (savedInstanceState != null) {
                isKeyboardShown = savedInstanceState.getBoolean(IS_KEYBOARD_SWOWN_ARG);
            }

            startChar = getString(R.string.startChar);
            inputEditText = view.findViewById(R.id.inputE);
            inputEditText.setText(startChar);
            TextWatcher watcher = new StartCharKeeper();
            inputEditText.addTextChangedListener(watcher);


        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(IS_KEYBOARD_SWOWN_ARG, keyboard.isShown());
    }

    private class StartCharKeeper implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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

}
