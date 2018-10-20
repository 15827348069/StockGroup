package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.RecommendAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/2/24.
 */

public class RecommendActivity extends BaseActivity {
    private PullToRefreshListView recommend_list;
    private RecommendAdapter adapter;
    private List<Group>infolist;
    private int page,pages;

    @Override
    public int getLayoutResId() {
        return R.layout.recommend_group_list;
    }

    @Override
    public void initView() {
        initTitle("推荐老师");
        recommend_list= (PullToRefreshListView) findViewById(R.id.recommend_list);
        recommend_list.setMode(PullToRefreshBase.Mode.BOTH);
    }

    public void initData() {
        page=1;
        pages=0;
        if(infolist==null){
            infolist=new ArrayList<>();
        }else{
            infolist.clear();
        }
        adapter=new RecommendAdapter(this,infolist);
        recommend_list.setAdapter(adapter);
        getRecommend();
    }

    @Override
    public void addListener() {
        adapter.setOnCareClink(new RecommendAdapter.OnCareClink() {
            @Override
            public void onCareClink(int position) {
                care_group(position);
            }
        });
        recommend_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                page+=1;
                getRecommend();
            }
        });
        recommend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group groupbean=infolist.get(position-1);
                ShowActivity.showGroupDetailActivity(RecommendActivity.this,groupbean);
            }
        });
    }

    private void getRecommend(){
        WebBase.recommend(page,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                page=result.optInt("page");
                pages=result.optInt("pages");
                if(!result.isNull("groups")){
                    JSONArray recommend=result.optJSONArray("groups");
                    infolist.addAll(JSONParse.getGroupList(recommend));
                    adapter.notifyDataSetChanged();
                }
                recommend_list.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                recommend_list.onRefreshComplete();
            }
        });
    }
    private void care_group(final int position){
        if(SettingDefaultsManager.getInstance().authToken()==null||SettingDefaultsManager.getInstance().authToken().equals("")){
            Toast.makeText(getBaseContext(),"登陆后才可关注！",Toast.LENGTH_SHORT).show();
            infolist.get(position).setIs_recommend(0);
            adapter.notifyDataSetChanged();
            ShowActivity.showActivity(this,LoginActivity.class);
        }else{
            String group_id=infolist.get(position).getId();
            WebBase.follow(group_id, new JSONHandler(true,RecommendActivity.this,"正在关注圈主...") {
                @Override
                public void onSuccess(JSONObject obj) {
                    Toast.makeText(getBaseContext(),"关注成功",Toast.LENGTH_SHORT).show();
                    infolist.get(position).setIs_recommend(1);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(String err_msg) {
                    Toast.makeText(getBaseContext(),err_msg,Toast.LENGTH_SHORT).show();
                    infolist.get(position).setIs_recommend(0);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
