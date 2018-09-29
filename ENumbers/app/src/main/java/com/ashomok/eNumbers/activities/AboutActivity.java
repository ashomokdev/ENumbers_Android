package com.ashomok.eNumbers.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ashomok.eNumbers.BuildConfig;
import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.Settings;
import com.ashomok.eNumbers.menu.ItemClickListener;

/**
 * Created by Iuliia on 30.08.2015.
 */
public class AboutActivity extends AppCompatActivity {

    private TextView mTextView_email;
    private static final String TAG = "AboutActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.about_layout);

            initToolbar();

            //base app data
            TextView mTextView_appName = findViewById(R.id.appName);
            mTextView_appName.setText(R.string.appName);

            TextView mTextView_developer = findViewById(R.id.developer);
            mTextView_developer.setText(R.string.developer);

            mTextView_email = findViewById(R.id.email);
            mTextView_email.setText(Html.fromHtml("<u>" + getString(R.string.my_email) + "</u>"));
            mTextView_email.setOnClickListener(view -> copyTextToClipboard(mTextView_email.getText()));

            TextView mTextView_version = findViewById(R.id.version);
            String version = getString(R.string.version) + " " + BuildConfig.VERSION_NAME;
            mTextView_version.setText(version);


            //ocr explanation
            TextView mTextView_ocr_explanationHeader = findViewById(R.id.ocr_header);
            mTextView_ocr_explanationHeader.setText(R.string.ocr_header);
            TextView mTextView_ocr_explanation = findViewById(R.id.ocr_explanation);
            mTextView_ocr_explanation.setText(R.string.ocr_explanation);


            //sources of data
            TextView mTextView_sourcesHeader = findViewById(R.id.sources_header);
            mTextView_sourcesHeader.setText(getString(R.string.data_sources));

            TextView mTextview_sources = findViewById(R.id.sources);
            mTextview_sources.setText(Html.fromHtml(
                    getString(R.string.data_source_1) + "<br/>" +
                            getString(R.string.data_source_3)
            ));

            if (Settings.isFree) {
                //please buy ads-free version
                TextView mTextView_ads_freeHeader = findViewById(R.id.ads_free_version_header);
                mTextView_ads_freeHeader.setText(getString(R.string.ads_free_version_header));

                TextView mTextView_ads_free = findViewById(R.id.ads_free_version);
                mTextView_ads_free.setText(R.string.ads_free_version_explanation);

                TextView mTextView_buy_paid = findViewById(R.id.buy_paid);
                mTextView_buy_paid.setText(Html.fromHtml("<u>" + getString(R.string.buy_paid) + "</u>"));

                mTextView_buy_paid.setOnClickListener(view -> {
                    ItemClickListener itemClickListener = new ItemClickListener(view.getContext());
                    itemClickListener.buyPaidVersion();
                });
            }

            TextView privacy_policy_link = findViewById(R.id.privacy_policy_link);
            privacy_policy_link.setText(
                    Html.fromHtml("<u>" + getString(R.string.privacy_policy_agreement) + "</u>"));

            privacy_policy_link.setOnClickListener(view -> openPrivacyPolicy());

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private void openPrivacyPolicy() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Settings.PRIVACY_POLICY_LINK)));
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_about_layout);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void copyTextToClipboard(CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.email), text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(this, getString(R.string.copied), Toast.LENGTH_SHORT).show();
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 300 milliseconds
        if (v != null) {
            v.vibrate(300);
        }
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
