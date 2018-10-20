package com.zbmf.StockGroup.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.LeftTitleAdapter;
import com.zbmf.StockGroup.adapter.RightContentAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.StockholdsBean;
import com.zbmf.StockGroup.beans.Traders;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.DisplayUtil;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;
import com.zbmf.StockGroup.view.SyncHorizontalScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MatchHoldActivity extends BaseActivity {
    private PullToRefreshScrollView myscrllview;
    private ListViewForScrollView leftTitleListView, rightContentListView;
    private SyncHorizontalScrollView rightTitleScrollView, rightContentScrollView;
    private List<StockholdsBean> list = new ArrayList<StockholdsBean>();
    String[] rightTitles = {"持仓股数", "可用股", "成本价", "当前价", "浮动盈亏", "盈亏比率"};
    private RightContentAdapter adapter;
    private LeftTitleAdapter adapter1;
    private MatchInfo matchBean;
    private boolean isRush;
    private int page,pages;
    private int flag;
    private Traders traders;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_match_trusts;
    }

    @Override
    public void initView() {
        setupView();
    }

    @Override
    public void initData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            matchBean= (MatchInfo) bundle.getSerializable(IntentKey.MATCH_BAEN);
            flag=bundle.getInt(IntentKey.FLAG);
            traders= (Traders) bundle.getSerializable(IntentKey.TRADER);
            initTitle(flag==0?getString(R.string.my_hold):"持仓股票");
            myscrllview.setMode(flag==0?PullToRefreshBase.Mode.BOTH: PullToRefreshBase.Mode.PULL_FROM_START);
        }
        page=1;
        getData();
    }

    @Override
    public void addListener() {
        if(flag==0){
            myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                    isRush=true;
                    getData();
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                    page+=1;
                    getData();
                }
            });
        }else{
            myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
                @Override
                public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                    isRush=true;
                    getData();
                }
            });
        }

        // 设置左侧股票名称触发事件
        leftTitleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                showOpreateDialog(list.get(arg2));
            }
        });
        // 设置右侧股票内容触发事件
        rightContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                showOpreateDialog(list.get(arg2));
            }
        });
    }

    private void getData() {
        if(flag==0){
            getHolder();
        }else{
            gettraderHolds();
        }
    }

    private void setupView() {
        myscrllview = (PullToRefreshScrollView)findViewById(R.id.myscrllview);
        rightTitleScrollView = (SyncHorizontalScrollView) findViewById(R.id.matchOperate_rightTitleScrollView);
        rightContentScrollView = (SyncHorizontalScrollView) findViewById(R.id.matchOperate_rightContentScrollView);
        rightTitleScrollView.setScrollView(rightContentScrollView);
        rightContentScrollView.setScrollView(rightTitleScrollView);


        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < rightTitles.length; i++) {
            View convertView = LayoutInflater.from(MatchHoldActivity.this).inflate(R.layout.trusts_single_cell, null);
            TextView title = (TextView) convertView.findViewById(R.id.trusts_cell_name);

            title.setText(rightTitles[i]);
            layout.addView(convertView);
        }
        rightTitleScrollView.addView(layout);

        leftTitleListView = (ListViewForScrollView) findViewById(R.id.matchOperate_leftTitleListView);
        rightContentListView = (ListViewForScrollView) findViewById(R.id.matchOperate_rightContentListView);
        adapter = new RightContentAdapter(this);
        adapter.setList(list);
        adapter1 = new LeftTitleAdapter(this);
        adapter1.setList(list);

        rightContentListView.setAdapter(adapter);
        leftTitleListView.setAdapter(adapter1);

    }
    private void gettraderHolds(){
        WebBase.traderHolds(traders.getUser_id(), new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                myscrllview.onRefreshComplete();
                if(isRush){
                    isRush=false;
                    list.clear();
                }
                if(obj.has("holds")){
                    list.addAll(JSONParse.getHolder(obj.optJSONArray("holds")));
                }
                if(list.size()>0){
                    adapter.notifyDataSetChanged();
                    adapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
    private void getHolder(){
//        WebBase.getHoldlist(page, new JSONHandler() {
        WebBase.getHoldlist2(page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                myscrllview.onRefreshComplete();
                if(obj.has("result")){
                    JSONObject result=obj.optJSONObject("result");
                    page=result.optInt("page");
                    pages=result.optInt("pages");
                    if(isRush){
                        isRush=false;
                        list.clear();
                    }
                    if(result.has("stocks")){
                        list.addAll(JSONParse.getHolder(result.optJSONArray("stocks")));
                    }
                    if(list.size()>0){
                        adapter.notifyDataSetChanged();
                        adapter1.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
    public void showOpreateDialog(final StockholdsBean stockholdsBean) {
        if(flag==0){
            final Dialog dialog1 = new Dialog(this, R.style.myDialogTheme);
            LayoutInflater inflater = this.getLayoutInflater();
            View layout = inflater.inflate(R.layout.dialog_tip2, null);
            TextView tvTip = (TextView) layout.findViewById(R.id.tvTip);
            tvTip.setText(stockholdsBean.getName());
            TextView tvCancl = (TextView) layout.findViewById(R.id.tvCancl);
            TextView tvConfirm = (TextView) layout.findViewById(R.id.tvConfirm);
            TextView tvConfirm1 = (TextView) layout.findViewById(R.id.tvConfirm1);
            tvCancl.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    dialog1.dismiss();
                }
            });
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(IntentKey.STOCKHOLDER,stockholdsBean);
                    ShowActivity.showActivity(MatchHoldActivity.this,bundle, StockSellActivity.class);
                    dialog1.dismiss();
                }
            });

            tvConfirm1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(IntentKey.MATCH_BAEN,matchBean);
                    bundle.putSerializable(IntentKey.STOCKHOLDER,stockholdsBean);
                    ShowActivity.showActivity(MatchHoldActivity.this,bundle, StockBuyActivity.class);
                    dialog1.dismiss();
                }
            });
            dialog1.setContentView(layout);

            // 设置对话框的出现位置，借助于window对象
            Window win = dialog1.getWindow();
            win.setGravity(Gravity.CENTER);

            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = (int) (DisplayUtil.getScreenWidthPixels(this) * 0.65);
            win.setAttributes(lp);
            dialog1.setCancelable(false);
            dialog1.show();
        }else{
            Bundle bundle=new Bundle();
            bundle.putSerializable(IntentKey.MATCH_BAEN,matchBean);
            bundle.putSerializable(IntentKey.STOCKHOLDER,stockholdsBean);
            ShowActivity.showActivity(this,bundle, SimulateOneStockCommitActivity.class);
        }
    }
}
