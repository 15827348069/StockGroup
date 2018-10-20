package com.zbmf.groupro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.BlogBean;
import com.zbmf.groupro.utils.ImageLoaderOptions;

import java.util.List;

/**
 * Created by xuhao on 2017/2/20.
 */

public class MyblogAdapter extends BaseAdapter {
    private LayoutInflater inflaterl;
    private List<BlogBean>info;
    public MyblogAdapter(Context context, List<BlogBean>infolist){
        this.inflaterl=LayoutInflater.from(context);
        this.info=infolist;
    }
    @Override
    public int getCount() {
        return info.size();
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
        HeadMessageItem item=null;
        if(view==null){
            view=inflaterl.inflate(R.layout.my_blog_item,null);
            item=new HeadMessageItem(view);
            view.setTag(item);
        }else{
            item= (HeadMessageItem) view.getTag();
        }
        BlogBean bb=info.get(i);
        ImageLoader.getInstance().displayImage(bb.getImg(),item.img, ImageLoaderOptions.BigProgressOptions());
//        ImageLoader.getInstance().displayImage(bb.getAvatar(),item.avatar, ImageLoaderOptions.AvatarOptions());
        item.title.setText(bb.getTitle());
        item.date.setText(bb.getDate());
        return view;
    }
    private class HeadMessageItem{
        ImageView img;
//        RoundedCornerImageView avatar;
        TextView title,date;
        public HeadMessageItem(View view){
            this.img= (ImageView) view.findViewById(R.id.my_blog_img);
//            this.avatar= (RoundedCornerImageView) view.findViewById(R.id.my_blog_avatar);
            this.title= (TextView) view.findViewById(R.id.my_blog_title);
            this.date= (TextView) view.findViewById(R.id.my_blog_date);
        }
    }
}
