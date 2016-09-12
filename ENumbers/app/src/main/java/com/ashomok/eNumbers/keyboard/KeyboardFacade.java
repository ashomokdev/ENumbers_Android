package com.ashomok.eNumbers.keyboard;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.ashomok.eNumbers.R;

/**
 * Created by iuliia on 9/4/16.
 */
public class KeyboardFacade {

    private static final String TAG = KeyboardFacade.class.getSimpleName();
    private Context context;
    private Keyboard customKeyboard;
    private Keyboard defaultKeyboard;
    private boolean isDefaultKeyboardShown;

    public KeyboardFacade(Context context) {
        this.context = context;
    }

    public void init() {
        customKeyboard = new CustomKeyboard();
        customKeyboard.init(context);
        customKeyboard.setOnKeyboardSwitchListener(new OnKeyboardSwitchListener() {
            @Override
            public void onKeyboardSwitch() {
                showDefaultKeyboard();
                Log.d(TAG, "switch to default keyboard");
            }
        });


        defaultKeyboard = new DefaultKeyboard();
        defaultKeyboard.init(context);
        defaultKeyboard.setOnKeyboardSwitchListener(new OnKeyboardSwitchListener() {
            @Override
            public void onKeyboardSwitch() {
                showCustomKeyboard();
                Log.d(TAG, "switch to custom keyboard");
            }
        });

        EditText editText = (EditText) ((Activity) context).findViewById(R.id.inputE);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    hide();
                    return true;
                }
                return false;
            }
        });

    }

    public void showDefaultKeyboard() {
        setKeyboardVisibility(customKeyboard, false);
        setKeyboardVisibility(defaultKeyboard, true);
        isDefaultKeyboardShown = true;
    }


    public void showCustomKeyboard() {
        setKeyboardVisibility(customKeyboard, true);
        setKeyboardVisibility(defaultKeyboard, false);
        isDefaultKeyboardShown = false;
    }

    private void setKeyboardVisibility(Keyboard keyboard, boolean isVisible) {
        if (isVisible) {
            keyboard.show();
        } else {
            keyboard.hide();
        }
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        if (context == null) {
            Log.e(TAG, "Keyboard was not initialized. Call init() before");
        } else {
            customKeyboard.setOnSubmitListener(onSubmitListener);
            defaultKeyboard.setOnSubmitListener(onSubmitListener);
        }
    }

    public void show() {
        showCustomKeyboard();
    }

    public boolean isDefaultKeyboardShown() {
        return isDefaultKeyboardShown;
    }

    public void hide() {
        if (isDefaultKeyboardShown) {
            setKeyboardVisibility(defaultKeyboard, false);
        } else {
            setKeyboardVisibility(customKeyboard, false);
        }
    }
}
