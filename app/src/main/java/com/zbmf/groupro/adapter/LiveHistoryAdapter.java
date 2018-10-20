package com.zbmf.groupro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.LiveMessage;
import com.zbmf.groupro.utils.ImageLoaderOptions;
import com.zbmf.groupro.utils.MessageType;
import com.zbmf.groupro.view.MyTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xuhao on 2017/2/7.
 */

public class LiveHistoryAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<LiveMessage>info;
    private MyTextView.OnTextClickListener listener;
    private View.OnClickListener onClickListener;
    private boolean is_tf;
    DateFormat df = new SimpleDateFormat("HH:mm");
    private Context context;
    public LiveHistoryAdapter(Context context, List<LiveMessage>infolist,MyTextView.OnTextClickListener listener,View.OnClickListener onClickListener,boolean is_tf){
        this.inflater=LayoutInflater.from(context);
        this.info=infolist;
        this.listener=listener;
        this.context=context;
        this.onClickListener=onClickListener;
        this.is_tf=is_tf;
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
    public void add_tf(boolean add_tf){
        is_tf=add_tf;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HistoryItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.live_history_item,null);
            item=new HistoryItem(view);
            view.setTag(item);
        }else{
           item= (HistoryItem) view.getTag();
        }
        LiveMessage message=info.get(i);
        item.live_history_date.setText(df.format(new Date(message.getMessage_time())));
        switch (message.getMessage_type()){
            case MessageType.FANS:
                if(!is_tf){
                    item.live_history_content.setMessageText(context.getResources().getString(R.string.add_to_tf));
                    item.live_history_img.setVisibility(View.GONE);
                }else{
                    if(message.getImportent()==0){
                        item.live_history_content.getPaint().setFakeBoldText(false);
                        item.live_history_content.setTextColor(context.getResources().getColor(R.color.black));
                    }else{
                        item.live_history_content.getPaint().setFakeBoldText(true);
                        item.live_history_content.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    }
                    item.live_history_content.setMessageText(message.getMessage_countent());
                    if(message.getMessage_or_img()!=null&&message.getMessage_or_img().equals(MessageType.IMG)){
                        item.live_history_img.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(message.getThumb(),item.live_history_img, ImageLoaderOptions.BigProgressOptions());
                        item.live_history_img.setTag(message.getImg_url());
                        item.live_history_img.setOnClickListener(onClickListener);
                    }else{
                        item.live_history_img.setVisibility(View.GONE);
                    }
                }
                break;
            case MessageType.CHAT:
                if(message.getImportent()==0){
                    item.live_history_content.getPaint().setFakeBoldText(false);
                    item.live_history_content.setTextColor(context.getResources().getColor(R.color.black));
                }else{
                    item.live_history_content.getPaint().setFakeBoldText(true);
                    item.live_history_content.setTextColor(context.getResources().getColor(R.color.colorAccent));
                }
                item.live_history_content.setOnClickListener(null);
                item.live_history_content.setTextClickListener(listener);
                item.live_history_content.setMessageText(message.getMessage_countent());
                if(message.getMessage_or_img()!=null&&message.getMessage_or_img().equals(MessageType.IMG)){
                    item.live_history_img.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(message.getThumb(),item.live_history_img, ImageLoaderOptions.BigProgressOptions());
                    item.live_history_img.setTag(message.getImg_url());
                    item.live_history_img.setOnClickListener(onClickListener);
                }else{
                    item.live_history_img.setVisibility(View.GONE);
                }
                break;
        }
        return view;
    }
    private class HistoryItem{
        TextView live_history_date;
        MyTextView live_history_content;
        ImageView live_history_img;
        public HistoryItem(View view){
            this.live_history_date= (TextView) view.findViewById(R.id.live_history_date);
            this.live_history_content= (MyTextView) view.findViewById(R.id.live_history_content);
            this.live_history_img= (ImageView) view.findViewById(R.id.live_history_img);
        }
    }
}
