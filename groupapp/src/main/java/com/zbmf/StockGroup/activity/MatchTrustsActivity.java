package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.LeftTitleAdapter;
import com.zbmf.StockGroup.adapter.TrustsAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.StockholdsBean;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.dialog.TextDialog;
import com.zbmf.StockGroup.interfaces.DialogYesClick;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;
import com.zbmf.StockGroup.view.SyncHorizontalScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MatchTrustsActivity extends BaseActivity {
    private PullToRefreshScrollView myscrllview;
    private ListViewForScrollView leftTitleListView, rightContentListView;
    private SyncHorizontalScrollView rightTitleScrollView, rightContentScrollView;
    private TextView tv_title;
    private List<StockholdsBean> list = new ArrayList<StockholdsBean>();
    String[] rightTitles = {"委托类型", "数量", "委托价格", "冻结金额", "委托时间"};
    private TrustsAdapter adapter;
    private LeftTitleAdapter adapter1;
    private MatchInfo matchBean;
    private LinearLayout mNoLLView;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_match_trusts;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.my_weituo));
        myscrllview = (PullToRefreshScrollView)findViewById(R.id.myscrllview);
        mNoLLView = (LinearLayout) findViewById(R.id.ll_none);
        myscrllview.setMode(PullToRefreshBase.Mode.BOTH);
        rightTitleScrollView = (SyncHorizontalScrollView) findViewById(R.id.matchOperate_rightTitleScrollView);
        rightContentScrollView = (SyncHorizontalScrollView) findViewById(R.id.matchOperate_rightContentScrollView);
        rightTitleScrollView.setScrollView(rightContentScrollView);
        rightContentScrollView.setScrollView(rightTitleScrollView);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < rightTitles.length; i++) {
            View convertView = LayoutInflater.from(MatchTrustsActivity.this).inflate(R.layout.trusts_single_cell, null);
            TextView title = (TextView) convertView.findViewById(R.id.trusts_cell_name);

            title.setText(rightTitles[i]);
            layout.addView(convertView);
        }
        rightTitleScrollView.addView(layout);

        leftTitleListView = (ListViewForScrollView) findViewById(R.id.matchOperate_leftTitleListView);
        rightContentListView = (ListViewForScrollView) findViewById(R.id.matchOperate_rightContentListView);
        adapter = new TrustsAdapter(this);
        adapter.setList(list);
        adapter1 = new LeftTitleAdapter(this);
        adapter1.setList(list);
        leftTitleListView.setAdapter(adapter1);
        rightContentListView.setAdapter(adapter);
    }

    @Override
    public void initData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            matchBean = (MatchInfo)bundle.getSerializable(IntentKey.MATCH_BAEN);
        }
        if (matchBean != null) {
            //获取订单列表
            page=1;
            getOrderList();
        }
    }

    @Override
    public void addListener() {
        rightContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                StockholdsBean sb = (StockholdsBean) arg0.getItemAtPosition(arg2);
                showDialog(sb);
            }
        });
        leftTitleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                StockholdsBean sb = (StockholdsBean) arg0.getItemAtPosition(arg2);
                showDialog(sb);
            }
        });
        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                isRush=true;
                page=1;
                getOrderList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page+=1;
                getOrderList();
            }
        });
    }

    private boolean isFirst = true,isRush;
    private int page,pages;
    private void getOrderList(){
        WebBase.getOrderList(1, new JSONHandler(isFirst,MatchTrustsActivity.this,getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                myscrllview.onRefreshComplete();
                StockholdsBean sb=JSONParse.getOrderList(obj);
                page=sb.getPage();
                pages=sb.getPages();
                if(isRush){
                    isRush=false;
                    list.clear();
                }
                list.addAll(sb.getInfolist());
                adapter.notifyDataSetChanged();
                adapter1.notifyDataSetChanged();
                if(isFirst&&list.size()==0){
                    mNoLLView.setVisibility(View.VISIBLE);
                    isFirst=false;
                }else if(page==pages&&list.size()==0){
                    showToast("暂无委托记录");
                    mNoLLView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String err_msg) {
                myscrllview.onRefreshComplete();
                showToast(err_msg);
            }
        });
    }
    private void showDialog(final StockholdsBean sb) {
        TextDialog.createDialog(this)
                .setMessage("撤单"+sb.getName())
                .setLeftButton(getString(R.string.cancel))
                .setRightButton("撤单")
                .setRightClick(new DialogYesClick() {
                    @Override
                    public void onYseClick() {
                        WebBase.withdraw(sb.getId(), new JSONHandler() {
                            @Override
                            public void onSuccess(JSONObject obj) {
                                isRush=true;
                                page=1;
                                getOrderList();
                                showToast("撤单成功");
                            }

                            @Override
                            public void onFailure(String err_msg) {
                                showToast(err_msg);
                            }
                        });
                    }
                })
                .show();
    }

}
