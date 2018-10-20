package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.Actives;
import com.zbmf.StocksMatch.beans.General;

/**
 * Created by Administrator on 2016/1/8.
 */
public class ActivesAdapter extends ListAdapter<Actives>{
    public ActivesAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View view = null;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext,R.layout.actives_item, null);
            holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Actives actives = mList.get(position);
        holder.tv_name.setText(actives.getTitle());
        holder.tv_date.setText(actives.getStart_time()+mContext.getString(R.string.sperator)+actives.getEnd_time());
        holder.tv_content.setText(actives.getDesc());
        return view;
    }

    static class ViewHolder {
        TextView tv_date, tv_name, tv_content;
    }
}
