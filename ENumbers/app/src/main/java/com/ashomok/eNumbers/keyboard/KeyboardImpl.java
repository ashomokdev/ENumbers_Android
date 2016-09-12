package com.ashomok.eNumbers.keyboard;

import android.content.Context;
import android.util.Log;

/**
 * Created by iuliia on 9/5/16.
 */
public abstract class KeyboardImpl implements Keyboard{

    private static final String TAG = KeyboardImpl.class.getSimpleName();

    boolean isVisible;
    Context context;
    OnKeyboardSwitchListener onKeyboardSwitchListener;

    @Override
    public void hide() {

        isVisible = false;
    }

    @Override
    public void show() {

        isVisible = true;
    }

    @Override
    public void setOnKeyboardSwitchListener(OnKeyboardSwitchListener listener) {
        if (context == null) {
            Log.e(TAG, "Keyboard was not initialized. Call init() before");
        } else {
            onKeyboardSwitchListener = listener;
        }
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

}
