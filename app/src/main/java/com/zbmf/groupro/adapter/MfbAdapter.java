package com.zbmf.groupro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.MFBean;

import java.util.List;

/**
 * Created by xuhao on 2017/2/20.
 */

public class MfbAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<MFBean>infolist;
    public MfbAdapter(Context context, List<MFBean>info){
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
        mfbItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.mfb_detail_item,null);
            item=new mfbItem(view);
            view.setTag(item);
        }else{
            item= (mfbItem) view.getTag();
        }
        MFBean pb=infolist.get(i);
        item.title.setText(pb.getTitle());
        item.date.setText(pb.getDate());
        item.count.setText(pb.getCount());
        return view;
    }
    private class mfbItem{
        private TextView title,date,count;
        public mfbItem(View view){
            this.count= (TextView) view.findViewById(R.id.item_mfb_count);
            this.date= (TextView) view.findViewById(R.id.item_mfb_date);
            this.title= (TextView) view.findViewById(R.id.item_mfb_title);
        }
    }
}
