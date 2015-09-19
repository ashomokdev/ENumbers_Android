//package com.example.eNumbers;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.CursorAdapter;
//import android.widget.TextView;
//import java.util.List;
//
///**
// * Created by y.belyaeva on 28.07.2015.
// */
//public class ENumberListAdapter extends CursorAdapter {
//
//    private List<ENumber> listData;
//
//    private LayoutInflater layoutInflater;
//
//    // Default constructor
//    public ENumberListAdapter(Context context, Cursor cursor, int flags) {
//        super(context, cursor, flags);
//        layoutInflater = (LayoutInflater) context.getSystemService(
//                Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    public ENumberListAdapter(Context aContext, List<ENumber> listData) {
//        super(aContext, null, false);
//
//        this.listData = listData;
//
//        layoutInflater = LayoutInflater.from(aContext);
//    }
//
//    @Override
//    public int getCount() {
//
//        return listData.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//
//        return listData.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolder holder;
//
//        if (convertView == null) {
//
//            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
//
//            holder = new ViewHolder();
//            holder.ECodeView = (TextView) convertView.findViewById(R.id.ECode);
//            holder.ENameView = (TextView) convertView.findViewById(R.id.EName);
//            holder.EPurposeView = (TextView) convertView.findViewById(R.id.EPurpose);
//            holder.EStatusView = (TextView) convertView.findViewById(R.id.EStatus);
//
//            convertView.setTag(holder);
//        } else {
//
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        holder.ECodeView.setText(listData.get(position).getCode());
//        holder.ENameView.setText(listData.get(position).getName());
//        holder.EPurposeView.setText(listData.get(position).getPurpose());
//        holder.EStatusView.setText(listData.get(position).getStatus());
//
//        return convertView;
//    }
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//        // R.layout.list_row_layout is your xml layout for each row
//        return layoutInflater.inflate(R.layout.list_row_layout, viewGroup, false);
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        TextView textViewTitle = (TextView) view.findViewById(R.id.ECode);
//        String title = cursor.getString( cursor.getColumnIndex( "code" ) );
//        textViewTitle.setText(title);
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
//        TextView EStatusView;
//    }
//}
//
