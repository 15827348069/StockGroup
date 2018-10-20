package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.widget.CircleImageView;

/**
 * 用户主页 -比赛adapter
 * Created by lulu on 16/1/9.
 */
public class UserMatchAdapter extends ListAdapter<MatchBean>{

    private ImageLoader imageLoader;
    public UserMatchAdapter(Activity context) {
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

        MatchBean mb = mList.get(position);
        holder.civ.setVisibility(View.GONE);
        holder.tv_username.setText(mb.getTitle());
        return convertView;
    }


    static class ViewHolder{
        TextView tv_username;
        CircleImageView civ;
        ImageView iv_right;
    }


}
