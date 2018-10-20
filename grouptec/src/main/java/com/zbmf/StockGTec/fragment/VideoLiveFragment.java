package com.zbmf.StockGTec.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.activity.VideoLiveActivity;
import com.zbmf.StockGTec.activity.VideoPlayActivity;
import com.zbmf.StockGTec.adapter.ViedoAdapter;
import com.zbmf.StockGTec.api.AppUrl;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.Videos;
import com.zbmf.StockGTec.utils.Constants;
import com.zbmf.StockGTec.utils.JSONparse;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.ShowActivity;
import com.zbmf.StockGTec.view.PullToRefreshBase;
import com.zbmf.StockGTec.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class VideoLiveFragment extends Fragment {
    private String mParam1;
    private int page, PAGES;
    private ViedoAdapter adapter;
    private List<Videos> list = new ArrayList<>();
    private PullToRefreshListView listview;
    public static final String METHOD = AppUrl.groupFans;
    private String groupId = SettingDefaultsManager.getInstance().UserId();
    private int is_live;//视频(0)or直播(1) 不传全部
    private LinearLayout ll_none;

    public VideoLiveFragment() {

    }

    public static VideoLiveFragment newInstance(int is_live) {
        VideoLiveFragment fragment = new VideoLiveFragment();
        Bundle args = new Bundle();
        args.putInt("is_live", is_live);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            is_live = getArguments().getInt("is_live");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //787878166
        ll_none = (LinearLayout) view.findViewById(R.id.ll_none);
        listview = (PullToRefreshListView) view.findViewById(R.id.listview);
        adapter = new ViedoAdapter(getActivity(), list);
        listview.setAdapter(adapter);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                GetsVideos(1, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                GetsVideos(2, false);
            }
        });
        GetsVideos(1, false);

        adapter.setOnItemClickListener(new ViedoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Videos videos = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("video", videos);
                if (is_live == 1) {
                    ShowActivity.startActivity(getActivity(), bundle, VideoLiveActivity.class.getName());
                } else {
                    ShowActivity.startActivity(getActivity(), bundle, VideoPlayActivity.class.getName());
                }
            }
        });

        view.findViewById(R.id.btn_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qq_url="mqqwpa://im/chat?chat_type=crm&uin=787878166&version=1&src_type=web&web_src=http:://wpa.b.qq.com";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qq_url)));
            }
        });
    }


    private void GetsVideos(final int dir, boolean show) {
        if (dir == 2) {
            page++;
        } else
            page = 1;

        WebBase.GetsVideos(page, Constants.PER_PAGE, is_live, new JSONHandler(show, getActivity(), "正在加载...") {
            @Override
            public void onSuccess(JSONObject obj) {
                listview.onRefreshComplete();
                Videos videos = JSONparse.GetsVideos(obj);
                if (videos != null && videos.getList() != null) {
                    PAGES = videos.pages;
                    if (videos.getList().size() > 0){
                        if (dir == 1)
                            list.clear();
                        list.addAll(videos.getList());
                        ll_none.setVisibility(View.GONE);
                    }else{
                        ll_none.setVisibility(View.VISIBLE);
                    }
                }

                if (page == PAGES) {
//                    Toast.makeText(getContext(), "已加载全部数据", Toast.LENGTH_SHORT).show();
                    listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else
                    listview.setMode(PullToRefreshBase.Mode.BOTH);


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String err_msg) {
                listview.onRefreshComplete();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
