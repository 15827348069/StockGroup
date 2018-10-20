package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.StockAskBean;
import com.zbmf.worklibrary.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pq
 * on 2018/3/31.
 */

public class StockAskAdapter extends ListAdapter<StockAskBean.Result.Asks> {

    private ViewHolder mHolder;

    public StockAskAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_ask_question;
    }

    @Override
    public View getHolderView(int position, View convertView, StockAskBean.Result.Asks asks) {
        mHolder = (ViewHolder) convertView.getTag();
        if (mHolder==null){
            mHolder=new ViewHolder(convertView);
            convertView.setTag(mHolder);
        }
        String question_html = "<font size=\"30px\" color=\"#ea3535\">@ " + asks.getStock_name() +
                "(" + asks.getSymbol() + ") " + "</font><font size=\"30px\" color=\"#000000\">" +
                asks.getAsk_content() + "</font>";
        mHolder.tvQuestion.setText(Html.fromHtml(question_html));
        mHolder.tvAskDate.setText(asks.getAsk_at());
        mHolder.tvAskName.setText(asks.getAsk_name());
        if (!asks.getReply_content().isEmpty()) {
            mHolder.tvAnswerDate.setText(asks.getReply_at());
            mHolder.tvAnswerName.setText(asks.getReply_name());
            mHolder.tvAnswer.setText(asks.getReply_content());
            mHolder.ivAnswer.setVisibility(View.VISIBLE);
            mHolder.llAnswer.setVisibility(View.VISIBLE);
            mHolder.tvAnswerDate.setVisibility(View.VISIBLE);
        } else {
            mHolder.llAnswer.setVisibility(View.GONE);
            mHolder.tvAnswerDate.setVisibility(View.GONE);
            mHolder.tvAnswer.setVisibility(View.GONE);
            String html = "<font size=\"30px\" color=\"#000000\">等待&nbsp;</font><font size=\"30px\" color=\"#888888\">"
                    + asks.getReply_name() + " 董秘</font><font size=\"30px\" color=\"#000000\">&nbsp;回答</font>";
            mHolder.tvAnswer.setText(Html.fromHtml(html));
        }
        return convertView;
    }
    public class ViewHolder{
        @BindView(R.id.tv_ask_date)
        TextView tvAskDate;
        @BindView(R.id.tv_question)
        TextView tvQuestion;
        @BindView(R.id.tv_answer_date)
        TextView tvAnswerDate;
        @BindView(R.id.tv_answer)
        TextView tvAnswer;
        @BindView(R.id.tv_ask_name)
        TextView tvAskName;
        @BindView(R.id.tv_answer_name)
        TextView tvAnswerName;
        @BindView(R.id.iv_answer)
        ImageView ivAnswer;
        @BindView(R.id.ll_answer)
        LinearLayout llAnswer;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
