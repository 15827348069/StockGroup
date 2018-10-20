package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.Record;
import com.zbmf.StocksMatch.widget.CircleImageView;

/**
 * 用户获奖记录adapter
 * Created by lulu on 16/1/9.
 */
public class RecordAdapter extends ListAdapter<Record>{

    private ImageLoader imageLoader;
    public RecordAdapter(Activity context) {
        super(context);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView != null){
            holder = (ViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(mContext).inflate(R.layout.focus_item, null);
            holder = new ViewHolder();
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.civ = (CircleImageView)convertView.findViewById(R.id.civ);
            holder.iv_right = (ImageView) convertView.findViewById(R.id.iv_right);
            convertView.setTag(holder);
        }

        holder.civ.setVisibility(View.GONE);holder.iv_right.setVisibility(View.GONE);
        Record record = mList.get(position);
        holder.tv_username.setText(record.getWin_at()+mContext.getString(R.string.get1)+record.getAward());
        return convertView;
    }


    static class ViewHolder{
        TextView tv_username;
        CircleImageView civ;
        ImageView iv_right;
    }


}
