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

    private static List<Row> dataList = new ArrayList<Row>(){{
        add(new Row(100, 199, R.string.colours));
        add(new Row(200, 299, R.string.preservatives));
        add(new Row(300, 399, R.string.antioxidants));
        add(new Row(400, 499, R.string.thickeners));
        add(new Row(500, 599, R.string.pH_regulators));
        add(new Row(600, 699, R.string.flavour_enhancers));
        add(new Row(700, 799, R.string.antibiotics));
        add(new Row(900, 999, R.string.miscellaneous));
        add(new Row(1000, 1599, R.string.additional_chemicals));
    }};

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

            List<Row> listItems = dataList;

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
