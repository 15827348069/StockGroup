package com.zbmf.StockGroup.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.SimulateOneStockCommitActivity;
import com.zbmf.StockGroup.activity.StockBuyActivity;
import com.zbmf.StockGroup.adapter.LatestCommentAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.StockComments;
import com.zbmf.StockGroup.beans.StockholdsBean;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LatestCommentFragment extends BaseFragment implements LatestCommentAdapter.OnAdapterClickListener {
    private PullToRefreshListView mPullToRefreshListView;
    private List<StockComments> list = new ArrayList<>();
    LatestCommentAdapter adapter;
    private int page, pages;
    private boolean isRush;
    private LinearLayout ll_none;
    private TextView no_message_text;
    private MatchInfo matchInfo;
    public void OnRushMatch(MatchInfo match){
        this.matchInfo=match;
    }
    public LatestCommentFragment() {
    }

    public static LatestCommentFragment newInstance(MatchInfo matchInfo) {
        LatestCommentFragment fragment = new LatestCommentFragment();
        Bundle args = new Bundle();
        args.putSerializable(IntentKey.MATCH_BAEN,matchInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchInfo= (MatchInfo) getArguments().getSerializable(IntentKey.MATCH_BAEN);
        }
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_stock_latest_comment, null);
    }

    @Override
    protected void initView() {
        mPullToRefreshListView = getView(R.id.listview);
        ll_none = getView(R.id.ll_none);
        no_message_text = getView(R.id.no_message_text);
        no_message_text.setText("暂无评论");
        adapter = new LatestCommentAdapter(getActivity());
        adapter.setOnAdapterClickListener(this);
        adapter.setList(list);
        mPullToRefreshListView.setAdapter(adapter);

        //跳转至个股评论汇总
        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.STOCKHOLDER, list.get(position-1).getDealSys());
                ShowActivity.showActivity(getActivity(), bundle, SimulateOneStockCommitActivity.class);
            }
        });
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                isRush = true;
                getsComment();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page += 1;
                getsComment();
            }
        });
    }
    public  void scrollTop(){
        mPullToRefreshListView.ScrollTop();
    }
    @Override
    protected void initData() {
        page=1;
        getsComment();
    }
    private void getsComment(){
        WebBase.getsComment(page,null, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                mPullToRefreshListView.onRefreshComplete();
                if(obj.has("result")){
                    JSONObject result=obj.optJSONObject("result");
                    if(isRush){
                        list.clear();
                        isRush=false;
                    }
                    page=result.optInt("page");
                    pages=result.optInt("pages");
                    if(result.has("stock_comments")){
                        list.addAll(JSONParse.getCommentsList(result.optJSONArray("stock_comments")));
                    }
                    if(list.size()>0){
                        adapter.notifyDataSetChanged();
                        ll_none.setVisibility(View.GONE);
                        mPullToRefreshListView.setVisibility(View.VISIBLE);
                    }else if(page==pages){
                        ll_none.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
                mPullToRefreshListView.onRefreshComplete();
            }
        });
    }
    @Override
    public void onAdapterClickListener(View v, StockholdsBean dealSys) {
        switch (v.getId()) {
            case R.id.tv_comment:
                Bundle bundle=new Bundle();
                bundle.putSerializable(IntentKey.STOCKHOLDER,dealSys);
                bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                ShowActivity.showActivity(getActivity(), bundle,SimulateOneStockCommitActivity.class);
                break;
            case R.id.tv_trade_follow_buy:
                ShowActivity.showWebViewActivity(getActivity(), HtmlUrl.TRADER_BUY+dealSys.getSymbol());
                break;
            case R.id.tv_follow_buy:
                bundle = new Bundle();
                bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                bundle.putSerializable(IntentKey.STOCKHOLDER, dealSys);
                ShowActivity.showActivity(getActivity(), bundle, StockBuyActivity.class);
                break;
        }
    }

}
