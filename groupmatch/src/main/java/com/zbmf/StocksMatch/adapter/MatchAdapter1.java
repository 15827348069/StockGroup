package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.Record;
import com.zbmf.StocksMatch.widget.CircleImageView;

/**
 * 供用户选择比赛adapter
 * Created by lulu on 16/1/19.
 */
public class MatchAdapter1 extends ListAdapter<MatchBean>{

    public MatchAdapter1(Activity context) {
        super(context);
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
            holder.ll_line = (LinearLayout) convertView.findViewById(R.id.ll_line);
            convertView.setTag(holder);
        }

        holder.civ.setVisibility(View.GONE);holder.iv_right.setVisibility(View.GONE);
        holder.ll_line.setVisibility(View.GONE);holder.iv_right.setVisibility(View.GONE);
        MatchBean matchBean = mList.get(position);
        holder.tv_username.setMaxLines(1);
        holder.tv_username.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        holder.tv_username.setText(matchBean.getTitle());
        return convertView;
    }


    static class ViewHolder{
        TextView tv_username;
        CircleImageView civ;
        ImageView iv_right;
        LinearLayout ll_line;
    }


}
