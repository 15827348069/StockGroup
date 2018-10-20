package com.zbmf.groupro.adapter;

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

import com.zbmf.groupro.R;
import com.zbmf.groupro.beans.Ask;
import com.zbmf.groupro.utils.SettingDefaultsManager;

import java.util.List;


/**
 * Created by iMac on 2017/2/21.
 */

public class AskAdapter extends BaseAdapter{

    private List<Ask> mAsks;
    private Context mContext;

    public AskAdapter(Context cxt,List<Ask> asks){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_question, null);
            holder = new ViewHolder();
            holder.tv_ask_date = (TextView) convertView.findViewById(R.id.tv_ask_date);
            holder.tv_question = (TextView) convertView.findViewById(R.id.tv_question);
            holder.tv_answer_date = (TextView) convertView.findViewById(R.id.tv_answer_date);
            holder.tv_answer = (TextView) convertView.findViewById(R.id.tv_answer);
            holder.iv_answer = (ImageView) convertView.findViewById(R.id.iv_answer);
            convertView.setTag(holder);
        }

        Ask ask = mAsks.get(position);
        String target_nickname = ask.getTarget_nickname();
        String content = ask.getAsk_content();
        holder.tv_ask_date.setText(ask.getPosted_at());
        String nickname = ask.getNickname().equals(SettingDefaultsManager.getInstance().NickName()) ?  "我" :ask.getNickname();

        String html = "<font size=\"30px\" color=\"#888888\">"+nickname+"：</font><font size=\"30px\" color=\"#000000\">"/*+"&nbsp;&nbsp;&nbsp;"*/+content+"</font>";
        Spanned text = Html.fromHtml(html);
        holder.tv_question.setText(text);


        if(ask.getPost()!=null && !TextUtils.isEmpty(ask.getPost().getUser_id())){
            content = ask.getPost().getPost_content();
            holder.iv_answer.setVisibility(View.VISIBLE);
            holder.tv_answer.setText(ask.getPost().getPost_content());
            holder.tv_answer_date.setVisibility(View.VISIBLE);
            holder.tv_answer_date.setText(ask.getPost().getPosted_at());
            html =  "<font size=\"30px\" color=\"#888888\">"+target_nickname+"：</font><font size=\"30px\" color=\"#000000\">"+content+"</font>";
        }else{
            holder.tv_answer_date.setVisibility(View.GONE);
            holder.iv_answer.setVisibility(View.GONE);
            html =  "<font size=\"30px\" color=\"#000000\">等待&nbsp;</font><font size=\"30px\" color=\"#888888\">"+target_nickname+"</font><font size=\"30px\" color=\"#000000\">&nbsp;回答</font>";
        }
        text = Html.fromHtml(html);
        holder.tv_answer.setText(text);
        return convertView;
    }

    static class ViewHolder{
        TextView tv_ask_date,tv_question,tv_answer_date,tv_answer;
        ImageView iv_answer;
    }
}
