package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.UserDetailRankAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Rank;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.listener.ScrollViewChangeListener;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.OverscrollHelper;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserDetailActivity extends BaseActivity implements View.OnClickListener, ScrollViewChangeListener {

    /* @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_user_detail);
     }*/
    private RoundedCornerImageView my_detail_avatar;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mWeekFirst;
    private TextView mTotalRank;
    private TextView mTotalYield;
    private TextView mSubmitBtnStock;
    private PullToRefreshScrollView mStock_detail_scrollview;
    private ListViewForScrollView mStock_detail_list;
    private UserDetailRankAdapter mUserDetailRankAdapter;
    private String mUserId;
    private int page, mTotal;
    private boolean isFirst;
    private List<Rank> rankList;
    private RelativeLayout mShadeView;
    private Button mOpenVIPBtn;
    private int mType, measuredHeight;
    private LinearLayout mTopView1;
    private LinearLayout mTopView2;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_user_detail;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.select_stock_match_zbmf));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUserId = extras.getString(IntentKey.USER_ID);
            mType = extras.getInt(IntentKey.FLAG);
        }
        my_detail_avatar = getView(R.id.my_detail_avatar);
        mTopView1 = getView(R.id.topView1);
        mTopView2 = getView(R.id.topView2);

        mTv1 = getView(R.id.tv1);
        mTv2 = getView(R.id.tv2);
        mWeekFirst = getView(R.id.weekFirst);
        mTotalRank = getView(R.id.totalRank);
        mTotalYield = getView(R.id.totalYield);
        mSubmitBtnStock = getView(R.id.submitBtnStock);
        mStock_detail_scrollview = getView(R.id.stock_detail_scrollview);
        mStock_detail_list = getView(R.id.stock_detail_list);
        mShadeView = getView(R.id.shade_view);
        TextView firstAddTv = getView(R.id.firstAddTv);
        firstAddTv.setVisibility(View.GONE);
        mOpenVIPBtn = getView(R.id.openVIPBtn);
        mStock_detail_scrollview.setMode(PullToRefreshBase.Mode.BOTH);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mStock_detail_list.setNestedScrollingEnabled(false);
//        }

        new OverscrollHelper().setScrollViewChangeListener(this);
        rankList = new ArrayList<>();
        mUserDetailRankAdapter = new UserDetailRankAdapter(this, this);
        mUserDetailRankAdapter.setList(rankList);
        mStock_detail_list.setAdapter(mUserDetailRankAdapter);
        if (mType == IntentKey.OTHER_TYPE) {
            mSubmitBtnStock.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        page = 1;
        isFirst = true;
        getUserSelectStockRecord(mUserId, String.valueOf(page), isFirst);
    }

    @Override
    public void addListener() {
        mSubmitBtnStock.setOnClickListener(this);
        mOpenVIPBtn.setOnClickListener(this);
        mStock_detail_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                isFirst = true;
                rankList.clear();
                getUserSelectStockRecord(mUserId, String.valueOf(page), isFirst);
                mStock_detail_scrollview.ScrollTop();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page += 1;
                isFirst = false;
                getUserSelectStockRecord(mUserId, String.valueOf(page), isFirst);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submitBtnStock) {
            //跳转至提交个股页面
            ShowActivity.showStockArgumentActivity(this);
        } else if (v.getId() == R.id.openVIPBtn) {
            ShowActivity.skipVIPActivity(this);
        }
    }

    //获取用户的选股记录
    private void getUserSelectStockRecord(String userId, final String page1, final boolean is_first) {
        WebBase.getUserSelectStockRecord(userId, page1, new JSONHandler(is_first, this, getString(R.string.loading)) {

            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")) {
                    JSONObject result = obj.optJSONObject("result");
                    if (result != null) {
                        page = result.optInt("page");
                        mTotal = result.optInt("total");
                        JSONArray ranks = result.optJSONArray("ranks");
                        JSONObject user = result.optJSONObject("user");
                        int user_id = 0;
                        String nickname = "";
                        String avatar = "";
                        if (user != null) {
                            user_id = user.optInt("user_id");
                            nickname = user.optString("nickname");
                            avatar = user.optString("avatar");
                            double total_score = user.optDouble("total_score");
                            double total_yield = user.optDouble("total_yield");
                            int total_rank = user.optInt("total_rank");
                            int top_times = user.optInt("top_times");
                            mTv1.setText(nickname);
                            mTv2.setText(String.format("总积分:%s", String.valueOf(total_score)));
                            ImageLoader.getInstance().displayImage(avatar, my_detail_avatar, ImageLoaderOptions.RoundedBitMapoptios());
                            mWeekFirst.setText(String.format(top_times + "%s", "次"));
                            mTotalRank.setText(String.valueOf(total_rank));
                            mTotalYield.setText(String.format("%s", DoubleFromat.getStockDouble(total_yield * 100, 2) + "%"));
                        }
                        int length = ranks.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject jsonObject = ranks.optJSONObject(i);
                            rankList.add(new Rank(jsonObject.optInt("week_rank"), jsonObject.optString("symbol"),
                                    jsonObject.optString("stock_name"), jsonObject.optDouble("week_yield")
                                    , jsonObject.optDouble("week_score"), user_id,
                                    nickname, avatar, jsonObject.optString("reason"),
                                    jsonObject.optDouble("high_yield"), jsonObject.optString("start_at")
                                    , jsonObject.optString("end_at"), jsonObject.optInt("round")));
                        }


                        if (SettingDefaultsManager.getInstance().getIsVip() == 0 ||
                                TextUtils.isEmpty(SettingDefaultsManager.getInstance().authToken())) {
                            List<Rank> rankDataList = new ArrayList<>();
                            for (int i = 0; i < rankList.size(); i++) {
                                if (i > 2) {
                                    rankDataList.add(rankList.get(i));
                                }
                            }
                            mUserDetailRankAdapter.setList(rankDataList);
                            mShadeView.setVisibility(View.VISIBLE);
                        } else {
                            mShadeView.setVisibility(View.GONE);
                            mUserDetailRankAdapter.setList(rankList);
                        }
                    }
                }
                if (!is_first) {
                    isFirst = false;
                }
                mStock_detail_scrollview.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                if (!is_first) {
                    isFirst = false;
                }
                mStock_detail_scrollview.onRefreshComplete();
            }
        });
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
        int top = mTopView1.getTop();
        if (y >= top) {
            if (mTopView1.getVisibility() == View.VISIBLE) {
                mTopView1.setVisibility(View.GONE);
            }
            if (mTopView2.getVisibility() == View.GONE) {
                mTopView2.setVisibility(View.VISIBLE);
            }
        } else {
            if (mTopView1.getVisibility() == View.GONE) {
                mTopView1.setVisibility(View.VISIBLE);
            }
            if (mTopView2.getVisibility() == View.VISIBLE) {
                mTopView2.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void scrollStop() {

    }
}
