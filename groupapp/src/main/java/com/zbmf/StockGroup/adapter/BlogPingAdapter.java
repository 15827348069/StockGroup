package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.BlogPingBean;
import com.zbmf.StockGroup.utils.EditTextUtil;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;

/**
 * Created by xuhao on 2017/2/22.
 */

public class BlogPingAdapter extends BaseAdapter {
    private List<BlogPingBean>infolist;
    private LayoutInflater inflater;
    private Context context;
    public BlogPingAdapter(Context context,List<BlogPingBean>info){
        this.infolist=info;
        this.context=context;
        this.inflater=LayoutInflater.from(context);
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
        BlogPingItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.blog_ping_item,null);
            item=new BlogPingItem(view);
            view.setTag(item);
        }else{
            item= (BlogPingItem) view.getTag();
        }
        BlogPingBean bp=infolist.get(i);
        item.name.setText(bp.getUser_nickname());
        item.date.setText(bp.getPosted_at());
        item.content.setText(EditTextUtil.getContent(context,bp.getContent()));
//        ViewFactory.imgCircleView(viewGroup.getContext(),bp.getUser_avatar(),item.avatar);
        ImageLoader.getInstance().displayImage(bp.getUser_avatar(),item.avatar, ImageLoaderOptions.AvatarOptions());
        return view;
    }
    private class BlogPingItem{
        TextView name,date,content;
        RoundedCornerImageView avatar;
        public BlogPingItem(View view){
            this.name= (TextView) view.findViewById(R.id.ping_user_name);
            this.date= (TextView) view.findViewById(R.id.ping_user_date);
            this.content= (TextView) view.findViewById(R.id.ping_user_content);
            this.avatar= (RoundedCornerImageView) view.findViewById(R.id.ping_user_avatar);
        }
    }
}
