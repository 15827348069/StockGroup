package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveLoginListener;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.PublishInfo;
import com.bokecc.sdk.mobile.live.pojo.RoomInfo;
import com.bokecc.sdk.mobile.live.pojo.TemplateInfo;
import com.bokecc.sdk.mobile.live.pojo.Viewer;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.SeriesVideoAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/7/7.
 */

public class SeriesVideoActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_video_name,tv_video_teacher_name,tv_video_time;
    private PullToRefreshListView series_list_view;
    private SeriesVideoAdapter adapter;
    private List<Video>infolist;
    private Video.SeriesVideo seriesVideo;
    private String series_id;
    private int page;
    private boolean isRush,commit,video_play;
    public void  initView(){
        initTitle("系列课程列表");
        tv_video_name=getView(R.id.tv_video_name);
        tv_video_teacher_name=getView(R.id.tv_video_teacher_name);
        tv_video_time=getView(R.id.tv_video_time);
        series_list_view=getView(R.id.series_list_view);
        series_list_view.setMode(PullToRefreshBase.Mode.BOTH);
    }
    public void initData(){
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            seriesVideo= (Video.SeriesVideo) bundle.getSerializable(IntentKey.VIDEO_SERIES);
            series_id=seriesVideo.getSeries_id();
            commit=seriesVideo.getCommit();
            video_play=seriesVideo.getIs_play();
            isRush=false;
            infolist=new ArrayList<>();
            adapter=new SeriesVideoAdapter(getBaseContext(),infolist);
            series_list_view.setAdapter(adapter);
            setTextViewMessage();
            rushData();
        }
    }
    private void setTextViewMessage(){
        LogUtil.e(seriesVideo.toString());
        tv_video_name.setText(seriesVideo.getName()!=null?seriesVideo.getName():"");
        tv_video_teacher_name.setText(seriesVideo.getTeacher_name()!=null?seriesVideo.getTeacher_name():"");
        String seriesMessage="";
        if(seriesVideo.getStatus()){
            seriesMessage=getString(R.string.video_pecial_done).replace("[*]",seriesVideo.getPhase()!=null?seriesVideo.getPhase():"");
        }else{
            seriesMessage=getString(R.string.video_pecial).replace("[*]",seriesVideo.getPhase()!=null?seriesVideo.getPhase():"");
        }
        tv_video_time.setText(seriesMessage);
    }
    @Override
    public void addListener() {
        series_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                rushData();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                page+=1;
                getCompilanDetail();
            }
        });
        series_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(commit){
                    Video video=infolist.get(position-1);
                    if(video.getIs_live()){
                        setLoginMessage(video);
                    }else{
                        if(video_play){
                            Intent intent=new Intent();
                            Bundle bundle=new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                            intent.putExtras(bundle);
                            setResult(RESULT_OK,intent);
                            finish();
                        }else{
                            Bundle bundle=new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                            ShowActivity.showActivity(SeriesVideoActivity.this,bundle, VideoPlayActivity.class);
                        }
                    }
                }
            }
        });
    }

    public void rushData(){
        page=1;
        isRush=true;
        getCompilanDetail();
    }
    public void setLoginMessage(final Video video){
        DWLive.getInstance().setDWLiveLoginParams(new DWLiveLoginListener() {
            @Override
            public void onLogin(TemplateInfo templateInfo, Viewer viewer, RoomInfo roomInfo, PublishInfo publishInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(video_play){
                            Intent intent=new Intent();
                            Bundle bundle=new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                            intent.putExtras(bundle);
                            setResult(RESULT_OK,intent);
                            finish();
                        }else{
                            Bundle bundle=new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                            ShowActivity.showActivity(SeriesVideoActivity.this,bundle, VideoPlayActivity.class);
                        }
                    }
                });
            }

            @Override
            public void onException(final DWLiveException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(video_play){
                            Intent intent=new Intent();
                            Bundle bundle=new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                            intent.putExtras(bundle);
                            setResult(RESULT_OK,intent);
                            finish();
                        }else{
                            Bundle bundle=new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                            ShowActivity.showActivity(SeriesVideoActivity.this,bundle, VideoPlayActivity.class);
                        }
                    }
                });
            }
        }, Constants.CC_USERID,video.getBokecc_id()+"", SettingDefaultsManager.getInstance().NickName(),"");
        DWLive.getInstance().startLogin();
    }

    public void getCompilanDetail(){
        WebBase.Series(series_id,page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if(!obj.isNull("videos")){
                    if(isRush){
                        isRush=false;
                        infolist.clear();
                    }
                    infolist.addAll(JSONParse.getVideoList(obj.optJSONArray("videos")));
                    adapter.notifyDataSetChanged();
                }
                if(!obj.isNull("series")){
                    seriesVideo=JSONParse.getSeries(obj.optJSONObject("series"));
                    setTextViewMessage();
                }
                series_list_view.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                series_list_view.onRefreshComplete();
                Toast.makeText(getBaseContext(),err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_series_layout;
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
