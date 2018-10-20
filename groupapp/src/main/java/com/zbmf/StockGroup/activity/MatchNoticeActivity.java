package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.MatchNoticeAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MatchAnnouncements;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2018/1/8.
 */

public class MatchNoticeActivity extends BaseActivity {
    private PullToRefreshListView pullList;
    private MatchNoticeAdapter adapter;
    private List<MatchAnnouncements>infolist;
    private int page;
    private boolean isRush;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_notice_layout;
    }

    @Override
    public void initView() {
        initTitle("公告");
        pullList=getView(R.id.pull_list_view);
    }

    @Override
    public void initData() {
        infolist=new ArrayList<>();
        adapter=new MatchNoticeAdapter(this);
        pullList.setAdapter(adapter);
        getNotice();
    }

    @Override
    public void addListener() {
        pullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page=1;
                isRush=true;
                getNotice();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getNotice();
            }
        });
        pullList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putParcelable(IntentKey.MATCHANNOUNCEMENTS,adapter.getItem(position-1));
                ShowActivity.showActivity(MatchNoticeActivity.this,bundle, AnnouncementActivity.class);
            }
        });
    }
    private void getNotice(){
        WebBase.getAnnouncements(page,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                LogUtil.e(obj.toString());
                if(obj.has("result")){
                    JSONObject result=obj.optJSONObject("result");
                    page=result.optInt("page");
                    if(isRush){
                        infolist.clear();
                        isRush=false;
                    }
                    if(result.has("announcements")){
                        JSONArray jsonArray=result.optJSONArray("announcements");
                        if(jsonArray.length()>0){
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.optJSONObject(i);
                                MatchAnnouncements matchAnnouncements=new MatchAnnouncements();
                                matchAnnouncements.setGroup_id(jsonObject.optString("group_id"));
                                matchAnnouncements.setContent(jsonObject.optString("content"));
                                matchAnnouncements.setPosted_at(jsonObject.optString("posted_at"));
                                matchAnnouncements.setSubject(jsonObject.optString("subject"));
                                matchAnnouncements.setTopic_id(jsonObject.optString("topic_id"));
                                infolist.add(matchAnnouncements);
                            }
                            adapter.setList(infolist);
                        }
                    }
                }
                if(pullList!=null&&pullList.isRefreshing()){
                    pullList.onRefreshComplete();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                if(pullList!=null&&pullList.isRefreshing()){
                    pullList.onRefreshComplete();
                }
                isRush=!isRush;
            }
        });
    }
}
