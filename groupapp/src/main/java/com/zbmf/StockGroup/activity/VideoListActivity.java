package com.zbmf.StockGroup.activity;

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
import com.zbmf.StockGroup.adapter.VideoItemAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.beans.VideoTeacher;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.constans.VideoItemType;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuhao on 2017/8/22.
 */

public class VideoListActivity extends BaseActivity{
    private List<Video> infolist;
    private PullToRefreshListView videoList;
    private VideoItemAdapter adapter;
    private boolean isRush,isFirst=true;
    private Map<String,String> param;
    private int page,pages;
    private TextView no_video_message;
    private VideoTeacher videoTeacher;
    private String group_id;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_videoteacher_layout;
    }

    @Override
    public void initView() {
        videoTeacher= (VideoTeacher) getIntent().getSerializableExtra(IntentKey.VIDEO_TEACHER);
        group_id=videoTeacher!=null?videoTeacher.getId():"";
        initTitle((videoTeacher!=null?videoTeacher.getName()+"的":"")+"视频");
        no_video_message= (TextView) findViewById(R.id.no_video_message);
        videoList= (PullToRefreshListView) findViewById(R.id.video_teacher_list);
        videoList.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void initData() {
        infolist=new ArrayList<>();
        adapter=new VideoItemAdapter(VideoListActivity.this,infolist);
        adapter.setVideoViewType(VideoItemType.VIDEO);
        videoList.setAdapter(adapter);
        getVideoList();
    }

    @Override
    public void addListener() {
        videoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video video=infolist.get(position-1);
                if(video.getIs_live()){
                    setLoginMessage(video);
                }else{
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                    ShowActivity.showActivity(VideoListActivity.this,bundle, VideoPlayActivity.class);
                }
            }
        });
        videoList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRush=true;
                page=1;
                getVideoList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page+=1;
                getVideoList();
            }
        });
    }

    private void getVideoList(){
        if(param==null){
            param=new HashMap<>();
        }
        param.put("page", page+"");
        param.put("user_id", group_id);
        WebBase.GetsVideos(param, new JSONHandler(isFirst,VideoListActivity.this,getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                page=result.optInt("page");
                pages=result.optInt("pages");
                if(!result.isNull("videos")){
                    if(isRush){
                        isRush=false;
                        infolist.clear();
                    }
                    infolist.addAll(JSONParse.getVideoList(result.optJSONArray("videos")));
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                    if(infolist.size()==0){
                        if(no_video_message.getVisibility()==View.GONE){
                            no_video_message.setVisibility(View.VISIBLE);
                        }
                    }else{
                        if(no_video_message.getVisibility()==View.VISIBLE){
                            no_video_message.setVisibility(View.GONE);
                        }
                    }
                }
                if(isFirst){
                    isFirst=false;
                }
                videoList.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
                videoList.onRefreshComplete();
            }
        });
    }
    private void setLoginMessage(final Video video){
        DWLive.getInstance().setDWLiveLoginParams(new DWLiveLoginListener() {
            @Override
            public void onLogin(TemplateInfo templateInfo, Viewer viewer, RoomInfo roomInfo, PublishInfo publishInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                        ShowActivity.showActivityForResult(VideoListActivity.this,bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                    }
                });
            }

            @Override
            public void onException(final DWLiveException e) {
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                        ShowActivity.showActivityForResult(VideoListActivity.this,bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                    }
                });
            }
        }, Constants.CC_USERID,video.getBokecc_id()+"", SettingDefaultsManager.getInstance().NickName(),"");
        DWLive.getInstance().startLogin();
    }
}
