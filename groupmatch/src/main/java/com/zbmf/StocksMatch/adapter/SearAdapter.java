package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.beans.User;

import java.util.List;

/**
 * 搜索结果adapter
 * Created by Administrator on 2016/1/7.
 */
public class SearAdapter extends ListAdapter<General> {


    public SearAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.sear_stock_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        General t = mList.get(position);
        if(t instanceof User){
            User u = (User)t;
            holder.tv_name.setText(u.getNickname());
            holder.tv_code.setVisibility(View.GONE);
        }else if(t instanceof MatchBean){
            MatchBean m = (MatchBean)t;
            holder.tv_name.setText(m.getTitle());
            holder.tv_code.setVisibility(View.GONE);
        }else{
            Stock s = (Stock)t;
            holder.tv_name.setText(s.getName());
            holder.tv_code.setText(s.getSymbol());
            holder.tv_code.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tv_name, tv_code;
    }
}
