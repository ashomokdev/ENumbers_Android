package com.example.eNumbers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

/**
 * Created by y.belyaeva on 28.07.2015.
 */
//public class ENumberListAdapter extends ResourceCursorAdapter{
//
//    public ENumberListAdapter(Context context, int layout, Cursor c, int flags) {
//        super(context, layout, c, flags);
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//       //  Find fields to populate in inflated template
//
//        LayoutInflater inflator = LayoutInflater.from(context).inflate(R.layout.enumb_proxy_list_row_layout, null, true);
//        TextView tvBody = inflator.inflate(R.id.ECode, false);
//       // TextView tvPriority = (TextView) view.findViewById(R.id.EName);
//        // Extract properties from cursor
//        String body = cursor.getString(cursor.getColumnIndexOrThrow("code"));
//        String priority = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//        // Populate fields with extracted properties
//        tvBody.setText(body);
//       // tvPriority.setText(priority);
//
//        ENumbFlag flag = new ENumbFlag(context);
//        flag.setmIsGreen(true);
//    }
//}
public class ENumberListAdapter extends CursorAdapter {
    public ENumberListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
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
            ENumbFlag flag = (ENumbFlag) view.findViewById(R.id.imageViewFlag);

            // Extract properties from cursor
            String code = cursor.getString(cursor.getColumnIndexOrThrow("code"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String purpose = cursor.getString(cursor.getColumnIndexOrThrow("purpose"));
            String dangerLevel = cursor.getString(cursor.getColumnIndexOrThrow("dangerLevel"));

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
//public class ENumberListAdapter extends CursorAdapter {
//    Cursor c;
//    private LayoutInflater mInflater;
//
//    // Default constructor
//    public ENumberListAdapter(Context context, Cursor cursor, int flags) {
//        super(context, cursor, flags);
//        this.c= cursor;
//        mInflater = (LayoutInflater) context.getSystemService(
//                Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolder holder;
//
//        if (convertView == null) {
//
//            convertView = mInflater.inflate(R.layout.enumb_proxy_list_row_layout, null);
//
//            holder = new ViewHolder();
//            holder.ECodeView = (TextView) convertView.findViewById(R.id.ECode);
//            holder.ENameView = (TextView) convertView.findViewById(R.id.EName);
//            holder.EPurposeView = (TextView) convertView.findViewById(R.id.EPurpose);
//            holder.messageListItemView = new ENumbFlag(parent.getContext());
//            holder.messageListItemView.setmIsYellow(true);
//            convertView.setTag(holder);
//        } else {
//
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        holder.ECodeView.setText(c.getString(1));
//        holder.ENameView.setText(c.getString(2));
//        holder.EPurposeView.setText(c.getString(3));
//
//
//
//        return convertView;
//    }
//
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//        // R.layout.enumb_proxy_list_row_layout is your xml layout for each row
//        return mInflater.inflate(R.layout.enumb_proxy_list_row_layout, viewGroup, false);
//    }
//
//
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//
////        ENumbFlag flag = new ENumbFlag(context);
////        flag.setmIsGreen(true);
//
//
//        TextView textViewTitle = (TextView) view.findViewById(R.id.ECode);
//        String title = cursor.getString( cursor.getColumnIndex( "code" ) );
//        textViewTitle.setText(title);
//
////        TextView name = (TextView) view.findViewById(R.id.name);
////        name.setText(cursor.getString(cursor.getColumnIndex("name")));
////
////        TextView phone = (TextView) view.findViewById(R.id.phone);
////        phone.setText(cursor.getString(cursor.getColumnIndex("phone")));
//    }
//
//    static class ViewHolder {
//
//        TextView ECodeView;
//
//        TextView ENameView;
//
//        TextView EPurposeView;
//
//        ENumbFlag messageListItemView;
//    }
//}

