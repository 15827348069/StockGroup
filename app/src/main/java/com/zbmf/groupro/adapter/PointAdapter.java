package com.zbmf.groupro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.PointBean;

import java.util.List;

/**
 * Created by xuhao on 2017/2/20.
 */

public class PointAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<PointBean>infolist;
    public PointAdapter(Context context,List<PointBean>info){
        this.inflater=LayoutInflater.from(context);
        this.infolist=info;
    }
    @Override
    public int getCount() {
        return infolist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PointItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.point_detail_item,null);
            item=new PointItem(view);
            view.setTag(item);
        }else{
            item= (PointItem) view.getTag();
        }
        PointBean pb=infolist.get(i);
        item.title.setText(pb.getTitle());
        item.date.setText(pb.getDate());
        item.count.setText(pb.getCount());
        return view;
    }
    private class PointItem{
        private TextView title,date,count;
        public PointItem(View view){
            this.count= (TextView) view.findViewById(R.id.item_point_count);
            this.date= (TextView) view.findViewById(R.id.item_point_date);
            this.title= (TextView) view.findViewById(R.id.item_point_title);
        }
    }
}
