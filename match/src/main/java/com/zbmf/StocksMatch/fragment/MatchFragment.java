package com.zbmf.StocksMatch.fragment;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.activity.SearchActivity;
import com.zbmf.StocksMatch.adapter.MatchFragmentAdapter;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.fragment.drill.MatchCityFragment;
import com.zbmf.StocksMatch.fragment.drill.MatchListFragment;
import com.zbmf.StocksMatch.listener.IMainActivityCilck;
import com.zbmf.worklibrary.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xuhao
 * on 2017/11/27.
 * 比赛
 */

public class MatchFragment extends BaseFragment implements IMainActivityCilck,View.OnClickListener{

    @BindView(R.id.timeline_tablayout)
    TabLayout timelineTablayout;
    @BindView(R.id.timeline_viewpager)
    ViewPager timelineViewpager;
    @BindView(R.id.searchBtn)
    ImageView searchBtn;

    public static MatchFragment newInstance() {
        MatchFragment fragment = new MatchFragment();
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_match_layout;
    }

    @Override
    protected BasePresenter initPresent() {
        return null;
    }

    @Override
    protected void initView() {
        setTitleMessage(getString(R.string.match));
        searchBtn.setVisibility(View.VISIBLE);
        searchBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        List<Fragment>infolist=new ArrayList<>();
        infolist.add(MatchCityFragment.newInstance(Constans.CITY));
        infolist.add(MatchCityFragment.newInstance(Constans.SCHOOL));
        infolist.add(MatchCityFragment.newInstance(Constans.BUSINESS));
        infolist.add(MatchListFragment.newInstance(Constans.NEW_MATCH));
        infolist.add(MatchListFragment.newInstance(Constans.All_MATCH));
        MatchFragmentAdapter matchFragmentAdapter=new MatchFragmentAdapter(getActivity(),
                getChildFragmentManager(),
                infolist,
                Arrays.asList(getResources().getStringArray(R.array.match_tag)));
        timelineViewpager.setAdapter(matchFragmentAdapter);
        timelineTablayout.setupWithViewPager(timelineViewpager);
        timelineViewpager.setOffscreenPageLimit(infolist.size());
    }

    @Override
    public void onCityClick() {
        timelineViewpager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.searchBtn://点击跳转搜索页面
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }
    }
}
