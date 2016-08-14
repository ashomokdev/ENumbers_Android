package com.ashomok.eNumbers.activities.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.ashomok.eNumbers.R;

/**
 * Created by iuliia on 8/8/16.
 */

//Activity A
public class SubcategoriesListActivity extends AppCompatActivity implements SubcategoriesListFragment.OnItemSelectedListener {

    private static final String TAG = SubcategoriesListActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.subcategories_list_activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Check whether the activity is using the layout version with
        // the list_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.list_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            SubcategoriesListFragment firstFragment = new SubcategoriesListFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            try {
                String label = getResources().getString(((Row) getIntent().getExtras().getSerializable(Row.TAG)).getTitleResourceID());
                setTitle(label);
            } catch (Exception e) {
                Log.w(TAG, "Can't set label for activity's action bar");
            }

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction().add(R.id.list_container, firstFragment).commit();
        } else {
            Log.e(TAG, "R.id.list_container not found in layout file.");
        }
    }

    /**
     * This is a callback that the list fragment (Fragment A)
     * calls when a list item is selected
     */
    @Override
    public void onItemSelected(int position) {
        SubcategoryFragment subcategoryFragment = (SubcategoryFragment) getFragmentManager()
                .findFragmentById(R.id.details_container);
        if (subcategoryFragment == null) {
            // SubcategoryFragment (Fragment B) is not in the layout (handset layout),
            // so start SubcategoryActivity (Activity B)
            // and pass it the info about the selected item
            Intent intent = new Intent(this, SubcategoryActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        } else {
            // DisplayFragment (Fragment B) is in the layout (tablet layout),
            // so tell the fragment to update
            subcategoryFragment.updateContent(position);
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
