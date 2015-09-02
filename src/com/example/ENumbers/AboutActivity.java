package com.example.eNumbers;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.TextView;

/**
 * Created by Iuliia on 30.08.2015.
 */
public class AboutActivity extends AppCompatActivity {
    private GestureDetectorCompat mDetector;

    private TextView mTextView_appName;
    private TextView mTextView_developer;
    private TextView mTextView_version;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_layout);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about_layout);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mTextView_appName = (TextView) findViewById(R.id.appName);
            mTextView_appName.setText(R.string.appName);

            mTextView_developer = (TextView) findViewById(R.id.developer);
            mTextView_developer.setText(R.string.developer);

            mTextView_version = (TextView) findViewById(R.id.version);
            mTextView_version.setText(R.string.version);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            final int SWIPE_MIN_DISTANCE = 120;
            final int SWIPE_MAX_OFF_PATH = 250;
            final int SWIPE_THRESHOLD_VELOCITY = 200;
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //"Right to Left"
                    finish();
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //"Left to Right"
                    finish();

                }
            } catch (Exception e) {
                // nothing
            }
            return true;
        }
    }
}
