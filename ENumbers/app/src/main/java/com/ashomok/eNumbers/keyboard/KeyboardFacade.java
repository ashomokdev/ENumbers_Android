package com.ashomok.eNumbers.keyboard;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.ashomok.eNumbers.R;

/**
 * Created by iuliia on 9/4/16.
 */
//todo modify also en_list_fragment.xml
public class KeyboardFacade {

    private static final String TAG = KeyboardFacade.class.getSimpleName();
    private Context context;
    private Keyboard customKeyboard;
    private Keyboard defaultKeyboard;
    private boolean isDefaultKeyboardShown;
    private View switchKeyboardView;
    private FloatingActionButton fab;

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

        EditText editText = (EditText) ((Activity) context).findViewById(R.id.inputE);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_UP)) {
                    hide();
                    return true;
                }
                return false;
            }
        });

        fab = (FloatingActionButton) ((Activity) context).findViewById(R.id.fab);

        initSwitchButton();
    }

    private void initSwitchButton() {
        //init switchKeyboard button
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        ViewGroup relLayout = (ViewGroup) ((Activity) context).findViewById(R.id.switch_btn_root);
        switchKeyboardView = inflater.inflate(R.layout.switch_keybord_btn, relLayout, true);

        CustomToggleButton switchButton = (CustomToggleButton) ((Activity) context).findViewById(R.id.switch_btn);

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

        switchKeyboardView.setVisibility(View.GONE);
    }

    public void showDefaultKeyboard() {
        setKeyboardVisibility(customKeyboard, false);
        setKeyboardVisibility(defaultKeyboard, true);
        isDefaultKeyboardShown = true;
        switchKeyboardView.setVisibility(View.VISIBLE);
        fab.hide();
    }


    public void showCustomKeyboard() {
        setKeyboardVisibility(customKeyboard, true);
        setKeyboardVisibility(defaultKeyboard, false);
        isDefaultKeyboardShown = false;
        switchKeyboardView.setVisibility(View.VISIBLE);
        fab.hide();
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

    public void hide() {

        switchKeyboardView.setVisibility(View.GONE);

        if (isDefaultKeyboardShown) {
            setKeyboardVisibility(defaultKeyboard, false);
        } else {
            setKeyboardVisibility(customKeyboard, false);
        }

        fab.show();
    }
}
