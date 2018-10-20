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
import com.zbmf.groupro.beans.NewsFeed;
import com.zbmf.groupro.utils.ImageLoaderOptions;
import com.zbmf.groupro.view.RoundedCornerImageView;

import java.util.List;


public class DynamicAdapter extends BaseAdapter {
    private LayoutInflater inflaterl;
    private List<NewsFeed> mNewsFeeds;
    public DynamicAdapter(Context context, List<NewsFeed>infolist){
        this.inflaterl=LayoutInflater.from(context);
        this.mNewsFeeds=infolist;
    }
    @Override
    public int getCount() {
        return mNewsFeeds.size();
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
            view=inflaterl.inflate(R.layout.mf_head_message_item,null);
            item=new HeadMessageItem(view);
            view.setTag(item);
        }else{
            item= (HeadMessageItem) view.getTag();
        }


        NewsFeed newsFeed = mNewsFeeds.get(i);
        ImageLoader.getInstance().displayImage(newsFeed.getCover(),item.img, ImageLoaderOptions.BigProgressOptions());
        ImageLoader.getInstance().displayImage(newsFeed.getUser().getAvatar(),item.avatar, ImageLoaderOptions.AvatarOptions());
        item.title.setText(newsFeed.getSubject());
        item.name.setText(newsFeed.getUser().getNickname());
        item.date.setText(newsFeed.getPosted_at());

        item.look.setVisibility(View.GONE);
        item.pinglun.setVisibility(View.GONE);
        return view;
    }
    private class HeadMessageItem{
        ImageView img;
        RoundedCornerImageView avatar;
        TextView title,name,date,look,pinglun;
        public HeadMessageItem(View view){
            this.img= (ImageView) view.findViewById(R.id.head_message_img);
            this.avatar= (RoundedCornerImageView) view.findViewById(R.id.head_message_avatar);
            this.title= (TextView) view.findViewById(R.id.head_message_title);
            this.name= (TextView) view.findViewById(R.id.head_message_name);
            this.date= (TextView) view.findViewById(R.id.head_message_date);
            this.look= (TextView) view.findViewById(R.id.head_message_look);
            this.pinglun= (TextView) view.findViewById(R.id.head_message_pinglun);
        }
    }
}
