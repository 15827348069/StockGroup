package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Invite;

import java.util.List;

/**
 * Created by xuhao on 2017/7/21.
 */

public class InviteAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Invite>infolist;
    public InviteAdapter(Context context, List<Invite>data){
        this.inflater=LayoutInflater.from(context);
        this.infolist=data;
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
        InviteHolder holder;
        if(view==null){
            view=inflater.inflate(R.layout.item_invite_layout,null);
            holder=new InviteHolder(view);
            view.setTag(holder);
        }else{
            holder= (InviteHolder) view.getTag();
        }
        Invite invite = infolist.get(position);
        holder.reward.setText(invite.getMpay() + "");
        holder.nickname.setText(invite.getNickname());
        holder.time.setText(invite.getInvited_at());
        return view;
    }
    private class InviteHolder{
        TextView nickname,time,reward;
        public InviteHolder(View view){
            this.nickname= (TextView) view.findViewById(R.id.tv_nickname);
            this.time= (TextView) view.findViewById(R.id.tv_reply_time);
            this.reward= (TextView) view.findViewById(R.id.tv_reward);
        }
    }
}
