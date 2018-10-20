package com.zbmf.StocksMatch.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.MatchFragmentAdapter;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.fragment.rank.MatchRankFragment;
import com.zbmf.StocksMatch.fragment.rank.NewDiealFragment;
import com.zbmf.StocksMatch.fragment.rank.TraderFragment;
import com.zbmf.StocksMatch.listener.LastUpdateTime;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xuhao
 * on 2017/12/1.
 */

public class MatchRankActivity extends BaseActivity<BasePresenter> implements LastUpdateTime {
    @BindView(R.id.rank_tab_layout)
    TabLayout rankTabLayout;
    @BindView(R.id.rank_viewpager)
    ViewPager rankViewpager;
    @BindView(R.id.lastly_update_time)
    TextView lastlyUpdateTime;
    private boolean mTraderHide;
    private String[] titleTag = new String[]{"最新交易", "日榜", "周榜", "月榜", "总榜"};

    @Override
    protected int getLayout() {
        return R.layout.activity_match_rank_layout;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.match_rank);
    }

    @Override
    protected void initData(Bundle bundle) {
        ShowOrHideProgressDialog.showProgressDialog(this,this,getString(R.string.hard_loading));
        MyActivityManager.getMyActivityManager().pushAct(this);
        if (bundle != null) {
            String match_id = bundle.getString(IntentKey.MATCH_ID);
            int flag = bundle.getInt(IntentKey.FLAG);
            String myFlag = bundle.getString(IntentKey.MY_FLAG);
            String matchName = bundle.getString(IntentKey.MATCH_NAME);
            mTraderHide = bundle.getBoolean(IntentKey.TRADER_HIDE);
            List<Fragment> fragmentList = new ArrayList<>();
            fragmentList.add(NewDiealFragment.newInstance(match_id));//最新交易
            MatchRankFragment matchRankFragment = MatchRankFragment.newInstance(match_id, Constans.DAY_RANK, myFlag, matchName);
            fragmentList.add(matchRankFragment);
            matchRankFragment = MatchRankFragment.newInstance(match_id, Constans.WEEK_RANK, myFlag, matchName);
            fragmentList.add(matchRankFragment);
            matchRankFragment = MatchRankFragment.newInstance(match_id, Constans.MOUNTH_RANK, myFlag, matchName);
            fragmentList.add(matchRankFragment);
            matchRankFragment = MatchRankFragment.newInstance(match_id, Constans.ALL_RANK, myFlag, matchName);
            fragmentList.add(matchRankFragment);
            if (!mTraderHide) {
                fragmentList.add(TraderFragment.newInstance(match_id));//操盘高手
            }
            matchRankFragment.setLastUpdateTime(this);
            /*fragmentList.add(MatchRankFragment.newInstance(match_id,Constans.RANK_LIST));//排行榜*/
            /*fragmentList.add(MatchRankFragment.newInstance(match_id,Constans.LASTLY_DEALS));//最新交易*/
            MatchFragmentAdapter adapter;
            if (!mTraderHide) {
                adapter = new MatchFragmentAdapter(this, getSupportFragmentManager(),
                        fragmentList, Arrays.asList(getResources().getStringArray(R.array.match_rank_tag)));
            } else {
                adapter = new MatchFragmentAdapter(this, getSupportFragmentManager(),
                        fragmentList, Arrays.asList(titleTag));
            }
            rankViewpager.setAdapter(adapter);
            rankTabLayout.setupWithViewPager(rankViewpager);
            rankViewpager.setOffscreenPageLimit(fragmentList.size());
            setViewPagerWithCurrent(flag);
        }
    }

    private void setViewPagerWithCurrent(int flag) {
        if (flag == Constans.NEW_DEAL_FLAG) {
            rankViewpager.setCurrentItem(0);
            lastlyUpdateTime.setVisibility(View.GONE);
        } else if (flag == Constans.DAY_RANK_FLAG) {
            rankViewpager.setCurrentItem(1);
            lastlyUpdateTime.setVisibility(View.VISIBLE);
        } else if (flag == Constans.WEEK_RANK_FLAG) {
            rankViewpager.setCurrentItem(2);
            lastlyUpdateTime.setVisibility(View.VISIBLE);
        } else if (flag == Constans.MONTH_RANK_FLAG) {
            rankViewpager.setCurrentItem(3);
            lastlyUpdateTime.setVisibility(View.VISIBLE);
        } else if (flag == Constans.ALL_RANK_FLAG) {
            rankViewpager.setCurrentItem(4);
            lastlyUpdateTime.setVisibility(View.VISIBLE);
        } else if (flag == Constans.TRADER_RANK_FLAG) {
            if (!mTraderHide) {
                rankViewpager.setCurrentItem(5);
                lastlyUpdateTime.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected BasePresenter initPresent() {
        return null;
    }

    @Override
    public void lastUpdateTIme(String updateTime) {
        assert lastlyUpdateTime != null;
        lastlyUpdateTime.setText(updateTime);
    }
}
