package com.ashomok.eNumbers.keyboard;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

class CustomKeyboardListener implements KeyboardView.OnKeyboardActionListener {


    private static final String TAG = CustomKeyboardListener.class.getSimpleName();
    private final EditText editText;
    private OnSubmitListener submitListener;


    CustomKeyboardListener(EditText text) {
        this.editText = text;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = editText.getEditableText();

        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                editable.delete(editable.length() - 1, editable.length());
                break;
            case 44://comma
                editable.append(", E");
                break;
            default:
                char code = (char) primaryCode;
                editable.append(code);
                break;
        }
    }


    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

}
