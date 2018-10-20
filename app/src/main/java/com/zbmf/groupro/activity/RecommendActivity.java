package com.zbmf.groupro.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.RecommendAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/2/24.
 */

public class RecommendActivity extends Activity implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener{
    private PullToRefreshListView recommend_list;
    private RecommendAdapter adapter;
    private List<Group>infolist;
    private TextView group_title_name;
    private int page,pages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_group_list);
        init();
        initData();
    }

    public void init(){
        recommend_list= (PullToRefreshListView) findViewById(R.id.recommend_list);
        findViewById(R.id.group_title_return).setOnClickListener(this);
        group_title_name= (TextView) findViewById(R.id.group_title_name);
        group_title_name.setText("推荐老师");
        infolist=new ArrayList<>();
        adapter=new RecommendAdapter(this,infolist,this);
        recommend_list.setAdapter(adapter);
        recommend_list.setMode(PullToRefreshBase.Mode.BOTH);
        recommend_list.getLoadingLayoutProxy().setPullLabel("加载更多数据");
        recommend_list.getLoadingLayoutProxy().setRefreshingLabel("正在加载新数据...");
        recommend_list.getLoadingLayoutProxy().setReleaseLabel("松开加载数据");
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
//                if(!groupbean.is_recommend()){
//                    ShowActivity.showChatActivity(RecommendActivity.this,groupbean);
//                }else{
//                    ShowActivity.showGroupDetailActivity(RecommendActivity.this,groupbean);
//                }
                ShowActivity.showGroupDetailActivity(RecommendActivity.this,groupbean);
            }
        });
    }

    private void initData() {
        page=1;
        pages=0;
        infolist.clear();
        getRecommend();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.group_title_return:
                finish();
                break;
        }
    }
    private void getRecommend(){
        WebBase.recommend(page,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                page=result.optInt("page");
                pages=result.optInt("pages");
                JSONArray recommend=result.optJSONArray("groups");
                int size=recommend.length();
                for(int i=0;i<size;i++){
                    JSONObject oj=recommend.optJSONObject(i);
                    Group group=new Group();
                    group.setId(oj.optString("id"));
                    group.setName(oj.optString("name"));
                    group.setNick_name(oj.optString("nickname"));
                    group.setAvatar(oj.optString("avatar"));
                    group.setDescription(oj.optString("content"));
                    group.setIs_private(oj.optInt("is_private"));
                    group.setIs_close(oj.optInt("is_close"));
                    if(oj.optInt("is_followed")==0){
                        //未关注，按钮可以点击
                        group.setIs_recommend(true);
                    }else{
                        //已关注，按钮可以点击
                        group.setIs_recommend(false);
                    }
                    infolist.add(group);
                }
                adapter.notifyDataSetChanged();
                recommend_list.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                recommend_list.onRefreshComplete();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int position= (int) compoundButton.getTag();
        if(b){
            care_group(position);
        }
    }
    private void care_group(final int position){
        if(SettingDefaultsManager.getInstance().authToken()==null||SettingDefaultsManager.getInstance().authToken().equals("")){
            Toast.makeText(getBaseContext(),"登陆后才可关注！",Toast.LENGTH_SHORT).show();
            infolist.get(position).setIs_recommend(true);
            adapter.notifyDataSetChanged();
            ShowActivity.showActivity(this,LoginActivity.class);
        }else{
            String group_id=infolist.get(position).getId();
            WebBase.follow(group_id, new JSONHandler(true,RecommendActivity.this,"正在关注圈主...") {
                @Override
                public void onSuccess(JSONObject obj) {
                    Toast.makeText(getBaseContext(),"关注成功",Toast.LENGTH_SHORT).show();
                    infolist.get(position).setIs_recommend(false);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(String err_msg) {
                    Toast.makeText(getBaseContext(),err_msg,Toast.LENGTH_SHORT).show();
                    infolist.get(position).setIs_recommend(true);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
