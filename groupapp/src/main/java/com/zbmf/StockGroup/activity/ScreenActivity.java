package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ScreenAdapter;
import com.zbmf.StockGroup.adapter.ScreenProductAdatper;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.FullyGridLayoutManager;
import com.zbmf.StockGroup.view.GridViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 找股票列表页
 * Created by xuhao on 2017/10/16.
 */

public class ScreenActivity extends BaseActivity implements View.OnClickListener, ScreenAdapter.onItemClick {
    private GridViewForScrollView rv_screen_product;
    private PullToRefreshScrollView scrollview_screen;
    private ScreenProductAdatper adapter;
    private List<Screen>infolist;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_screen_layout;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.find_stock));
        getView(R.id.imb_msg).setVisibility(View.VISIBLE);
        rv_screen_product=getView(R.id.rv_screen_product);
        scrollview_screen=getView(R.id.scrollview_screen);
        scrollview_screen.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        getView(R.id.rl_stock_hei).setOnClickListener(this);
    }

    @Override
    public void initData() {
        infolist=new ArrayList<>();
        adapter=new ScreenProductAdatper(this,infolist);
        adapter.setItemClick(this);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        manager.setSmoothScrollbarEnabled(true);
//        rv_screen_product.setLayoutManager(manager);
        rv_screen_product.setAdapter(adapter);
        getScreenList();
    }

    @Override
    public void addListener() {
        getView(R.id.imb_msg).setOnClickListener(this);
        scrollview_screen.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getScreenList();
            }
        });
        rv_screen_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putSerializable(IntentKey.SCREEN,infolist.get(position));
                ShowActivity.showActivityForResult(ScreenActivity.this,bundle,ScreenDetailActivity.class, RequestCode.SCREEN);
            }
        });
    }
    private void getScreenList(){
        WebBase.getScreenProducts(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                scrollview_screen.onRefreshComplete();
                if(!result.isNull("screen")){
                    infolist.clear();
                    infolist.addAll(JSONParse.getScreenList(result.optJSONArray("screen")));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                scrollview_screen.onRefreshComplete();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imb_msg:
                if(ShowActivity.isLogin(this)){
                    ShowActivity.showActivity(this,StockMessageActivity.class);
                }
                break;
            case R.id.rl_stock_hei:
                ShowActivity.showWebViewActivity(this, HtmlUrl.STOCK_HEI);
                break;
        }
    }

    @Override
    public void onItemClick(Screen screen) {
        Bundle bundle=new Bundle();
        bundle.putSerializable(IntentKey.SCREEN,screen);
        ShowActivity.showActivityForResult(ScreenActivity.this,bundle,ScreenDetailActivity.class, RequestCode.SCREEN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        if(resultCode==RESULT_OK&&requestCode==RequestCode.SCREEN){
            getScreenList();
        }
    }
}
