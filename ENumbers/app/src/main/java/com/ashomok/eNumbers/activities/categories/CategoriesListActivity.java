package com.ashomok.eNumbers.activities.categories;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ashomok.eNumbers.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iuliia on 8/9/16.
 */
public class CategoriesListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = CategoriesListActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.categories_list_activity_layout);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            ListView listView = (ListView) findViewById(R.id.lv_categories);

            List<CategorySettings> listItems = new ArrayList<>(CategoriesFragmentFactory.settingsList.values());

            listView.setAdapter(new RowsAdapter(this, listItems));

            listView.setOnItemClickListener(this);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//todo
    }
}
