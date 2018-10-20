package com.zbmf.StockGroup.fragment.care;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.LoginActivity;
import com.zbmf.StockGroup.adapter.RecommendAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.fragment.BaseFragment;
import com.zbmf.StockGroup.fragment.teacher.RankingFragment;
import com.zbmf.StockGroup.interfaces.LoadFinish;
import com.zbmf.StockGroup.interfaces.TeacherToStudy;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.CustomViewpager;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/8/16.
 */

public class RankTeacherFragment extends BaseFragment implements RecommendAdapter.OnCareClink {
    private ListViewForScrollView recommend_list;
    private RecommendAdapter adapter;
    private List<Group> infolist;
    private int page, pages;
    private int flags;
    private boolean isRush, isFirst = true;
    private CustomViewpager customViewpager;

    public void setCustomViewPage(CustomViewpager customViewPage) {
        this.customViewpager = customViewPage;
    }
    public static RankTeacherFragment newInstance(int flag) {
        RankTeacherFragment fragment = new RankTeacherFragment();
        Bundle args = new Bundle();
        args.putInt(IntentKey.FLAG, flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            flags = bundle.getInt(IntentKey.FLAG);
        }
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_care_fans_layout, null);
    }

    @Override
    protected void initView() {
        recommend_list = getView(R.id.lv_focus);
        infolist = new ArrayList<>();
        adapter = new RecommendAdapter(getActivity(), infolist);
        adapter.setOnCareClink(this);
        recommend_list.setAdapter(adapter);
        recommend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowActivity.showGroupDetailActivity(getActivity(),  infolist.get(position));
            }
        });
    }

    @Override
    protected void initData() {
        if(!SettingDefaultsManager.getInstance().authToken().isEmpty()){
            page = 1;
            isRush = true;
            getRecommend();
        }
    }

    public void rushList() {
        if(!SettingDefaultsManager.getInstance().authToken().isEmpty()){
            page = 1;
            isRush = true;
            getRecommend();
        }
    }

    public void getMore() {
        page += 1;
        getRecommend();
    }
    public void setViewPageHeight(int position){
        if(customViewpager!=null){
            customViewpager.setObjectForPosition(getFragmentView(),position);
            customViewpager.resetHeight(position);
        }
    }
    @Override
    public void onCareClink(final int position) {
        if (SettingDefaultsManager.getInstance().authToken() == null || SettingDefaultsManager.getInstance().authToken().equals("")) {
            showToast("登陆后才可关注！");
            infolist.get(position).setIs_recommend(0);
            adapter.notifyDataSetChanged();
            ShowActivity.showActivity(getActivity(), LoginActivity.class);
        } else {
            String group_id = infolist.get(position).getId();
            WebBase.follow(group_id, new JSONHandler(true, getActivity(), "正在关注圈主...") {
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

    private void getRecommend() {
        WebBase.getTeaCher(flags, page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONArray recommend = null;
                if (obj.isNull("result")) {
                    if (!obj.isNull("groups")) {
                        recommend = obj.optJSONArray("groups");
                    }
                } else {
                    JSONObject result = obj.optJSONObject("result");
                    page = result.optInt("page");
                    pages = result.optInt("pages");
                    if (!result.isNull("groups")) {
                        recommend = result.optJSONArray("groups");
                    }
                }
                if (isRush) {
                    infolist.clear();
                    isRush = false;
                }
                if (recommend != null) {
                    infolist.addAll(JSONParse.getGroupList(recommend));
                    adapter.notifyDataSetChanged();
                }
                if(loadFinish!=null){
                    loadFinish.onFinish();
                }
                isFirst = false;
            }

            @Override
            public void onFailure(String err_msg) {
                if(loadFinish!=null){
                    loadFinish.onFinish();
                }
            }
        });
    }
    private LoadFinish loadFinish;
    public void setLoadFinish(LoadFinish loadFinish) {
        this.loadFinish = loadFinish;
    }
}
