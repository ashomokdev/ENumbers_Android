package com.ashomok.eNumbers.activities.categories;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.ashomok.eNumbers.R;

import java.io.PrintWriter;

/**
 * Created by iuliia on 8/8/16.
 */

//Activity A
public class SubcategoriesListActivity extends AppCompatActivity implements SubcategoriesListFragment.OnItemSelectedListener {

    private String category;
    private static final String TAG = SubcategoriesListActivity.class.getSimpleName();

    private static final String TITLE_ARG = "title";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        FragmentManager.enableDebugLogging(true);
        getSupportFragmentManager().dump("", null,
                new PrintWriter(System.out, true), null);


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
                updateActivityTitle(savedInstanceState.getCharSequence(TITLE_ARG).toString());
                return;
            }

            SubcategoriesListFragment firstFragment = new SubcategoriesListFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            try {
                category = getResources().getString(((Row) getIntent().getExtras().getSerializable(Row.TAG)).getTitleResourceID());
                updateActivityTitle();
            } catch (Exception e) {
                Log.e(TAG, "Activity title can not be updated.");
            }

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction().add(R.id.list_container, firstFragment)
                    .commit();
        } else {
            Log.e(TAG, "R.id.list_container not found in layout file.");
        }
        if (findViewById(R.id.details_container) != null) {
            SubcategoryFragment secondFragment = new SubcategoryFragment();
            getFragmentManager().beginTransaction().add(R.id.details_container, secondFragment)
                    .commit();
        } else {
            Log.d(TAG, "handset device. R.id.details_container not found in layout file");
        }
    }

    private void updateActivityTitle() {
        updateActivityTitle("");
    }

    /**
     * This is a callback that the list fragment (Fragment A)
     * calls when a list item is selected
     */
    @Override
    public void onItemSelected(Row row) {

        SubcategoryFragment subcategoryFragment = (SubcategoryFragment) getFragmentManager()
                .findFragmentById(R.id.details_container);
        if (subcategoryFragment == null) {

            // SubcategoryFragment (Fragment B) is not in the layout (handset layout),
            // replace the fragment
            SubcategoryFragment newFragment = new SubcategoryFragment();
            Bundle args = new Bundle();
            args.putSerializable(Row.TAG, row);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            //replace whatever is in the list_container view with this fragment
            //add transaction to the back stack so the user can navigate back

            transaction.replace(R.id.list_container, newFragment);
            transaction.addToBackStack(null)
                    .commit();

        } else {
            // DisplayFragment (Fragment B) is in the layout (tablet layout),
            // so tell the fragment to update
            subcategoryFragment.updateContent(row);
        }

        updateActivityTitle(getResources().getString(row.getTitleResourceID()));
    }

    private void updateActivityTitle(String title) {
        String newTitle;
        try {
            if (category != null && !title.isEmpty()) {
                newTitle = category + ", " + title;
            } else if (category != null && title.isEmpty()) {
                newTitle = category;
            } else {
                newTitle = title;
            }
            setTitle(newTitle);
        } catch (Exception e) {
            Log.w(TAG, "Can't set label for activity's action bar");
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putCharSequence(TITLE_ARG, getTitle());
    }
}
