package com.ashomok.eNumbers.activities;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by iuliia on 9/24/16.
 * Detecting when user has dismissed the soft keyboard with BACK btn
 * uses for hiding switch keyboard btn
 */

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    private EditTextImeBackListener mOnImeBack;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            if (mOnImeBack != null)
                mOnImeBack.onImeBack(this, this.getText().toString());
        }
        return super.dispatchKeyEvent(event);
    }

    public void setOnEditTextImeBackListener(EditTextImeBackListener listener) {
        mOnImeBack = listener;
    }

}

