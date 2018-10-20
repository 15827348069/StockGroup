package com.zbmf.StockGroup.fragment.teacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.LoginActivity;
import com.zbmf.StockGroup.adapter.RecommendAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.fragment.BaseFragment;
import com.zbmf.StockGroup.interfaces.TeacherToStudy;
import com.zbmf.StockGroup.constans.Constants;
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
 * Created by xuhao on 2017/8/16.
 */

public class RankingFragment extends BaseFragment implements RecommendAdapter.OnCareClink, View.OnClickListener {
    private PullToRefreshListView recommend_list;
    private RecommendAdapter adapter;
    private List<Group> infolist;
    private int page,pages;
    private int flags;
    private boolean isRush,isFirst=true;
    private LinearLayout no_message_layout;

    private TeacherToStudy teacherToStudy;

    public RankingFragment setTeacherToStudy(TeacherToStudy teacherToStudy) {
        this.teacherToStudy = teacherToStudy;
        return this;
    }

    public static RankingFragment newInstance(int flag){
        RankingFragment fragment=new RankingFragment();
        Bundle args = new Bundle();
        args.putInt(IntentKey.FLAG, flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            flags=bundle.getInt(IntentKey.FLAG);
        }
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_ranking,null);
    }

    @Override
    protected void initView() {
        recommend_list= getView(R.id.recommend_list);
        getView(R.id.tv_to_study).setOnClickListener(this);
        no_message_layout=getView(R.id.no_message_layout);
        infolist=new ArrayList<>();
        adapter=new RecommendAdapter(getActivity(),infolist);
        adapter.setOnCareClink(this);
        recommend_list.setAdapter(adapter);
        recommend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group groupbean=infolist.get(position-1);
                ShowActivity.showGroupDetailActivity(getActivity(),groupbean);
            }
        });
    }

    @Override
    protected void initData() {
        page=1;
        isRush=true;
        setRecommend_list();
        getRecommend();
    }
    private void setRecommend_list(){
        if(flags== Constants.NOW_LIVE){
            recommend_list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            recommend_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

                @Override
                public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                    initData();
                }
            });
        }else{
            recommend_list.setMode(PullToRefreshBase.Mode.BOTH);
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
        }
    }
    @Override
    public void onCareClink(final int position) {
            if(SettingDefaultsManager.getInstance().authToken()==null||SettingDefaultsManager.getInstance().authToken().equals("")){
                showToast("登陆后才可关注！");
                infolist.get(position).setIs_recommend(0);
                adapter.notifyDataSetChanged();
                ShowActivity.showActivity(getActivity(),LoginActivity.class);
            }else{
                String group_id=infolist.get(position).getId();
                WebBase.follow(group_id, new JSONHandler(true,getActivity(),"正在关注圈主...") {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        showToast("关注成功");
                        infolist.get(position).setIs_recommend(1);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String err_msg) {
                        showToast(err_msg);
                        infolist.get(position).setIs_recommend(0);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
    }
    private void getRecommend(){
        WebBase.getTeaCher(flags,page,new JSONHandler(isFirst,getActivity(),getResources().getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                recommend_list.onRefreshComplete();
                JSONArray  recommend=null;
                if(obj.isNull("result")){
                    if(!obj.isNull("groups")){
                        recommend=obj.optJSONArray("groups");
                    }
                }else{
                    JSONObject result=obj.optJSONObject("result");
                    page=result.optInt("page");
                    pages=result.optInt("pages");
                    if(!result.isNull("groups")){
                        recommend=result.optJSONArray("groups");
                    }
                }
                if(isRush){
                    infolist.clear();
                    isRush=false;
                }
                if(recommend!=null){
                    infolist.addAll(JSONParse.getGroupList(recommend));
                    adapter.notifyDataSetChanged();
                }
                if(page==pages&&!isFirst){
                    showToast("已加载全部数据");
                    recommend_list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                if(flags== Constants.NOW_LIVE&&infolist.size()==0){
                    no_message_layout.setVisibility(View.VISIBLE);
                }else{
                    no_message_layout.setVisibility(View.GONE);
                }
                isFirst=false;
            }

            @Override
            public void onFailure(String err_msg) {
                recommend_list.onRefreshComplete();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_to_study:
                if(teacherToStudy!=null){
                    teacherToStudy.toStudy();
                }
                break;
        }
    }
}
