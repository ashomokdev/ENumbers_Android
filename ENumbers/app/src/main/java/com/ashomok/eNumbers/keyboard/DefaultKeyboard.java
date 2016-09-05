package com.ashomok.eNumbers.keyboard;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ashomok.eNumbers.R;

/**
 * Created by iuliia on 9/4/16.
 */
public class DefaultKeyboard
{
    private static final String TAG = DefaultKeyboard.class.getSimpleName();
    private Context context;

    public DefaultKeyboard(Context context) {
       this.context = context;
    }

    public void init(){
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        ViewGroup relLayout = (ViewGroup)((Activity)context).findViewById(R.id.keyboard_root);
        View view = inflater.inflate(R.layout.default_keyboard, relLayout, true);
    }
}
