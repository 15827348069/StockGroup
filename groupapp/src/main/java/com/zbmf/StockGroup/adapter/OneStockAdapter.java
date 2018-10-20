package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.StockComments;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;


/**
 * Created by iMac on 2017/2/21.
 */

public class OneStockAdapter extends BaseAdapter{

    private List<StockComments> oneStockCommits;
    private Context cxt;

    public OneStockAdapter(Context cxt, List<StockComments> oneStockCommits){
        this.oneStockCommits = oneStockCommits;
        this.cxt = cxt;
    }

    @Override
    public int getCount() {
        return oneStockCommits.size();
    }

    @Override
    public Object getItem(int position) {
        return oneStockCommits.get(position);
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
            convertView = LayoutInflater.from(cxt).inflate(R.layout.one_stock_commit_i, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        StockComments stockComments=oneStockCommits.get(position);
        holder.tv_commit_time.setText(stockComments.getCreate_at());
        holder.tv_userName.setText(stockComments.getNickname());
        holder.tv_desc.setText(stockComments.getDesc());
//        ViewFactory.imgCircleView(parent.getContext(),stockComments.getUser_img(),holder.iv_rcv);
        ImageLoader.getInstance().displayImage(stockComments.getUser_img(),holder.iv_rcv, ImageLoaderOptions.AvatarOptions());
        return convertView;
    }

    private class ViewHolder{
            RoundedCornerImageView iv_rcv;
            TextView tv_userName,tv_desc,tv_commit_time;
            public  ViewHolder(View view){
                iv_rcv= (RoundedCornerImageView) view.findViewById(R.id.iv_rcv);
                tv_userName= (TextView) view.findViewById(R.id.tv_userName);
                tv_desc= (TextView) view.findViewById(R.id.tv_desc);
                tv_commit_time= (TextView) view.findViewById(R.id.tv_commit_time);
            }
    }
}
