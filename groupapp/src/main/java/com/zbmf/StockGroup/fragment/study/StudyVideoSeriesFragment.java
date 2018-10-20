package com.zbmf.StockGroup.fragment.study;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveLoginListener;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.PublishInfo;
import com.bokecc.sdk.mobile.live.pojo.RoomInfo;
import com.bokecc.sdk.mobile.live.pojo.TemplateInfo;
import com.bokecc.sdk.mobile.live.pojo.Viewer;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.VideoPlayActivity;
import com.zbmf.StockGroup.adapter.ChouseVideoAdapter;
import com.zbmf.StockGroup.adapter.VideoSeriesAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.beans.VideoTeacher;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.GridViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/11/14.
 */

public class StudyVideoSeriesFragment extends Fragment implements VideoSeriesAdapter.OnItemClickLitener {
    private View view;
    private List<VideoTeacher> infolist;
    private VideoSeriesAdapter adapter;
    private RecyclerView series_recycler;
    private List<Video>chouseList;
    private GridViewForScrollView grid_view_chouse;
    private ChouseVideoAdapter chouseVideoAdapter;
    private int page,pages;
    private boolean isRush;
    private PullToRefreshScrollView pull_to_refresh_scrollview;

    public static StudyVideoSeriesFragment newInstance(){
        StudyVideoSeriesFragment fragment=new StudyVideoSeriesFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_series_layout,null);
        }
        initView();
        initListener();
        initData();
        return view;
    }
    private void initView(){
        series_recycler= (RecyclerView) view.findViewById(R.id.series_recycler);
        grid_view_chouse= (GridViewForScrollView) view.findViewById(R.id.grid_view_chouse);
        pull_to_refresh_scrollview= (PullToRefreshScrollView) view.findViewById(R.id.pull_to_refresh_scrollview);
    }
    private void initListener(){
        grid_view_chouse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video video= (Video) chouseVideoAdapter.getItem(position);
                if(video!=null&&video.getSeriesVideo()!=null){
                    Video.SeriesVideo seriesVideo= video.getSeriesVideo();
                    if(seriesVideo!=null&&seriesVideo.getVideo()!=null){
                        Video video_into=seriesVideo.getVideo();
                        if(video_into.getIs_live()){
                            setLoginMessage(video_into);
                        }else{
                            Bundle bundle=new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY,video_into);
                            ShowActivity.showActivity(getActivity(),bundle, VideoPlayActivity.class);
                        }
                    }
                }
            }
        });
        pull_to_refresh_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                rushMessage();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page+=1;
                getSeries();
            }
        });
    }
    public void rushMessage(){
        page=1;
        isRush=true;
        getSeries();
        getTeacherList();
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
                LogUtil.e(e.getMessage());
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

    private void initData(){
        page=1;

        infolist=new ArrayList<>();
        adapter=new VideoSeriesAdapter(getActivity(),infolist);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        series_recycler.setLayoutManager(linearLayoutManager);
        adapter.setOnItemClickLitener(this);
        series_recycler.setAdapter(adapter);
        getTeacherList();

        chouseList=new ArrayList<>();
        chouseVideoAdapter=new ChouseVideoAdapter(getActivity());
        grid_view_chouse.setAdapter(chouseVideoAdapter);
        getSeries();
    }

    @Override
    public void onAskCilck(VideoTeacher teacher) {
        ShowActivity.showVideoListActivity(getActivity(),teacher);
    }

    private void getSeries(){
        WebBase.GetSeries(page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if(!obj.isNull("result")){
                    JSONObject result=obj.optJSONObject("result");
                    page=result.optInt("page");
                    pages=result.optInt("pages");
                    if(!result.isNull("series")){
                        if(isRush){
                            isRush=false;
                            chouseList.clear();
                        }
                        chouseList.addAll(JSONParse.getSeriesList(result.optJSONArray("series")));
                        if(chouseList.size()>0){
                            chouseVideoAdapter.setList(chouseList);
                        }
                    }
                }
                pull_to_refresh_scrollview.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                pull_to_refresh_scrollview.onRefreshComplete();
            }
        });
    }
    private void getTeacherList(){
        WebBase.GetTeachers(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if(!obj.isNull("result")){
                    JSONArray result=obj.optJSONArray("result");
                    if(infolist==null){
                        infolist=new ArrayList<VideoTeacher>();
                    }else{
                        infolist.clear();
                    }
                    infolist.addAll(JSONParse.getVideoTeacherList(result));
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(String err_msg) {
                if(isRush){
                    isRush=false;
                }
            }
        });
    }
}
