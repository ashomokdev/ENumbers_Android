package com.ashomok.eNumbers.keyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.activities.CustomEditText;
import com.ashomok.eNumbers.activities.EditTextImeBackListener;

/**
 * Created by iuliia on 9/4/16.
 */
public class CustomKeyboardFacade {

    private static final String TAG = CustomKeyboardFacade.class.getSimpleName();
    private final Context context;
    private Keyboard customKeyboard;
    private FloatingActionButton fab;

    public CustomKeyboardFacade(Context context) {
        this.context = context;
    }

    /**
     * Initialization only. Visibility not set.
     */
    @SuppressLint("RestrictedApi")
    public void init() {
        customKeyboard = new CustomKeyboard();
        customKeyboard.init(context);

        CustomEditText editText = ((Activity) context).findViewById(R.id.inputE);

        editText.setOnEditTextImeBackListener((ctrl, text) -> hide());

        fab = ((Activity) context).findViewById(R.id.fab);

        OnVisibilityChangedListener onVisibilityChangedListener = isVisible -> {
            if (fab != null) {
                if (isVisible) {
                    fab.setVisibility(View.GONE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        };

        customKeyboard.setOnVisibilityChangedListener(onVisibilityChangedListener);
    }

    public void showCustomKeyboard() {
        setKeyboardVisibility(customKeyboard, true);
    }

    private void setKeyboardVisibility(Keyboard keyboard, boolean isVisible) {
        if (isVisible) {
            keyboard.show();
        } else {
            keyboard.hide();
        }
    }

    public void show() {
        showCustomKeyboard();
    }

    public boolean isShown() {
        return customKeyboard.isVisible();
    }

    private void hide() {
        setKeyboardVisibility(customKeyboard, false);
    }
}
