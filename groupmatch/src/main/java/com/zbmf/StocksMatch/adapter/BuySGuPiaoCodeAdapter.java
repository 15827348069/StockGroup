package com.zbmf.StocksMatch.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.db.DatabaseImpl;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

public class BuySGuPiaoCodeAdapter extends BaseAdapter implements Filterable {
    private Context ct;
    private List<HashMap<String, String>> data;
    DBFilter filter;

    public BuySGuPiaoCodeAdapter(Context context) {
        this.ct = context;
        this.data = new ArrayList<HashMap<String, String>>();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position).get("number");
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ListItem item = null;
        if (convertView == null) {
            item = new ListItem();
            LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.buy_gupiao_code_list_item_layout, null);
            item.name = (TextView) convertView.findViewById(R.id.code_name);
            item.number = (TextView) convertView.findViewById(R.id.code_number);
            convertView.setTag(item);
        } else {
            item = (ListItem) convertView.getTag();
        }
        item.name.setText(data.get(position).get("name"));
        item.number.setText(data.get(position).get("number"));
        return convertView;
    }

    public class ListItem {
        TextView name, number;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new DBFilter();
        }

        return filter;
    }

    private class DBFilter extends Filter {



        protected FilterResults performFiltering(CharSequence constraint) {
            Cursor cursor = new DatabaseImpl(ct).getStocks1(constraint.toString());
            List<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", cursor.getString(cursor.getColumnIndex("name")));
                    map.put("number", cursor.getString(cursor.getColumnIndex("code")));
                    datas.add(map);
                }
            }
            FilterResults results = new FilterResults();
            results.values = datas;
            results.count = datas.size();
            if (cursor != null) {
                cursor.close();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub
            if (results.values != null) {
                data = (List<HashMap<String, String>>) results.values;
            } else {
                data.clear();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
