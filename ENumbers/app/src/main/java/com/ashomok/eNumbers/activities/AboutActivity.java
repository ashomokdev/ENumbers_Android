package com.ashomok.eNumbers.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.widget.TextView;

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

            mTextView_appName = (TextView) findViewById(R.id.appName);
            mTextView_appName.setText(R.string.appName);

            mTextView_developer = (TextView) findViewById(R.id.developer);
            mTextView_developer.setText(R.string.developer);

            mTextView_email = (TextView) findViewById(R.id.email);
            mTextView_email.setText(Html.fromHtml("<u>" + getString(R.string.email) + "</u>"));
            mTextView_email.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    callSendMeMail();
                }
            });
            // mTextView_email.setMovementMethod(LinkMovementMethod.getInstance()); TODO use for sources

            mTextView_version = (TextView) findViewById(R.id.version);

            PackageInfo pInfo  = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            mTextView_version.setText(getString(R.string.version, version));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private void callSendMeMail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                getString(R.string.mailto), mTextView_email.getText().toString(), null));

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { mTextView_email.getText().toString() });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback));
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_to_dev)));
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
