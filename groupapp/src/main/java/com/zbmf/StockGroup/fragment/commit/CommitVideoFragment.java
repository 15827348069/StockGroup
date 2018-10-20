package com.zbmf.StockGroup.fragment.commit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.zbmf.StockGroup.activity.SeriesVideoActivity;
import com.zbmf.StockGroup.activity.VideoPlayActivity;
import com.zbmf.StockGroup.adapter.VideoItemAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.constans.VideoItemType;
import com.zbmf.StockGroup.fragment.BaseFragment;
import com.zbmf.StockGroup.interfaces.TeacherToStudy;
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
 * 订阅的视频
 */
public class CommitVideoFragment extends BaseFragment {
    private int page,pages;
    private List<Video>infolist;
    private VideoItemAdapter adapter;
    private PullToRefreshListView listview;
    private boolean isRush;
    private Map<String,String> param;
    private boolean isFirst=true;
    private LinearLayout ll_none;
    private TextView no_message_text,right_button;
    private TeacherToStudy teacherToStudy;

    public void setTeacherToStudy(TeacherToStudy teacherToStudy) {
        this.teacherToStudy = teacherToStudy;
    }

    public static CommitVideoFragment newInstance() {
        CommitVideoFragment fragment = new CommitVideoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_coupon_used, null);
    }

    @Override
    protected void initView() {
        ll_none=getView(R.id.ll_none);
        no_message_text=getView(R.id.no_message_text);
        right_button=getView(R.id.tv_right_button);
        no_message_text.setText(getString(R.string.no_msg));
        right_button.setVisibility(View.VISIBLE);
        right_button.setText(getString(R.string.to_study));
        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(teacherToStudy!=null){
                    teacherToStudy.toStudy();
                }
            }
        });

        listview=getView(R.id.listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video video=infolist.get(position-1);
                if(video.getIs_series()){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(IntentKey.VIDEO_SERIES,video.getSeriesVideo());
                    ShowActivity.showActivity(getActivity(),bundle,SeriesVideoActivity.class);
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
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                page+=1;
                getVideo();
            }
        });
    }
    public void setLoginMessage(final Video video){
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

    @Override
    protected void initData() {
        //初始化数据
        if(infolist==null){
            infolist=new ArrayList<>();
        }else{
            infolist.clear();
        }
        adapter=new VideoItemAdapter(getActivity(),infolist);
        adapter.setVideoViewType(VideoItemType.VIDEO);
        listview.setAdapter(adapter);
        rush();
    }
    public void rush(){
        if(infolist ==null){
            infolist=new ArrayList<>();
        }
        isRush=true;
        page=1;
        getVideo();
    }
    public void getVideo(){
        if(param==null){
            param=new HashMap<>();
        }
        WebBase.GetPayVideoLog(page,new JSONHandler(isFirst,getActivity(),getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                listview.onRefreshComplete();
                JSONObject result=obj.optJSONObject("result");
                page=result.optInt("page");
                pages=result.optInt("pages");
                if(isRush){
                    isRush=false;
                    infolist.clear();
                }
                infolist.addAll(JSONParse.getPayVideoList(result));
                adapter.notifyDataSetChanged();
                if(infolist.size()!=0){
                    ll_none.setVisibility(View.GONE);
                }else{
                    ll_none.setVisibility(View.VISIBLE);
                }
                if(isFirst){
                    isFirst=false;
                }
            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
                listview.onRefreshComplete();
                if(isFirst){
                    isFirst=false;
                }
            }
        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
