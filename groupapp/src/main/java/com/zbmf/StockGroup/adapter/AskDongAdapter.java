package com.zbmf.StockGroup.adapter;

import android.app.Activity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.AskBean;

/**
 * Created by xuhao on 2018/1/30.
 */

public class AskDongAdapter extends ListAdapter<AskBean> {
    public AskDongAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_ask_question, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AskBean askBean = getItem(position);
        String question_html = "<font size=\"30px\" color=\"#ea3535\">@ " + askBean.getStock_name() + "(" + askBean.getSymbol() + ") " + "</font><font size=\"30px\" color=\"#000000\">" + askBean.getAsk_content() + "</font>";
        holder.tv_question.setText(Html.fromHtml(question_html));
        holder.tv_ask_date.setText(askBean.getAsk_at());
        holder.tv_ask_name.setText(askBean.getAsk_name());
        if (!askBean.getReply_content().isEmpty()) {
            holder.tv_answer_date.setText(askBean.getReply_at());
            holder.tv_answer_name.setText(askBean.getReply_name());
            holder.tv_answer.setText(askBean.getReply_content());
            holder.iv_answer.setVisibility(View.VISIBLE);
            holder.ll_answer.setVisibility(View.VISIBLE);
            holder.tv_answer_date.setVisibility(View.VISIBLE);
        } else {
            holder.ll_answer.setVisibility(View.GONE);
            holder.tv_answer_date.setVisibility(View.GONE);
            holder.iv_answer.setVisibility(View.GONE);
            String html = "<font size=\"30px\" color=\"#000000\">等待&nbsp;</font><font size=\"30px\" color=\"#888888\">" + askBean.getReply_name() + " 董秘</font><font size=\"30px\" color=\"#000000\">&nbsp;回答</font>";
            holder.tv_answer.setText(Html.fromHtml(html));
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_ask_date, tv_question, tv_ask_name, tv_answer_name, tv_answer_date, tv_answer;
        ImageView iv_answer;
        private LinearLayout ll_answer;

        public ViewHolder(View view) {
            tv_ask_date = (TextView) view.findViewById(R.id.tv_ask_date);
            tv_question = (TextView) view.findViewById(R.id.tv_question);
            tv_answer_date = (TextView) view.findViewById(R.id.tv_answer_date);
            tv_answer = (TextView) view.findViewById(R.id.tv_answer);
            tv_ask_name = (TextView) view.findViewById(R.id.tv_ask_name);
            tv_answer_name = (TextView) view.findViewById(R.id.tv_answer_name);
            iv_answer = (ImageView) view.findViewById(R.id.iv_answer);
            ll_answer= (LinearLayout) view.findViewById(R.id.ll_answer);
        }
    }
}
