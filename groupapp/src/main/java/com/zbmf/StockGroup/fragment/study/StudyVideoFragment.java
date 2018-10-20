package com.zbmf.StockGroup.fragment.study;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveLoginListener;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.PublishInfo;
import com.bokecc.sdk.mobile.live.pojo.RoomInfo;
import com.bokecc.sdk.mobile.live.pojo.TemplateInfo;
import com.bokecc.sdk.mobile.live.pojo.Viewer;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.SeriesVideoActivity;
import com.zbmf.StockGroup.activity.VideoPlayActivity;
import com.zbmf.StockGroup.activity.VideoTeacherActivity;
import com.zbmf.StockGroup.adapter.VideoItemAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.constans.VideoItemType;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshAdapterViewBase;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by xuhao on 2017/11/10.
 */

public class StudyVideoFragment extends Fragment implements View.OnClickListener, PullToRefreshAdapterViewBase.onScrolls {
    private PullToRefreshListView video_list;
    private VideoItemAdapter adapter;
    private List<Video> infolist;
    private Map<String,String> param;
    private TextView no_video_message;
    private TextView tv_teacher_num;
    private int page,pages;
    private boolean isRush,isFirst,isVideo=true;
    private GroupRadioGone groupRadioGone;
    private View view;
    public static StudyVideoFragment newInstance(){
        StudyVideoFragment studyVideoFragment=new StudyVideoFragment();
        return studyVideoFragment;
    }
    public interface GroupRadioGone{
        void onGone();
        void onVisible();
    }
    public void setGroupRadioGone(GroupRadioGone radioGone){
        this.groupRadioGone=radioGone;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_video_study,null);
        initView();
        initData();
        return view;
    }
    protected <T extends View>T getView(int resourcesId){
        return (T) view.findViewById(resourcesId);
    }
    protected void initView() {
        video_list=getView(R.id.study_veido__list);
//        imb_select=getView(R.id.imb_select);
        no_video_message=getView(R.id.no_video_message);
        tv_teacher_num=getView(R.id.tv_teacher_num);
        tv_teacher_num.setOnClickListener(this);

//        imb_select.setOnClickListener(this);
        video_list.setMode(PullToRefreshBase.Mode.BOTH);
        video_list.setOnScroll(this);
        video_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                isRush=true;
                param.clear();
                rush(false,param);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                page+=1;
                if(isVideo){
                    getVideo();
                }else{
                    getSeries();
                }

            }
        });
        video_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video video=infolist.get(position-1);
                if(video.getIs_series()){
                    Video.SeriesVideo seriesVideo=  video.getSeriesVideo();
                    if(seriesVideo!=null){
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(IntentKey.VIDEO_SERIES,seriesVideo);
                        ShowActivity.showActivity(getActivity(),bundle,SeriesVideoActivity.class);
                    }
                }else{
                    if(video.getIs_live()){
                        setLoginMessage(video);
                    }else{
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                        ShowActivity.showActivity(getActivity(),bundle, VideoPlayActivity.class);
                    }
                }
            }
        });
    }

    protected void initData() {
        if(infolist==null){
            infolist=new ArrayList<>();
        }else{
            infolist.clear();
        }
        isFirst=true;
        adapter=new VideoItemAdapter(getActivity(),infolist);
        adapter.setVideoViewType(VideoItemType.VIDEO);
        video_list.setAdapter(adapter);
        if(param!=null){
            param.clear();
        }else{
            param=new HashMap<>();
        }
        getVideo();
    }
    public void rush(boolean show,Map<String,String> p){
        if(isVideo){
            rushVideo(show,p);
        }else{
            rushSeries(show);
        }
    }
    private void setLoginMessage(final Video video){
        DWLive.getInstance().setDWLiveLoginParams(new DWLiveLoginListener() {
            @Override
            public void onLogin(TemplateInfo templateInfo, Viewer viewer, RoomInfo roomInfo, PublishInfo publishInfo) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                        ShowActivity.showActivityForResult(getActivity(),bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                    }
                });
            }

            @Override
            public void onException(final DWLiveException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                        ShowActivity.showActivityForResult(getActivity(),bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                    }
                });
            }
        }, Constants.CC_USERID,video.getBokecc_id()+"", SettingDefaultsManager.getInstance().NickName(),"");
        DWLive.getInstance().startLogin();
    }
    public void rushVideo(boolean is_show,Map<String,String> p){
        isFirst=is_show;
        if(param!=null){
            param.clear();
        }else{
            param=new HashMap<>();
        }
        Iterator<Map.Entry<String, String>> iterator = p.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            param.put(entry.getKey(), entry.getValue());
        }
        isVideo=true;
        if(video_list==null||adapter==null){
            return;
        }
        if(infolist ==null){
            infolist=new ArrayList<>();
        }
        page=1;
        adapter.setVideoViewType(VideoItemType.VIDEO);
        if(tv_teacher_num.getVisibility()==View.VISIBLE){
            tv_teacher_num.setVisibility(View.GONE);
        }
        getVideo();
    }
    public void rushSeries(boolean isshow){
        isFirst=isshow;
        isVideo=false;
        if(video_list==null||adapter==null){
            return;
        }
        if(infolist ==null){
            infolist=new ArrayList<>();
        }
        page=1;
        adapter.setVideoViewType(VideoItemType.SERIES);
        getSeries();
    }
    private void getVideo(){
        if(param==null){
            param=new HashMap<>();
        }
        param.put("page", page+"");
        WebBase.GetsVideos(param, new JSONHandler(isFirst,getActivity(),getString(R.string.loading)) {
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
                    if(isFirst){
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
                isFirst=false;
                video_list.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                video_list.onRefreshComplete();
                if(isVideo&&infolist.size()>0&&infolist.get(0).getIs_series()){
                    infolist.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getSeries(){
        WebBase.GetSeries(page, new JSONHandler(isFirst,getActivity(),getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                if(!obj.isNull("result")){
                    JSONObject result=obj.optJSONObject("result");
                    page=result.optInt("page");
                    pages=result.optInt("pages");
                    if(!result.isNull("count_teacher")){
                        if(tv_teacher_num.getVisibility()==View.GONE){
                            tv_teacher_num.setVisibility(View.VISIBLE);
                        }
                        tv_teacher_num.setText("共"+result.optString("count_teacher")+"位名师加入");
                    }
                    if(!result.isNull("series")){
                        if(isRush){
                            isRush=false;
                            infolist.clear();
                        }
                        if(isFirst){
                            infolist.clear();
                        }
                        infolist.addAll(JSONParse.getSeriesList(result.optJSONArray("series")));
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
                }
                isFirst=false;
                video_list.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                video_list.onRefreshComplete();
                if(isVideo&&infolist.size()>0&&!infolist.get(0).getIs_series()){
                    infolist.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_teacher_num:
                ShowActivity.showActivity(getActivity(), VideoTeacherActivity.class);
                break;
        }
    }

    @Override
    public void scrollTop() {
        if(groupRadioGone!=null){
            groupRadioGone.onGone();
        }
        if(!isVideo){
            if(tv_teacher_num.getVisibility()==View.GONE){
                tv_teacher_num.setVisibility(View.VISIBLE);
            }
        }else{
            if(tv_teacher_num.getVisibility()==View.VISIBLE){
                tv_teacher_num.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void scrollDown() {
        if(groupRadioGone!=null){
            groupRadioGone.onVisible();
        }
    }

    @Override
    public void scrollBottom() {
        if(groupRadioGone!=null){
            groupRadioGone.onGone();
        }
    }
}
