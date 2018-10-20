package com.zbmf.groupro.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.BoxDetailBean;
import com.zbmf.groupro.view.MyTextView;

import java.util.List;

/**
 * Created by xuhao on 2017/1/10.
 */

public class BoxCountentIAdapter extends BaseAdapter {
    private List<BoxDetailBean.Items>info;
    private LayoutInflater inflater;
    private MyTextView.OnTextClickListener onClickListener;
    public BoxCountentIAdapter(Context context, List<BoxDetailBean.Items>infolist,MyTextView.OnTextClickListener onClickListener){
        this.inflater=LayoutInflater.from(context);
        this.info=infolist;
        this.onClickListener=onClickListener;
    }
    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int i) {
        return info.get(i);
    }

    @Override
    public long getItemId(int i) {
        BoxDetailBean.Items items=info.get(i);
        return Long.valueOf(items.getItem_id());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BoxItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.box_countent_iten,null);
            item=new BoxItem(view);
            view.setTag(item);
        }else{
            item= (BoxItem) view.getTag();
        }
        BoxDetailBean.Items bb=info.get(i);
        item.data.setText(bb.getCreated_at());
//        item.countent.setTextClickListener(onClickListener);
        item.countent.setText(Html.fromHtml(bb.getContent()));
        return view;
    }
    public class BoxItem{
        private TextView data;
        private TextView countent;
        public BoxItem(View view){
            this.data= (TextView) view.findViewById(R.id.box_countent_data);
            this.countent= (TextView) view.findViewById(R.id.box_countent_id);
        }
    }
}
