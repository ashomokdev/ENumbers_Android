package com.ashomok.eNumbers.keyboard;

import android.content.Context;
import android.util.Log;

/**
 * Created by iuliia on 9/4/16.
 */
public class KeyboardFacade {

    private static final String TAG = KeyboardFacade.class.getSimpleName();
    private Context context;
    private Keyboard customKeyboard;
    private Keyboard defaultKeyboard;

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


    }

    private void showDefaultKeyboard() {
        setKeyboardVisibility(customKeyboard, false);
        setKeyboardVisibility(defaultKeyboard, true);
    }


    private void showCustomKeyboard() {
        setKeyboardVisibility(customKeyboard, true);
        setKeyboardVisibility(defaultKeyboard, false);
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
}
