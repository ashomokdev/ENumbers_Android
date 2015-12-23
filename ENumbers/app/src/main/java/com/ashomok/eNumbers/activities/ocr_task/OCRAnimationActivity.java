package com.ashomok.eNumbers.activities.ocr_task;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.ashomok.eNumbers.R;

/**
 * Created by Iuliia on 13.12.2015.
 */
public class OCRAnimationActivity extends AppCompatActivity {

    private Button cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_animation_layout);

        Bundle extras = getIntent().getExtras();


        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        OCRAnimationFragment dataFragment = (OCRAnimationFragment) fm.findFragmentByTag("data");

        // create the fragment and data the first time
        if (dataFragment == null) {
            // add the fragment
            dataFragment = new OCRAnimationFragment();
            dataFragment.setArguments(extras);
            fm.beginTransaction().add(R.id.content_frame, dataFragment, "data").commit();
        }

        cancel = (Button) findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

}
