package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.User;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;


/**
 * Created by xuhao on 2017/1/20.
 */

public class ChangeUserAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<User>infolist;
    public ChangeUserAdapter(Context context,List<User>info){
        this.inflater=LayoutInflater.from(context);
        this.infolist=info;
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
        Item item=null;
        if(view==null){
           view=inflater.inflate(R.layout.change_user_list,null);
            item=new Item(view);
            view.setTag(item);
        }else{
            item= (Item) view.getTag();
        }
        User user=infolist.get(i);
        item.name.setText(user.getNickname());
//        ViewFactory.imgCircleView(viewGroup.getContext(),user.getAvatar(),item.avatar);
        ImageLoader.getInstance().displayImage(user.getAvatar(),item.avatar, ImageLoaderOptions.AvatarOptions());
        if(user.getUser_id().equals(SettingDefaultsManager.getInstance().UserId())){
            item.check.setVisibility(View.VISIBLE);
        }else{
            item.check.setVisibility(View.GONE);
        }
        return view;
    }
    private class Item{
        TextView name;
        ImageView check;
        RoundedCornerImageView avatar;
        public Item(View view){
            name= (TextView) view.findViewById(R.id.change_user_name);
            check= (ImageView) view.findViewById(R.id.change_user_check_box);
            avatar= (RoundedCornerImageView) view.findViewById(R.id.change_user_avatar);
        }
    }

}
