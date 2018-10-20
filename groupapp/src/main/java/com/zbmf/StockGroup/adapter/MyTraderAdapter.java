package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Traders;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;

/**
 * Created by xuhao on 2017/11/10.
 */

public class MyTraderAdapter extends ListAdapter<Traders> {
    public MyTraderAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_my_trading_layout,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        Traders traders= (Traders) getItem(position);
        holder.tv_expired_at.setText("有效期至"+traders.getExpired_at());
        holder.tv_nickname.setText(traders.getNickname());
//        ViewFactory.imgCircleView(parent.getContext(),traders.getAvatar(),holder.avatar);
        ImageLoader.getInstance().displayImage(traders.getAvatar(),holder.avatar, ImageLoaderOptions.AvatarOptions());
        return convertView;
    }
    private class ViewHolder{
        private TextView tv_nickname,tv_expired_at;
        private ImageView avatar;
        public ViewHolder(View view){
            avatar= (ImageView) view.findViewById(R.id.iv_rcv);
            tv_nickname= (TextView) view.findViewById(R.id.tv_nickname);
            tv_expired_at= (TextView) view.findViewById(R.id.tv_expired_at);
        }
    }
}
