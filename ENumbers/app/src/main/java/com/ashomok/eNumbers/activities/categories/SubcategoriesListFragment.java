package com.ashomok.eNumbers.activities.categories;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ashomok.eNumbers.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iuliia on 8/12/16.
 */
public class SubcategoriesListFragment extends ListFragment {

    private OnItemSelectedListener mCallback;

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    private static final String ARGUMENT_START_NUMBER = "arg_start_number";
    private static final String ARGUMENT_END_NUMBER = "arg_end_number";
    private static final String ARGUMENT_TITLE_RESOURCE_ID = "arg_title_resource_id";

    private int startNumber;
    private int endNumber;
    private int titleResourceID;

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

    public static Fragment newInstance(Row settings) {
        SubcategoriesListFragment categoryFragment = new SubcategoriesListFragment();

        Bundle arguments = new Bundle();

        arguments.putInt(ARGUMENT_START_NUMBER, settings.getStartNumber());
        arguments.putInt(ARGUMENT_END_NUMBER, settings.getEndNumber());
        arguments.putInt(ARGUMENT_TITLE_RESOURCE_ID, settings.getTitleResourceID());

        categoryFragment.setArguments(arguments);
        return categoryFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;
            try {
                mCallback = (OnItemSelectedListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnItemSelectedListener");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startNumber = getArguments().getInt(ARGUMENT_START_NUMBER);
        endNumber = getArguments().getInt(ARGUMENT_END_NUMBER);
        titleResourceID = getArguments().getInt(ARGUMENT_TITLE_RESOURCE_ID);
    }

    public void updateContent(Row settings) {
        //// TODO: 8/13/16
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.listview, null);

        ListView listView = (ListView) view.findViewById(R.id.lv_subcategories);

        List<Row> listItems = dataList; //todo

        listView.setAdapter(new RowsAdapter(view.getContext(), listItems));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                // Notify the parent activity of selected item
                mCallback.onItemSelected(position);

                // Set the item as checked to be highlighted when in two-pane layout
                getListView().setItemChecked(position, true);
            }
        });

        return view;
    }
}
