package com.ashomok.eNumbers.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.ad.AdContainer;
import com.ashomok.eNumbers.ad.AdMobContainerImpl;
import com.ashomok.eNumbers.tools.LogHelper;

/**
 * Created by iuliia on 11/12/16.
 */

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
            LogHelper.d(TAG, e.getMessage());
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
