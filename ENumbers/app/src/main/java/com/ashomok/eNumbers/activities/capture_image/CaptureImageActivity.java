package com.ashomok.eNumbers.activities.capture_image;

import android.app.Activity;

import android.os.Bundle;

import com.ashomok.eNumbers.R;

/**
 * Created by Iuliia on 19.02.2016.
 */
public class CaptureImageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_image_layout);

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
    }
}
