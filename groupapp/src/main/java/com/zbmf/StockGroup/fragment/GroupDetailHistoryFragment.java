package com.zbmf.StockGroup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.GroupDetailActivity;
import com.zbmf.StockGroup.adapter.HistoryDateAdapter;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuhao on 2017/2/25.
 */

public class GroupDetailHistoryFragment extends BaseFragment {
    private List<Date>infolist;
    private ListViewForScrollView group_detail_list;
    private HistoryDateAdapter adapter;
    private Group group_bean;
    public static GroupDetailHistoryFragment newInstance(Group group) {
        GroupDetailHistoryFragment fragment = new GroupDetailHistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable("GROUP", group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            group_bean = (Group) getArguments().getSerializable("GROUP");
        }
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.group_detail_fragment,null);
    }

    @Override
    protected void initView() {
        infolist=new ArrayList<>();
        adapter=new HistoryDateAdapter(getContext(),infolist,group_bean.getNick_name());
        group_detail_list=getView(R.id.group_detail_list);
        group_detail_list.setAdapter(adapter);
        group_detail_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                group_bean.setLive_history_date(infolist.get(i));
                ShowActivity.showLiveHistoryActivity(getActivity(),group_bean);
            }
        });

    }

    @Override
    protected void initData() {
        infolist.clear();
        infolist.addAll(getDate());
        adapter.notifyDataSetChanged();
        GroupDetailActivity activity= (GroupDetailActivity) getActivity();
        activity.viewPager.setObjectForPosition(getFragmentView(),3);
    }
    private List<Date> getDate(){
        List<Date>info=new ArrayList<>();
        for(int i = -1; i>= Constants.HISTORY_DAYS; i--){
            info.add(DateUtil.afterNDay(i));
        }
        return  info;
    }
    public void setGroup(Group group){
        this.group_bean=group;
        if(adapter!=null&&group!=null&&group.getNick_name()!=null){
            adapter.setGroup_nick_name(group_bean.getNick_name());
        }
    }
}
