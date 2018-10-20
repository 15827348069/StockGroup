package com.zbmf.StockGroup.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.luck.picture.lib.entity.LocalMedia;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.TopicGdAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MyTopicData;
import com.zbmf.StockGroup.beans.PointsBean;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.listener.ScrollViewChangeListener;
import com.zbmf.StockGroup.utils.EditTextUtil;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.DrawableCenterTextView;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.OverscrollHelper;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivty extends BaseActivity implements OnClickListener, TopicGdAdapter.ItemClickSkip,
        TopicGdAdapter.DZClick, ScrollViewChangeListener {
    private String mTopic_id;
    private boolean isFirst = true;
    private MyTopicData mMyTopicData;
    private ImageView mHt_detail_iv;
    private TextView mTopicName;
    private TextView mTopicTitle;
    private TextView mGzCount;
    private TextView mGdCount;
    private TextView mGz_btn;
    //    private TextView mJianjie_tv;
    private PullToRefreshScrollView mGdScrollview;
    private DrawableCenterTextView mFb_gd_btn;
    private int mPage = 1;
    private int mTotal;
    private TopicGdAdapter mTopicGdAdapter;
    private String mAvatar;
    private int mStatus;
    private LinearLayout mLl_none;
    private TextView mNo_message_text;
    private ExpandableTextView mExpand_text_view;
    private SparseBooleanArray mCollapsedStatus;
    private OverscrollHelper mOverscrollHelper;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_topic_detail;
    }

    @Override
    public void initView() {
        initTitle("话题");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTopic_id = extras.getString("topic_id");
            mAvatar = extras.getString("avatar");
            mStatus = extras.getInt("status");
            if (TextUtils.isEmpty(mTopic_id)) {
                return;
            }
        }
        mImgs = new ArrayList<>();

        mOverscrollHelper = new OverscrollHelper();
        mOverscrollHelper.setScrollViewChangeListener(this);
        mCollapsedStatus = new SparseBooleanArray();

        mLl_none = getView(R.id.ll_none);
        mNo_message_text = getView(R.id.no_message_text);

        mHt_detail_iv = getView(R.id.ht_detail_iv);
        mTopicName = getView(R.id.topicName);
        mTopicTitle = getView(R.id.topicTitle);
        mGzCount = getView(R.id.gzCount);
        mGdCount = getView(R.id.gdCount);
        mGz_btn = getView(R.id.gz_btn);
        mExpand_text_view = getView(R.id.expand_text_view);
//        mJianjie_tv = getView(R.id.jianjie_tv);
        mGdScrollview = getView(R.id.gdScrollview);
        ListViewForScrollView gdListView = getView(R.id.gdListView);
        mFb_gd_btn = getView(R.id.fb_gd_btn);
        if (mStatus == 1) {
            mGz_btn.setText("已关注");
            setGzBg();
        }
        mTopicGdAdapter = new TopicGdAdapter(this, mCollapsedStatus,this,mImgs);
        mTopicGdAdapter.setItemClickSkip(this);
        mTopicGdAdapter.setDZClick(this);
        gdListView.setAdapter(mTopicGdAdapter);
        mGdScrollview.setMode(PullToRefreshBase.Mode.BOTH);
        mGdScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mTopicGdAdapter.getList() != null) {
                    mTopicGdAdapter.getList().clear();
                }
                mPage = 1;
                getHtGdList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mTopicGdAdapter.getList() != null) {
                    if (mTopicGdAdapter.getList().size() == mTotal) {
                        showToast("暂无更多数据");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mGdScrollview.onRefreshComplete();
                            }
                        }, 1000);
                    } else {
                        mPage += 1;
                        getHtGdList();
                    }
                }
            }
        });
        //注册广播
        IntentFilter filter = new IntentFilter(IntentKey.FLUSH_TOPIC_DETAI_FLAG);
        registerReceiver(mBroadcastReceiver, filter);
        IntentFilter intentFilter = new IntentFilter(IntentKey.FLUSH_TOPIC_LIST);
        registerReceiver(nBroadCast, intentFilter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mOverscrollHelper.setScrollViewChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOverscrollHelper.setScrollViewChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void initData() {
        getHtDetail();
        getHtGdList();
    }

    //获取话题详情
    private void getHtDetail() {
        //获取话题详情
        WebBase.getHtDetail(mTopic_id, new JSONHandler(true, this, getString(R.string.loading)) {

            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")) {
                    JSONObject data = obj.optJSONObject("data");
                    int topic_id = data.optInt("topic_id");
                    int type_id = data.optInt("type_id");
                    int vp_number = data.optInt("vp_number");
                    int is_hot = data.optInt("is_hot");
                    int users = data.optInt("users");
                    mStatus = data.optInt("status");
                    String img = data.optString("img");
                    String name = data.optString("name");
                    String title = data.optString("title");
                    String body = data.optString("body");
                    String created_at = data.optString("created_at");
                    mMyTopicData = new MyTopicData(topic_id, type_id, vp_number, is_hot, users,
                            mStatus, img, name, title, body, created_at);
                    setViewTv();
                    if (mStatus == 1) {
                        mGz_btn.setText("已关注");
                        setGzBg();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Log.e("--TAG", err_msg);
            }
        });
    }
    private List<LocalMedia> mImgs;
    private void getHtGdList() {
        //获取话题列表
        WebBase.getHtGdIdeaList(mTopic_id, String.valueOf(mPage), new JSONHandler(isFirst,this, getString(R.string.loading)) {

            @Override
            public void onSuccess(JSONObject obj) {
                mGdScrollview.onRefreshComplete();
                if (obj.optString("status").equals("ok")) {
                    mPage = obj.optInt("page");
                    mTotal = obj.optInt("total");
                    JSONArray points = obj.optJSONArray("points");
                    List<PointsBean> pointList=null;
                    if (points!=null){
                        pointList = JSONParse.getPointList(points);
                    }
                    //分页
                    if (mPage <= 1) {
                        mTopicGdAdapter.setList(pointList);
                        for (int i = 0; i < pointList.size(); i++) {
                            String img_keys = pointList.get(i).getImg_keys();
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(img_keys);
                            Log.i("--TAG","--   imgPath :"+img_keys);
                            mImgs.add(localMedia);
                        }
                    } else {
                        mTopicGdAdapter.addList(pointList);
                        for (int i = 0; i < pointList.size(); i++) {
                            String img_keys = pointList.get(i).getImg_keys();
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(img_keys);
                            Log.i("--TAG","--   imgPath :"+img_keys);
                            mImgs.add(localMedia);
                        }
                    }
                    if (mTotal < 1) {
                        mLl_none.setVisibility(View.VISIBLE);
                        mNo_message_text.setText("暂无观点");
                    } else {
                        mLl_none.setVisibility(View.GONE);
                    }
                }
                isFirst = false;
            }

            @Override
            public void onFailure(String err_msg) {
                mGdScrollview.onRefreshComplete();
                Log.e("--TAG", err_msg);
            }
        });
    }

    private void setGzBg() {
        mGz_btn.setBackground(getResources().getDrawable(R.drawable.btn_bg2));
        mGz_btn.setTextColor(getResources().getColor(R.color.cb8));
        Drawable drawable = this.getResources().getDrawable(R.drawable.dgou);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mGz_btn.setCompoundDrawables(drawable, null, null, null);
    }

    private void setViewTv() {
        if (mMyTopicData != null) {
            if (!TextUtils.isEmpty(mAvatar)) {
                ImageLoader.getInstance().displayImage(mAvatar, mHt_detail_iv, ImageLoaderOptions.ProgressOptions());
            } else {
                ImageLoader.getInstance().displayImage(mMyTopicData.getImg(), mHt_detail_iv, ImageLoaderOptions.ProgressOptions());
            }
            mTopicName.setText(String.format("#%s",mMyTopicData.getTitle()));
            mTopicTitle.setText(/*String.format("#%s",*/mMyTopicData.getName()/*)*/);
            mGzCount.setText(String.format(mMyTopicData.getUsers() + "%s", "关注"));
            mGdCount.setText(String.format(mMyTopicData.getVp_number() + "%s", "观点"));
            String body = mMyTopicData.getBody();
            mExpand_text_view.setText(EditTextUtil.getContent(this, body), mCollapsedStatus, 0);
            mStatus = mMyTopicData.getStatus();
            if (mStatus == 1) {
                mGz_btn.setText("已关注");
                setGzBg();
            } else {
                mGz_btn.setText("+关注");
            }
        }
    }

    private void gzTopic(int status) {
        if (!TextUtils.isEmpty(mTopic_id)) {
            WebBase.gzTopic(mTopic_id, String.valueOf(status), new JSONHandler() {
                @Override
                public void onSuccess(JSONObject obj) {
                    if (obj.optString("status").equals("ok")) {
                        int is_subscribe1 = obj.optInt("is_subscribe");
                        if (is_subscribe1 == 1) {
                            showToast("关注成功");
                            mGz_btn.setText("已关注");
                            setGzBg();
                            mStatus = 1;
                            getHtDetail();
                        } else {
                            showToast("取消关注成功");
                            mGz_btn.setText("+关注");
                            mGz_btn.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                            mGz_btn.setTextColor(getResources().getColor(R.color.white));
                            mGz_btn.setCompoundDrawables(null,null,null,null);
                            mStatus = 0;
                            getHtDetail();
                        }
                        //发送广播,通知关注的页面更新数据
                        Intent intent = new Intent(IntentKey.RECEIVER_FLAG);
                        intent.putExtra("update", "update");
                        sendBroadcast(intent);
                    }
                }

                @Override
                public void onFailure(String err_msg) {
                    Log.e("--TAG", err_msg);
                }
            });
        }
    }

    @Override
    public void addListener() {
        mGz_btn.setOnClickListener(this);
        mFb_gd_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gz_btn:
                if (mStatus == 0) {
                    //当前未关注
                    gzTopic(1);
                } else if (mStatus == 1) {
                    //当前已关注
                    gzTopic(0);
                }
                break;
            case R.id.fb_gd_btn:
                if (!TextUtils.isEmpty(mTopic_id) && mMyTopicData != null) {
                    ShowActivity.skipFB_GD_Activity(this, mTopic_id, mMyTopicData.getTitle());
                }
                break;
        }
    }

    private TopicGdAdapter.ViewHolder mHolder;

    @Override
    public void skipNext(String viewpoint_id, TopicGdAdapter.ViewHolder holder) {
        mHolder = holder;
        ShowActivity.skipGDDetailActivity(this, viewpoint_id);
    }

    @Override
    public void dzClick(String viewpoint_id, String status, final TopicGdAdapter.ViewHolder holder) {
        //观点点赞
        WebBase.pointDz(viewpoint_id, status, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")){
                    int hits = obj.optInt("hits");
                    mTopicGdAdapter.setDzCount(String.valueOf(hits),holder);
                }
            }

            @Override
            public void onFailure(String err_msg) {
               showToast(err_msg);
            }
        });
    }

    //注册广播，接收通知，更新数据
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String hits = intent.getStringExtra("hits");
            String comment_count = intent.getStringExtra("comment_count");
            if (!TextUtils.isEmpty(hits)) {
                mTopicGdAdapter.setDzCount(hits, mHolder);
            }
            if (!TextUtils.isEmpty(comment_count)) {
                mTopicGdAdapter.setCommentCount(comment_count, mHolder);
            }
        }
    };

    private BroadcastReceiver nBroadCast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPage = 1;
            getHtGdList();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(nBroadCast);
    }

    @Override
    public void onScrollChanged(PullToRefreshScrollView scrollView, int x, int y, int oldx, int oldy) {

    }

    @Override
    public void scrollTop() {
        if (mFb_gd_btn.getVisibility() == View.VISIBLE) {
            Log.i("--TAG","--    滑动  scrollTop ");
            mFb_gd_btn.setVisibility(View.GONE);
        }
    }

    @Override
    public void scrollDown() {
        if (mFb_gd_btn.getVisibility() == View.GONE) {
            Log.i("--TAG","--    滑动  scrollDown ");
            mFb_gd_btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void scrollBottom() {

    }

    @Override
    public void onScroll(int x, int y) {

    }

    @Override
    public void scrollStop() {
//        if (mFb_gd_btn.getVisibility() == View.GONE) {
//            mFb_gd_btn.setVisibility(View.VISIBLE);
//        }
    }
}
