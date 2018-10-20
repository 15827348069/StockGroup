package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.interfaces.OnFansClick;
import com.zbmf.StockGroup.utils.DisplayUtil;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;

import java.util.List;

/**
 * Created by xuhao on 2017/8/28.
 */

public class FansDiscountsAdapter extends BaseAdapter {
    private List<Group>infolist;
    private LayoutInflater inflater;
    private OnFansClick addFans;
    private int bottom,top;

    public void setAddFans(OnFansClick addFans) {
        this.addFans = addFans;
    }

    public FansDiscountsAdapter(Context context, List<Group>info){
        this.inflater=LayoutInflater.from(context);
        this.infolist=info;
        bottom=DisplayUtil.dip2px(context,20);
        top=DisplayUtil.dip2px(context,15);
    }
    @Override
    public int getCount() {
        return infolist.size();
    }

    @Override
    public Object getItem(int position) {
        return infolist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder=null;
        if(view==null){
            view=inflater.inflate(R.layout.item_fans_discounts,null);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        Group group= (Group) getItem(position);
        holder.name.setText(group.getNick_name());
        holder.careNum.setText(group.getFollow_num()+"人关注");
        holder.activity.setText(group.getCoupon());
        holder.recommend_desc.setText(group.getContent());
//        ViewFactory.imgCircleView(parent.getContext(),group.getAvatar(),holder.imageView);
        ImageLoader.getInstance().displayImage(group.getAvatar(),holder.imageView, ImageLoaderOptions.AvatarOptions());
        holder.commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addFans!=null){
                    addFans.onFans(((Group) getItem(position)).getId());
                }
            }
        });
        holder.to_group_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addFans!=null){
                    addFans.onGroup(((Group) getItem(position)).getId());
                }
            }
        });
        if(position==infolist.size()-1){
            view.setPadding(top,top,top,bottom);
        }else{
            view.setPadding(top,top,top,0);
        }
        return view;
    }
    private class ViewHolder{
        ImageView imageView;
        TextView name,careNum,activity,commit,recommend_desc;
        LinearLayout to_group_layout;
        public ViewHolder(View view){
            this.imageView= (ImageView) view.findViewById(R.id.recommend_item_avatar);
            this.name= (TextView) view.findViewById(R.id.recommend_item_name);
            this.careNum= (TextView) view.findViewById(R.id.recommend_follownum);
            this.activity= (TextView) view.findViewById(R.id.tv_activity);
            this.commit= (TextView) view.findViewById(R.id.tv_discounts);
            this.recommend_desc= (TextView) view.findViewById(R.id.recommend_desc);
            this.to_group_layout= (LinearLayout) view.findViewById(R.id.to_group_layout);
        }
    }
}
