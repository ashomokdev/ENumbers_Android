package com.ashomok.eNumbers.keyboard;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.activities.CustomEditText;
import com.ashomok.eNumbers.activities.EditTextImeBackListener;

/**
 * Created by iuliia on 9/4/16.
 */
public class KeyboardFacade {

    private static final String TAG = KeyboardFacade.class.getSimpleName();
    private Context context;
    private Keyboard customKeyboard;
    private Keyboard defaultKeyboard;
    private boolean isDefaultKeyboardShown;
    private FloatingActionButton fab;
    private CustomToggleButton switchButton;

    public KeyboardFacade(Context context) {
        this.context = context;
    }

    /**
     * Initialization only. Visibility not set.
     */
    public void init() {
        customKeyboard = new CustomKeyboard();
        customKeyboard.init(context);

        defaultKeyboard = new DefaultKeyboard();
        defaultKeyboard.init(context);

        CustomEditText editText = (CustomEditText) ((Activity) context).findViewById(R.id.inputE);

        editText.setOnEditTextImeBackListener(new EditTextImeBackListener() {
            @Override
            public void onImeBack(CustomEditText ctrl, String text) {
                hide();
            }
        });

        fab = (FloatingActionButton) ((Activity) context).findViewById(R.id.fab);

        initSwitchButton();

        OnVisibilityChangedListener onVisibilityChangedListener = new OnVisibilityChangedListener() {
            @Override
            public void onVisibilityChanged(boolean isVisible) {
                if (fab != null) {
                    if (isVisible) {
                        fab.setVisibility(View.GONE);
                    } else {
                        fab.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        defaultKeyboard.setOnVisibilityChangedListener(onVisibilityChangedListener);
        customKeyboard.setOnVisibilityChangedListener(onVisibilityChangedListener);
    }

    private void initSwitchButton() {
        switchButton = new CustomToggleButton(context);

        switchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked) {
                    //custom active
                    showCustomKeyboard();
                } else {
                    //default active
                    showDefaultKeyboard();
                }
            }
        });
    }

    public void showDefaultKeyboard() {
        setKeyboardVisibility(customKeyboard, false);
        setKeyboardVisibility(defaultKeyboard, true);
        isDefaultKeyboardShown = true;
        switchButton.show(false);
    }


    public void showCustomKeyboard() {
        setKeyboardVisibility(customKeyboard, true);
        setKeyboardVisibility(defaultKeyboard, false);
        isDefaultKeyboardShown = false;
        switchButton.show(true);
    }

    private void setKeyboardVisibility(Keyboard keyboard, boolean isVisible) {
        if (isVisible) {
            keyboard.show(); //error on landscape mode here
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

    public boolean isShown() {
        return customKeyboard.isVisible() || defaultKeyboard.isVisible();
    }

    private void hide() {

        switchButton.hide();

        if (isDefaultKeyboardShown) {
            setKeyboardVisibility(defaultKeyboard, false);
        } else {
            setKeyboardVisibility(customKeyboard, false);
        }
    }
}
