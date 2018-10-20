package com.zbmf.StockGroup.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.zbmf.StockGroup.GroupActivity;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.CareTeacherActivity;
import com.zbmf.StockGroup.activity.FansDiscountsActivity;
import com.zbmf.StockGroup.activity.GoldStockActivity;
import com.zbmf.StockGroup.activity.SearchActivity;
import com.zbmf.StockGroup.adapter.CareAdapter;
import com.zbmf.StockGroup.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.db.DBManager;
import com.zbmf.StockGroup.db.Database;
import com.zbmf.StockGroup.fragment.care.RankTeacherFragment;
import com.zbmf.StockGroup.interfaces.LoadFinish;
import com.zbmf.StockGroup.listener.ScrollViewChangeListener;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.CustomViewpager;
import com.zbmf.StockGroup.view.OverscrollHelper;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by xuhao
 * on 2017/8/16.
 * 圈子 fragment
 */

public class CareFragments extends BaseFragment implements ViewPager.OnPageChangeListener, LoadFinish,
        /*PullToRefreshScrollView.onScrolls,*/ View.OnClickListener ,ScrollViewChangeListener {
    private GroupActivity groupActivity;
    private List<Group> infolist;
    private RecyclerView home_tuijian;
    private CareAdapter adapter;

    private SlidingTabLayout mTab, care_top_tab_layout;
    public CustomViewpager mViewpager;
    private List<Fragment> mList;
    private List<String> title_list;
    private DBManager dbManager;
    private Database db;
    private boolean isFirstIn = true;
    private PullToRefreshScrollView sc_focus;
    private RankTeacherFragment recomedFragment, liveFragment, exclusiveFragment, arrowFragment;
    private int select;
    private TextView care_more_point;
    private RelativeLayout more_layout;
    private View care_line;
    private Button more_teacher_button;

    private LinearLayout ll_content;
    private TextView tv_noCare;
    private ImageView mImv_fans_coupons;
    private LinearLayout mLl;

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_care, null);
    }

    public static CareFragments newInstance() {
        CareFragments fragment = new CareFragments();
        return fragment;
    }

    @Override
    protected void initView() {
        dbManager = new DBManager(getContext());
        db = new Database(getContext());
        groupActivity = (GroupActivity) getActivity();
        ((TextView) getView(R.id.group_title_name)).setText("圈子");
        care_more_point = getView(R.id.care_more_point);
        more_layout = getView(R.id.more_layout);
        mImv_fans_coupons = getView(R.id.imv_fans_coupons);
        mLl = getView(R.id.ll);

        getView(R.id.group_title_return).setVisibility(View.GONE);
        getView(R.id.search_button).setVisibility(View.VISIBLE);
        getView(R.id.search_button).setOnClickListener(this);
        care_line = getView(R.id.care_line);
        home_tuijian = getView(R.id.homt_tuijian);
        more_teacher_button = getView(R.id.more_teacher_button);
        ll_content=getView(R.id.ll_content);
        tv_noCare=getView(R.id.tv_no_care);
        more_teacher_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.FLAG, 1);
                ShowActivity.showActivity(getActivity(), bundle, CareTeacherActivity.class.getName());
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);

      new OverscrollHelper().setScrollViewChangeListener(this);

        home_tuijian.setLayoutManager(gridLayoutManager);
        infolist = new ArrayList<>();
        adapter = new CareAdapter(getActivity(), infolist);
        adapter.setOnItemClickLitener(new CareAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                isFirstIn = true;
                ShowActivity.showChatActivity(getActivity(), infolist.get(position));
            }
        });
        home_tuijian.setAdapter(adapter);
        initCareFragment();

        sc_focus = getView(R.id.care_refresh);
        sc_focus.setMode(PullToRefreshBase.Mode.BOTH);

        getView(R.id.imv_god_stock).setOnClickListener(this);
        getView(R.id.imv_fans_coupons).setOnClickListener(this);

        boolean isShowFans = SettingDefaultsManager.getInstance().getIsShowFans();
        if (isShowFans){
            ViewGroup.LayoutParams lp = mLl.getLayoutParams();
            lp.width=ViewGroup.LayoutParams.WRAP_CONTENT;
            mLl.setLayoutParams(lp);
            mImv_fans_coupons.setVisibility(View.GONE);
        }else {
            ViewGroup.LayoutParams lp = mLl.getLayoutParams();
            lp.width=ViewGroup.LayoutParams.MATCH_PARENT;
            mLl.setLayoutParams(lp);
            mImv_fans_coupons.setVisibility(View.VISIBLE);
        }

        sc_focus.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                RunshList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                switch (select) {
                    case 0:
                        recomedFragment.getMore();
                        break;
                    case 1:
                        liveFragment.getMore();
                        break;
                    case 2:
                        exclusiveFragment.getMore();
                        break;
                    case 3:
                        arrowFragment.getMore();
                        break;
                }
            }
        });
//        sc_focus.setOnScroll(this);
    }

    private void rush() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sc_focus.onRefreshComplete();
            }
        }, 300);
    }

    @Override
    protected void initData() {
        isFirstIn = true;
        if(!SettingDefaultsManager.getInstance().authToken().isEmpty()){
            userGroups();
        }
    }

    private void RunshList() {
        if(!SettingDefaultsManager.getInstance().authToken().isEmpty()){
            userGroups();
            switch (select) {
                case 0:
                    recomedFragment.rushList();
                    break;
                case 1:
                    liveFragment.rushList();
                    break;
                case 2:
                    exclusiveFragment.rushList();
                    break;
                case 3:
                    arrowFragment.rushList();
                    break;
            }
        }
    }

    public void setData(boolean updata) {
        setinitData(updata);
    }

    public void updateDate(final boolean is_live, final int unmber) {
        if (getFragmentView() != null && care_more_point == null) {
            care_more_point = getView(R.id.care_more_point);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (infolist != null && adapter != null) {
                    for (Group group : infolist) {
                        if (is_live) {
                            int liveNo = dbManager.getUnredCount(group.getId());
                            group.setUnredcount(liveNo);
                        } else {
                            int chatNo = db.getChatUnReadNum(group.getId());
                            group.setChat(chatNo);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                if (care_more_point != null) {
                    if (unmber != 0 && is_live) {
                        care_more_point.setVisibility(View.VISIBLE);
                        if (unmber == 99) {
                            care_more_point.setText(unmber + "+");
                        } else {
                            care_more_point.setText(unmber + "");
                        }
                    } else {
                        care_more_point.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }, 200);
    }

    private void initCareFragment() {
        mList = new ArrayList<>();
        title_list = Arrays.asList(getResources().getStringArray(R.array.find_teacher));
        mTab = getView(R.id.care_tab_layout);
        care_top_tab_layout = getView(R.id.care_top_tab_layout);
        mViewpager = getView(R.id.care_viewpager_teacher);

        recomedFragment = RankTeacherFragment.newInstance(Constants.PEOPLE_RECOMED);
        recomedFragment.setCustomViewPage(mViewpager);
        recomedFragment.setLoadFinish(this);
        mViewpager.setObjectForPosition(recomedFragment.getFragmentView(), 0);

        liveFragment = RankTeacherFragment.newInstance(Constants.NOW_LIVE);
        liveFragment.setCustomViewPage(mViewpager);
        liveFragment.setLoadFinish(this);
        mViewpager.setObjectForPosition(liveFragment.getFragmentView(), 1);

        exclusiveFragment = RankTeacherFragment.newInstance(Constants.EXCLUSIVE);
        exclusiveFragment.setCustomViewPage(mViewpager);
        exclusiveFragment.setLoadFinish(this);
        mViewpager.setObjectForPosition(exclusiveFragment.getFragmentView(), 2);

        arrowFragment = RankTeacherFragment.newInstance(Constants.PEOPLE_ARROW);
        arrowFragment.setCustomViewPage(mViewpager);
        arrowFragment.setLoadFinish(this);
        mViewpager.setObjectForPosition(arrowFragment.getFragmentView(), 3);

        mList.add(recomedFragment);
        mList.add(liveFragment);
        mList.add(exclusiveFragment);
        mList.add(arrowFragment);

        ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getActivity().getSupportFragmentManager(), title_list, mList);
        mViewpager.setAdapter(adapter);
        mViewpager.setOffscreenPageLimit(mList.size());
        mViewpager.setOnPageChangeListener(this);
        mTab.setViewPager(mViewpager);
        care_top_tab_layout.setViewPager(mViewpager);
    }

    private void userGroups() {
        WebBase.userGroups(1, 3, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result = obj.optJSONObject("result");
                if (!result.isNull("groups")) {
                    if (isFirstIn) {
                        isFirstIn = false;
                    }
                    if (infolist == null) {
                        infolist = new ArrayList<Group>();
                    } else {
                        infolist.clear();
                    }
                    infolist.addAll(JSONParse.careGroups(result.optJSONArray("groups")));
                    for (Group group1 : infolist) {
                        int liveNo = dbManager.getUnredCount(group1.getId());
                        group1.setUnredcount(liveNo);
                    }
                    for (Group group1 : infolist) {
                        int chatNo = db.getChatUnReadNum(group1.getId())/*+dbManager.getOfflineMsgConunt(Constants.ROOM,group1.getId())*/;
                        group1.setChat(chatNo);
                    }
                    if (infolist.size() == 0) {
                        dbManager.setUnablemessage(null);
                        ll_content.setVisibility(View.INVISIBLE);
                        tv_noCare.setVisibility(View.VISIBLE);
                    } else if (infolist.size() < 3) {
                        ll_content.setVisibility(View.VISIBLE);
                        tv_noCare.setVisibility(View.INVISIBLE);
                        if (more_layout.getVisibility() == View.VISIBLE) {
                            more_layout.setVisibility(View.INVISIBLE);
                            care_line.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        ll_content.setVisibility(View.VISIBLE);
                        tv_noCare.setVisibility(View.INVISIBLE);
                        if (more_layout.getVisibility() == View.INVISIBLE) {
                            more_layout.setVisibility(View.VISIBLE);
                            care_line.setVisibility(View.VISIBLE);
                        }
                        sc_focus.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    groupActivity.setCare_menu_point();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                if (checkVa(err_msg)) {
                    SettingDefaultsManager.getInstance().setAuthtoken("");
                    ((GroupActivity) getActivity()).checked();
                } else {
                    Toast.makeText(getActivity(), err_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkVa(String err_msg) {
        if (err_msg.equals("用户登录失败或已过期") || err_msg.equals(Constants.NEED_LOGIN)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRush() {
        super.onRush();
        if (isFirstIn) {
            if (dbManager == null) {
                dbManager = new DBManager(getContext());
            }
            RunshList();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        select = position;
        mViewpager.resetHeight(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFinish() {
        rush();
        switch (select) {
            case 0:
                recomedFragment.setViewPageHeight(select);
                break;
            case 1:
                liveFragment.setViewPageHeight(select);
                break;
            case 2:
                exclusiveFragment.setViewPageHeight(select);
                break;
            case 3:
                arrowFragment.setViewPageHeight(select);
                break;
        }
    }

    @Override
    public void onScrollChanged(PullToRefreshScrollView scrollView, int x, int y, int oldx, int oldy) {
    }

    @Override
    public void scrollTop() {
    }

    @Override
    public void scrollDown() {
    }

    @Override
    public void scrollBottom() {
    }

    @Override
    public void onScroll(int x, int y) {
        int top = mTab.getTop();
        if (y >= top) {
            if (care_top_tab_layout.getVisibility() == View.GONE) {
                care_top_tab_layout.setVisibility(View.VISIBLE);
            }
        } else {
            if (care_top_tab_layout.getVisibility() == View.VISIBLE) {
                care_top_tab_layout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void scrollStop() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_fans_coupons:
                ShowActivity.showActivity(getActivity(), FansDiscountsActivity.class);
                break;
            case R.id.imv_god_stock:
                ShowActivity.showActivity(getActivity(), GoldStockActivity.class);
                break;
            case R.id.search_button:
                ShowActivity.showActivity(getActivity(), SearchActivity.class);
                break;
        }
    }
}
