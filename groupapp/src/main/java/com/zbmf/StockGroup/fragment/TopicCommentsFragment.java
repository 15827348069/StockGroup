package com.zbmf.StockGroup.fragment;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.TopicCommentAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.CommentBean;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.listener.ScrollViewChangeListener;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.MyScrollView;
import com.zbmf.StockGroup.view.OverscrollHelper;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopicCommentsFragment extends BaseFragment implements AbsListView.OnScrollListener,ScrollViewChangeListener {

    private String mViewpoint_id;
    private TopicCommentAdapter mTopicCommentAdapter;
    private int page=1;
    private boolean isFirst=true;
    private int mTotal;
    private Intent mIntent;
    private LinearLayout mLl_none;
    private TextView mNo_message_text;
    private PullToRefreshScrollView mCommentScrollview;
    private ListViewForScrollView mCommentListView;
    private static MyScrollView mMySc;
    @SuppressLint("StaticFieldLeak")
    private static LinearLayout mBottomTab;

    public TopicCommentsFragment() {}

    public static TopicCommentsFragment topicCommentInstance(String viewPointId,MyScrollView mySc,LinearLayout bottomTab){
        mMySc=mySc;
        mBottomTab=bottomTab;
        TopicCommentsFragment topicCommentsFragment = new TopicCommentsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("viewPointId",viewPointId);
        topicCommentsFragment.setArguments(bundle);
        return topicCommentsFragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_topic_comments, null);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        Bundle arguments = getArguments();
        mViewpoint_id = arguments.getString("viewPointId");
        //注册广播
        getActivity().registerReceiver(comBroadCast,new IntentFilter(IntentKey.FLUSH_TOPIC_COMMENT_LIST));
        new OverscrollHelper().setScrollViewChangeListener(this);
        mCommentScrollview = getView(R.id.commentScrollview);
        mCommentListView = getView(R.id.commentListView);
        mLl_none = getView(R.id.ll_none);
        mNo_message_text = getView(R.id.no_message_text);
        mLl_none.setVisibility(View.VISIBLE);
        mNo_message_text.setText("暂无评论");

        mTopicCommentAdapter = new TopicCommentAdapter(getActivity());
        mCommentListView.setAdapter(mTopicCommentAdapter);
        mCommentScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mTopicCommentAdapter.getList()!=null){
                    page=1;
                    getCommentList();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                  if (mTopicCommentAdapter.getList()!=null){
                      if (mTopicCommentAdapter.getList().size()==mTotal){
                          showToast("暂无更多评论");
                          new Handler().postDelayed(new Runnable() {
                              @Override
                              public void run() {
                                  mCommentScrollview.onRefreshComplete();
                              }
                          },1000);
                      }else {
                          page+=1;
                          getCommentList();
                      }
                  }
            }
        });
        //设置listview的滚动监听；这个主要监听的是listview滑动到最后一条时，scrollview就能滚动了
        mCommentListView.setOnScrollListener(this);
        //设置listveiw的触屏事件；
        mCommentListView.setOnTouchListener(new View.OnTouchListener() {


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

    //获取Adapter的数据集合
    public List<CommentBean> getCommentBeanList(){
        List<CommentBean> list = null;
        if (mTopicCommentAdapter!=null){
            if (mTopicCommentAdapter.getList()!=null){
                 list = mTopicCommentAdapter.getList();
            }
        }
        return list;
    }

    @Override
    protected void initData() {
        //获取评论列表
        getCommentList();
    }
    public void getCommentList() {
        if (!TextUtils.isEmpty(mViewpoint_id)) {
            WebBase.commentList(mViewpoint_id, String.valueOf(page), new JSONHandler(isFirst, getActivity(), getString(R.string.loading)) {
                @Override
                public void onSuccess(JSONObject obj) {
                    mCommentScrollview.onRefreshComplete();
                    if (obj.optString("status").equals("ok")) {
                        page = obj.optInt("page");
                        mTotal = obj.optInt("total");
                        JSONArray comments = obj.optJSONArray("comments");
                        List<CommentBean> commentList = JSONParse.getCommentList(comments);
                        if (page <= 1) {
                            mTopicCommentAdapter.setList(commentList);
                        } else {
                            mTopicCommentAdapter.addList(commentList);
                        }
                        if (mTotal<1){
                            mLl_none.setVisibility(View.VISIBLE);
                            mCommentListView.setVisibility(View.GONE);
                            mNo_message_text.setText("暂无评论");
                        }else {
                            mCommentListView.setVisibility(View.VISIBLE);
                            mLl_none.setVisibility(View.GONE);
                        }
                        isFirst = false;
                    }
                }

                @Override
                public void onFailure(String err_msg) {
                    Log.e("--TAG", err_msg);
                }
            });
        }
    }

    private BroadcastReceiver comBroadCast=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            page = intent.getIntExtra("page",0);
            if (page!=0){
                getCommentList();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(comBroadCast);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if((firstVisibleItem+visibleItemCount)==totalItemCount/*||firstVisibleItem==0*/){
            //如果listview部分加载到最后一条了 ，拦截listview的触屏事件，意思就是scrollview可以滚动了；
            mMySc.requestDisallowInterceptTouchEvent(false);
        }else {
            mMySc.requestDisallowInterceptTouchEvent(true);
        }
        if (firstVisibleItem==0){
            mMySc.requestDisallowInterceptTouchEvent(false);
        }else {
            mMySc.requestDisallowInterceptTouchEvent(true);
        }
    }

    @Override
    public void onScrollChanged(PullToRefreshScrollView scrollView, int x, int y, int oldx, int oldy) {}

    @Override
    public void scrollTop() {
        mBottomTab.setVisibility(View.GONE);
    }

    @Override
    public void scrollDown() {
        mBottomTab.setVisibility(View.VISIBLE);
    }

    @Override
    public void scrollBottom() {}

    @Override
    public void onScroll(int x, int y) {}

    @Override
    public void scrollStop() {}
}
