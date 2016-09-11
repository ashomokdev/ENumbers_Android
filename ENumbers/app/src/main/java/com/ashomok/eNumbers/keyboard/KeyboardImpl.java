package com.ashomok.eNumbers.keyboard;

import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.view.View;

/**
 * Created by iuliia on 9/5/16.
 */
public abstract class KeyboardImpl implements Keyboard{

    private static final String TAG = KeyboardImpl.class.getSimpleName();
    KeyboardView keyboardView;
    boolean isVisible;

    @Override
    public void hide() {
        Log.d(TAG, "hide");

        keyboardView.setVisibility(View.GONE);
        isVisible = false;
    }

}
