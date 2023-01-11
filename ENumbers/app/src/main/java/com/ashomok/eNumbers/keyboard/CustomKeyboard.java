package com.ashomok.eNumbers.keyboard;

import static android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.activities.CustomEditText;
import com.ashomok.eNumbers.tools.LogHelper;

import java.lang.reflect.Method;

/**
 * Created by iuliia on 9/4/16.
 */
public class CustomKeyboard {

    private static final String TAG = CustomKeyboard.class.getSimpleName();
    private final Context context;
    private KeyboardView keyboardView;
    private CustomEditText editText;

    public CustomKeyboard(Context context) {
        this.context = context;
    }

    public void init() {
        editText = ((Activity) context).findViewById(R.id.inputE);
        keyboardView = ((Activity) context).findViewById(R.id.keyboard);
        keyboardView.setKeyboard(new Keyboard(context, R.xml.keyboard_keys));
        keyboardView.setOnKeyboardActionListener( new CustomKeyboardListener(editText));
        editText.setSelection(editText.getText().length()); //starts type after "E"
        editText.setOnEditTextImeBackListener((ctrl, text) -> hide());
    }

    public void hide() {
        LogHelper.d(TAG, "hide");
        keyboardView.setVisibility(View.GONE);
    }

    public void show() {
        LogHelper.d(TAG, "show");
        hideDefaultKeyboard();
        keyboardView.setVisibility(View.VISIBLE);

        //for keyboard close when on back pressed
        FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(TAG, POP_BACK_STACK_INCLUSIVE);
        if (!fragmentPopped) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(TAG);
            fragmentTransaction.commit();
        }
    }

    public void hideDefaultKeyboard() {
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            //  block default keyboard
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                editText.setShowSoftInputOnFocus(false);
            } else {
                try {
                    final Method method = EditText.class.getMethod(
                            "setShowSoftInputOnFocus", boolean.class);
                    method.setAccessible(true);
                    method.invoke(view, false);
                } catch (Exception e) {
                    LogHelper.e(TAG, e.getMessage());
                }
            }
        }
    }
}
