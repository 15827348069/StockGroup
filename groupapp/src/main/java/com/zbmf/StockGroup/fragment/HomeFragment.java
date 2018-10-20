package com.zbmf.StockGroup.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveLoginListener;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.PublishInfo;
import com.bokecc.sdk.mobile.live.pojo.RoomInfo;
import com.bokecc.sdk.mobile.live.pojo.TemplateInfo;
import com.bokecc.sdk.mobile.live.pojo.Viewer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zbmf.StockGroup.GroupActivity;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.AskStockActivity;
import com.zbmf.StockGroup.activity.DongAskActivity;
import com.zbmf.StockGroup.activity.LookStockActivity;
import com.zbmf.StockGroup.activity.ScreenActivity;
import com.zbmf.StockGroup.activity.ScreenDetailActivity;
import com.zbmf.StockGroup.activity.SimulateStockAccActivity;
import com.zbmf.StockGroup.activity.StockModeActivity;
import com.zbmf.StockGroup.activity.StudyActivity;
import com.zbmf.StockGroup.activity.VideoPlayActivity;
import com.zbmf.StockGroup.activity.VotingTeacherRankActivity;
import com.zbmf.StockGroup.activity.ZbmfSelectStockMatch;
import com.zbmf.StockGroup.adapter.CycleViewPager;
import com.zbmf.StockGroup.adapter.HeadMessageAdapter;
import com.zbmf.StockGroup.adapter.HomeSmartStockAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.HomeImage;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.constans.AppConfig;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.utils.TimeOnItemClickListener;
import com.zbmf.StockGroup.utils.ViewFactory;
import com.zbmf.StockGroup.utils.WebClickUitl;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 * xuhao on 2017/2/13.
 */

public class HomeFragment extends GroupBaseFragment implements View.OnClickListener {
    private CycleViewPager cycleViewPager, zbmfcycleViewPager;
    private List<HomeImage> url_list, activity_list;
    private List<ImageView> views, zbmfViews;
    private List<Group> infolist;
    private List<BlogBean> bloglist;
    //    private List<NewsFeed> activitylist;
    //    private List<Traders>tradersList;
    private RecyclerView home_screen;
    private HeadMessageAdapter headadapter;
    private ListViewForScrollView home_mf_head_message;
    private PullToRefreshScrollView home_scrollview;
    //    private HomeActivityAdapter activityAdapter;
    private int page, pages;
    public boolean first_onload_url, first_onload_bloglist, first_onload_screen;
    private ImageView imv_voting;

    private ImageView imv_chose_stock, imv_reg_trader;
    private TextView tv_voting;
    private HomeSmartStockAdapter mSmartStockAdapter;
    //    private ImageView home_video_img;
//    private TextView tv_video_name;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.home_fragment_layout, null);
    }

    @Override
    protected void initView() {
        //初始化控件
//        home_tuijian = getView(R.id.homt_tuijian);
        home_mf_head_message = getView(R.id.home_mf_head_message);
        home_scrollview = getView(R.id.home_scrollview);
//        home_activity_list = getView(R.id.home_activity_list);
        imv_voting = getView(R.id.imv_voting);
        home_screen = getView(R.id.home_screen);
//        home_trader=getView(R.id.home_trader);

//        getView(R.id.more_teacher_button).setOnClickListener(this);
        getView(R.id.btn_dong_ask).setOnClickListener(this);
//        getView(R.id.btn_stock).setOnClickListener(this);
        getView(R.id.btn_stock_comment).setOnClickListener(this);
        getView(R.id.btn_find_stock).setOnClickListener(this);
//        getView(R.id.btn_strategy).setOnClickListener(this);
        getView(R.id.btn_match).setOnClickListener(this);
        getView(R.id.btn_wengu).setOnClickListener(this);
        getView(R.id.btn_stock_match).setOnClickListener(this);
        getView(R.id.btn_open).setOnClickListener(this);
        getView(R.id.tv_more_button).setOnClickListener(this);
        getView(R.id.imv_voting).setOnClickListener(this);
        getView(R.id.btn_school).setOnClickListener(this);
        getView(R.id.btn_mode_stock).setOnClickListener(this);
        getView(R.id.tv_find_teacher).setOnClickListener(this);
        getView(R.id.tv_strategy_button).setOnClickListener(this);
        getView(R.id.imv_chose_stock).setOnClickListener(this);
        getView(R.id.imv_reg_trader).setOnClickListener(this);
        getView(R.id.tv_more_screen_button).setOnClickListener(this);
        getView(R.id.joinVIP).setOnClickListener(this);

        imv_chose_stock = getView(R.id.imv_chose_stock);
        imv_reg_trader = getView(R.id.imv_reg_trader);

        tv_voting = getView(R.id.tv_voting);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        views = new ArrayList<>();
        zbmfViews = new ArrayList<>();
        activity_list = new ArrayList<>();
        url_list = new ArrayList<>();
        infolist = new ArrayList<>();
        bloglist = new ArrayList<>();

        headadapter = new HeadMessageAdapter(getActivity(), bloglist);
        home_mf_head_message.setAdapter(headadapter);
        home_mf_head_message.setOnItemClickListener(new TimeOnItemClickListener() {
            @Override
            public void onNoDoubleClick(int position) {
                ShowActivity.showBlogDetailActivity(getActivity(), bloglist.get(position));
            }
        });
        home_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        home_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                first_onload_url = true;
                first_onload_bloglist = true;
                first_onload_screen = true;
                initData();
            }
        });
        home_screen.setLayoutManager(new LinearLayoutManager(getContext()));//, LinearLayoutManager.VERTICAL,false
        home_screen.setNestedScrollingEnabled(false);
        //重新造Adapter
        mSmartStockAdapter = new HomeSmartStockAdapter(getActivity());
        home_screen.setAdapter(mSmartStockAdapter);
        mSmartStockAdapter.setSmartItemClickListener(new HomeSmartStockAdapter.SmartItemClickListener() {
            @Override
            public void sItemClick(Screen screen,int flag) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.SCREEN, screen);
                bundle.putInt(IntentKey.FLAG, flag);
                ShowActivity.showActivityForResult(getActivity(), bundle, ScreenDetailActivity.class, RequestCode.SCREEN);
            }
        });
        mSmartStockAdapter.setSubscribe(new HomeSmartStockAdapter.SubscribeVIP() {
            @Override
            public void onSubscribeVIP(Screen screen, int flag) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.SCREEN, screen);
                bundle.putInt(IntentKey.FLAG, flag);
                ShowActivity.showActivityForResult(getActivity(), bundle, ScreenDetailActivity.class, RequestCode.SCREEN);
            }
        });
        tv_voting.setText(getString(R.string.voting_teacher).replace("[**]", DateUtil.getTime(DateUtil.getTimes(), Constants.YYYY年MM月)));
    }

    @Override
    protected void initData() {
        //初始化数据
        page = 1;
        pages = 0;
        getImageUrl();
//        getHomeTeacher();
        getBlog_message();
        getActivityMessage();
        getScreenList();
//        getTrader();
//        ViewFactory.getRoundImgView(getActivity(),AppConfig.HOME_ACTIVITY,imv_voting);

        ImageLoader.getInstance().displayImage(AppConfig.HOME_ACTIVITY, imv_voting, new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.icon_home_voting)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_home_voting)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .showStubImage(R.drawable.icon_home_voting)     // 设置图片下载期间显示的图片
                .showImageOnFail(R.drawable.icon_home_voting)
                .build());
    }

    public void getScreenList() {
        WebBase.getScreenProducts(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result = obj.optJSONObject("result");
                if (!result.isNull("screen")) {
                    mSmartStockAdapter.clearDataList();
                    List<Screen> screen = JSONParse.getScreenList(result.optJSONArray("screen"));
                    for (int i = 0; i < screen.size(); i++) {
                        String name = screen.get(i).getName();
                        if (name.equals("牛动乾坤")) {
                            screen.remove(i);
                        }
                    }
                    mSmartStockAdapter.addData(screen);
                }
                if (first_onload_screen) {
                    first_onload_screen = false;
                }
                RunshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                if (first_onload_screen) {
                    first_onload_screen = false;
                }
                RunshComplete();
            }
        });
    }


    private void getActivityMessage() {
        WebBase.index(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (!obj.isNull("activity")) {
                    JSONArray activity = obj.optJSONArray("activity");
                    activity_list.clear();
                    for (int i = 0; i < activity.length(); i++) {
                        JSONObject object = activity.optJSONObject(i);
                        if (object.optInt("is_pic") == 1) {
                            if (object != null) {
                                HomeImage h = new HomeImage();
                                h.setType("link");
                                h.setId(object.optString("advert_id"));
                                h.setTitle(object.optString("subject"));
                                h.setLink_url(object.optString("jump_url"));
                                h.setImg_url(object.optString("img_url"));
                                h.setIs_login(object.optInt("is_login"));
                                activity_list.add(h);
                            }
                        } else {
//                            NewsFeed newsFeed = new NewsFeed();
//                            newsFeed.setFeed_id(object.optString("advert_id"));
//                            newsFeed.setSubject(object.optString("subject"));
//                            newsFeed.setCover(object.optString("img_url"));
//                            NewsFeed.Link link = new NewsFeed.Link();
//                            link.setApp(object.optString("jump_url"));
//                            newsFeed.setLink(link);
//                            activitylist.add(newsFeed);
                        }
                    }
                    upZbmfActivity();
//                    activityAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
        WebBase.getHomeActivity(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.has("adverts")) {
                    JSONArray adverts = obj.optJSONArray("adverts");
                    for (int i = 0; i < adverts.length(); i++) {
                        JSONObject oj = adverts.optJSONObject(i);
                        HomeImage h = new HomeImage();
                        h.setType("link");
                        h.setId(oj.optString("advert_id"));
                        h.setTitle(oj.optString("subject"));
                        h.setLink_url(oj.optString("jump_url"));
                        h.setImg_url(oj.optString("img_url"));
                        h.setIs_login(oj.optInt("is_login"));
                        if (i == 0) {
                            imv_chose_stock.setTag(h);
                            ImageLoader.getInstance().displayImage(h.getImg_url(), imv_chose_stock, ImageLoaderOptions.ProgressOptions());
                        } else if (i == 1) {
                            imv_reg_trader.setTag(h);
                            ImageLoader.getInstance().displayImage(h.getImg_url(), imv_reg_trader, ImageLoaderOptions.ProgressOptions());
                        }

                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.tv_more_screen_button:
//                ShowActivity.showActivityForResult(getActivity(), null, ScreenActivity.class, RequestCode.SCREEN);
                ShowActivity.skipNewSmartStockActivity(getActivity());
                break;
            case R.id.tv_find_teacher:
                if (ShowActivity.isLogin(getActivity())) {
                    ((GroupActivity) getActivity()).selectViewPage(1);
                }
                break;
            case R.id.imv_voting:
                ShowActivity.showActivity(getActivity(), VotingTeacherRankActivity.class);
                break;
            case R.id.joinVIP://加入VIP会员
                ShowActivity.skipVIPActivity(getActivity());
                break;
            case R.id.btn_dong_ask:
                //董秘问答
                ShowActivity.showActivity(getActivity(), DongAskActivity.class);
                break;
            case R.id.btn_wengu:
                if (ShowActivity.isLogin(getActivity())) {
                    ShowActivity.showActivity(getActivity(), AskStockActivity.class);
                }
                break;
            case R.id.tv_more_button:
                bundle = new Bundle();
                bundle.putInt(IntentKey.FLAG, 1);
                ShowActivity.showActivity(getActivity(), bundle, LookStockActivity.class);
                break;
            case R.id.btn_mode_stock:
                ShowActivity.showActivity(getActivity(), StockModeActivity.class);
                break;
            case R.id.btn_open:
                ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.OPEN);
                break;
            case R.id.btn_stock_match:
                startActivity(new Intent(getActivity(), ZbmfSelectStockMatch.class));
//                ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.FIND_STOCK);
                break;
            case R.id.btn_match:
                if (ShowActivity.isLogin(getActivity())) {
                    ShowActivity.showActivity(getActivity(), SimulateStockAccActivity.class);
                }
                break;
            case R.id.btn_stock_comment:
                //看股市
                ShowActivity.showActivity(getActivity(), LookStockActivity.class);
                break;
            case R.id.imv_reg_trader:
                HomeImage homeImage = (HomeImage) view.getTag();
                if (homeImage != null) {
                    if (homeImage.getIs_login() == 1) {
                        if (ShowActivity.isLogin(getActivity())){
                            if (homeImage.getLink_url().contains(Constants.VIP_SKIP_URL)) {
                                ShowActivity.skipVIPActivity(getActivity());
                            } else if (homeImage.getLink_url().contains(Constants.SELECT_STOCK_SKIP_URL)) {
                                ShowActivity.skipZbmfSelectStockActivity(getActivity());
                            } else {
//                                WebClickUitl.ShowActivity(getActivity(), homeImage.getLink_url());
                                ShowActivity.showWebViewActivity(getActivity(), homeImage.getLink_url());
                            }
                        }
                    } else {
                        if (homeImage.getLink_url().contains(Constants.VIP_SKIP_URL)) {
                            ShowActivity.skipVIPActivity(getActivity());
                        }
                        else {
                            ShowActivity.showWebViewActivity(getActivity(), homeImage.getLink_url());
                        }
                    }
                }
                break;
            case R.id.imv_chose_stock:
                HomeImage homeImage1 = (HomeImage) view.getTag();
                if (homeImage1 != null) {
                    if (homeImage1.getIs_login() == 1) {
                        if (ShowActivity.isLogin(getActivity())) {
//                            WebClickUitl.ShowActivity(getActivity(), homeImage1.getLink_url());
                            if (homeImage1.getLink_url().contains(Constants.VIP_SKIP_URL)) {
                                ShowActivity.skipVIPActivity(getActivity());
                            } else if (homeImage1.getLink_url().contains(Constants.SELECT_STOCK_SKIP_URL)) {
                                ShowActivity.skipZbmfSelectStockActivity(getActivity());
                            } else {
                                WebClickUitl.ShowActivity(getActivity(), homeImage1.getLink_url());
                            }
                        }
                    } else {
                        if (homeImage1.getLink_url().contains(Constants.VIP_SKIP_URL)) {
                            ShowActivity.skipVIPActivity(getActivity());
                        }
                        else {
                            WebClickUitl.ShowActivity(getActivity(), homeImage1.getLink_url());
//                            ShowActivity.showWebViewActivity(getActivity(), homeImage1.getLink_url());
                        }
                    }
                }
                break;
            case R.id.btn_find_stock:
                //找股票
                ShowActivity.skipNewSmartStockActivity(getActivity());
//                ShowActivity.showActivity(getActivity(), ScreenActivity.class);
                break;
//            case R.id.btn_strategy:
//                //策略
//                ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.RUN_TRADER);
//                break;
            case R.id.tv_strategy_button:
                ShowActivity.showActivity(getActivity(), ScreenActivity.class);
                break;
//            case R.id.more_teacher_button:
//                bundle = new Bundle();
//                bundle.putInt(IntentKey.FLAG, 3);
//                ShowActivity.showActivityForResult(getActivity(), bundle, FindTeacherActivity.class, RequestCode.STUDY);
//                break;
            case R.id.tv_more_video:
            case R.id.btn_school:
                //炒股学院
                ShowActivity.showActivity(getActivity(), StudyActivity.class);
                break;
            case R.id.home_video_img:
                Video video = (Video) view.getTag();
                if (video != null) {
                    if (video.getIs_live()) {
                        setLoginMessage(video);
                    } else {
                        bundle = new Bundle();
                        bundle.putSerializable(IntentKey.VIDEO_KEY, video);
                        ShowActivity.showActivity(getActivity(), bundle, VideoPlayActivity.class);
                    }
                }
                break;
        }
    }

    private void setLoginMessage(final Video video) {
        DWLive.getInstance().setDWLiveLoginParams(new DWLiveLoginListener() {
            @Override
            public void onLogin(TemplateInfo templateInfo, Viewer viewer, RoomInfo roomInfo, PublishInfo publishInfo) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(IntentKey.VIDEO_KEY, video);
                        ShowActivity.showActivityForResult(getActivity(), bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                    }
                });
            }

            @Override
            public void onException(final DWLiveException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(IntentKey.VIDEO_KEY, video);
                        ShowActivity.showActivityForResult(getActivity(), bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                    }
                });
            }
        }, Constants.CC_USERID, video.getBokecc_id() + "", SettingDefaultsManager.getInstance().NickName(), "");
        DWLive.getInstance().startLogin();
    }

    public void getImageUrl() {
        WebBase.getAdverts(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONArray adverts = obj.optJSONArray("adverts");
                int size = adverts.length();
                url_list.clear();
                for (int i = 0; i < size; i++) {
                    JSONObject oj = adverts.optJSONObject(i);
                    if (oj != null) {
                        HomeImage h = new HomeImage();
                        h.setType("link");
                        h.setId(oj.optString("advert_id"));
                        h.setTitle(oj.optString("subject"));
                        h.setLink_url(oj.optString("jump_url"));
                        h.setImg_url(oj.optString("img_url"));
                        h.setIs_login(oj.optInt("is_login"));
                        url_list.add(h);
                    }
                }
                up_data_img();
                if (first_onload_url) {
                    first_onload_url = false;
                }
                RunshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                if (first_onload_url) {
                    first_onload_url = false;
                }
                RunshComplete();
            }
        });
    }

    public void up_data_img() {
        if (cycleViewPager == null && getActivity().getFragmentManager() != null) {
            cycleViewPager = (CycleViewPager) getActivity().getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
        }
        views.clear();
        for (int i = 0; i < url_list.size(); i++) {
            views.add(ViewFactory.getImageView(getActivity(), url_list.get(i).getImg_url()));
        }
        cycleViewPager.setCycle(true);
        cycleViewPager.setData(views, url_list, mAdCycleViewListener);
        cycleViewPager.setWheel(true);
        cycleViewPager.setTime(2000);
        cycleViewPager.setIndicatorCenter();
    }

    public void upZbmfActivity() {
        if (zbmfcycleViewPager == null && getActivity().getFragmentManager() != null) {
            zbmfcycleViewPager = (CycleViewPager) getActivity().getFragmentManager().findFragmentById(R.id.fragment_home_zbmf_content);
        }
        zbmfViews.clear();
        for (int i = 0; i < activity_list.size(); i++) {
//            zbmfViews.add(ViewFactory.getActivityImageView(getActivity(), activity_list.get(i).getImg_url()));
            zbmfViews.add(ViewFactory.getRoundImgView(getActivity(), activity_list.get(i).getImg_url()));
        }
        zbmfcycleViewPager.setCycle(activity_list.size() > 1 ? true : false);
        zbmfcycleViewPager.setData(zbmfViews, activity_list, mAdCycleViewListener);
        zbmfcycleViewPager.setWheel(activity_list.size() > 1 ? true : false);
        zbmfcycleViewPager.setIndicatorLayout(activity_list.size() > 1 ? View.VISIBLE : View.GONE);
    }

    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {
        @Override
        public void onImageClick(HomeImage info, int position, View imageView) {
            if (info.getIs_login() == 1) {
                if (ShowActivity.isLogin(getActivity())) {
                    String url = info.getLink_url();
                    if (!TextUtils.isEmpty(url)) {
                        if (url.contains(Constants.VIP_SKIP_URL)) {
                            ShowActivity.skipVIPActivity(getActivity());
                        } else if (url.contains(Constants.SELECT_STOCK_SKIP_URL)) {
                            ShowActivity.skipZbmfSelectStockActivity(getActivity());
                        } else {
                            WebClickUitl.ShowActivity(getActivity(), url);
                        }
                    }
                }
            } else {
                String url = info.getLink_url();
                if (!TextUtils.isEmpty(url)) {
                    if (url.contains(Constants.VIP_SKIP_URL)) {
                        ShowActivity.skipVIPActivity(getActivity());
                    }
                    else {
                        WebClickUitl.ShowActivity(getActivity(), url);
                    }
                }
            }
        }
    };

    public void getGroupInfo(Group groupbean) {
        ShowActivity.showGroupDetailActivity(getActivity(), groupbean);
    }

//    private void getHomeTeacher() {
//        WebBase.recommendList(3, new JSONHandler() {
//            @Override
//            public void onSuccess(JSONObject obj) {
//                JSONObject result = obj.optJSONObject("result");
//                if (!result.isNull("groups")) {
//                    infolist.clear();
//                    infolist.addAll(JSONParse.getGroupList(result.optJSONArray("groups")));
//                    adapter.notifyDataSetChanged();
//                }
//                if (first_onload_infolist) {
//                    first_onload_infolist = false;
//                }
//                RunshComplete();
//            }
//
//            @Override
//            public void onFailure(String err_msg) {
//                if (first_onload_infolist) {
//                    first_onload_infolist = false;
//                }
//                RunshComplete();
//            }
//        });
//    }

    private void getBlog_message() {
        if (page == pages) {
            showToast("已加载全部数据");
            home_scrollview.onRefreshComplete();
            return;
        }
        WebBase.searchUserBlogs(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result = obj.optJSONObject("result");
                page = result.optInt("page");
                pages = result.optInt("pages");
                bloglist.clear();
                JSONArray blogs = result.optJSONArray("blogs");
                int size = blogs.length();
                for (int i = 0; i < size; i++) {
                    JSONObject blog = blogs.optJSONObject(i);
                    BlogBean blogBean = new BlogBean();
                    blogBean.setImg(blog.optString("cover"));
                    blogBean.setTitle(blog.optString("subject"));
                    blogBean.setDate(blog.optString("posted_at"));
                    blogBean.setLook_number(blog.optJSONObject("stat").optString("views"));
                    blogBean.setPinglun(blog.optJSONObject("stat").optString("replys"));
                    blogBean.setAvatar(blog.optJSONObject("user").optString("avatar"));
                    blogBean.setName(blog.optJSONObject("user").optString("nickname"));
                    blogBean.setApp_link(blog.optJSONObject("link").optString("app"));
                    blogBean.setWap_link(blog.optJSONObject("link").optString("wap"));
                    blogBean.setBlog_id(blog.optString("blog_id"));
                    bloglist.add(blogBean);
                }
                headadapter.notifyDataSetChanged();
                if (first_onload_bloglist) {
                    first_onload_bloglist = false;
                }
                RunshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
                if (first_onload_bloglist) {
                    first_onload_bloglist = false;
                }
                RunshComplete();
            }
        });
    }

    private void RunshComplete() {
        if (!first_onload_bloglist && !first_onload_screen && !first_onload_url && home_scrollview.isRefreshing()) {
            home_scrollview.onRefreshComplete();
        }
    }
}
