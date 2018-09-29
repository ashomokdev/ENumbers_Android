package com.ashomok.eNumbers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.ad.AdContainer;
import com.ashomok.eNumbers.ad.AdMobContainerImpl;

/**
 * Created by iuliia on 11/12/16.
 */

//todo merge SearchByNamesActivity with its fragment for simplify code
public class SearchByNamesActivity extends AppCompatActivity {

    private static final String TAG = SearchByNamesActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.search_by_name_activity);

            AdContainer adMobContainer = new AdMobContainerImpl(this);
            ViewGroup parent = findViewById(R.id.search_by_name_parent);
            adMobContainer.initAd(parent);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
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
