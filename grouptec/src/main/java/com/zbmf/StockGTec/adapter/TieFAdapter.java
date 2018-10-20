package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.Fans;
import com.zbmf.StockGTec.utils.ImageLoaderOptions;
import com.zbmf.StockGTec.view.RoundedCornerImageView;

import java.util.List;

import static com.zbmf.StockGTec.R.id.tv_name;

public class TieFAdapter extends BaseAdapter{

    private List<Fans> list;
    private Context mContext;

    public TieFAdapter(Context cxt, List<Fans> list){
        this.list = list;
        mContext = cxt;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tief, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView)convertView.findViewById(tv_name);
            holder.icv = (RoundedCornerImageView) convertView.findViewById(R.id.icv);
            holder.tv_time1 = (TextView)convertView.findViewById(R.id.tv_time1);
            holder.tv_time2 = (TextView)convertView.findViewById(R.id.tv_time2);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(holder);
        }

        Fans fans = list.get(position);
        holder.tv_name.setText(fans.getNickname());

        ImageLoader.getInstance().displayImage(fans.getAvatar(),holder.icv, ImageLoaderOptions.AvatarOptions());
        holder.tv_time1.setText("开始时间"+fans.getJoin_at());
        holder.tv_time2.setText("到期时间"+fans.getExpire_at());
        int level = fans.getFans_level();
        if(level == 20 || level == 10){
            holder.iv.setVisibility(View.VISIBLE);
        }else{
            holder.iv.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder{
        TextView tv_name,tv_time1,tv_time2;
        RoundedCornerImageView icv;ImageView iv;
    }
}
