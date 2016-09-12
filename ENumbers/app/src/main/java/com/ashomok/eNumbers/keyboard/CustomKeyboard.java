package com.ashomok.eNumbers.keyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ashomok.eNumbers.R;

import java.lang.reflect.Method;

/**
 * Created by iuliia on 9/2/16.
 */
public class CustomKeyboard extends KeyboardImpl {
    private static final String TAG = CustomKeyboard.class.getSimpleName();
    private EditText editText;
    private AudioManager audioManager;
    private OnSubmitListener onSubmitListener;
    private static String startChar;
    private KeyboardView keyboardView;


    @Override
    public void init(Context context) {
        this.context = context;

        startChar = context.getString(R.string.startChar);

        editText = (EditText) ((Activity) context).findViewById(R.id.inputE);

        keyboardView = (KeyboardView) ((Activity) context).findViewById(R.id.keyboard);
        Keyboard customKeyboard = new Keyboard(context, R.xml.keyboard_keys);
        keyboardView.setKeyboard(customKeyboard);

        CustomKeyboardListener numbersListener = new CustomKeyboardListener(editText);

        numbersListener.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmit() {
                onSubmitListener.onSubmit();
                hide();
            }
        });

        numbersListener.setKeyboardSwitchListener(new OnKeyboardSwitchListener() {
            @Override
            public void onKeyboardSwitch() {
                onKeyboardSwitchListener.onKeyboardSwitch();
            }
        });

        keyboardView.setOnKeyboardActionListener(numbersListener);

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        editText.setSelection(editText.getText().length()); //starts type after "E"
        editText.addTextChangedListener(new StartCharKeeper());
        editText.setOnEditorActionListener(new BtnDoneHandler());
    }

    @Override
    public void hide() {
        Log.d(TAG, "hide");
        super.hide();
        keyboardView.setVisibility(View.GONE);
    }

    @Override
    public void show() {
        Log.d(TAG, "show");
        super.show();
        keyboardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setOnSubmitListener(OnSubmitListener listener) {
        if (editText == null || context == null) {
            Log.e(TAG, "Keyboard was not initialized. Call init() before");
        } else {
            onSubmitListener = listener;
        }
    }

    private class BtnDoneHandler implements EditText.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                onSubmitListener.onSubmit();
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
