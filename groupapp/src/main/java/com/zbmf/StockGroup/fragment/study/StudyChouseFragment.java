package com.zbmf.StockGroup.fragment.study;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveLoginListener;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.PublishInfo;
import com.bokecc.sdk.mobile.live.pojo.RoomInfo;
import com.bokecc.sdk.mobile.live.pojo.TemplateInfo;
import com.bokecc.sdk.mobile.live.pojo.Viewer;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.SearchStockBlogActivity;
import com.zbmf.StockGroup.activity.SeriesVideoActivity;
import com.zbmf.StockGroup.activity.VideoPlayActivity;
import com.zbmf.StockGroup.adapter.ChouseVideoAdapter;
import com.zbmf.StockGroup.adapter.StudyStockAdapter;
import com.zbmf.StockGroup.adapter.VideoLivingAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.HomeImage;
import com.zbmf.StockGroup.beans.NewsFeed;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.constans.StudyBlog;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.utils.WebClickUitl;
import com.zbmf.StockGroup.view.GridViewForScrollView;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zbmf.StockGroup.constans.StudyBlog.zbmf_cwfx;
import static com.zbmf.StockGroup.constans.StudyBlog.zbmf_gpfx;
import static com.zbmf.StockGroup.constans.StudyBlog.zbmf_hgfx;
import static com.zbmf.StockGroup.constans.StudyBlog.zbmf_jczs;
import static com.zbmf.StockGroup.constans.StudyBlog.zbmf_jyzn;
import static com.zbmf.StockGroup.constans.StudyBlog.zbmf_xgff;

/**
 * Created by xuhao on 2017/11/10.
 */

public class StudyChouseFragment extends Fragment implements View.OnClickListener {
    private PullToRefreshScrollView pull_to_refresh_scrollview;
    private VideoLivingAdapter adapter;
    private ListViewForScrollView list_live,list_stock_study;
    private List<Video>videoList;
    private List<HomeImage>chouseList;
    private GridViewForScrollView grid_view_chouse;
    private ChouseVideoAdapter chouseVideoAdapter;
    private boolean video,chouse,blog;
    private List<BlogBean>bloglist;
    private StudyStockAdapter studyStockAdapter;
    private GroupRadioGone groupRadioGone;
    private Animation wheelAnimation;
    private ImageView imv_rush;
    private TextView tv_living;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_jczs:
                showSearchStockBlog(zbmf_jczs);
                break;
            case R.id.tv_hgfx:
                showSearchStockBlog(zbmf_hgfx);
                break;
            case R.id.tv_cwfx:
                showSearchStockBlog(zbmf_cwfx);
                break;
            case R.id.tv_xgff:
                showSearchStockBlog(zbmf_xgff);
                break;
            case R.id.tv_jyzn:
                showSearchStockBlog(zbmf_jyzn);
                break;
            case R.id.tv_gpfx:
                showSearchStockBlog(zbmf_gpfx);
                break;
            case R.id.lv_change:
                chouse=false;
                imv_rush.startAnimation(wheelAnimation);
                getUserBlogs();
                break;
            case R.id.tv_search:
                ShowActivity.showActivity(getActivity(), SearchStockBlogActivity.class);
                break;
            case R.id.tv_all_video:
                if(onMoreVideo!=null){
                    onMoreVideo.onMoreVideo();
                }
                break;
        }
    }
    private void showSearchStockBlog(String id){
        Bundle bundle=new Bundle();
        bundle.putString(IntentKey.BLOG_ID,id);
        ShowActivity.showActivity(getActivity(),bundle, SearchStockBlogActivity.class);
    }
    public interface GroupRadioGone{
        void onGone();
        void onVisible();
    }
    private onMoreVideo onMoreVideo;

    public void setOnMoreVideo(onMoreVideo onMoreVideo) {
        this.onMoreVideo = onMoreVideo;
    }

    public interface onMoreVideo{
        void onMoreVideo();
    }

    public void setGroupRadioGone(GroupRadioGone radioGone){
        this.groupRadioGone=radioGone;
    }

    public static StudyChouseFragment newInstance(){
        StudyChouseFragment fragment=new StudyChouseFragment();
        return fragment;
    }
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_study_chouse,null);
        }
        initView();
        initListener();
        initDate();
       return view;
    }
    private void initListener(){
        view.findViewById(R.id.tv_jczs).setOnClickListener(this);
        view.findViewById(R.id.tv_hgfx).setOnClickListener(this);
        view.findViewById(R.id.tv_cwfx).setOnClickListener(this);
        view.findViewById(R.id.tv_xgff).setOnClickListener(this);
        view.findViewById(R.id.tv_jyzn).setOnClickListener(this);
        view.findViewById(R.id.tv_gpfx).setOnClickListener(this);
        view.findViewById(R.id.tv_all_video).setOnClickListener(this);
        view.findViewById(R.id.lv_change).setOnClickListener(this);
        view.findViewById(R.id.tv_search).setOnClickListener(this);
    }
    private void initView(){
        tv_living= (TextView) view.findViewById(R.id.layout_living);
        view.findViewById(R.id.lv_change).setVisibility(View.VISIBLE);
        view.findViewById(R.id.stock_blog_title).setVisibility(View.VISIBLE);
        pull_to_refresh_scrollview= (PullToRefreshScrollView) view.findViewById(R.id.pull_to_refresh_scrollview);
        pull_to_refresh_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pull_to_refresh_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                OnRushMessage();
            }
        });
        list_live= (ListViewForScrollView) view.findViewById(R.id.list_live);
        grid_view_chouse= (GridViewForScrollView) view.findViewById(R.id.grid_view_chouse);
        list_stock_study= (ListViewForScrollView) view.findViewById(R.id.list_stock_study);
        list_live.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showVideoDeatil((Video) adapter.getItem(position));
            }
        });
        grid_view_chouse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url=chouseList.get(position).getLink_url();
                if(url!=null||! TextUtils.isEmpty(url)){
                    WebClickUitl.ShowActivity(getActivity(),url);
                }
            }
        });
        list_stock_study.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowActivity.showBlogDetailActivity(getActivity(), (BlogBean) studyStockAdapter.getItem(position));
            }
        });
        imv_rush= (ImageView) view.findViewById(R.id.imv_rush);
        wheelAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.circle_progress_ami);
    }
    public void OnRushMessage(){
        blog=false;
        video=false;
        chouse=false;
        getVideoMessage();
        getRecommend();
        getUserBlogs();
    }
    private void initDate(){
        videoList=new ArrayList<>();
        adapter=new VideoLivingAdapter(getActivity());
        adapter.setList(videoList);
        list_live.setAdapter(adapter);
        getVideoMessage();

        chouseList=new ArrayList<>();
        chouseVideoAdapter=new ChouseVideoAdapter(getActivity());
        chouseVideoAdapter.setList(chouseList);
        grid_view_chouse.setAdapter(chouseVideoAdapter);
        getRecommend();

        bloglist=new ArrayList<>();
        studyStockAdapter=new StudyStockAdapter(getActivity());
        studyStockAdapter.setList(bloglist);
        list_stock_study.setAdapter(studyStockAdapter);
        getUserBlogs();
    }

    /**
     * 获取正在直播
     */
    private void getVideoMessage(){
        Map<String ,String>param=new HashMap<>();
        param.put("is_live","1");
        WebBase.GetsVideos(param, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                if(!result.isNull("videos")){
                    videoList.clear();
                    videoList.addAll(JSONParse.getVideoList(result.optJSONArray("videos")));
                }
                if(videoList.size()==0){
                    if(tv_living.getVisibility()==View.VISIBLE){
                        tv_living.setVisibility(View.GONE);
                    }
                    if(list_live.getVisibility()==View.VISIBLE){
                        list_live.setVisibility(View.GONE);
                    }
                }else{
                    if(tv_living.getVisibility()==View.GONE){
                        tv_living.setVisibility(View.VISIBLE);
                    }
                    if(list_live.getVisibility()==View.GONE){
                        list_live.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }

                video=true;
                RushScrollView();
            }

            @Override
            public void onFailure(String err_msg) {
                video=true;
                RushScrollView();
            }
        });
    }

    /**
     * 获取推荐视频
     */
    private void getRecommend(){
        WebBase.StudyRecommend(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                chouseList.clear();
                if(obj.has("recommed")){
                    chouseList.addAll(JSONParse.getHomeImageList(obj.optJSONArray("recommed")));
                    chouseVideoAdapter.notifyDataSetChanged();
                }
                chouse=true;
                RushScrollView();
            }

            @Override
            public void onFailure(String err_msg) {
                chouse=true;
                RushScrollView();
            }
        });
    }

    /**
     * 获取炒股入门数据
     */
    private void getUserBlogs(){
        WebBase.getRudimentsBlogs(StudyBlog.zbmf_jczs,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                bloglist.clear();
                if(obj.has("result")){
                    JSONObject result=obj.optJSONObject("result");
                    if(result!=null&&result.has("blogs")){
                        bloglist.addAll(JSONParse.getBlogBean(result.optJSONArray("blogs")));
                    }
                    studyStockAdapter.notifyDataSetChanged();
                }
                blog=true;
                CancelAnimation();
            }

            @Override
            public void onFailure(String err_msg) {
                blog=true;
                RushScrollView();
                CancelAnimation();
            }
        });
    }
    private void CancelAnimation(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wheelAnimation.cancel();
                imv_rush.clearAnimation();
            }
        },500);
    }
    private void RushScrollView(){
        if( pull_to_refresh_scrollview!=null&& pull_to_refresh_scrollview.isRefreshing())
        pull_to_refresh_scrollview.onRefreshComplete();
    }
    private void showVideoDeatil(Video video){
        if(video.getIs_live()){
            setLoginMessage(video);
        }else{
            Bundle bundle=new Bundle();
            bundle.putSerializable(IntentKey.VIDEO_KEY,video);
            ShowActivity.showActivity(getActivity(),bundle, VideoPlayActivity.class);
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

}
