package com.ashomok.eNumbers.keyboard;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.ashomok.eNumbers.R;

import static android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

/**
 * Created by iuliia on 9/2/16.
 */
class CustomKeyboard extends KeyboardImpl {
    private static final String TAG = CustomKeyboard.class.getSimpleName();
    private KeyboardView keyboardView;


    @Override
    public void init(Context context) {

        super.init(context);

        keyboardView = (KeyboardView) ((Activity) context).findViewById(R.id.keyboard);
        Keyboard customKeyboard = new Keyboard(context, R.xml.keyboard_keys);
        keyboardView.setKeyboard(customKeyboard);

        CustomKeyboardListener numbersListener = new CustomKeyboardListener(editText);

        keyboardView.setOnKeyboardActionListener(numbersListener);

        editText.setSelection(editText.getText().length()); //starts type after "E"

        keyboardView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {

            }
        });
    }

    @Override
    public void hide() {
        Log.d(TAG, "hide");
        super.hide();

        keyboardView.setVisibility(View.GONE);

        editText.setSelection(editText.getText().length());

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) switchButton.getLayoutParams();
        params.removeRule(RelativeLayout.ABOVE);

        onVisibilityChangedListener.onVisibilityChanged(false);
    }

    @Override
    public void show() {
        Log.d(TAG, "show");
        super.show();

        //for keyboard close when on back pressed
        FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(TAG, POP_BACK_STACK_INCLUSIVE);
        if (!fragmentPopped) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(TAG);
            fragmentTransaction.commit();
        }

        keyboardView.setVisibility(View.VISIBLE);

        //make switchButton on the top of customKeyboard
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) switchButton.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.keyboard);
        switchButton.setLayoutParams(params);

        onVisibilityChangedListener.onVisibilityChanged(true);

    }
}


