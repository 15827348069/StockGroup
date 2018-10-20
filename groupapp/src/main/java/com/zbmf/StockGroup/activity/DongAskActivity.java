package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.fragment.ask.AllAskFragment;
import com.zbmf.StockGroup.fragment.ask.MyAskFragment;
import com.zbmf.StockGroup.fragment.ask.TimeAskFragment;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xuhao on 2018/1/30.
 */

public class DongAskActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private SlidingTabLayout mTab;
    private ViewPager mViewpager;
    private List<Fragment> mList;
    private List<String> title_list;
    private String positionStr;
    private TimeAskFragment timeAskFragment;
    private MyAskFragment myAskFragment;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_dong_ask_layout;
    }

    @Override
    public void initView() {
        initTitle("董秘问答");
        mTab = (SlidingTabLayout) findViewById(R.id.dong_tab_layout);
        mViewpager = (ViewPager) findViewById(R.id.viewpager_teacher);
        initFragment();
    }

    private void initFragment() {
        mList = new ArrayList<>();
        title_list = Arrays.asList(getResources().getStringArray(R.array.dong_ask));
        timeAskFragment = TimeAskFragment.newsIntance();
        myAskFragment = MyAskFragment.newsIntance();
        mList.add(AllAskFragment.newsIntance());
        mList.add(myAskFragment);
        mList.add(timeAskFragment);
    }

    @Override
    public void initData() {
        ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        mViewpager.setAdapter(adapter);
        mTab.setViewPager(mViewpager);
        mViewpager.setOffscreenPageLimit(mList.size());
        getPoint();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int select = bundle.getInt(IntentKey.SELECT);
            mViewpager.setCurrentItem(select, false);
            if(!SettingDefaultsManager.getInstance().authToken().isEmpty()){
                switch (select){
                    case 1:
                        positionStr = "reply";
                        break;
                    case 2:
                        positionStr = "notice";
                        break;
                }
                getPoint();
            }
        }
    }

    @Override
    public void addListener() {
        mViewpager.addOnPageChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.ASK_DONG:
                    mViewpager.setCurrentItem(1, false);
                    myAskFragment.rushList();
                    break;
                case RequestCode.ADD_SETTING:
                    timeAskFragment.rushList();
                    break;
            }
        }
        if(requestCode==RequestCode.LOGIN&&!SettingDefaultsManager.getInstance().authToken().isEmpty()){
            timeAskFragment.rushList();
            myAskFragment.rushList();
        }
    }

    private void getPoint() {
        WebBase.reMind(positionStr, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.has("remind")) {
                    JSONObject remind = obj.optJSONObject("remind");
                    if (remind.optInt("reply") > 0) {
                        mTab.showDot(1);
                    } else {
                        mTab.showMsg(1, 0);
                    }
                    if (remind.optInt("notice") > 0) {
                        mTab.showDot(2);
                    } else {
//                        mTab.showDot(2);
                        mTab.showMsg(2, 0);
                    }
                }

            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position!=0){
            if(ShowActivity.isLogin(this)){
                switch (position) {
                    case 1:
                        positionStr = "reply";
                        getPoint();
                        timeAskFragment.DissMissPop();
                        break;
                    case 2:
                        positionStr = "notice";
                        getPoint();
                        break;
                }
            }else{
                mViewpager.setCurrentItem(0);
            }
        } else{
            positionStr = null;
            timeAskFragment.DissMissPop();
            getPoint();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
