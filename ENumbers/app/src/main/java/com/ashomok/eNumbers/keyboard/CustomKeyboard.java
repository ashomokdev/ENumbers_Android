package com.ashomok.eNumbers.keyboard;

import android.app.Activity;
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
    private Context context;
    private EditText editText;
    private AudioManager audioManager;
    private CustomKeyboard.OnSubmitListener listener;
    private static String startChar;

    private void createCustomKeyboard(Context context) {

        editText = (EditText) ((Activity) context).findViewById(R.id.inputE);

        keyboardView = (KeyboardView) ((Activity) context).findViewById(R.id.keyboard);
        Keyboard customKeyboard = new Keyboard(context, R.xml.keyboard_keys);
        keyboardView.setKeyboard(customKeyboard);
        CustomKeyboardListener numbersListener = new CustomKeyboardListener(editText);


        startChar = context.getString(R.string.startChar);

        //todo reduntant?
//        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (view != null) {
//            imm.hideSoftInputFromWindow((Activity)context).getWindowToken(), 0);
//        }

        numbersListener.setSubmitListener(new CustomKeyboardListener.SubmitListener() {
            @Override
            public void onSubmit() {
                notifyListener();
                hideCustomKeyboard();
            }
        });

        keyboardView.setOnKeyboardActionListener(numbersListener);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomKeyboard();
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    hideCustomKeyboard();
                    return true;
                }
                return false;
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideDefaultKeyboard(v);
                } else {
                    hideCustomKeyboard();
                }
            }
        });

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        editText.setSelection(editText.getText().length()); //starts type after "E"
        editText.addTextChangedListener(new StartCharKeeper());
        editText.setOnEditorActionListener(new BtnDoneHandler());

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

    public void init(Context context) {
        this.context = context;
        createCustomKeyboard(context);
    }

    public interface OnSubmitListener {
        void onSubmit();
    }

    private void notifyListener() {
        listener.onSubmit();
    }

    public void setOnSubmitListener(CustomKeyboard.OnSubmitListener listener) {
        if (editText == null || context == null) {
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
