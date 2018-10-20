package com.zbmf.groupro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.ImageLoaderOptions;
import com.zbmf.groupro.view.RoundedCornerImageView;

import java.util.List;

/**
 * Created by xuhao on 2017/2/24.
 */

public class RecommendAdapter extends BaseAdapter {
    private List<Group>infolist;
    private LayoutInflater inflater;
    private CompoundButton.OnCheckedChangeListener listener;
    public RecommendAdapter(Context context, List<Group>info, CompoundButton.OnCheckedChangeListener listener){
        this.infolist=info;
        this.inflater=LayoutInflater.from(context);
        this.listener=listener;
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
        RecommendItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.recommend_item,null);
            item=new RecommendItem(view);
            view.setTag(item);
        }else{
            item= (RecommendItem) view.getTag();
        }
        Group group=infolist.get(i);
        item.name.setText(group.getNick_name());
        item.desc.setText(group.getDescription());
        ImageLoader.getInstance().displayImage(group.getAvatar(),item.avatar, ImageLoaderOptions.AvatarOptions());
        if(group.is_recommend()){
            item.care_button.setTag(i);
            item.care_button.setOnCheckedChangeListener(listener);
            item.care_button.setText("+关注");
        }else{
            item.care_button.setText("已关注");
            item.care_button.setOnCheckedChangeListener(null);
        }
        item.care_button.setChecked(!group.is_recommend());
        item.care_button.setEnabled(group.is_recommend());
        return view;
    }
    private class RecommendItem{
        TextView name,desc;
        CheckBox care_button;
        RoundedCornerImageView avatar;
        public RecommendItem(View view){
            name= (TextView) view.findViewById(R.id.recommend_item_name);
            desc= (TextView) view.findViewById(R.id.recommend_item_desc);
            care_button= (CheckBox) view.findViewById(R.id.recommend_item_button);
            avatar= (RoundedCornerImageView) view.findViewById(R.id.recommend_item_avatar);
        }
    }
}
