package com.example.eNumbers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;

/**
 * Created by Iuliia on 14.09.2015.
 */
public class ENDetailsActivity  extends AppCompatActivity {

    private EN complexJavaObjEN;

    private Toolbar toolbar;
    private TextView mTextView_ecode;
    private TextView mTextView_ename;
    private TextView mTextView_epurpose;
    private TextView mTextView_status;
    private TextView mTextView_additional_info;
    private TextView mTextView_approved_in;
    private TextView mTextView_banned_in;
    private TextView mTextView_typical_products;
    private TextView mTextView_danger_level;
    private TextView mTextView_bad_for_children;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String jsonString = extras.getString("objectKey");

                if(jsonString!=null){
                    Gson gson = new Gson();
                    complexJavaObjEN = gson.fromJson(jsonString, EN.class);
                }
            }

            setContentView(R.layout.details_layout);

            toolbar = (Toolbar) findViewById(R.id.toolbar_details_layout);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            mTextView_ecode = (TextView) findViewById(R.id.eCodeFull);
            mTextView_ecode.setText(complexJavaObjEN.getCode());

//            mTextView_ename = (TextView) findViewById(R.id.eNameFull);
//            mTextView_ename.setText(complexJavaObjEN.getName());
        }
        catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage() + e.getStackTrace().toString());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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

        //TODO
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
