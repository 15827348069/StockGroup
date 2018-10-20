package com.zbmf.groupro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.BlogBean;
import com.zbmf.groupro.utils.ImageLoaderOptions;
import com.zbmf.groupro.view.RoundedCornerImageView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by xuhao on 2017/2/20.
 */

public class HeadMessageAdapter extends BaseAdapter {
    private LayoutInflater inflaterl;
    private List<BlogBean>info;
    DecimalFormat    df   = new DecimalFormat("######0.00");
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
                ImageLoader.getInstance().displayImage(bb.getImg(),item.img, ImageLoaderOptions.BigProgressOptions());
                ImageLoader.getInstance().displayImage(bb.getAvatar(),item.avatar, ImageLoaderOptions.AvatarOptions());
                item.title.setText(bb.getTitle());
                item.name.setText(bb.getName());
                item.date.setText(bb.getDate());
                if(bb.getLook_number()!=null){
                    item.look.setVisibility(View.VISIBLE);
                    item.look.setText(getNumber(bb.getLook_number()));
                }else{
                    item.look.setVisibility(View.GONE);
                }
                if(bb.getPinglun()!=null){
                    item.pinglun.setVisibility(View.VISIBLE);
                    item.pinglun.setText(getNumber(bb.getPinglun()));
                }else{
                    item.pinglun.setVisibility(View.GONE);
                }
            }
        return view;
    }
    public String getNumber(String number){
        Double num=Double.valueOf(number);
        if(num>10000){
            return df.format(num/10000)+"万";
        }else{
            return number;
        }
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
