package com.ashomok.eNumbers.activities.ocr_task;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ashomok.eNumbers.R;

/**
 * Created by Iuliia on 13.12.2015.
 */
public class OCRAnimationActivity extends AppCompatActivity {
    private String imageUri;
    private ObjectAnimator animation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_animation_layout);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imageUri = bundle.getString("image");
        }
        Button cancel = findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        ImageView ocrAnimationView = findViewById(R.id.ocr_image);
        ocrAnimationView.setImageURI(Uri.parse(imageUri));
        initAnimatedBand(getScreenLength());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //reinit
        findViewById(R.id.scan_band).setX(0);
        findViewById(R.id.scan_band).setY(0);
        if (animation!=null) {
            animation.cancel();
        }
        initAnimatedBand(getScreenLength());
    }

    private int getScreenLength() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private void initAnimatedBand(int screenLength) {
        animation = ObjectAnimator.ofFloat(findViewById(R.id.scan_band), View.TRANSLATION_X, screenLength);
        animation.setDuration(2000);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.start();
    }
}
