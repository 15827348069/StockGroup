package com.zbmf.StockGroup.activity;

import android.text.Html;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.VotingTeacherAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.constans.Constants;
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

public class VotingTeacherRankActivity extends BaseActivity implements VotingTeacherAdapter.OnGroup {
    private ListViewForScrollView listViewForScrollView;
    private VotingTeacherAdapter adapter;
    private List<Group>infolist;
    private boolean isMonth;
    private TextView tv_date,tv_chose_month,tv_desc;
    private PullToRefreshScrollView pull_to_refresh_scrollview;
    private String month;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_voting_teacher_layout;
    }

    @Override
    public void initView() {
        initTitle("评选");
        listViewForScrollView=getView(R.id.list_voting);
        tv_date=getView(R.id.tv_date);
        tv_chose_month=getView(R.id.tv_chose_month);
        tv_desc=getView(R.id.tv_desc);
        pull_to_refresh_scrollview=getView(R.id.pull_to_refresh_scrollview);
        pull_to_refresh_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    @Override
    public void initData() {
        tv_desc.setText(Html.fromHtml(Constants.VOTING_DESC));
        tv_date.setText(getString(R.string.voting_teacher).replace("[**]",DateUtil.getTime(DateUtil.getTimes(),Constants.YYYY年MM月)));
        infolist=new ArrayList<>();
        adapter=new VotingTeacherAdapter(this);
        adapter.setOnGroup(this);
        adapter.setList(infolist);
        listViewForScrollView.setAdapter(adapter);
        month=DateUtil.getMonth(0,Constants.YYYYMM);
        getData();
    }

    @Override
    public void addListener() {
        tv_chose_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMonth){
                    month=DateUtil.getMonth(0,Constants.YYYYMM);
                    getData();
                    tv_date.setText(getString(R.string.voting_teacher).replace("[**]",DateUtil.getMonth(0,Constants.YYYY年MM月)));
                }else{
                    month=DateUtil.getMonth(-1,Constants.YYYYMM);
                    getData();
                    tv_date.setText(getString(R.string.voting_teacher).replace("[**]",DateUtil.getMonth(-1,Constants.YYYY年MM月)));
                }
                tv_chose_month.setText(isMonth?"上月排行":"本月排行");
                isMonth=!isMonth;
            }
        });
        getView(R.id.tv_find_stock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.showActivity(VotingTeacherRankActivity.this,ScreenActivity.class);
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
        WebBase.vote(month,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if(obj.has("groups")){
                    infolist.clear();
                    infolist.addAll(JSONParse.getGroupList(obj.optJSONArray("groups")));
                    adapter.notifyDataSetChanged();
                }
                pull_to_refresh_scrollview.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                pull_to_refresh_scrollview.onRefreshComplete();
            }
        });
    }

    @Override
    public void onGroup(Group group) {
        ShowActivity.showGroupDetailActivity(this,group);
    }
}
