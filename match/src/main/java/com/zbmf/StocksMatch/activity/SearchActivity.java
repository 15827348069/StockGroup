package com.zbmf.StocksMatch.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.SearchMatchAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.SearchMatchBean;
import com.zbmf.StocksMatch.listener.ISearchView;
import com.zbmf.StocksMatch.presenter.SearchPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.StocksMatch.view.ShowOrHideProgressDialog;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshListView;

import java.util.List;

import butterknife.BindView;

public class SearchActivity extends BaseActivity<SearchPresenter> implements ISearchView {
    @BindView(R.id.search_et)
    EditText searchEt;
    @BindView(R.id.cancel_tv)
    TextView cancelTv;
    @BindView(R.id.pull_list)
    PullToRefreshListView pullList;
    private SearchPresenter mSearchPresenter;
    private int page = 1;
    private int per_page = ParamsKey.D_PERPAGE;
    private SearchMatchAdapter mSearchMatchAdapter;
    private int mTotal;
    private Handler mHandler=new Handler();

    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected String initTitle() {
        return "搜索比赛";
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMatch();
            }
        });
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchMatch();
                    return true;
                }
                return false;
            }
        });
        //listView的点击事件
        pullList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowActivity.showSearchMatch(SearchActivity.this,mSearchMatchAdapter.getItem(position-1));
            }
        });
        pullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                if (!TextUtils.isEmpty(searchEt.getText().toString().trim())) {
                    page = 1;
                    per_page = 10;
                    //清空listview的数据
                    mSearchMatchAdapter.clearList();
                    ShowOrHideProgressDialog.showProgressDialog(SearchActivity.this,
                            SearchActivity.this, getString(R.string.hard_loading));
                    mSearchPresenter.serachMatch(searchEt.getText().toString().trim(), page, per_page);
                    ShowOrHideProgressDialog.showProgressDialog(SearchActivity.this,
                            SearchActivity.this,getString(R.string.searching));
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                if (mTotal>mSearchMatchAdapter.getList().size()){
                    if (!TextUtils.isEmpty(searchEt.getText().toString().trim())) {
                        page+=1;
                        mSearchPresenter.serachMatch(searchEt.getText().toString().trim(), page, per_page);
                    }
                }else {
                    showToast(getString(R.string.no_data));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pullList.onRefreshComplete();
                        }
                    },1000);
                }
            }
        });
        mSearchMatchAdapter = new SearchMatchAdapter(this);
        pullList.setAdapter(mSearchMatchAdapter);
    }

    @Override
    protected SearchPresenter initPresent() {
        mSearchPresenter = new SearchPresenter();
        return mSearchPresenter;
    }

    public void clearEt(View v) {
        searchEt.getText().clear();
    }

    public void searchMatch() {
        if (!TextUtils.isEmpty(searchEt.getText().toString().trim())) {
            mSearchPresenter.setKeyWord(searchEt.getText().toString().trim());
            mSearchPresenter.serachMatch(searchEt.getText().toString().trim(), page, per_page);
            ShowOrHideProgressDialog.showProgressDialog(this,this,getString(R.string.searching));
        }
    }

    //获取搜索到比赛的结果
    @Override
    public void searchMatchResult(SearchMatchBean searchMatchBean) {
        ShowOrHideProgressDialog.disMissProgressDialog();
        if (pullList.isRefreshing()) {
            pullList.onRefreshComplete();
        }
        DissLoading();
        if (searchMatchBean != null) {
            SearchMatchBean.Result result = searchMatchBean.getResult();
            if (result != null) {
                int nPage = result.getPage();
                mTotal = result.getTotal();
                page = nPage;
                List<SearchMatchBean.Result.Matches> matches = result.getMatches();
                if (matches.size()==0){
                    showToast(getString(R.string.no_result));
                }
                List<SearchMatchBean.Result.Matches> list = mSearchMatchAdapter.getList();
                if (list == null) {
                    mSearchMatchAdapter.setList(matches);
                } else {
                    mSearchMatchAdapter.addList(matches);
                }
            }
        }else {
            showToast(getString(R.string.no_result));
        }
    }
}
