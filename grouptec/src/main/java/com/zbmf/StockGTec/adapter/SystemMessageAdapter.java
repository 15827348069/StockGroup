package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.SystemMessage;
import com.zbmf.StockGTec.utils.ImageLoaderOptions;
import com.zbmf.StockGTec.view.RoundedCornerImageView;

import java.util.List;

/**
 * Created by xuhao on 2017/3/6.
 */

public class SystemMessageAdapter extends BaseAdapter {
    private List<SystemMessage> infolist;
    private LayoutInflater inflater;
    public SystemMessageAdapter(Context context,List<SystemMessage>info){
        this.inflater=LayoutInflater.from(context);
        this.infolist=info;
    }
    @Override
    public int getCount() {
        return infolist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SystemMessageItem item=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.message_item_layout,null);
            item=new SystemMessageItem(convertView);
            convertView.setTag(item);
        }else{
            item= (SystemMessageItem) convertView.getTag();
        }
        SystemMessage sm=infolist.get(position);
        item.type.setText(sm.getMessage_type());
        item.countent.setText(sm.getCountent());
        item.message_data.setText(sm.getDate());
        ImageLoader.getInstance().displayImage(sm.getAvatar(),item.avatar, ImageLoaderOptions.AvatarOptions());
        return convertView;
    }
    private class SystemMessageItem{
        TextView type,countent,message_data;
        RoundedCornerImageView avatar;
        public SystemMessageItem(View view){
            this.type= (TextView) view.findViewById(R.id.message_type);
            this.countent= (TextView) view.findViewById(R.id.system_message_countent);
            this.avatar= (RoundedCornerImageView) view.findViewById(R.id.message_avatar);
            this.message_data= (TextView) view.findViewById(R.id.message_data);
        }
    }
}
