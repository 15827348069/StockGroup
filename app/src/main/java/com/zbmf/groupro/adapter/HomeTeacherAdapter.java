package com.zbmf.groupro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.ImageLoaderOptions;
import com.zbmf.groupro.view.RoundedCornerImageView;

import java.util.List;

/**
 * Created by xuhao on 2017/2/20.
 */

public class HomeTeacherAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Group>info;
    public HomeTeacherAdapter(Context context,List<Group>infolist){
        this.inflater=LayoutInflater.from(context);
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
        HomeTeacherItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.home_teacher_item,null);
            item=new HomeTeacherItem(view);
            view.setTag(item);
        }else{
            item= (HomeTeacherItem) view.getTag();
        }
        Group group=info.get(i);
        ImageLoader.getInstance().displayImage(group.getAvatar(),item.avatar, ImageLoaderOptions.AvatarOptions());
        item.name.setText(group.getNick_name());
        item.desc.setText(group.getDescription());
        return view;
    }
    private class HomeTeacherItem{
        RoundedCornerImageView avatar;
        TextView name,desc;
        private HomeTeacherItem(View view){
            avatar= (RoundedCornerImageView) view.findViewById(R.id.home_teacher_avatar);
            name= (TextView) view.findViewById(R.id.home_teacher_name);
            desc= (TextView) view.findViewById(R.id.home_teacher_desc);
        }
    }
}
