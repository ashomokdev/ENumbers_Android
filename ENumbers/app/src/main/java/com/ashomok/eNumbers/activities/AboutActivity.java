package com.ashomok.eNumbers.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ashomok.eNumbers.BuildConfig;
import com.ashomok.eNumbers.R;

/**
 * Created by Iuliia on 30.08.2015.
 */
public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView mTextView_appName;
    private TextView mTextView_developer;
    private TextView mTextView_email;
    private TextView mTextView_version;
    private TextView mTextView_sourcesHeader;
    private TextView mTextview_sources;
    private TextView mTextView_ocr_explanationHeader;
    private TextView mTextView_ocr_explanation;
    private static final String TAG = "AboutActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.about_layout);

            toolbar = (Toolbar) findViewById(R.id.toolbar_about_layout);
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            //base app data
            mTextView_appName = (TextView) findViewById(R.id.appName);
            mTextView_appName.setText(R.string.appName);

            mTextView_developer = (TextView) findViewById(R.id.developer);
            mTextView_developer.setText(R.string.developer);

            mTextView_email = (TextView) findViewById(R.id.email);
            mTextView_email.setText(Html.fromHtml("<u>" + getString(R.string.my_email) + "</u>"));
            mTextView_email.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    copyTextToClipboard(mTextView_email.getText());
                }
            });

            mTextView_version = (TextView) findViewById(R.id.version);
            String version = getString(R.string.version) + " " + BuildConfig.VERSION_NAME;
            mTextView_version.setText(version);


            //ocr explanation
            mTextView_ocr_explanationHeader = (TextView) findViewById(R.id.ocr_header);
            mTextView_ocr_explanationHeader.setText(R.string.ocr_header);
            mTextView_ocr_explanation = (TextView) findViewById(R.id.ocr_explanation);
            mTextView_ocr_explanation.setText(R.string.ocr_explanation);


            //sources of data
            mTextView_sourcesHeader = (TextView) findViewById(R.id.sources_header);
            mTextView_sourcesHeader.setText(getString(R.string.data_sources));

            mTextview_sources = (TextView) findViewById(R.id.sources);
            mTextview_sources.setText(Html.fromHtml(
                    getString(R.string.data_source_1) + "<br/>" +
                    getString(R.string.data_source_2) + "<br/>" +
                    getString(R.string.data_source_3) + "<br/>"
            ));


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private void copyTextToClipboard(CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.email), text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, getString(R.string.copied), Toast.LENGTH_SHORT).show();
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 300 milliseconds
        v.vibrate(300);
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
