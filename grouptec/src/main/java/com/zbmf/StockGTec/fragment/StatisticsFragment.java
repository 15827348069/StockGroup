package com.zbmf.StockGTec.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StockGTec.R;

/**
 * Created by xuhao on 2017/3/2.
 */

public class StatisticsFragment extends BaseFragment {
    private TextView statiscs_text;
    private String select_count;
                    //访客，浏览量，博文，收到提问，回答，投票，新增粉丝，新增铁粉，新增年份，收到礼物
    private TextView visitor_text,page_view_text,blog_count,ask_text,answer_text,vote_text,new_fans,new_tie_fans,new_year_fans,gift_text;
                    //访客魔方宝，投票魔方宝，新增铁粉魔方宝，年粉魔方宝，礼物魔方宝
    private TextView visitor_mfb,vote_mfb,new_tie_fans_mfb,new_year_fans_mfb,gift_mfb;
    private static final String ARG_PARAM1 = "SELECT_COUNT";
    public static  StatisticsFragment newIntence(String select_count){
        StatisticsFragment sf=new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, select_count);
        sf.setArguments(args);
        return sf;
    }
    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.statistics_layout,null);
    }

    @Override
    protected void initView() {
        visitor_text=getView(R.id.visitor_text);
        page_view_text=getView(R.id.page_view_text);
        blog_count=getView(R.id.blog_count);
        ask_text=getView(R.id.ask_text);
        answer_text=getView(R.id.answer_text);
        vote_text=getView(R.id.vote_text);
        new_fans=getView(R.id.new_fans);
        new_tie_fans=getView(R.id.new_tie_fans);
        new_year_fans=getView(R.id.new_year_fans);
        gift_text=getView(R.id.gift_text);

        visitor_mfb=getView(R.id.visitor_mfb);
        vote_mfb=getView(R.id.vote_mfb);
        new_tie_fans_mfb=getView(R.id.new_tie_fans_mfb);
        new_year_fans_mfb=getView(R.id.new_year_fans_mfb);
        gift_mfb=getView(R.id.gift_mfb);

    }

    @Override
    protected void initData() {
        if(getArguments()!=null){
            select_count=getArguments().getString(ARG_PARAM1);
        }
        visitor_text.setText(select_count);
        page_view_text.setText(select_count);
        blog_count.setText(select_count);
        ask_text.setText(select_count);
        answer_text.setText(select_count);
        vote_text.setText(select_count);
        new_fans.setText(select_count);
        new_tie_fans.setText(select_count);
        new_year_fans.setText(select_count);
        gift_text.setText(select_count);
    }
}
