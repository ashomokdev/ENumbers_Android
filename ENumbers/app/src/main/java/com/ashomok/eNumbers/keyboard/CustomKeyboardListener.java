package com.ashomok.eNumbers.keyboard;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.widget.EditText;

class CustomKeyboardListener implements KeyboardView.OnKeyboardActionListener {


    private final EditText text;
    private SubmitListener submitListener;

    public CustomKeyboardListener(EditText text) {
        this.text = text;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = text.getEditableText();

        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                editable.delete(editable.length() - 1, editable.length());
                break;
            case 44://comma
                editable.append(", E");
                break;
            case Keyboard.KEYCODE_DONE:
                SubmitListener submitListener = getSubmitListener();
                if (submitListener != null) {
                    submitListener.onSubmit();
                }
                break;
            case 1://switch keyboard
                //// TODO: 9/2/16
                break;
            default:
                char code = (char) primaryCode;
                editable.append(code);
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

    public SubmitListener getSubmitListener() {
        return submitListener;
    }

    public void setSubmitListener(SubmitListener submitListener) {
        this.submitListener = submitListener;
    }

    public interface SubmitListener {
        void onSubmit();
    }

}
