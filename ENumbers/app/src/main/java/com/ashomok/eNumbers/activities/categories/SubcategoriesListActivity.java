package com.ashomok.eNumbers.activities.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ashomok.eNumbers.R;

/**
 * Created by iuliia on 8/8/16.
 */

//Activity A
public class SubcategoriesListActivity extends AppCompatActivity implements SubcategoriesListFragment.OnItemSelectedListener  {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.subcategories_list_activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /** This is a callback that the list fragment (Fragment A)
     calls when a list item is selected */
    @Override
    public void onItemSelected(int position) {
        SubcategoryFragment subcategoryFragment = (SubcategoryFragment) getFragmentManager()
                .findFragmentById(R.id.details_frag);
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

}
