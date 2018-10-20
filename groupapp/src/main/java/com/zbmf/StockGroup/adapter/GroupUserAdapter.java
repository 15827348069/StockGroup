package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;

/**
 * Created by xuhao on 2016/12/14.
 */

public class GroupUserAdapter extends BaseAdapter {
    private List<Group>infolist;
    private LayoutInflater inflater;
    public GroupUserAdapter(Context context,List<Group>info){
        this.inflater=LayoutInflater.from(context);
        this.infolist=info;
    }
    @Override
    public int getCount() {
        return infolist.size();
    }

    @Override
    public Object getItem(int i) {
        return infolist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GroupUserItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.group_user_adapter,null);
            item=new GroupUserItem(view);
            view.setTag(item);
        }else{
            item= (GroupUserItem) view.getTag();
        }
        Group gb=infolist.get(i);
        item.name.setText(gb.getName());
        item.desc.setText(gb.getDescription());
        ImageLoader.getInstance().displayImage(gb.getAvatar(),item.avatar);
        return view;
    }
    public class GroupUserItem{
        private TextView name;
        private TextView desc;
        private RoundedCornerImageView avatar;
        public GroupUserItem(View view){
            name= (TextView) view.findViewById(R.id.group_user_name);
            desc= (TextView) view.findViewById(R.id.group_user_desc);
            avatar= (RoundedCornerImageView) view.findViewById(R.id.group_user_avatar);
        }
    }
}
