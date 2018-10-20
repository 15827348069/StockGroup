package com.zbmf.groupro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by xuhao on 2017/2/13.
 */

public class HistoryDateAdapter extends BaseAdapter {
    private List<Date>infolist;
    private LayoutInflater inflater;
    private String group_nick_name;
    public HistoryDateAdapter(Context context,List<Date>infolist,String group_nick_name){
        this.inflater=LayoutInflater.from(context);
        this.infolist=infolist;
        this.group_nick_name=group_nick_name;
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
        HistoryDateItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.history_date_item,null);
            item=new HistoryDateItem(view);
            view.setTag(item);
        }else{
            item= (HistoryDateItem) view.getTag();
        }
        item.history_date_text.setText(group_nick_name+"老师"+DateUtil.getTime(infolist.get(i).getTime(), Constants.YYYY年MM月dd日)+"直播记录");
        return view;
    }
    private class HistoryDateItem{
        TextView history_date_text;
        public HistoryDateItem(View view){
            history_date_text= (TextView) view.findViewById(R.id.history_date_text);
        }
    }
}
