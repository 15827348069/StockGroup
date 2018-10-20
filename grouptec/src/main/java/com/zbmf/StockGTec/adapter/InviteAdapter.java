package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.Invite;

import java.util.List;

public class InviteAdapter extends BaseAdapter {

    private List<Invite> list;
    private Context mContext;

    public InviteAdapter(Context cxt, List<Invite> list) {
        this.list = list;
        mContext = cxt;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_invite, null);
            holder = new ViewHolder();
            holder.tv_mpay = (TextView) convertView.findViewById(R.id.tv_mpay);
            holder.tv_invited_at = (TextView) convertView.findViewById(R.id.tv_invited_at);
            holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            convertView.setTag(holder);
        }
        Invite invite = list.get(position);
        holder.tv_mpay.setText(invite.getMpay() + "");
        holder.tv_nickname.setText(invite.getNickname());
        holder.tv_invited_at.setText(invite.getInvited_at().substring(0, 10));
        return convertView;
    }

    static class ViewHolder {
        TextView tv_mpay, tv_invited_at, tv_nickname;
    }

}
