package com.ashomok.eNumbers.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.keyboard.CustomKeyboard;
import com.ashomok.eNumbers.tools.LogHelper;

/**
 * Created by iuliia on 11/16/16.
 */

public abstract class ENListKeyboardFragment extends ENListFragment {
    private static String startChar;
    private CustomKeyboard keyboard;
    private EditText inputEditText;

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        keyboard = new CustomKeyboard(getActivity());
        keyboard.init();
        inputEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                keyboard.show();
            } else {
                keyboard.hide();
            }
        });
        inputEditText.setOnClickListener(view -> keyboard.show());
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            startChar = getString(R.string.startChar);
            inputEditText = view.findViewById(R.id.inputE);
            inputEditText.setText(startChar);
            TextWatcher watcher = new StartCharKeeper();
            inputEditText.addTextChangedListener(watcher);
        } catch (Exception e) {
            LogHelper.e(this.getClass().getCanonicalName(), e.getMessage());
        }
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
