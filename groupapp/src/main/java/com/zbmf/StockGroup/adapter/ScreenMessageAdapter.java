package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.ScreenMessage;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.view.MyTextView;

import java.util.List;

/**
 * Created by xuhao on 2017/10/18.
 */

public class ScreenMessageAdapter extends BaseAdapter {
    private List<ScreenMessage>infolist;
    private LayoutInflater inflater;
    private MyTextView.OnTextClickListener onTextClickListener;
    public void setOnTextClickListener(MyTextView.OnTextClickListener listener){
        this.onTextClickListener=listener;
    }
    public ScreenMessageAdapter(Context context,List<ScreenMessage>info){
        this.inflater=LayoutInflater.from(context);
        this.infolist=info;
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if(view==null){
            view=inflater.inflate(R.layout.item_message_layout,null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        ScreenMessage message=infolist.get(position);
        viewHolder.tv_message_name.setText(message.getSubject());
        viewHolder.tv_message_date.setText(DateUtil.getNewChatTime(message.getCreated_at()*1000));
        viewHolder.tv_message_from.setText("消息来自于："+message.getScreen_name());
        viewHolder.tv_screen_message_content.setTextClickListener(onTextClickListener);
        viewHolder.tv_screen_message_content.setScreenMessage(message.getContent(),message.getScreen_id());
        return view;
    }
    private class ViewHolder{
        TextView tv_message_date,tv_message_name,tv_message_from;
        MyTextView tv_screen_message_content;
        public  ViewHolder(View view){
            this.tv_message_date= (TextView) view.findViewById(R.id.tv_message_date);
            this.tv_message_name= (TextView) view.findViewById(R.id.tv_message_name);
            this.tv_message_from= (TextView) view.findViewById(R.id.tv_message_from);
            this.tv_screen_message_content= (MyTextView) view.findViewById(R.id.tv_screen_message_content);
        }
    }
}
