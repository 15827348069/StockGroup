package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.OneStockAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.DealSys;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.ShareBean;
import com.zbmf.StockGroup.beans.Stock;
import com.zbmf.StockGroup.beans.StockComments;
import com.zbmf.StockGroup.beans.StockMode;
import com.zbmf.StockGroup.beans.StockholdsBean;
import com.zbmf.StockGroup.constans.AppConfig;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.dialog.EditTextDialog;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.utils.ShowWebShareLayout;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SimulateOneStockCommitActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshScrollView mScrollview;
    private ListViewForScrollView mListview;
    private List<StockComments> mStockCommitList = new ArrayList<>();
    private TextView tv_share,tv_msg;
    private StockholdsBean stockholdsBean;
    private DealSys dealSys;
    private StockMode stockMode;
    private OneStockAdapter adapter;
    private int page,pages;
    private boolean isRush;
    private LinearLayout ll_none;
    private TextView no_message_text;
//    private LinearLayout ly_sell_layout;
//    private RoundedCornerImageView iv_rcv1;
//    private TextView tv_nickname,tv_deal_content;
    private String symbol,symbol_name,share_title;
    private MatchInfo matchInfo;
    private ShowWebShareLayout showWebShareLayout;
    private EditTextDialog editTextDialog;
//    private TextView stock_price,stock_amount,stock_amount_price,tv_stock_ask;
    private RelativeLayout stock_title_layout;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_simulate_one_stock;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(showWebShareLayout==null){
            showWebShareLayout=new ShowWebShareLayout(this);
        }
        showWebShareLayout.onNewIntent(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
//        stock_price=getView(R.id.tv_stock_price);
//        stock_amount=getView(R.id.tv_stock_amount);
//        stock_amount_price=getView(R.id.tv_stock_amount_price);
        stock_title_layout=getView(R.id.stock_title_layout);
//        tv_stock_ask=getView(R.id.tv_stock_ask);
//        tv_latest_comment = (TextView) findViewById(R.id.tv_comment);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        mScrollview=getView(R.id.mPullToRefreshScrollView);
        mScrollview.setMode(PullToRefreshBase.Mode.BOTH);
        mListview = (ListViewForScrollView) findViewById(R.id.listview);
//        ly_sell_layout=getView(R.id.ly_sell_layout);
//        iv_rcv1=getView(R.id.iv_rcv1);
//        tv_nickname=getView(R.id.tv_nickname);
//        tv_deal_content=getView(R.id.tv_deal_content);
        adapter = new OneStockAdapter(this, mStockCommitList);
        mListview.setAdapter(adapter);
        ll_none=getView(R.id.ll_none);
        no_message_text=getView(R.id.no_message_text);
        no_message_text.setText("暂无评论");
//        tv_latest_comment.setTextColor(getResources().getColor(R.color.black_66));
        Drawable drawable_n = getResources().getDrawable(R.drawable.icon_simulate);
        drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(),drawable_n.getMinimumHeight());
//        tv_latest_comment.setCompoundDrawables(drawable_n,null,null,null);
//        tv_latest_comment.setBackground(getResources().getDrawable(R.drawable.shape_layout_line_gray_50dp));
        if(editTextDialog==null){
            editTextDialog= getEditTextDialog();
            editTextDialog.show();
            editTextDialog.dismiss();
        }
        if(showWebShareLayout==null){
            showWebShareLayout=new ShowWebShareLayout(this);
        }
    }

    @Override
    public void initData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            matchInfo= (MatchInfo) getIntent().getSerializableExtra(IntentKey.MATCH_BAEN);
            if( bundle.getSerializable(IntentKey.STOCKHOLDER) instanceof StockholdsBean){
                stockholdsBean= (StockholdsBean) bundle.getSerializable(IntentKey.STOCKHOLDER);
//                ly_sell_layout.setVisibility(View.GONE);
                initTitle(stockholdsBean.getName()+"("+stockholdsBean.getSymbol()+")");
                symbol=stockholdsBean.getSymbol();
                symbol_name=stockholdsBean.getName();
//                tv_latest_comment.setText(stockholdsBean.getComment_count()+"");
//                getSymbol(symbol);
            }else if(bundle.getSerializable(IntentKey.STOCKHOLDER) instanceof DealSys ){
//                ly_sell_layout.setVisibility(View.VISIBLE);
                dealSys= (DealSys) bundle.getSerializable(IntentKey.STOCKHOLDER);
                initTitle(dealSys.getStock_name()+"("+dealSys.getSumbol()+")");
                symbol=dealSys.getSumbol();
                symbol_name=dealSys.getStock_name();
//                ImageLoader.getInstance().displayImage(dealSys.getUser_img(),iv_rcv1, ImageLoaderOptions.AvatarOptions());
//                tv_nickname.setText(dealSys.getNickname());
//                tv_latest_comment.setText(dealSys.getCount_comments());
//                tv_deal_content.setText(dealSys.getDate()+dealSys.getAction()+dealSys.getStock_name()+"("+dealSys.getSumbol()+")");
//                getSymbol(symbol);
              }else if(bundle.getSerializable(IntentKey.STOCKHOLDER) instanceof Stock){
//                ly_sell_layout.setVisibility(View.GONE);
                Stock stock= (Stock) bundle.getSerializable(IntentKey.STOCKHOLDER);
                initTitle(stock.getName()+"("+stock.getSymbol()+")");
                symbol=stock.getSymbol();
                symbol_name=stock.getName();
                setStockMessage(stock);
            }else if(bundle.getSerializable(IntentKey.STOCKHOLDER) instanceof StockMode){
//                ly_sell_layout.setVisibility(View.GONE);
                stockMode=(StockMode) bundle.getSerializable(IntentKey.STOCKHOLDER);
                symbol=stockMode.getSymbol();
                symbol_name=stockMode.getStockNmae();
                initTitle(symbol_name+"("+symbol+")");
//                getSymbol(symbol);
            }
            share_title=symbol_name+"("+symbol+")"+"好不好，来资本魔方聊牛股！";
            getsComment();
        }
    }
    private EditTextDialog getEditTextDialog(){
        return EditTextDialog.createDialog(this, R.style.myDialogTheme)
                .setRightButton("发表")
                .setEditHint("发表个股评论和投资理念...")
                .setSendClick(new EditTextDialog.OnSendClick() {
                    @Override
                    public void onSend(String message, int type) {
                        WebBase.addComment(symbol, message, new JSONHandler(true,SimulateOneStockCommitActivity.this,getString(R.string.commit)) {
                            @Override
                            public void onSuccess(JSONObject obj) {
                                showToast("评论成功");
                                isRush=true;
                                page=1;
                                getsComment();
                                editTextDialog.dismiss();
                            }

                            @Override
                            public void onFailure(String err_msg) {
                                showToast("评论失败");
                            }
                        });
                    }
                });
    }
    @Override
    public void addListener() {
        tv_msg.setOnClickListener(this);
        tv_share.setOnClickListener(this);
//        tv_stock_ask.setOnClickListener(this);
//        findViewById(R.id.tv_trade_follow_buy).setOnClickListener(this);
//        findViewById(R.id.tv_follow_buy).setOnClickListener(this);
//        tv_latest_comment.setOnClickListener(this);
//        tv_latest_comment.setClickable(false);
//        tv_latest_comment.setEnabled(false);
        mScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                isRush=true;
                getsComment();
//                getSymbol(symbol);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page+=1;
                getsComment();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Bundle bundle =null;
        switch (v.getId()) {
            case R.id.tv_stock_ask:
                 bundle=new Bundle();
                bundle.putString(IntentKey.STOCK_NAME,symbol_name);
                bundle.putString(IntentKey.STOCK_SYMOL,symbol);
                ShowActivity.showActivity(this,bundle,AskStockSendActivity.class);
                break;
            case R.id.tv_share:
                showWebShareLayout.showShareLayout(
                        new ShareBean(
                                share_title != null ? share_title : getString(R.string.app_name),
                                AppConfig.SHARE_LOGO_IMG,
                               AppConfig.SHARE_STOCK_URL));
                break;
            case R.id.tv_msg:
                if(editTextDialog==null){
                    editTextDialog= getEditTextDialog();
                }
                editTextDialog.show();
                break;
            case R.id.tv_trade_follow_buy:
                ShowActivity.showWebViewActivity(this, HtmlUrl.TRADER_BUY+symbol);
                break;
            case R.id.tv_follow_buy:
                if(matchInfo==null){
                    getMatchInfo(true);
                }else{
                    bundle=new Bundle();
                    bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                    if(dealSys!=null){
                        bundle.putSerializable(IntentKey.STOCKHOLDER, dealSys);
                    }else if(stockholdsBean!=null){
                        bundle.putSerializable(IntentKey.STOCKHOLDER, stockholdsBean);
                    }else if(stockMode!=null){
                        bundle.putSerializable(IntentKey.STOCKHOLDER, stockMode);
                    }
                    ShowActivity.showActivity(SimulateOneStockCommitActivity.this, bundle, StockBuyActivity.class);
                }
                break;
        }
    }
    private void getsComment(){
        WebBase.getsComment(page,symbol, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if(obj.has("result")){
                    JSONObject result=obj.optJSONObject("result");
                    if(isRush){
                        mStockCommitList.clear();
                        isRush=false;
                    }
                    page=result.optInt("page");
                    pages=result.optInt("pages");
                    mScrollview.onRefreshComplete();
//                    tv_latest_comment.setText(result.optInt("total")+"");
                    if(result.has("stock_comments")){
                        mStockCommitList.addAll(JSONParse.getCommentsList(result.optJSONArray("stock_comments")));
                    }
                    if(mStockCommitList.size()>0){
                        adapter.notifyDataSetChanged();
                        ll_none.setVisibility(View.GONE);
                        mListview.setVisibility(View.VISIBLE);
                    }else if(page==pages){
                        ll_none.setVisibility(View.VISIBLE);
                        mListview.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                mScrollview.onRefreshComplete();
                showToast(err_msg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMatchInfo(false);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setStockMessage(Stock stock){
//        if(stock!=null){
//            stock_price.setText(DoubleFromat.getStockDouble(stock.getCurrent(),2));
//            double amount_price=stock.getCurrent()-stock.getClose();
//            double amount=(amount_price/stock.getClose())*100;
//            stock_amount_price.setText(DoubleFromat.getStockDouble(amount_price,2));
//            String amount_str;
//            if(amount_price<0){
////                setTitleBackGround(getResources().getDrawable(R.drawable.icon_green_stock_title));
////                setActionBackGround(getResources().getDrawable(R.drawable.icon_green_action));
//                stock_title_layout.setBackground(getResources().getDrawable(R.drawable.icon_green_stock));
//                tv_stock_ask.setTextColor(getResources().getColor(R.color.green));
//                amount_str=DoubleFromat.getStockDouble(amount,2)+"%";
//            }else{
////                setTitleBackGround(getResources().getDrawable(R.drawable.icon_red_stock_title));
////                setActionBackGround(getResources().getDrawable(R.drawable.icon_red_action));
//                stock_title_layout.setBackground(getResources().getDrawable(R.drawable.icon_red_stock));
//                tv_stock_ask.setTextColor(getResources().getColor(R.color.colorAccent));
//                amount_str="+"+DoubleFromat.getStockDouble(amount,2)+"%";
//            }
//            stock_amount.setText(amount_str);
//        }
    }
//    private void getSymbol(String symbol) {
//        WebBase.getStockRealtimeInfo(symbol, new JSONHandler() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onSuccess(JSONObject obj) {
//                Stock stock=JSONParse.getStockRealtimeInfo(obj);
//                setStockMessage(stock);
//            }
//
//            @Override
//            public void onFailure(String err_msg) {
//
//            }
//        });
//    }
    private void getMatchInfo(final boolean isShow){
//        WebBase.getPlayer(new JSONHandler() {
        WebBase.getMatchPlayer(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
//                matchInfo=JSONParse.getMatchMessage(obj);
                matchInfo=JSONParse.getMatchMessage1(obj);
                if(isShow){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(IntentKey.MATCH_BAEN, matchInfo);
                    if(dealSys!=null){
                        bundle.putSerializable(IntentKey.STOCKHOLDER, dealSys);
                    }else if(stockholdsBean!=null){
                        bundle.putSerializable(IntentKey.STOCKHOLDER, stockholdsBean);
                    }else if(stockMode!=null){
                        bundle.putSerializable(IntentKey.STOCKHOLDER, stockMode);
                    }
                    ShowActivity.showActivity(SimulateOneStockCommitActivity.this, bundle, StockBuyActivity.class);
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
}
