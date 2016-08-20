package com.ashomok.eNumbers.activities.categories;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ashomok.eNumbers.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iuliia on 8/12/16.
 */
public class SubcategoriesListFragment extends Fragment {

    private static final String TAG = SubcategoriesListFragment.class.getSimpleName();

    public static final String CHECKED_ROW_POS_ARG = "checkedRowPosition";
    private int checkedRowPosition;

    private OnItemSelectedListener mCallback;

    private ListView listView;

    private Context context;

    public interface OnItemSelectedListener {
        void onItemSelected(Row row);
    }

    private Row row;

    private static List<Row> dataList = new ArrayList<Row>() {{
        add(new Row(100, 199, R.string.all));
        add(new Row(100, 109, R.string.yellows));
        add(new Row(110, 119, R.string.oranges));
        add(new Row(120, 129, R.string.reds));
        add(new Row(130, 139, R.string.blues));
        add(new Row(140, 149, R.string.greens));
        add(new Row(150, 159, R.string.browns));
        add(new Row(160, 199, R.string.gold));

        add(new Row(200, 299, R.string.all));
        add(new Row(200, 209, R.string.sorbates));
        add(new Row(210, 219, R.string.benzoates));
        add(new Row(220, 229, R.string.sulphites));
        add(new Row(230, 239, R.string.phenols));
        add(new Row(240, 259, R.string.nitrates));
        add(new Row(260, 269, R.string.acetates));
        add(new Row(270, 279, R.string.lactates));
        add(new Row(280, 289, R.string.propionates));
        add(new Row(290, 299, R.string.others));

        add(new Row(300, 399, R.string.all));
        add(new Row(300, 305, R.string.ascorbates));
        add(new Row(306, 309, R.string.Tocopherol));
        add(new Row(310, 319, R.string.gallates));
        add(new Row(320, 329, R.string.lactates));
        add(new Row(330, 339, R.string.citrates));
        add(new Row(340, 349, R.string.phosphates));
        add(new Row(350, 359, R.string.malates));
        add(new Row(360, 369, R.string.succinates));
        add(new Row(370, 399, R.string.others));

        add(new Row(400, 499, R.string.all));
        add(new Row(400, 409, R.string.alginates));
        add(new Row(410, 419, R.string.natural_gums));
        add(new Row(420, 429, R.string.other_natural));
        add(new Row(430, 439, R.string.polyoxyethene));
        add(new Row(440, 449, R.string.natural));
        add(new Row(450, 459, R.string.phosphates));
        add(new Row(460, 469, R.string.cellulose));
        add(new Row(470, 489, R.string.fatty));
        add(new Row(490, 499, R.string.others));

        add(new Row(500, 599, R.string.all));
        add(new Row(500, 509, R.string.mineral));
        add(new Row(510, 519, R.string.chlorides));
        add(new Row(520, 529, R.string.sulphates));
        add(new Row(530, 549, R.string.alkali));
        add(new Row(550, 559, R.string.silicates));
        add(new Row(570, 579, R.string.stearates));
        add(new Row(580, 599, R.string.others));

        add(new Row(600, 699, R.string.all));
        add(new Row(620, 629, R.string.glutamates));
        add(new Row(630, 639, R.string.inosinates));
        add(new Row(640, 649, R.string.others));

        add(new Row(700, 799, R.string.all));

        add(new Row(900, 999, R.string.all));
        add(new Row(900, 909, R.string.waxes));
        add(new Row(910, 919, R.string.synthetic));
        add(new Row(920, 929, R.string.improving));
        add(new Row(930, 949, R.string.packaging));
        add(new Row(950, 969, R.string.sweeteners));
        add(new Row(990, 999, R.string.foaming));

        add(new Row(1000, 1599, R.string.all));
    }};

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;
            initCallback(activity);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initCallback(activity);
    }


    public void updateContent(Row settings) {

        if (settings != null) {
            List<Row> rows = new ArrayList<>();
            for (Row r : dataList) {
                if (r.getStartNumber() >= row.getStartNumber() && r.getEndNumber() <= row.getEndNumber()) {
                    rows.add(r);
                }
            }

            if (listView != null) {
                listView.setAdapter(new RowsAdapter(context, rows));

                setCheckedRow(listView, checkedRowPosition);
            } else {
                Log.e(TAG, "listView == null");
            }
        }
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

            checkedRowPosition = bundle.getInt(CHECKED_ROW_POS_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            checkedRowPosition = savedInstanceState.getInt(CHECKED_ROW_POS_ARG);
        }

        View view = inflater.inflate(R.layout.listview, null);

        context = view.getContext();
        listView = (ListView) view.findViewById(R.id.lv_subcategories);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {

                checkedRowPosition = position;
                setCheckedRow(parent, checkedRowPosition);
            }
        });

        return view;
    }

    private void setCheckedRow(AdapterView<?> parent, int position) {
        //position == -1 for handset devices
        if (position >= 0) {
            Row row = (Row) parent.getAdapter().getItem(position);

            // Notify the parent activity of selected item
            mCallback.onItemSelected(row);

            // Set the item as checked to be highlighted when in two-pane layout
            listView.setItemChecked(position, true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContent(row);
    }

    private void initCallback(Activity activity) {
        try {
            mCallback = (OnItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save the current article selection in case we need to recreate the fragment
        outState.putSerializable(Row.TAG, row);

        outState.putInt(CHECKED_ROW_POS_ARG, checkedRowPosition);

        super.onSaveInstanceState(outState);
    }
}
