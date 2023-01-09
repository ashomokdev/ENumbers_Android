package com.ashomok.eNumbers.keyboard;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ashomok.eNumbers.R;

import java.lang.reflect.Method;

/**
 * Created by iuliia on 9/5/16.
 */
abstract class KeyboardImpl implements Keyboard {

    private static final String TAG = KeyboardImpl.class.getSimpleName();
    Context context;
    OnVisibilityChangedListener onVisibilityChangedListener;
    EditText editText;
    private boolean isVisible;

    public void init(Context c) {
        this.context = c;
        editText = ((Activity) context).findViewById(R.id.inputE);
    }

    @Override
    public void hide() {
        isVisible = false;

    }

    @Override
    public void show() {

        isVisible = true;
        hideDefaultKeyboard();

    }

    private void hideDefaultKeyboard() {
        Log.d(TAG, "hide default keyboard");

        View v = ((Activity) context).getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        //  block default keyboard
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((EditText) v).setShowSoftInputOnFocus(false);
        } else {
            try {
                final Method method = EditText.class.getMethod(
                        "setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(v, false);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());

            }
        }

        editText.setOnEditorActionListener(null);
    }

    @Override
    public void setOnVisibilityChangedListener(OnVisibilityChangedListener listener) {
        onVisibilityChangedListener = listener;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

}
