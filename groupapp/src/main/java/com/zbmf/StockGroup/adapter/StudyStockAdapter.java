package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.NewsFeed;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;

/**
 * Created by xuhao on 2017/11/13.
 */

public class StudyStockAdapter extends ListAdapter {
    public StudyStockAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_study_sotck,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        BlogBean blog= (BlogBean) getItem(position);
        holder.name.setText(Html.fromHtml(blog.getTitle()));
        return convertView;
    }
    private class ViewHolder{
        TextView name;
        public ViewHolder(View view){
            name= (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
