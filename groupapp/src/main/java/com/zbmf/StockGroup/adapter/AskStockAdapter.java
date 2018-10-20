package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Ask;
import com.zbmf.StockGroup.interfaces.ToGroupClick;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;

import java.util.List;


/**
 * Created by iMac on 2017/2/21.
 */

public class AskStockAdapter extends BaseAdapter{

    private List<Ask> mAsks;
    private Context mContext;
    private ToGroupClick groupClick;

    public void setGroupClick(ToGroupClick groupClick) {
        this.groupClick = groupClick;
    }

    public AskStockAdapter(Context cxt, List<Ask> asks){
        mAsks = asks;
        mContext = cxt;
    }

    @Override
    public int getCount() {
        return mAsks.size();
    }

    @Override
    public Object getItem(int position) {
        return mAsks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_ask_question, null);
            holder = new ViewHolder();
            holder.tv_ask_date = (TextView) convertView.findViewById(R.id.tv_ask_date);
            holder.tv_question = (TextView) convertView.findViewById(R.id.tv_question);
            holder.tv_answer_date = (TextView) convertView.findViewById(R.id.tv_answer_date);
            holder.tv_answer = (TextView) convertView.findViewById(R.id.tv_answer);
            holder.tv_ask_name= (TextView) convertView.findViewById(R.id.tv_ask_name);
            holder.tv_answer_name= (TextView) convertView.findViewById(R.id.tv_answer_name);
            convertView.setTag(holder);
        }

        final Ask ask = mAsks.get(position);
        holder.tv_ask_date.setText(ask.getPosted_at());
        holder.tv_ask_name.setText(ask.getNickname());
        holder.tv_question.setText(ask.getAsk_content());

        holder.tv_answer_name.setText(ask.getPost().getNickname());
        holder.tv_answer_date.setText(ask.getPost().getPosted_at());
        holder.tv_answer.setText(ask.getPost().getPost_content());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupClick!=null){
                    groupClick.toGroup(ask.getPost().getPost_id());
                }
            }
        });
        return convertView;
    }

    static class ViewHolder{
        TextView tv_ask_date,tv_question,tv_ask_name,tv_answer_name,tv_answer_date,tv_answer;
    }
}
