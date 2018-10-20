package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.NewsFeed;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;


public class DynamicAdapter extends BaseAdapter {
    private LayoutInflater inflaterl;
    private List<NewsFeed> mNewsFeeds;
    private int flag;
    public DynamicAdapter(Context context, List<NewsFeed>infolist){
        this.inflaterl=LayoutInflater.from(context);
        this.mNewsFeeds=infolist;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int getItemViewType(int position) {
        return flag;
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
        int viewType=getItemViewType(i);
        switch (viewType){
            case Constants.BLOG_FRAGMENT:
                view=getBolgView(i,view);
                break;
            case Constants.MFWW:
                view=getMfwwBlog(i,view);
                break;
            case Constants.ZBMFTT:
                view= getMfwwBlog(i,view);
                break;
            case Constants.MFTT:
                view= getMfwwBlog(i,view);
                break;
        }
        return view;
    }
    private View getBolgView(int i, View view){
        HeadMessageItem item=null;
        if(view==null){
            view=inflaterl.inflate(R.layout.item_care_blog_layout,null);
            item=new HeadMessageItem(view);
            view.setTag(item);
        }else{
            item= (HeadMessageItem) view.getTag();
        }
        NewsFeed newsFeed = mNewsFeeds.get(i);
        item.title.setText(newsFeed.getSubject());
        if(newsFeed.getUser()!=null&& !TextUtils.isEmpty(newsFeed.getUser().getAvatar())){
            item.avatar.setVisibility(View.VISIBLE);
//            ViewFactory.imgCircleView(view.getContext(),newsFeed.getUser()!=null?newsFeed.getUser().getAvatar():"",item.avatar);
            ImageLoader.getInstance().displayImage(newsFeed.getUser()!=null?newsFeed.getUser().getAvatar():"",item.avatar, ImageLoaderOptions.AvatarOptions());
        }else{
            item.avatar.setVisibility(View.GONE);
        }
        item.name.setText(newsFeed.getUser()!=null?newsFeed.getUser().getNickname():"");
        item.look.setText(newsFeed.getStat()!=null?newsFeed.getStat().getViews():"0");
        return view;
    }
    private View getMfwwBlog(int i, View view){
        MfWWHeadMessageItem item=null;
        if(view==null){
            view=inflaterl.inflate(R.layout.mf_head_message_item,null);
            item=new MfWWHeadMessageItem(view);
            view.setTag(item);
        }else{
            item= (MfWWHeadMessageItem) view.getTag();
        }
        NewsFeed newsFeed = mNewsFeeds.get(i);
        item.no_blog_message.setVisibility(View.GONE);
        item.blog_message_layout.setVisibility(View.VISIBLE);
//        ViewFactory.getImgView(view.getContext(),newsFeed.getCover(),item.img);
        ImageLoader.getInstance().displayImage(newsFeed.getCover(),item.img, ImageLoaderOptions.BigProgressOptions());
//        if(newsFeed.getUser()!=null&& !TextUtils.isEmpty(newsFeed.getUser().getAvatar())){
//            item.avatar.setVisibility(View.VISIBLE);
//            ImageLoader.getInstance().displayImage(newsFeed.getUser()!=null?newsFeed.getUser().getAvatar():"",item.avatar, ImageLoaderOptions.AvatarOptions());
//        }else{
//            item.avatar.setVisibility(View.GONE);
//        }
        item.title.setText(newsFeed.getSubject());
        item.name.setText(newsFeed.getUser()!=null?newsFeed.getUser().getNickname():"");
        item.look.setText(newsFeed.getStat()!=null?newsFeed.getStat().getViews():"0");
        return view;
    }



    private class HeadMessageItem{
        RoundedCornerImageView avatar;
        TextView title,name,look;
        public HeadMessageItem(View view){
            this.avatar= (RoundedCornerImageView) view.findViewById(R.id.head_message_avatar);
            this.title= (TextView) view.findViewById(R.id.head_message_title);
            this.name= (TextView) view.findViewById(R.id.head_message_name);
            this.look= (TextView) view.findViewById(R.id.head_message_look);
        }
    }
    private class MfWWHeadMessageItem{
        ImageView img;
        RoundedCornerImageView avatar;
        TextView title,name,date,look,pinglun;
        private TextView no_blog_message;
        private RelativeLayout blog_message_layout;
        public MfWWHeadMessageItem(View view){
            this.img= (ImageView) view.findViewById(R.id.head_message_img);
            this.avatar= (RoundedCornerImageView) view.findViewById(R.id.head_message_avatar);
            this.title= (TextView) view.findViewById(R.id.head_message_title);
            this.name= (TextView) view.findViewById(R.id.head_message_name);
            this.date= (TextView) view.findViewById(R.id.head_message_date);
            this.look= (TextView) view.findViewById(R.id.head_message_look);
            this.pinglun= (TextView) view.findViewById(R.id.head_message_pinglun);
            this.no_blog_message= (TextView) view.findViewById(R.id.no_blog_message);
            this.blog_message_layout= (RelativeLayout) view.findViewById(R.id.blog_message_layout);
        }
    }
}
