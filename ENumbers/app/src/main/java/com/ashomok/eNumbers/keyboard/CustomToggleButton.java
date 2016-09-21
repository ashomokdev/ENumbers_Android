package com.ashomok.eNumbers.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ashomok.eNumbers.R;

/**
 * Created by iuliia on 9/18/16.
 */
public class CustomToggleButton extends ImageView {
    private static final String TAG = CustomToggleButton.class.getSimpleName();
    private OnCheckedChangeListener onCheckedChangeListener;
    private boolean isChecked;

    @SuppressWarnings("deprecation")
    public CustomToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackground(getResources().getDrawable(R.drawable.custom_active));
        isChecked = true;
    }

    @SuppressWarnings("deprecation")
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCheckedChangeListener == null) {
                    Log.w(TAG, "onClick raised but no action will be taken.");
                } else {
                    isChecked = !isChecked;
                    onCheckedChangeListener.onCheckedChanged(isChecked);

                    if (isChecked) {
                        setBackground(getResources().getDrawable(R.drawable.custom_active));
                    } else {
                        setBackground(getResources().getDrawable(R.drawable.default_active));
                    }
                }
            }
        });
    }
}
