package com.ashomok.eNumbers.activities.capture_image;

import android.app.Activity;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.activities.ocr_task.OCRFirstRunDialogFragment;

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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstOpened = preferences.getBoolean("first_opened", true);
        if (firstOpened) {

            showWelcomeScreen();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("first_opened", false);
            editor.apply();
        }
    }

    private void showWelcomeScreen() {
        DialogFragment dialog = new OCRFirstRunDialogFragment();
        dialog.show(getFragmentManager(), "dialog");
    }
}
