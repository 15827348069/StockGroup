package com.zbmf.StockGroup.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.HtAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MyTopicData;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * 话题分类的fragment
 */
public class HtFragment extends BaseFragment implements HtAdapter.HtItemClick {

    private PullToRefreshScrollView mHtScrollview;
    private ListViewForScrollView mHtListView;
    private HtAdapter mHtAdapter;
    private int mTotal;
    private int mPage;
    private LinearLayout mLl_none;
    private int mType_id;
    private boolean isFirst=true;

    public static HtFragment HtInstance(int type_id) {
        HtFragment htFragment = new HtFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type_id", type_id);
        htFragment.setArguments(bundle);
        return htFragment;
    }

    public HtFragment() {

    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_ht, null);
        mType_id = getArguments().getInt("type_id");//对应的类型Id

        mHtScrollview = view.findViewById(R.id.htScrollview);
        mHtListView = view.findViewById(R.id.htListView);
        mLl_none = view.findViewById(R.id.ll_none);
        TextView no_message_text = view.findViewById(R.id.no_message_text);
        mLl_none.setVisibility(View.VISIBLE);
        no_message_text.setText("暂无话题内容");

        mHtAdapter = new HtAdapter(getActivity());
        mHtAdapter.setHtItemClick(this);
        mHtListView.setAdapter(mHtAdapter);
        //获取分类话题列表
        if (isAdded()) {
            if (mHtAdapter.getList()!=null){
                mHtAdapter.getList().clear();
            }
            mPage=1;
            getTopicsList();
        }

        mHtScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mHtAdapter.getList()!=null){
                    mHtAdapter.getList().clear();
                }
                mPage=1;
                getTopicsList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mHtAdapter.getList()!=null){
                    if (mHtAdapter.getList().size()==mTotal){
                        showToast("暂无更多数据");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mHtScrollview.onRefreshComplete();
                            }
                        },1000);
                    }else {
                        mPage+=1;
                        getTopicsList();
                    }
                }
            }
        });

        return view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    private void getTopicsList() {
        //获取话题列表
        WebBase.getTopicsList(String.valueOf(mType_id),String.valueOf(mPage), new JSONHandler(isFirst, getActivity(), getString(R.string.loading)) {

            @Override
            public void onSuccess(JSONObject obj) {
                mHtScrollview.onRefreshComplete();
                if (obj.optString("status").equals("ok")) {
                    mPage = obj.optInt("page");
                    mTotal = obj.optInt("total");
                    JSONArray topic_list = obj.optJSONArray("topic_list");
                    List<MyTopicData> myTopicList = JSONParse.getMyTopicList(topic_list);
                    //后台接口有分页，此处要设置分页
                    //打印数据----》观察数据数量
                    if (myTopicList.size() > 0) {
                        mLl_none.setVisibility(View.GONE);
                    }
                    if (mPage <= 1) {
                            mHtAdapter.setList(myTopicList);
                    } else {
                        mHtAdapter.addList(myTopicList);
                    }
                    isFirst=false;
                }
            }

            @Override
            public void onFailure(String err_msg) {
                mHtScrollview.onRefreshComplete();
                Log.e("--TAG", err_msg);
            }
        });
    }

    @Override
    public void itemClick(int topic_id,String avatar,int status) {
        ShowActivity.skipTopicDetailActivty(getActivity(),String.valueOf(topic_id),avatar,status);
    }
}
