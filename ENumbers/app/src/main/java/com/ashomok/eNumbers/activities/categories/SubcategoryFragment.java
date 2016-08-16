package com.ashomok.eNumbers.activities.categories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashomok.eNumbers.R;
import com.ashomok.eNumbers.activities.ENListFragment;

import java.io.Serializable;

/**
 * Created by iuliia on 8/12/16.
 */

//Fragment B
public class SubcategoryFragment extends ENListFragment {

    private Row row;

    public void updateContent(Row row) {
        this.row = row;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable(Row.TAG);

            if (serializable instanceof Row) {
                row = (Row) serializable;

                GetInfoByENumbersRange(row.getStartNumber(), row.getEndNumber());
            }
        }
    }

    public void GetInfoByENumbersRange(int startValue, int endValue)
    {
        Bundle b = new Bundle();
        b.putInt("start_value", startValue);
        b.putInt("end_value", endValue);
        try {

            getLoaderManager().restartLoader(0, b, this);

        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
            throw e;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.en_list_fragment, container, false);
    }
}
