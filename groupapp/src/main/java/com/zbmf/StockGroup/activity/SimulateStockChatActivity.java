package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.fragment.LatestTradeFragment;
import com.zbmf.StockGroup.fragment.StockRankFragment;
import com.zbmf.StockGroup.fragment.TraderFragment;
import com.zbmf.StockGroup.utils.JSONParse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.StockGroup.R.id.viewpager;

//simulate stock home page 聊牛股页面
public class SimulateStockChatActivity extends BaseActivity {

    private SlidingTabLayout mTab;
    private ViewPager mViewpager;
    private List<Fragment> mList;
    private List<String> title_list;
    private MatchInfo matchInfo;
//    private LatestCommentFragment latestCommitFragment;
    private LatestTradeFragment latestTradeFragment;
    private StockRankFragment stockRankFragment;
    private TraderFragment traderFragment;
    private int select;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_simulate_stock_chat;
    }

    @Override
    public void initView() {
        initTitle("排行榜");
        initFragment();
        mTab = (SlidingTabLayout) findViewById(R.id.tablayout);
        mViewpager = (ViewPager) findViewById(viewpager);
        mTab.setVisibility(View.VISIBLE);
        getTitleLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (select) {
                    case 0:
                        latestTradeFragment.scrollTop();
                        break;
                    case 1:
                        stockRankFragment.scrollTop();
                        break;
                }
            }
        });
    }

    private void initFragment() {
        mList = new ArrayList<>();
        title_list = new ArrayList<>();
        title_list.add("最新交易");
        title_list.add("排行榜");
        title_list.add("操盘高手");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            select = bundle.getInt(IntentKey.SELECT, 0);
            matchInfo = (MatchInfo) bundle.getSerializable(IntentKey.MATCH_BAEN);
//            latestCommitFragment = LatestCommentFragment.newInstance(matchInfo);
            latestTradeFragment = LatestTradeFragment.newInstance(matchInfo);//最新交易
            stockRankFragment = StockRankFragment.newInstance(matchInfo);//排行榜
            traderFragment = TraderFragment.newInstance(matchInfo);//操盘高手
//            mList.add(latestCommitFragment);
            mList.add(latestTradeFragment);
            mList.add(stockRankFragment);
            mList.add(traderFragment);
        }
    }

    @Override
    public void initData() {
        ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        mViewpager.setAdapter(adapter);
        mTab.setViewPager(mViewpager);
        mViewpager.setOffscreenPageLimit(mList.size());
        mViewpager.setCurrentItem(select);
    }

    @Override
    public void addListener() {
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                select = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPlayerMessage();
    }

    private void getPlayerMessage() {
//        WebBase.getPlayer(new JSONHandler() {
        WebBase.getMatchPlayer(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
//                matchInfo = JSONParse.getMatchMessage(obj);
                matchInfo = JSONParse.getMatchMessage1(obj);
                if (matchInfo != null) {
//                    latestCommitFragment.OnRushMatch(matchInfo);
                    latestTradeFragment.OnRushMatch(matchInfo);
                    stockRankFragment.OnRushMatch(matchInfo);
                    traderFragment.OnRushMatch(matchInfo);
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
}
