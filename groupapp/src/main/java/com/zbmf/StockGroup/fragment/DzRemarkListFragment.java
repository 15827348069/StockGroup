package com.zbmf.StockGroup.fragment;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.GDDetailActivity;
import com.zbmf.StockGroup.adapter.DzAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.DzUser;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.listener.ScrollViewChangeListener;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.MyScrollView;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * 点赞的fragment
 */
public class DzRemarkListFragment extends BaseFragment implements  AbsListView.OnScrollListener,ScrollViewChangeListener {


    private String mViewPointID;
    private LinearLayout mLl_none;
    private TextView mNo_message_text;
    private DzAdapter mDzAdapter;
    private boolean isFirst = true;
    private PullToRefreshScrollView mCommentScrollview;
    private GDDetailActivity mGdDetailActivity;
    private static MyScrollView mMySc;
    @SuppressLint("StaticFieldLeak")
    private static LinearLayout mBotTab;

    public DzRemarkListFragment() {

    }

    public static DzRemarkListFragment dzRemarkInstance(String viewPointID, MyScrollView myScv,LinearLayout bottomTab) {
        mMySc=myScv;
        mBotTab=bottomTab;
        DzRemarkListFragment dzRemarkListFragment = new DzRemarkListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("viewPointID", viewPointID);
        dzRemarkListFragment.setArguments(bundle);
        return dzRemarkListFragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_dz_remark_list, null);
    }

    @Override
    protected void initView() {
        mGdDetailActivity = (GDDetailActivity) getActivity();
        mViewPointID = getArguments().getString("viewPointID");
        //注册广播
        IntentFilter filter = new IntentFilter(IntentKey.FLUSH_TOPIC_DETAI_FLAG);
        mGdDetailActivity.registerReceiver(mBroadcastReceiver, filter);

        mCommentScrollview = getView(R.id.commentScrollview);
        ListViewForScrollView dzListView = getView(R.id.dzListView);
        mLl_none = getView(R.id.ll_none);
        mNo_message_text = getView(R.id.no_message_text);
        mLl_none.setVisibility(View.VISIBLE);
        mNo_message_text.setText("暂无点赞");
        mDzAdapter = new DzAdapter(getActivity());

        dzListView.setAdapter(mDzAdapter);
        //设置listview的滚动监听；这个主要监听的是listview滑动到最后一条时，scrollview就能滚动了
        dzListView.setOnScrollListener(this);
        //设置listveiw的触屏事件；
        dzListView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //点击listview里面滚动停止时，scrollview拦截listview的触屏事件，就是scrollview该滚动了
                    mMySc.requestDisallowInterceptTouchEvent(false);
                } else {
                    //当listview在滚动时，不拦截listview的滚动事件；就是listview可以滚动，
                    mMySc.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        getUserDzList();
    }

    //获取点赞列表
    private void getUserDzList() {
        if (!TextUtils.isEmpty(mViewPointID)) {
            WebBase.getDzUserList(mViewPointID, new JSONHandler(isFirst, getActivity(), getString(R.string.loading)) {
                @Override
                public void onSuccess(JSONObject obj) {
                    mCommentScrollview.onRefreshComplete();
                    if (obj.optString("status").equals("ok")) {
                        JSONArray users = obj.optJSONArray("users");
                        List<DzUser> dzUser = JSONParse.getDzUser(users);
                        //接口没有分页--->直接setList()
                        mDzAdapter.setList(dzUser);
                        if (dzUser.size() < 1) {
                            mLl_none.setVisibility(View.VISIBLE);
                            mNo_message_text.setText("暂无点赞");
                        } else {
                            mLl_none.setVisibility(View.GONE);
                        }
                        isFirst = false;
                    }
                }

                @Override
                public void onFailure(String err_msg) {
                    mCommentScrollview.onRefreshComplete();
                    Log.e("--TAG_e", err_msg);
                }
            });
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String hits = intent.getStringExtra("hits");
//            if (!TextUtils.isEmpty(hits)){
            getUserDzList();
//            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGdDetailActivity.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if((firstVisibleItem+visibleItemCount)==totalItemCount||firstVisibleItem==0){
            //如果listview部分加载到最后一条了 ，拦截listview的触屏事件，意思就是scrollview可以滚动了；
            mMySc.requestDisallowInterceptTouchEvent(false);
        }else {
            mMySc.requestDisallowInterceptTouchEvent(true);
        }
    }

    @Override
    public void onScrollChanged(PullToRefreshScrollView scrollView, int x, int y, int oldx, int oldy) {}

    @Override
    public void scrollTop() {
        mBotTab.setVisibility(View.GONE);
    }

    @Override
    public void scrollDown() {
        mBotTab.setVisibility(View.VISIBLE);
    }

    @Override
    public void scrollBottom() {}

    @Override
    public void onScroll(int x, int y) {}

    @Override
    public void scrollStop() {}
}
