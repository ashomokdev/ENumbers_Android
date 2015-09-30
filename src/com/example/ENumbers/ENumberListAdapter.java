package com.example.eNumbers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

/**
 * Created by y.belyaeva on 28.07.2015.
 */
public class ENumberListAdapter extends CursorAdapter {
    public ENumberListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
        Log.e(this.getClass().getCanonicalName(), "ENumberListAdapter(Context context, Cursor cursor, int flags)");
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.e(this.getClass().getCanonicalName(),"newView(Context context, Cursor cursor, ViewGroup parent)");
        return LayoutInflater.from(context).inflate(R.layout.enumb_proxy_list_row_layout, null, false);

    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        try {
            // Find fields to populate in inflated template
            TextView ecode = (TextView) view.findViewById(R.id.ECode);
            TextView ename = (TextView) view.findViewById(R.id.EName);
            TextView epurpose = (TextView) view.findViewById(R.id.EPurpose);

            ENumbFlag flag = (ENumbFlag) view.findViewById(R.id.enumbFlag_enumb_proxy_list_row_layout);
            flag.setmIsGreen(false);
            flag.setmIsYellow(false);
            flag.setmIsRed(false);
            flag.setmIsGrey(false);

            // Extract properties from cursor
            String code = cursor.getString(cursor.getColumnIndexOrThrow("code"));

            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String purpose = cursor.getString(cursor.getColumnIndexOrThrow("purpose"));
            String dangerLevel = cursor.getString(cursor.getColumnIndexOrThrow("dangerLevel"));

            Log.e(this.getClass().getCanonicalName(), code + " " + dangerLevel);

            // Populate fields with extracted properties
            ecode.setText(code);
            ename.setText(name);
            epurpose.setText(purpose);

            switch (dangerLevel) {
                case "safe":
                    flag.setmIsGreen(true);
                    break;
                case "medium":
                    flag.setmIsYellow(true);
                    break;
                case "hight":
                    flag.setmIsRed(true);
                    break;
                case "unknown":
                    flag.setmIsGrey(true);
                    break;
                default:
                    flag.setmIsGrey(true);
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
