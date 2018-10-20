package com.zbmf.StockGroup.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.VideoTeacherAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.VideoTeacher;
import com.zbmf.StockGroup.constans.IntentKey;
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
 * Created by xuhao on 2017/8/22.
 */

public class VideoTeacherActivity extends BaseActivity implements VideoTeacherAdapter.OnItemClick {
    private List<VideoTeacher> infolist;
    private PullToRefreshListView videoList;
    private VideoTeacherAdapter adapter;
    private boolean isRush,isFirst=true;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_videoteacher_layout;
    }

    @Override
    public void initView() {
        initTitle("名师");
        videoList= (PullToRefreshListView) findViewById(R.id.video_teacher_list);
        videoList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    @Override
    public void initData() {
        adapter=new VideoTeacherAdapter(VideoTeacherActivity.this);
        videoList.setAdapter(adapter);
        getTeacherList();
    }

    @Override
    public void addListener() {
        adapter.setOnItemClick(this);
        videoList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                isRush=true;
                getTeacherList();
            }
        });
    }

    private void getTeacherList(){
        WebBase.GetTeachers(new JSONHandler(isFirst,VideoTeacherActivity.this, getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                videoList.onRefreshComplete();
                if(!obj.isNull("result")){
                    JSONArray result=obj.optJSONArray("result");
                    if(infolist==null){
                        infolist=new ArrayList<VideoTeacher>();
                    }
                    if(isRush){
                        isRush=false;
                        infolist.clear();
                    }
                    infolist.addAll(JSONParse.getVideoTeacherList(result));
                    adapter.rushAdapter(infolist);
                }

            }

            @Override
            public void onFailure(String err_msg) {
                if(isRush){
                    isRush=false;
                }
                videoList.onRefreshComplete();
            }
        });
        if(isFirst){
            isFirst=false;
        }
    }
    @Override
    public void onCancelCareClick(final int position) {
        String group_id=infolist.get(position).getId();
        WebBase.unfollow(group_id, new JSONHandler(true,VideoTeacherActivity.this, "正在取消关注圈主...") {
            @Override
            public void onSuccess(JSONObject obj) {
                showToast("取消关注成功");
                infolist.get(position).setAttention(0);
                adapter.rushAdapter(infolist);
            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
            }
        });
    }

    @Override
    public void onCareClick(final int position) {
        if(SettingDefaultsManager.getInstance().authToken()==null||SettingDefaultsManager.getInstance().authToken().equals("")){
            showToast("登陆后才可关注！");
            infolist.get(position).setAttention(0);
            adapter.rushAdapter(infolist);
            ShowActivity.showActivity(VideoTeacherActivity.this,LoginActivity.class);
        }else{
            String group_id=infolist.get(position).getId();
            WebBase.follow(group_id, new JSONHandler(true,VideoTeacherActivity.this,"正在关注圈主...") {
                @Override
                public void onSuccess(JSONObject obj) {
                    showToast("关注成功");
                    infolist.get(position).setAttention(1);
                    adapter.rushAdapter(infolist);
                }

                @Override
                public void onFailure(String err_msg) {
                    showToast(err_msg);
                    infolist.get(position).setAttention(0);
                    adapter.rushAdapter(infolist);
                }
            });
        }
    }

    @Override
    public void onEnterGroup(VideoTeacher teacher) {
        ShowActivity.showGroupDetailActivity(VideoTeacherActivity.this,teacher.getId());
    }

    @Override
    public void onDescClick(VideoTeacher teacher) {
        Bundle bundle=new Bundle();
        bundle.putSerializable(IntentKey.VDIEO_TEACHER_DESC,teacher.getDescription());
        ShowActivity.showActivity(VideoTeacherActivity.this,bundle,VideoTeacherDescActivity.class);
    }

    @Override
    public void onVideoClick(VideoTeacher teacher) {
        ShowActivity.showVideoListActivity(VideoTeacherActivity.this,teacher);
    }
}
