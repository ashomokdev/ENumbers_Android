package com.ashomok.eNumbers.keyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ashomok.eNumbers.R;

/**
 * Created by iuliia on 9/5/16.
 */
abstract class KeyboardImpl implements Keyboard {

    private static final String TAG = KeyboardImpl.class.getSimpleName();

    private boolean isVisible;
    Context context;
    static String startChar;
    FrameLayout switchButton;

    public void init(Context c) {
        this.context = c;
        startChar = context.getString(R.string.startChar);
        switchButton = (FrameLayout) ((Activity) context).findViewById(R.id.switch_btn_root);
    }

    @Override
    public void hide() {
        isVisible = false;
        switchButton.setVisibility(View.GONE);
    }

    @Override
    public void show() {

        isVisible = true;
        switchButton.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

}
