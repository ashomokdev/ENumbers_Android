package com.ashomok.eNumbers.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashomok.eNumbers.R;

/**
 * Created by iuliia on 11/15/16.
 */

public class SearchByNamesFragment extends ENListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_by_names_fragment, container, false);
    }

    @Override
    protected void loadDefaultData() {
        showAllData();
    }
}
