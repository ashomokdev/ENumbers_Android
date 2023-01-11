package com.ashomok.eNumbers.keyboard;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.TextView;

class CustomKeyboardListener implements KeyboardView.OnKeyboardActionListener {

    private static final String TAG = CustomKeyboardListener.class.getSimpleName();
    private final EditText editText;

    CustomKeyboardListener(EditText text) {
        this.editText = text;
    }

    @Override
    public void onPress(int primaryCode) {}

    @Override
    public void onRelease(int primaryCode) {}

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = editText.getEditableText();
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                if (editable.length() > 0) {
                    editable.delete(editable.length() - 1, editable.length());
                }
                break;
            case 44://comma
                editable.append(", E");
                break;
            case EditorInfo.IME_ACTION_DONE:
                editText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                break;
            default:
                char code = (char) primaryCode;
                editable.append(code);
                break;
        }
    }

    @Override
    public void onText(CharSequence text) {}

    @Override
    public void swipeLeft() {}

    @Override
    public void swipeRight() {}

    @Override
    public void swipeDown() {}

    @Override
    public void swipeUp() {}
}
