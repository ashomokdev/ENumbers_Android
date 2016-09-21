package com.ashomok.eNumbers.keyboard;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashomok.eNumbers.R;

import java.lang.reflect.Method;

/**
 * Created by iuliia on 9/4/16.
 */
class DefaultKeyboard extends KeyboardImpl {
    private static final String TAG = DefaultKeyboard.class.getSimpleName();

    private OnSubmitListener onSubmitListener;
    private  EditText editText;



    @Override
    public void init(Context context) {
        super.init(context);

        editText = (EditText) ((Activity) context).findViewById(R.id.inputE);
    }


    @Override
    public void setOnSubmitListener(OnSubmitListener listener) {
        Log.d(TAG, "setOnSubmitListener");
        if ( context == null) {
            Log.e(TAG, "Keyboard was not initialized. Call init() before");
        } else {
            onSubmitListener = listener;
        }
    }

    @Override
    public void hide() {
        super.hide();
        Log.d(TAG, "hide");

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
        editText.setText(startChar);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) switchButton.getLayoutParams();
        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    }

    @Override
    public void show() {
        Log.d(TAG, "show");
        super.show();

        View v = ((Activity) context).getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, 0);
        }

        //  unblock default keyboard
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((EditText) v).setShowSoftInputOnFocus(true);
        } else {
            try {
                final Method method = EditText.class.getMethod(
                        "setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(v, true);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());

            }
        }

        editText.setOnEditorActionListener(new BtnDoneHandler());
        editText.setText("");

            //make switchButton on the top of  default Keyboard
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) switchButton.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            switchButton.setLayoutParams(params);
    }

    private class BtnDoneHandler implements EditText.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                onSubmitListener.onSubmit();
                return true;
            }

            return false;
        }
    }
}
