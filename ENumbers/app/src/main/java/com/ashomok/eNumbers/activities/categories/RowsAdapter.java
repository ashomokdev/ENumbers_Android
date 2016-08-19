package com.ashomok.eNumbers.activities.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ashomok.eNumbers.R;

import java.util.List;

/**
 * Created by iuliia on 8/9/16.
 */
class RowsAdapter extends ArrayAdapter<Row> {


    public RowsAdapter(Context context, List<Row> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Row row = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_row, parent, false);
        }
        // Lookup view for data population
        TextView codes = (TextView) convertView.findViewById(R.id.codes);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        // Populate the data into the template view using the data object

        int startNumber = row.getStartNumber();
        int endNumber = row.getEndNumber();
        String text = "E" + startNumber + " - E" + endNumber;
        codes.setText(text);

        title.setText(getContext().getResources().getString(row.getTitleResourceID()));

        // Return the completed view to render on screen
        return convertView;
    }

}
