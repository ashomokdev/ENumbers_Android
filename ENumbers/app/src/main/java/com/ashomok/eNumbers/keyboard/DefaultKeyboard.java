package com.ashomok.eNumbers.keyboard;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.ashomok.eNumbers.R;

import java.lang.reflect.Method;

/**
 * Created by iuliia on 9/4/16.
 */
public class DefaultKeyboard implements Keyboard {
    private static final String TAG = DefaultKeyboard.class.getSimpleName();

    private boolean isVisible;
    private EditText editText;
    private Context context;
    private OnKeyboardSwitchListener onKeyboardSwitchListener;
    private View view;

    public void setVisibility (boolean visible) {
        isVisible = visible;
    }

    @Override
    public void init(Context context) {

        this.context = context;
        editText = (EditText) ((Activity) context).findViewById(R.id.inputE);

        //init custom button
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        ViewGroup relLayout = (ViewGroup) ((Activity) context).findViewById(R.id.keyboard_root);
        view = inflater.inflate(R.layout.default_keyboard, relLayout, true);

        ImageView switchButton = (ImageView) view.findViewById(R.id.switch_btn);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //switch keyboard
                OnKeyboardSwitchListener keyboardSwitchListener = getKeyboardSwitchListener();
                if (keyboardSwitchListener != null) {
                    keyboardSwitchListener.onKeyboardSwitch();
                }
            }
        });

        view.setVisibility(View.GONE);
    }

    public OnKeyboardSwitchListener getKeyboardSwitchListener() {
        if (onKeyboardSwitchListener == null) {
            Log.e(TAG, "keyboardSwitchListener was not setted. Call setKeyboardSwitchListener(OnKeyboardSwitchListener keyboardSwitchListener) before.");
        }
        return onKeyboardSwitchListener;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
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
    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        //// TODO: 9/6/16
    }

    @Override
    public void hide() {
        Log.d(TAG, "hide");

        view.setVisibility(View.GONE);

        View view =((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        isVisible = false;
    }

    @Override
    public void show() {
        Log.d(TAG, "show");

        view.setVisibility(View.VISIBLE);

        View view =((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
       isVisible = true;
    }
}
