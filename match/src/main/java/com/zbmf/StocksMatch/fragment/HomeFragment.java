package com.zbmf.StocksMatch.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.MainActivity;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.activity.MatchTraderInfoActivity;
import com.zbmf.StocksMatch.adapter.HomeCityAdapter;
import com.zbmf.StocksMatch.adapter.HomeHotMatchAdapter;
import com.zbmf.StocksMatch.adapter.HomeMatchAdapter;
import com.zbmf.StocksMatch.adapter.HomeTraderAdapter;
import com.zbmf.StocksMatch.adapter.SponsorAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.Adverts;
import com.zbmf.StocksMatch.bean.City;
import com.zbmf.StocksMatch.bean.HomeMatchList;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.bean.MatchNewAllBean;
import com.zbmf.StocksMatch.bean.PopWindowBean;
import com.zbmf.StocksMatch.bean.SchoolBean;
import com.zbmf.StocksMatch.bean.StockIndexBean;
import com.zbmf.StocksMatch.bean.Traders;
import com.zbmf.StocksMatch.bean.UserWallet;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.HtmlUrl;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.StocksMatch.listener.BannerPageClickListener;
import com.zbmf.StocksMatch.listener.BannerViewHolder;
import com.zbmf.StocksMatch.listener.IHomeView;
import com.zbmf.StocksMatch.listener.MZHolderCreator;
import com.zbmf.StocksMatch.listener.SponsorAdsClick;
import com.zbmf.StocksMatch.presenter.HomePresenter;
import com.zbmf.StocksMatch.util.MatchSharedUtil;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.view.MZBannerView;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.StocksMatch.view.ViewFactory;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshScrollView;
import com.zbmf.worklibrary.util.GsonUtil;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;
import com.zbmf.worklibrary.view.CycleViewPager;
import com.zbmf.worklibrary.view.ListViewForScrollView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuhao
 * on 2017/11/24.
 * 首页
 */

public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView,
        CycleViewPager.ImageCycleViewListener<Adverts>, HomeTraderAdapter.onItemClickListener,
        HomeCityAdapter.CityClick, SponsorAdsClick {
    @BindView(R.id.tv_sh_num)
    TextView tvShNum;
    @BindView(R.id.tv_sh_yield_num)
    TextView tvShYieldNum;
    @BindView(R.id.tv_sh_yield)
    TextView tvShYield;
    @BindView(R.id.tv_sz_num)
    TextView tvSzNum;
    @BindView(R.id.tv_sz_yield_num)
    TextView tvSzYieldNum;
    @BindView(R.id.tv_sz_yield)
    TextView tvSzYield;
    @BindView(R.id.tv_cy_num)
    TextView tvCyNum;
    @BindView(R.id.tv_cy_yield_num)
    TextView tvCyYieldNum;
    @BindView(R.id.tv_cy_yield)
    TextView tvCyYield;
    @BindView(R.id.tv_create_match_num)
    TextView tvCreateMatchNum;
    @BindView(R.id.tv_player)
    TextView tvPlayer;
    //    @BindView(R.id.rv_school)
//    RecyclerView rvSchool;
    @BindView(R.id.list_superme_match)
    ListViewForScrollView listSupermeMatch;
    @BindView(R.id.home_pull_scrollview)
    PullToRefreshScrollView homePullScrollview;
    @BindView(R.id.rv_trader)
    RecyclerView rvTrader;
    @BindView(R.id.rv_city)
    RecyclerView rvCity;
    @BindView(R.id.list_hot_match)
    ListViewForScrollView listHotMatch;
    @BindView(R.id.m_banner)
    MZBannerView banner;
    @BindView(R.id.kf_layout_id)
    LinearLayout kf_layout_id;
    @BindView(R.id.imgRecyclerView)
    RecyclerView imgRecyclerView;
    @BindView(R.id.sponsorView)
    LinearLayout sponsorView;

    private List<ImageView> views;
    private CycleViewPager cycleViewPager;

    private HomeMatchAdapter homeMatchAdapter;
    private HomeHotMatchAdapter hotMatchAdapter;
    private HomeTraderAdapter traderAdapter;
    private HomeCityAdapter cityAdapter;
    private static final int TIME = 50000;//每三秒获取一次证券指数
    private static final int[] BANNER = {R.drawable.bjs, R.drawable.gdcxkj, R.drawable.gdkjxy, R.drawable
            .gtjnzq, R.drawable.gyzq, R.drawable.hns, R.drawable.qhs, R.drawable.qkw, R.drawable.qlzq, R.drawable
            .shsf, R.drawable.shzf, R.drawable.xncs, R.drawable.zj, R.drawable.zjgss, R.drawable.zjlgs, R.
            drawable.zjzq, R.drawable.zszq, R.drawable.zxzq, R.drawable.zyzq, R.drawable.zzf};
    private static final int[] BANNER_TV = {R.string.bjs, R.string.gdcxkj, R.string.gdkjxy, R.string.gtjnzq
            , R.string.gyzq, R.string.hns, R.string.qhs, R.string.qkw, R.string.qlzq, R.string.shsf, R.string.shzf,
            R.string.xncs, R.string.zj, R.string.zjgss, R.string.zjlgs, R.string.zjzq, R.string.zszq, R.string.zxzq
            , R.string.zyzq, R.string.zzf};
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (HomePresenter.getInstance() != null) {
                HomePresenter.getInstance().getStockIndex();
            }
        }
    };
    private Dialog mDialog;
    private SponsorAdapter mSponsorAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home_layout;
    }

    @Override
    protected HomePresenter initPresent() {
        return HomePresenter.getInstance();
    }

    @Override
    protected void initView() {
        homePullScrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//只能刷新
        ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.hard_loading));
        homePullScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getPresenter().getDatas();
            }
        });
        if (cycleViewPager == null && getChildFragmentManager() != null) {
            cycleViewPager = (CycleViewPager) getChildFragmentManager().findFragmentById(R.id.home_cycleViewPage);
        }
        if (cycleViewPager != null) {
            cycleViewPager.setCycle(true);
            cycleViewPager.setWheel(true);
            cycleViewPager.setTime(2000);
            cycleViewPager.setIndicatorCenter();
        }
        imgRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mSponsorAdapter = new SponsorAdapter(getActivity());
        imgRecyclerView.setAdapter(mSponsorAdapter);
        mSponsorAdapter.setSponsorAdsClick(this);
        listSupermeMatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowActivity.showUserMatchDetail(getActivity(), homeMatchAdapter.getItem(i));
            }
        });
        listHotMatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowActivity.showHotMatchDetail(getActivity(), hotMatchAdapter.getItem(i));
            }
        });
        homePullScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                ShowOrHideProgressDialog.showProgressDialog(getActivity(), getActivity(), getString(R.string.hard_loading));
                getPresenter().getDatas();
            }
        });
    }

    @Override
    protected void initData() {
        homeMatchAdapter = new HomeMatchAdapter(getActivity());
        listSupermeMatch.setAdapter(homeMatchAdapter);

        hotMatchAdapter = new HomeHotMatchAdapter(getActivity());
        listHotMatch.setAdapter(hotMatchAdapter);

//        HomeSchoolAdapter schoolAdapter = new HomeSchoolAdapter(getActivity());
        // TODO: 2018/3/20 改成这里设置Banner的点击事件
        banner.setBannerPageClickListener(new BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {

            }
        });
        List<Integer> bannerList = new ArrayList<>();
        final List<Integer> tvList = new ArrayList<>();
        for (int aBANNER : BANNER) {
            bannerList.add(aBANNER);
        }
        for (int aBANNER_TV : BANNER_TV) {
            tvList.add(aBANNER_TV);
        }
        banner.setIndicatorVisible(false);
        //传进参赛单位的图片数量
        banner.setPages(bannerList, tvList, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder(int imgs) {
                BannerViewHolder bannerViewHolder = new BannerViewHolder(imgs);
                //设置Banner的点击事件
                bannerViewHolder.setBannerClickListener(new BannerPageClickListener() {
                    @Override
                    public void onPageClick(View view, int position) {
                    }
                });
                return bannerViewHolder;
            }
        });

        traderAdapter = new HomeTraderAdapter(getActivity());
        rvTrader.setLayoutManager(new GridLayoutManager(getActivity(), 3) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        traderAdapter.setItemClickListener(this);
        rvTrader.setAdapter(traderAdapter);
        cityAdapter = new HomeCityAdapter(getActivity());
        cityAdapter.setCityClick(this);
        rvCity.setLayoutManager(new GridLayoutManager(getActivity(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        rvCity.setAdapter(cityAdapter);

    }

    @Override
    public void refreshStockIndex(Map<String, String> map) {
        if (homePullScrollview.isRefreshing()) {
            homePullScrollview.onRefreshComplete();
        }
        //更新股票指数  399001:深圳指数 399005:中小板指数 399006:创业板指数 1A0001:上证指数
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (map != null) {
            String sz = map.get("399001");
            /*String zx = map.get("399005");*/
            String cy = map.get("399006");
            String shz = map.get("1A0001");
            refreshSecurity("1A0001", shz);
            refreshSecurity("399001", sz);
            refreshSecurity("399006", cy);
        }
        mHandler.postDelayed(mRunnable, TIME);//每两秒获取一次证券指数
    }

    private void refreshSecurity(String code, String security) {
        StockIndexBean stockIndexBean = GsonUtil.parseData(security, StockIndexBean.class);
        assert stockIndexBean != null;
        String close = stockIndexBean.getClose();
        String current = stockIndexBean.getCurrent();
        float curr = Float.parseFloat(current);
        float clo = Float.parseFloat(close);
        float f = curr - clo;
        String format = new DecimalFormat("0.00").format(f);
        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(2);//设置精确到小数点后两位
        String scale = instance.format(f / clo * 100) + "%";
        switch (code) {
            case "1A0001":
                setTvColor(f, tvShNum);
                setTvColor(f, tvShYieldNum);
                setTvColor(f, tvShYield);
                tvShNum.setText(current);
                setTvText(f, tvShYieldNum, format);
                setTvText(f, tvShYield, scale);
                break;
            case "399001":
                setTvColor(f, tvSzNum);
                setTvColor(f, tvSzYieldNum);
                setTvColor(f, tvSzYield);
                tvSzNum.setText(current);
                setTvText(f, tvSzYieldNum, format);
                setTvText(f, tvSzYield, scale);
                break;
            case "399006":
                setTvColor(f, tvCyNum);
                setTvColor(f, tvCyYieldNum);
                setTvColor(f, tvCyYield);
                tvCyNum.setText(current);
                setTvText(f, tvCyYieldNum, format);
                setTvText(f, tvCyYield, scale);
                break;
        }
    }

    private void setTvText(float f, TextView tv, String yield) {
        tv.setText(f >= 0 ? "+" + yield : yield);
    }

    private void setTvColor(float f, TextView tv) {
        tv.setTextColor(f >= 0 ? getActivity().getResources().getColor(R.color.red1) : getActivity()
                .getResources().getColor(R.color.green));
    }

    private List<MatchNewAllBean.Result.Matches> matchList = new ArrayList<>();
    private List<HomeMatchList.Result.Hot> hotMatch = new ArrayList<>();

    @Override
    public void RusnSupremeMatchAdapter(List<HomeMatchList.Result.Recommend> matchBeans) {
        if (homePullScrollview.isRefreshing()) {
            homePullScrollview.onRefreshComplete();
        }
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (matchList.size() < 2 && matchBeans != null) {//首页的推荐比赛最多为两条
            matchList.clear();
            for (int i = 0; i < matchBeans.size(); i++) {
                if (i < 2) {
                    MatchNewAllBean.Result.Matches matches = new MatchNewAllBean.Result.Matches();
                    matches.setIs_end(matchBeans.get(i).getIs_end() ? 1 : 0);
                    matches.setIs_player(matchBeans.get(i).getIs_player() ? 1 : 0);
                    matches.setMatch_id(matchBeans.get(i).getMatch_id());
                    matches.setMatch_name(matchBeans.get(i).getMatch_name());
                    matches.setMatch_type(matchBeans.get(i).getMatch_type());
                    matches.setPlayers(matchBeans.get(i).getPlayers());
                    matchList.add(matches);
                }
            }
        }
        homeMatchAdapter.setList(matchList);
    }

    //更新热门赛事推荐
    @Override
    public void RushHostMatch(List<HomeMatchList.Result.Hot> matchBeans) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (hotMatch.size() < 3 && matchBeans != null) {//
            hotMatch.clear();
            for (int i = 0; i < matchBeans.size(); i++) {
                if (i < 3) {
                    hotMatch.add(matchBeans.get(i));
                }
            }
        }
        hotMatchAdapter.setList(hotMatch);
    }

    //轮播图广告回传
    @Override
    public void RushBannerImage(List<Adverts> imgList) {
        if (homePullScrollview.isRefreshing()) {
            homePullScrollview.onRefreshComplete();
        }
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (views == null) {
            views = new ArrayList<>();
        } else {
            views.clear();
        }
        for (int i = 0; i < imgList.size(); i++) {
            views.add(ViewFactory.getImageView(getActivity(), imgList.get(i).getImg_url()));
        }
        cycleViewPager.setData(views, imgList, this);
    }

    @Override
    public void rushSponsorAds(final List<Adverts> sponsor, final int gainStatus) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //获取赞助商广告
                if (gainStatus == Constans.GAIN_DATA_SUCCESS) {
                    if (sponsor == null && sponsor.size() == 0) {
                        sponsorView.setVisibility(View.GONE);
                        return;
                    } else {
                        sponsorView.setVisibility(View.VISIBLE);
                    }
                    if (mSponsorAdapter.getSponsorList() != null) {
                        mSponsorAdapter.clearList();
                        mSponsorAdapter.addList(sponsor);
                    }
                }
            }
        });
    }

    @Override
    public void RushMatchSchool(SchoolBean schoolBean) {
        if (schoolBean != null) {
            ShowOrHideProgressDialog.disMissProgressDialog();
            SchoolBean.Result result = schoolBean.getResult();
            if (result != null) {
                tvCreateMatchNum.setText(String.format(getString(R.string.create_match), result.getMatches()));
                tvPlayer.setText(String.format(getString(R.string.match_player), result.getPlayers()));
                //这里没有设置参赛过的学校数据
//                schoolAdapter.refreshData(result.getSchools());
            }
        }
    }

    @Override
    public void RushTraderList(List<Traders> traders) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        traderAdapter.refreshData(traders);
    }

    private boolean isPopWindow = false;

    @SuppressLint("SimpleDateFormat")
    @Override
    public void popWindow(PopWindowBean popWindowBean, int gainStatus) {
        if (gainStatus == Constans.GAIN_DATA_SUCCESS && !isPopWindow) {
            //获取弹窗的数据
            ShowActivity.skipPopWindowAct(popWindowBean, getActivity(), "");
            isPopWindow = true;
        }
    }

    @Override
    public void RushCity(List<City> cityList) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        cityAdapter.refreshData(cityList);
    }

    @Override
    public void RushScrollView() {
        if (homePullScrollview.isRefreshing()) {
            homePullScrollview.onRefreshComplete();
        }
    }

    @Override
    public void onErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (ShowActivity.checkLoginStatus(getActivity(), msg)) {
                showToast(getString(R.string.login_err_tip));
                MatchSharedUtil.clearAuthToken();
            }
        }
    }

    @Override
    public void refreshMatchDesc(MatchDescBean.Result result) {
        if (result != null) {
            ShowActivity.showMatch4(getActivity(), result);
        }
    }

    @Override
    public void refreshMatchDescErr(String msg) {
    }

    @Override
    public void userWallet(UserWallet userWallet) {
        if (userWallet != null) {
            SharedpreferencesUtil.getInstance().putString(SharedKey.MFB_PAY, userWallet.getPay().getUnfrozen());
            SharedpreferencesUtil.getInstance().putString(SharedKey.MFB_POINT, userWallet.getPoint().getUnfrozen());
            SharedpreferencesUtil.getInstance().putString(SharedKey.MFB_COUPON, userWallet.getCoupon().getUnfrozen());
        }
    }

    @Override
    public void userWalletErr(String msg) {
    }

    @Override
    public void onImageClick(Adverts phoneAd, int position, View imageView) {
        //轮播图点击回调
        ShowActivity.circleImageClick(phoneAd, getActivity());
    }

    @OnClick({R.id.tv_trader_more, R.id.tv_city_more, R.id.tv_create, R.id.kf_layout_id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_trader_more:
                ShowActivity.showMatchRankActivity(getActivity(), Constans.MATCH_ID, Constans.TRADER_RANK_FLAG, false);
                break;
            case R.id.tv_city_more:
                MainActivity activity = (MainActivity) getActivity();
                activity.onCityClick();
                break;
            case R.id.tv_create:
                if (ShowActivity.isLogin(getActivity(), ParamsKey.MATCH_ORG_OTHER)) {
                    ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.CREATE_MATCH,
                            getString(R.string.create_matchs));
                }
                break;
            case R.id.kf_layout_id:
                if (mDialog == null) {
                    mDialog = dialog1();
                }
                mDialog.show();
                break;
        }
    }

    /**
     * 操盘高手点击事件
     *
     * @param traders
     */
    @Override
    public void onItemClick(Traders traders) {
        if (ShowActivity.isLogin(getActivity(), ParamsKey.FG_HOME_V)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.TRADER, traders);
            ShowActivity.showActivity(getActivity(), bundle, MatchTraderInfoActivity.class);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (banner != null) {
            banner.isPause();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (banner != null) {
            banner.isPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
        }
    }

    private Dialog dialog1() {
        Dialog dialog = new Dialog(getActivity(), R.style.myDialogTheme);
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.kefu_layout, null);
        TextView qq_button = (TextView) layout.findViewById(R.id.qq_kefu_button);
        TextView phone_button = (TextView) layout.findViewById(R.id.phone_id_button);
        phone_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000607878"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                mDialog.dismiss(); //
            }
        });
        qq_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=2852273339&version=1";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                mDialog.dismiss(); //关闭dialog
            }
        });
        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.bottomDialogStyle);
        dialog.setCancelable(true);
        return dialog;
    }

    @Override
    public void cityClick(String matchID) {
        if (!TextUtils.isEmpty(matchID)) {
            HomePresenter.getInstance().getMatchDesc(Integer.parseInt(matchID));
        }
    }

    @Override
    public void sponsor(int position, Adverts ad) {
        //跳转赞转上链接
        if (ad != null) {
            if (!TextUtils.isEmpty(ad.getJump_url())) {
                ShowActivity.showWebViewActivity(getActivity(), ad.getJump_url(), "");
            }
        }
    }
}
