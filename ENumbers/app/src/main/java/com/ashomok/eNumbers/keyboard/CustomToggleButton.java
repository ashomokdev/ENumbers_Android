package com.ashomok.eNumbers.keyboard;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ashomok.eNumbers.R;

/**
 * Created by iuliia on 9/18/16.
 */
class CustomToggleButton {
    private static final String TAG = CustomToggleButton.class.getSimpleName();
    private OnCheckedChangeListener onCheckedChangeListener;
    private boolean isChecked;
    private ImageView switchButton;
    private Context context;
    private View switchKeyboardView;

    @SuppressWarnings("deprecation")
    CustomToggleButton(Context context) {
        this.context = context;

        //init switchKeyboard button
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        ViewGroup relLayout = (ViewGroup) ((Activity) context).findViewById(R.id.switch_btn_root);
        switchKeyboardView = inflater.inflate(R.layout.switch_keybord_btn, relLayout, true);
        switchKeyboardView.setVisibility(View.GONE);

        switchButton = (ImageView) ((Activity) context).findViewById(R.id.switch_btn);

        isChecked = true;
        setBackground(isChecked);
    }

    @SuppressWarnings("deprecation")
    void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCheckedChangeListener == null) {
                    Log.w(TAG, "onClick raised but no action will be taken.");
                } else {
                    isChecked = !isChecked;
                    setBackground(isChecked);
                    onCheckedChangeListener.onCheckedChanged(isChecked);
                }
            }
        });
    }

    void show(boolean isCustom) {
        switchKeyboardView.setVisibility(View.VISIBLE);
        setBackground(isCustom);
    }

    void hide() {
        switchKeyboardView.setVisibility(View.GONE);
    }

    void setBackground(boolean isChecked) {

        if (isChecked) {
            switchButton.setBackground(context.getResources().getDrawable(R.drawable.custom_active));
        } else {
            switchButton.setBackground(context.getResources().getDrawable(R.drawable.default_active));
        }
    }
}
