package com.zbmf.StockGroup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.GroupDetailActivity;
import com.zbmf.StockGroup.adapter.AskAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Ask;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.view.AddMoreLayout;
import com.zbmf.StockGroup.view.ListViewForScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/2/25.
 */

public class GroupDetailAskFragment extends BaseFragment implements AddMoreLayout.OnSendClickListener {
    private int page,pages;
    private AskAdapter mAdapter;
    private List<Ask> asks=new ArrayList<>();
    private ListViewForScrollView lv;
    private Group group;
    public static GroupDetailAskFragment newInstance(Group group) {
        GroupDetailAskFragment fragment = new GroupDetailAskFragment();
        Bundle args = new Bundle();
        args.putSerializable("GROUP", group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            group = (Group) getArguments().getSerializable("GROUP");
        }
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.group_detail_fragment,null);
    }
    @Override
    protected void initView() {
        lv = getView(R.id.group_detail_list);
        lv.addFootView(this);
        mAdapter = new AskAdapter(getActivity(),asks);
        lv.setAdapter(mAdapter);
    }
    @Override
    protected void initData() {
        userAsks();
    }

    private void userAsks() {
        lv.onLoad();
        WebBase.groupAnsweredAsks(group.getId(),page, SettingDefaultsManager.getInstance().authToken(), new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                Ask ask = JSONParse.getAsks(obj);
                if(ask != null){
                    page=ask.getPage();
                    pages=ask.getPages();
                    if(page==pages){
                        lv.addAllData();
                    }else{
                        lv.onStop();
                    }
                    if(ask.getAsks()!= null && ask.getAsks().size()>0){
                        asks.addAll(ask.getAsks());
                        mAdapter.notifyDataSetChanged();
                        GroupDetailActivity activity= (GroupDetailActivity) getActivity();
                        activity.viewPager.setObjectForPosition(getFragmentView(),2);
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                lv.onStop();
            }
        });

    }

    @Override
    public void OnSendClickListener(View view) {
        page+=1;
        userAsks();
    }
}
