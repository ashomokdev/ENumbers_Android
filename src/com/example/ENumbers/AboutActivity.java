package com.example.eNumbers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.*;
import android.widget.TextView;

/**
 * Created by Iuliia on 30.08.2015.
 */
public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView mTextView_appName;
    private TextView mTextView_developer;
    private TextView mTextView_email;
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

        mTextView_email = (TextView) findViewById(R.id.email);
        mTextView_email.setText(Html.fromHtml("<u>"+ getString(R.string.email) + "</u>"));
        mTextView_email.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                callSendMeMail();
            }
        });
       // mTextView_email.setMovementMethod(LinkMovementMethod.getInstance()); TODO use for sourses

        mTextView_version = (TextView) findViewById(R.id.version);
        mTextView_version.setText(R.string.version);
    }

    private void callSendMeMail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mTextView_email.getText().toString(), null));

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { mTextView_email.getText().toString() });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        startActivity(Intent.createChooser(emailIntent, "Send mail to Developer:"));
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
