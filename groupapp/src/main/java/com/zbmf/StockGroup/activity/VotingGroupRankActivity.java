package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.VotingAdapter;
import com.zbmf.StockGroup.adapter.VotingTeacherAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/11/6.
 */

public class VotingGroupRankActivity extends BaseActivity implements VotingTeacherAdapter.OnGroup {
    private ListViewForScrollView listViewForScrollView,list_teacher_voting;
    private VotingAdapter adapter;
    private VotingTeacherAdapter teacherAdapter;
    private List<Group>infolist;
    private TextView tv_rank,tv_diff,tv_desc;
    private PullToRefreshScrollView pull_to_refresh_scrollview;
    private String month;
    private boolean user,teacher;
    private String group_id;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_voting_layout;
    }

    @Override
    public void initView() {
        initTitle("贡献榜");
        listViewForScrollView=getView(R.id.list_voting);
        list_teacher_voting=getView(R.id.list_teacher_voting);
        tv_rank=getView(R.id.tv_rank);
        tv_diff=getView(R.id.tv_diff);
        tv_desc=getView(R.id.tv_desc);
        getView(R.id.tv_sign).setVisibility(View.VISIBLE);
        pull_to_refresh_scrollview=getView(R.id.pull_to_refresh_scrollview);
        pull_to_refresh_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    @Override
    public void initData() {
        tv_desc.setText(Html.fromHtml(Constants.VOTING_DESC));
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            Group group= (Group) bundle.getSerializable(IntentKey.GROUP);
            group_id=group.getId();
        }
        infolist=new ArrayList<>();
        adapter=new VotingAdapter(this);
        adapter.setList(infolist);
        listViewForScrollView.setAdapter(adapter);
        teacherAdapter=new VotingTeacherAdapter(this);
        teacherAdapter.setOnGroup(this);
        list_teacher_voting.setAdapter(teacherAdapter);
        month= DateUtil.getMonth(0,Constants.YYYYMM);
        getData();
    }

    @Override
    public void addListener() {
        getView(R.id.tv_find_stock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showActivity(VotingGroupRankActivity.this,ScreenActivity.class);
            }
        });
        pull_to_refresh_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getData();
            }
        });
    }
    private void getData(){
        WebBase.userVote(group_id, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                tv_rank.setText(obj.optString("rank"));
                tv_diff.setText(getString(R.string.voting).replace("[*]",obj.optString("votes")).replace("[**]",obj.optString("diff")));
                infolist.clear();
                infolist.addAll(JSONParse.getGroupList(obj.optJSONArray("users")));
                adapter.notifyDataSetChanged();
                user=true;
                onRefreshScrollView();
            }

            @Override
            public void onFailure(String err_msg) {
                user=true;
                onRefreshScrollView();
            }
        });
        WebBase.vote(month,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if(obj.has("groups")){
                     List<Group>teacherList=JSONParse.getGroupList(obj.optJSONArray("groups"));
                    teacherAdapter.setList(teacherList);
                }
                teacher=true;
                onRefreshScrollView();
            }

            @Override
            public void onFailure(String err_msg) {
                teacher=true;
                onRefreshScrollView();
            }
        });
    }
    private void onRefreshScrollView(){
        if(user&&teacher&&pull_to_refresh_scrollview!=null&&pull_to_refresh_scrollview.isRefreshing()){
            pull_to_refresh_scrollview.onRefreshComplete();
        }
    }

    @Override
    public void onGroup(Group group) {
        if(group.getId().equals(group_id)){
            finish();
        }else{
            ShowActivity.showGroupDetailActivity(this,group);
        }
    }
}
