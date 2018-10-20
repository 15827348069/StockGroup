package com.zbmf.StockGroup.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.NoReadMsgBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pq
 * on 2018/7/5.
 */

public class NoReadMsgAdapter extends ListAdapter<NoReadMsgBean> {

    public NoReadMsgAdapter(Context context) {super(context);}
    private SkipNextView mSkipNextView;
    public void setSkipNextView(SkipNextView skipNextView){
        mSkipNextView=skipNextView;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (holder==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.no_msg_item,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=((ViewHolder) convertView.getTag());
        }
        final NoReadMsgBean noReadMsgBean = getItem(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(Long.parseLong(noReadMsgBean.getCreated_at())*1000L);
        String time = dateFormat.format(date);
        holder.mCreate_time.setText(time);
        holder.mDoContent.setText(noReadMsgBean.getContent());
        holder.mUserNick.setText(noReadMsgBean.getNick_name());
        holder.skipNextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSkipNextView!=null){
                    mSkipNextView.skipNextView(noReadMsgBean,noReadMsgBean.getType());
                }
            }
        });
        return convertView;
    }

    public class ViewHolder{

        private final TextView mCreate_time;
        private final TextView mUserNick;
        private final TextView mDoContent;
        private final LinearLayout skipNextView;

        ViewHolder(View view){
            mCreate_time = view.findViewById(R.id.create_time);
            mUserNick = view.findViewById(R.id.userNick);
            mDoContent = view.findViewById(R.id.doContent);
            skipNextView = view.findViewById(R.id.skipNextView);
        }
    }

    public interface SkipNextView{
        void skipNextView(NoReadMsgBean noReadMsgBean,int type);
    }
}
