package com.zbmf.StockGroup.fragment;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.NuggetsRecyclerAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.NuggetsStockBean0;
import com.zbmf.StockGroup.beans.StockMode;
import com.zbmf.StockGroup.beans.StockModeMenu;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.view.SyncHorizontalScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NuggetsTenFragment extends Fragment implements View.OnClickListener {


    private View mView;
    private LinearLayout mDialog_layout;
    private ImageView mTv_price_img, mTv_yield_img, mTv_all_yield_img, mTv_yellow_white_img;
    //    private PullToRefreshScrollView mMy_scrllview;
    private LinearLayout mCurrentPrice, mGains, mTransaction, mDynamic;
    private StockModeMenu mStockModeMenu;
    private Drawable arrow_nomal, mode_arrow, top_arrow;

    public static final String CURRENT = "current";
    public static final String YIELD = "yield";
    public static final String VRSI = "vrsi";
    public static final String YWPI = "ywpi";
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    private NuggetsRecyclerAdapter adapter;
    private RecyclerView mNuggetsRecyclerView;
    private TextView mNuggets_day;
    private LinearLayout mSticky_header;
    private LinearLayout mNo_message_layout;

    private String sortKey = CURRENT;
    private String sortOrder = DESC;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            initData();
//            handler.postDelayed(this,50000);
        }
    };
    private int mFlag;
    private SyncHorizontalScrollView mContent_titleScrollView;

    public NuggetsTenFragment() {
    }

    public static NuggetsTenFragment instance(StockModeMenu stockModeMenu, int flag) {
        NuggetsTenFragment nuggetsTenFragment = new NuggetsTenFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.STOCK_MODE_MENU, stockModeMenu);
        bundle.putInt(IntentKey.FLAG, flag);
        nuggetsTenFragment.setArguments(bundle);
        return nuggetsTenFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_nuggets_ten, container, false);
        initView();
        return mView;
    }

    protected <T extends View> T getView(int resourcesId) {
        return (T) mView.findViewById(resourcesId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        mFlag = getArguments().getInt(IntentKey.FLAG);
//        Log.i("--TAG","---- fragment 中的 flag :"+ mFlag);
        mStockModeMenu = (StockModeMenu) getArguments().getSerializable(IntentKey.STOCK_MODE_MENU);
        mDialog_layout = getView(R.id.dialog_layout);
        TextView id_tv_loadingmsg = getView(R.id.id_tv_loadingmsg);
        id_tv_loadingmsg.setText(getString(R.string.nuggetsing));
        mDialog_layout.setVisibility(View.VISIBLE);//显示加载dialog
//        mMy_scrllview = getView(R.id.my_scrllview);
        mNo_message_layout = getView(R.id.no_message_layout);
        mNo_message_layout.setVisibility(View.VISIBLE);
        mNuggetsRecyclerView = getView(R.id.nuggetsRecyclerView);
        mNuggets_day = getView(R.id.nuggets_day);
        mSticky_header = getView(R.id.sticky_header);
        mSticky_header.setVisibility(View.GONE);

        mContent_titleScrollView = getView(R.id.content_TitleScrollView);
        mCurrentPrice = getView(R.id.currentPrice);
        mGains = getView(R.id.gains);
        mTransaction = getView(R.id.transaction);
        mDynamic = getView(R.id.dynamic);

//        TextView tv_price = getView(R.id.tv_price);
        mTv_price_img = getView(R.id.tv_price_img);
//        TextView tv_yield = getView(R.id.tv_yield);
        mTv_yield_img = getView(R.id.tv_yield_img);
//        TextView tv_all_yield = getView(R.id.tv_all_yield);
        mTv_all_yield_img = getView(R.id.tv_all_yield_img);
//        //强弱力度
//        TextView tv_yellow_white = getView(R.id.tv_yellow_white);
        mTv_yellow_white_img = getView(R.id.tv_yellow_white_img);
        arrow_nomal = getResources().getDrawable(R.drawable.icon_arrow_nomal);
        top_arrow = getResources().getDrawable(R.drawable.icon_top);
        mode_arrow = getResources().getDrawable(R.drawable.mode_arrow);

        mTv_price_img.setImageDrawable(mode_arrow);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        mNuggetsRecyclerView.setLayoutManager(linearLayoutManager);
        mNuggetsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNuggetsRecyclerView.setHasFixedSize(true);
        mNuggetsRecyclerView.setNestedScrollingEnabled(false);
        //recyclerView设置adapter
        adapter = new NuggetsRecyclerAdapter(getActivity(), mFlag);
        adapter.setTitleView(mContent_titleScrollView);
        clickEvent();
        initData();
        mNuggetsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //newState 1:表示拖动滑动  2:表示惯性滑动
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != 0) {//表示正在滑动
                    handler.removeCallbacks(runnable);
                } else {
                    handler.post(runnable);
                }

//                mNuggetsRecyclerView.getLayoutManager()

                View stickyInfoView = recyclerView.getChildAt(0);//获取头部View
                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    mSticky_header.setVisibility(View.VISIBLE);
                    mNuggets_day.setText(String.valueOf(stickyInfoView.getContentDescription()));
                }
                View transInfoView = recyclerView.findChildViewUnder(mSticky_header.getMeasuredWidth() / 2
                        , mSticky_header.getMeasuredHeight() + 1);//位于headerView下方的itemView（该坐标是否在itemView内）
                if (transInfoView != null && transInfoView.getTag() != null) {
                    int tag = (int) transInfoView.getTag();
                    int top = transInfoView.getTop();
                    int h = mSticky_header.getMeasuredHeight();
//                    Log.i("--TAG","---- top "+top+"       headerH "+h);

                    int deltaY = transInfoView.getTop() - h * 2;
//                    Log.i("--TAG","--- deltaY "+deltaY);
                    if (tag == adapter.HAS_STICKY_VIEW)//当Item包含粘性头部一类时
                    {
                        if (transInfoView.getTop() > 0)//当Item还未移动出顶部时
                        {
                            mSticky_header.setTranslationY(deltaY);
                        } else//当Item移出顶部，粘性头部复原
                        {
                            mSticky_header.setTranslationY(0);
                        }
                    } else//当Item不包含粘性头部时
                    {
                        mSticky_header.setTranslationY(0);
                    }
                }
            }
        });
    }

    private void initData() {
        WebBase.getNewModelList(String.valueOf(mStockModeMenu.getProduct_id()), sortKey, sortOrder, new JSONHandler() {

            @Override
            public void onSuccess(JSONObject obj) {
                NuggetsStockBeanList.clear();
                adapter.clearData();
                if (obj.has("stocks")) {
                    try {
                        JSONObject stocks = obj.getJSONObject("stocks");
                        Iterator<String> keys = stocks.keys();//天 keys
                        while (keys.hasNext()) {
                            String key = keys.next();//天 key
                            JSONObject jsonObject = stocks.getJSONObject(key);
                            Iterator<String> keys1 = jsonObject.keys();//小时 keys
                            if (!keys1.hasNext()) {
                                mNuggetsStockBean0 = new NuggetsStockBean0();
                                mNuggetsStockBean0.setToDayV(key);//设置掘金日期
                                NuggetsStockBeanList.add(mNuggetsStockBean0);
                            }
                            while (keys1.hasNext()) {
                                mNuggetsStockBean0 = new NuggetsStockBean0();
                                mNuggetsStockBean0.setToDayV(key);//设置掘金日期

                                NuggetsStockBean0.ToDayModel toDayModel = new NuggetsStockBean0.ToDayModel();
                                String clock = keys1.next();//小时 key
                                toDayModel.setClock_10V(clock);//设置掘金时刻
                                JSONArray jsonArray = jsonObject.getJSONArray(clock);
                                List<NuggetsStockBean0.ToDayModel.Clock_10Model> clock_10ModelList = new ArrayList<>();
                                NuggetsStockBean0.ToDayModel.Clock_10Model clock_10Model = new NuggetsStockBean0.ToDayModel.Clock_10Model();
                                List<StockMode> stockModes = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObj = jsonArray.optJSONObject(i);
                                    stockModes.add(new StockMode(
                                            jsonObj.optString("name"),
                                            jsonObj.optString("symbol"),
                                            jsonObj.optDouble("current"),
                                            jsonObj.optDouble("yield"),
                                            jsonObj.optDouble("vrsi"),
                                            jsonObj.optDouble("ywpi"),
                                            jsonObj.optInt("repeat")
                                    ));
                                }
                                clock_10Model.setStockMode(stockModes);
                                clock_10ModelList.add(clock_10Model);
                                toDayModel.setClock_10Model(clock_10ModelList);
                                mNuggetsStockBean0.setTooDayModel(toDayModel);//设置掘金时刻model
                                NuggetsStockBeanList.add(mNuggetsStockBean0);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //为控件设置数据
                setDataWithView();
                mNo_message_layout.setVisibility(View.GONE);
                mSticky_header.setVisibility(View.VISIBLE);
                mDialog_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String err_msg) {
                mDialog_layout.setVisibility(View.GONE);
                mNo_message_layout.setVisibility(View.GONE);
                mDialog_layout.setVisibility(View.GONE);
            }
        });
    }

    //记录每一天有多少给时间段，每一个时间段里边对应有多少组数据
    private NuggetsStockBean0 mNuggetsStockBean0;
    private List<NuggetsStockBean0> NuggetsStockBeanList = new ArrayList<>();

    private void setDataWithView() {
        mDialog_layout.setVisibility(View.GONE);
        adapter.setDataList(NuggetsStockBeanList, mFlag);
        mNuggetsRecyclerView.setAdapter(adapter);
    }

    private void clickEvent() {
        mCurrentPrice.setOnClickListener(this);
        mGains.setOnClickListener(this);
        mTransaction.setOnClickListener(this);
        mDynamic.setOnClickListener(this);
    }

    private int priceInt = 0;
    private int gainsInt = 1;
    private int transactionInt = 1;
    private int dynamicInt = 1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.currentPrice:
                mContent_titleScrollView.scrollX(mContent_titleScrollView, 0);
                sortKey = CURRENT;
                if (priceInt == 0) {
                    //ASC升序
                    sortOrder = ASC;
                    initData();
                    priceInt = 1;
                    mTv_price_img.setImageDrawable(top_arrow);
                } else {
                    sortOrder = DESC;
                    initData();
                    priceInt = 0;
                    mTv_price_img.setImageDrawable(mode_arrow);
                }
                mTv_yield_img.setImageDrawable(arrow_nomal);
                mTv_all_yield_img.setImageDrawable(arrow_nomal);
                mTv_yellow_white_img.setImageDrawable(arrow_nomal);
                break;
            case R.id.gains:
                mContent_titleScrollView.scrollX(mContent_titleScrollView, 0);
                sortKey = YIELD;
                if (gainsInt == 0) {
                    sortOrder = ASC;
                    initData();
                    gainsInt = 1;
                    mTv_yield_img.setImageDrawable(top_arrow);
                } else {
                    sortOrder = DESC;
                    initData();
                    gainsInt = 0;
                    mTv_yield_img.setImageDrawable(mode_arrow);
                }
                mTv_price_img.setImageDrawable(arrow_nomal);
                mTv_all_yield_img.setImageDrawable(arrow_nomal);
                mTv_yellow_white_img.setImageDrawable(arrow_nomal);
                break;
            case R.id.transaction:
                mContent_titleScrollView.scrollX(mContent_titleScrollView, 0);
                sortKey = VRSI;
                if (transactionInt == 0) {
                    sortOrder = ASC;
                    initData();
                    transactionInt = 1;
                    mTv_all_yield_img.setImageDrawable(top_arrow);
                } else {
                    sortOrder = DESC;
                    initData();
                    transactionInt = 0;
                    mTv_all_yield_img.setImageDrawable(mode_arrow);
                }
                mTv_price_img.setImageDrawable(arrow_nomal);
                mTv_yield_img.setImageDrawable(arrow_nomal);
                mTv_yellow_white_img.setImageDrawable(arrow_nomal);
                break;
            case R.id.dynamic:
                mContent_titleScrollView.scrollX(mContent_titleScrollView, 0);
                sortKey = YWPI;
                if (dynamicInt == 0) {
                    sortOrder = ASC;
                    initData();
                    dynamicInt = 1;
                    mTv_yellow_white_img.setImageDrawable(top_arrow);
                } else {
                    sortOrder = DESC;
                    initData();
                    dynamicInt = 0;
                    mTv_yellow_white_img.setImageDrawable(mode_arrow);
                }
                mTv_price_img.setImageDrawable(arrow_nomal);
                mTv_yield_img.setImageDrawable(arrow_nomal);
                mTv_all_yield_img.setImageDrawable(arrow_nomal);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (handler != null) {
            handler.post(runnable);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler = null;
            runnable = null;
        }
    }
}
