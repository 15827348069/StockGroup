package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by xuhao on 2017/2/20.
 */

public class HeadMessageAdapter extends BaseAdapter {
    private LayoutInflater inflaterl;
    private List<BlogBean>info;
    DecimalFormat    df   = new DecimalFormat("######0");
    public HeadMessageAdapter(Context context,List<BlogBean>infolist){
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
            view=inflaterl.inflate(R.layout.mf_head_message_item,null);
            item=new HeadMessageItem(view);
            view.setTag(item);
        }else{
            item= (HeadMessageItem) view.getTag();
        }
        BlogBean bb=info.get(i);
            if(bb.is_delete()){
                item.no_blog_message.setVisibility(View.VISIBLE);
                item.blog_message_layout.setVisibility(View.GONE);
                item.pinglun.setVisibility(View.GONE);
                item.look.setVisibility(View.GONE);
            }else{
                item.no_blog_message.setVisibility(View.GONE);
                item.blog_message_layout.setVisibility(View.VISIBLE);
//                Glide.with(viewGroup.getContext()).load(bb.getImg()).into(item.img).
//                        onLoadStarted(viewGroup.getContext().getResources().getDrawable(R.drawable.default_image));
//                Glide.with(viewGroup.getContext()).load(bb.getAvatar()).into(item.avatar).
//                        onLoadStarted(viewGroup.getContext().getResources().getDrawable(R.drawable.avatar_default));
                ImageLoader.getInstance().displayImage(bb.getImg(),item.img, ImageLoaderOptions.BigProgressOptions());
                ImageLoader.getInstance().displayImage(bb.getAvatar(),item.avatar, ImageLoaderOptions.AvatarOptions());
                item.title.setText(bb.getTitle());
                item.name.setText(bb.getName());
                item.date.setText(bb.getDate());
                if(bb.getLook_number()!=null){
                    item.look.setVisibility(View.VISIBLE);
                    item.look.setText(bb.getLook_number());
                }else{
                    item.look.setVisibility(View.GONE);
                }
            }
        return view;
    }

    private class HeadMessageItem{
        ImageView img;
        RoundedCornerImageView avatar;
        TextView title,name,date,look,pinglun;
        private TextView no_blog_message;
        private RelativeLayout blog_message_layout;
        public HeadMessageItem(View view){
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
