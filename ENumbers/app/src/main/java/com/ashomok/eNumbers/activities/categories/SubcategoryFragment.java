package com.ashomok.eNumbers.activities.categories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.activities.ENListKeyboardFragment;
import com.ashomok.eNumbers.tools.LogHelper;

import java.io.Serializable;

/**
 * Created by iuliia on 8/12/16.
 */

//Fragment B
public class SubcategoryFragment extends ENListKeyboardFragment {

    private Row row;

    public void updateContent(Row row) {
        this.row = row;
        LoadInfoByENumbersRange(row.getStartNumber(), row.getEndNumber());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable(Row.TAG);

            if (serializable instanceof Row) {
                row = (Row) serializable;
            }
        }
    }


    private void LoadInfoByENumbersRange(int startValue, int endValue) {
        Bundle b = new Bundle();
        b.putInt("start_value", startValue);
        b.putInt("end_value", endValue);
        try {

            getLoaderManager().restartLoader(0, b, this);

        } catch (Exception e) {
            LogHelper.e(this.getClass().getCanonicalName(), e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // If activity recreated (such as from screen rotate), restore
        // the previous selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            Serializable serializable = savedInstanceState.getSerializable(Row.TAG);
            if (serializable instanceof Row) {
                row = (Row) serializable;
            }
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.subcategory_fragment, container, false);
    }


    @Override
    protected void loadData() {
        if (row != null) {
            updateContent(row);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current selection in case we need to recreate the fragment
        outState.putSerializable(Row.TAG, row);
    }

    @Override
    public void showAllData() {
        LoadInfoByENumbersRange(row.getStartNumber(), row.getEndNumber());
    }
}
