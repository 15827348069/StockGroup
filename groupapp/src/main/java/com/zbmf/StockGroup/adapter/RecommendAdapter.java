package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;

/**
 * Created by xuhao on 2017/2/24.
 */

public class RecommendAdapter extends BaseAdapter {
    private List<Group>infolist;
    private LayoutInflater inflater;
    private OnCareClink onCareClink;
    private Drawable drawable;
    private int white,gray;
    public RecommendAdapter(Context context, List<Group>info){
        this.infolist=info;
        this.inflater=LayoutInflater.from(context);
        drawable= context.getResources().getDrawable(R.drawable.icon_left_care);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        white=context.getResources().getColor(R.color.white);
        gray=context.getResources().getColor(R.color.black_66);
    }

    public void setOnCareClink(OnCareClink onCareClink) {
        this.onCareClink = onCareClink;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        RecommendItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.recommend_item,null);
            item=new RecommendItem(view);
            view.setTag(item);
        }else{
            item= (RecommendItem) view.getTag();
        }
        final Group group=infolist.get(i);
        item.name.setText(group.getNick_name());
        item.desc.setText(group.getFollow_num()+"人关注");
        item.recommend_style.setText(group.getStyle());
        item.recommend_tag.setText(group.getTags());
        item.care_button.setSelected(!group.is_recommend());
//        ViewFactory.imgCircleView(viewGroup.getContext(),group.getAvatar(),item.avatar);
        ImageLoader.getInstance().displayImage(group.getAvatar(),item.avatar, ImageLoaderOptions.AvatarOptions());
        if(!group.is_recommend()){
            item.care_button.setText("+关注");
            item.care_button.setTextColor(white);
            item.care_button.setCompoundDrawables(null,null,null,null);
            item.care_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onCareClink!=null){
                        onCareClink.onCareClink(i);
                    }
                }
            });
        }else{
            item.care_button.setTextColor(gray);
            item.care_button.setCompoundDrawables(drawable,null,null,null);
            item.care_button.setText("已关注");
            item.care_button.setOnClickListener(null);
        }
        return view;
    }
    private class RecommendItem{
        TextView name,desc;
        TextView care_button,recommend_tag,recommend_style;
        RoundedCornerImageView avatar;
        public RecommendItem(View view){
            name= (TextView) view.findViewById(R.id.recommend_item_name);
            desc= (TextView) view.findViewById(R.id.recommend_follownum);
            recommend_style= (TextView) view.findViewById(R.id.recommend_style);
            recommend_tag= (TextView) view.findViewById(R.id.recommend_tag);
            care_button= (TextView) view.findViewById(R.id.recommend_item_button);
            avatar= (RoundedCornerImageView) view.findViewById(R.id.recommend_item_avatar);
        }
    }
    public interface OnCareClink{
        void onCareClink(int position);
    }
}
