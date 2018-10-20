package com.zbmf.StocksMatch.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.PrizeAdapter;
import com.zbmf.StocksMatch.api.ParamsKey;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.PrizeListBean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.IWinPrizeView;
import com.zbmf.StocksMatch.presenter.WinAPrizePresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshBase;
import com.zbmf.worklibrary.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WinAPrizeActivity extends BaseActivity<WinAPrizePresenter> implements IWinPrizeView {
    //    @BindView(R.id.all_money)
//    TextView all_money;
//    @BindView(R.id.all_shouyi)
//    TextView all_shouyi;
//    @BindView(R.id.arrow_textview)
//    TextView arrow_textview;
    @BindView(R.id.match_account)
    TextView match_account;
    //    @BindView(R.id.content_view)
//    ListViewForScrollView content_view;
    @BindView(R.id.myscrllview)
    PullToRefreshListView myscrllview;
    @BindView(R.id.no_content)
    LinearLayout no_content;
    @BindView(R.id.no_message_text)
    TextView no_message_text;
    private WinAPrizePresenter mWinAPrizePresenter;
    private MatchInfo mMatchInfo;
    private int mPage;
    private int mTotal;
    private PrizeAdapter mPrizeAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_record;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.win_prize);
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        assert bundle != null;
        if (bundle.getSerializable(IntentKey.MATCH_INFO) instanceof MatchInfo) {
            mMatchInfo = (MatchInfo) bundle.getSerializable(IntentKey.MATCH_INFO);
        }
        mPrizeAdapter = new PrizeAdapter(this);
        myscrllview.setAdapter(mPrizeAdapter);
        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPrizeAdapter.getList() != null) {
                    mPrizeAdapter.clearList();
                }
                mPage = ParamsKey.D_PAGE;
                getPresenter().setFirst(true);
                getPresenter().getDatas();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mTotal > mPrizeAdapter.getList().size()) {
                    mPage += 1;
                    mWinAPrizePresenter.getWinAPrizeList(Constans.MATCH_ID,
                            String.valueOf(mMatchInfo.getResult().getMatch_id()), String.valueOf(mPage));
                } else {
                    showToast(getString(R.string.nomore_loading));
                    myscrllview.onRefreshComplete();
                }
            }
        });
        myscrllview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                showToast(" "+position);
//                PrizeListBean.Result.Records item = mPrizeAdapter.getItem(position);
//                ShowActivity.showStockDetail2(WinAPrizeActivity.this,new StockMode(item.get));
            }
        });
    }

    @Override
    protected WinAPrizePresenter initPresent() {
        mWinAPrizePresenter = new WinAPrizePresenter(Constans.MATCH_ID,
                String.valueOf(mMatchInfo.getResult().getMatch_id()), String.valueOf(ParamsKey.D_PAGE));
        return mWinAPrizePresenter;
    }

    private List<PrizeListBean.Result.Records> recordsList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    public void winAPrizeList(PrizeListBean.Result result) {
        if (myscrllview.isRefreshing()) {
            myscrllview.onRefreshComplete();
        }
        mPage = result.getPage();
        mTotal = result.getTotal();
        List<PrizeListBean.Result.Records> records = result.getRecords();
//        PrizeListBean.Result.Records records1 = new PrizeListBean.Result.Records();
//        records1.setWin_at("2016-06-21 00:00:00");
//        records1.setAward("第四期积分专题日收益第21-30名");
//        records.add(records1);
        match_account.setText("共获奖" + String.valueOf(mTotal) + "次");
        if (records==null||records.size()==0){
            myscrllview.setVisibility(View.GONE);
            no_content.setVisibility(View.VISIBLE);
            no_message_text.setText(getString(R.string.no_award));
        }else {
            no_content.setVisibility(View.GONE);
            myscrllview.setVisibility(View.VISIBLE);
            if (mPrizeAdapter.getList() == null) {
                mPrizeAdapter.setList(records);
            } else {
                if (mPage == ParamsKey.D_PAGE) {
                    recordsList.addAll(records);
                    mPrizeAdapter.clearList();
                    mPrizeAdapter.addList(recordsList);
                    recordsList.clear();
                } else {
                    mPrizeAdapter.addList(records);
                }
            }
        }
    }

    @Override
    public void winPrizeErr(String msg) {
        if (myscrllview.isRefreshing()) {
            myscrllview.onRefreshComplete();
        }
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

}
