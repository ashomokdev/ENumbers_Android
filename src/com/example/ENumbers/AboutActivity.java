package com.example.eNumbers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.TextView;

/**
 * Created by Iuliia on 30.08.2015.
 */
public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView mTextView_appName;
    private TextView mTextView_developer;
    private TextView mTextView_version;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar_about_layout);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
