package com.ashomok.eNumbers.keyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ashomok.eNumbers.R;

/**
 * Created by iuliia on 9/2/16.
 */
public class CustomKeyboard {
    private static final String TAG = CustomKeyboard.class.getSimpleName();
    private KeyboardView keyboardView;
    private View parent;
    private EditText editText;
    private AudioManager audioManager;
    private CustomKeyboard.OnSubmitListener listener;
    private static String startChar;

    private void createCustomKeyboard(View view, final EditText inputEditText) {
        keyboardView = (KeyboardView) view.findViewById(R.id.keyboard);
        Keyboard customKeyboard = new Keyboard(view.getContext(), R.xml.keyboard_keys);
        keyboardView.setKeyboard(customKeyboard);
        CustomKeyboardListener numbersListener = new CustomKeyboardListener(inputEditText);

        startChar = view.getContext().getString(R.string.startChar);

        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        numbersListener.setSubmitListener(new CustomKeyboardListener.SubmitListener() {
            @Override
            public void onSubmit() {
                notifyListener();
                hideCustomKeyboard();
            }
        });

        keyboardView.setOnKeyboardActionListener(numbersListener);

        inputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomKeyboard();
            }
        });

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

        inputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideDefaultKeyboard(v);
                } else {
                    hideCustomKeyboard();
                }
            }
        });

        audioManager = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);

        inputEditText.setSelection(inputEditText.getText().length()); //starts type after "E"
        inputEditText.addTextChangedListener(new StartCharKeeper());
        inputEditText.setOnEditorActionListener(new BtnDoneHandler());

    }

    private void hideCustomKeyboard() {
        keyboardView.setVisibility(View.GONE);
    }

    private void showCustomKeyboard() {
        keyboardView.setVisibility(View.VISIBLE);
    }

    private void hideDefaultKeyboard(View v) {

        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(keyboardView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        showCustomKeyboard();
    }

    public void init(View parent, EditText inputEditText) {
        this.parent = parent;
        this.editText = inputEditText;

        createCustomKeyboard(parent, inputEditText);
    }

    public interface OnSubmitListener {
        void onSubmit();
    }

    private void notifyListener() {
        listener.onSubmit();
    }

    public void setOnSubmitListener(CustomKeyboard.OnSubmitListener listener) {
        if (editText == null || parent == null) {
            Log.e(TAG, "Keyboard was not initialized. Call init() before");
        } else {
            this.listener = listener;
        }
    }

    private class BtnDoneHandler implements EditText.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                notifyListener();

                //reduntant todo delete
//                //to hide the soft keyboard
//                InputMethodManager imm = (InputMethodManager) parent.getContext().getSystemService(
//                        Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

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

                editText.setText(startChar);
            }
            editText.setSelection(editText.getText().length());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
