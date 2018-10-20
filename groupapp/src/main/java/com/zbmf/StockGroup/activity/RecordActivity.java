package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.RecordAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.Record;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 获奖记录
 */
public class RecordActivity extends BaseActivity{

    private TextView tv_tip,all_money, all_shouyi, arrow, match_number;
    private ListViewForScrollView listView;
    private PullToRefreshScrollView myscrllview;
    private RecordAdapter adapter;
    private List<Record> list = new ArrayList<Record>();
    private MatchInfo mb;
    private DecimalFormat df = new DecimalFormat("######0.00");
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private int page,pages;
    private boolean isRush;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_record;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.record));
        listView = getView(R.id.content_view);
        myscrllview = getView(R.id.myscrllview);
        all_money =  getView(R.id.all_money);
        all_shouyi =  getView(R.id.all_shouyi);
        arrow = getView(R.id.arrow_textview);
        match_number =  getView(R.id.match_account);
        tv_tip=getView(R.id.tv_tip);
        tv_tip.setText(R.string.record_tip);
        listView.setFocusable(false);
        myscrllview.smoothScrollTo(0, 20);
        adapter = new RecordAdapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void initData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mb = (MatchInfo) bundle.getSerializable(IntentKey.MATCH_BAEN);
        }
        if (mb != null) {
            setData();
        }
        getRecord();
    }

    @Override
    public void addListener() {
        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page=1;
                isRush=true;
                getRecord();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page+=1;
                getRecord();
            }
        });
    }

    private void setData() {
        all_money.setText(currencyFormat.format(mb.getTotal()));
        if (mb.getYield() >= 0) {
            all_shouyi.setTextColor(getResources().getColor(R.color.match_all_money));
            all_shouyi.setText("+" + df.format(mb.getYield() * 100) + "%");
        } else {
            all_shouyi.setTextColor(getResources().getColor(R.color.match_all_money_green));
            all_shouyi.setText(df.format(mb.getYield() * 100) + "%");
        }
        arrow.setText(mb.getTotal_rank().equals("0")?mb.getCount_players()+"":mb.getTotal_rank());
        match_number.setText(getResources().getString(R.string.record_count, mb.getRecords()));
    }
    private void getRecord(){
        WebBase.getWinRecords(page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                myscrllview.onRefreshComplete();
                Record record= JSONParse.getWinRecords(obj);
                page=record.getPage();
                pages=record.getPages();
                if(isRush){
                    isRush=false;
                    list.clear();
                }
                list.addAll(record.getRecords());
                if(page==pages&&list.size()==0){
                    tv_tip.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }else{
                    tv_tip.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
                myscrllview.onRefreshComplete();
            }
        });
    }
}
