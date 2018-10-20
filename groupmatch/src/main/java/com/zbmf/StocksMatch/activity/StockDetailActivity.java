package com.zbmf.StocksMatch.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.MatchAdapter1;
import com.zbmf.StocksMatch.adapter.StockDetailAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.Quotation;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.beans.StockholdsBean;
import com.zbmf.StocksMatch.utils.DataLoadDirection;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.CustomListView;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshBase;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshScrollView;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 股票详情
 * @author Administrator
 */
public class StockDetailActivity extends ExActivity implements View.OnClickListener {
    //TODO 我的比赛列表为空
    private ImageView iv_right,iv_refresh;
    private RelativeLayout rl_title;
    private CustomListView content_view;
    private StockDetailAdapter adapter;
    private LinearLayout ll_buy,ll_top;
    private TextView tv_right,tv_name,tv_symbol,tv_current,tv_max_price,tv_min_price,tv_close_price,tv_open_price,tv_total_amount,tv_high_price,tv_low_price,tv_total_volumn;
    private PullToRefreshScrollView myscrllview;
    private Get2Api server = null;
//    private Quotation quotation;
    private Stock stock;//股票
    private List<Stock> list = new ArrayList<Stock>();
    DecimalFormat df=new DecimalFormat("#0.00");

    private String[] nums = {"一","二","三","四","五"};
    private boolean isup = false;
    private String symbol;
    private MatchAdapter1 adapter1;
    private List<MatchBean> mlist ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        getData();
        new GetMyMatch(this).execute(0);
        setupView();

    }

    String str="";
    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            stock = (Stock) bundle.getSerializable("stock");
        }
    }

    private void setupView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_title).setVisibility(View.GONE);
        tv_right = (TextView)findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        iv_right = (ImageView)findViewById(R.id.iv_right);
        iv_refresh = (ImageView)findViewById(R.id.iv_refresh);
        iv_right.setImageResource(R.drawable.sx);
        iv_right.setVisibility(View.VISIBLE);
        rl_title = (RelativeLayout)findViewById(R.id.rl_title);
        ll_buy = (LinearLayout)findViewById(R.id.ll_buy);
        ll_top = (LinearLayout)findViewById(R.id.ll_top);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_symbol = (TextView)findViewById(R.id.tv_symbol);
        tv_current = (TextView)findViewById(R.id.tv_current);
        tv_max_price = (TextView)findViewById(R.id.tv_max_price);
        tv_min_price = (TextView)findViewById(R.id.tv_min_price);
        tv_close_price = (TextView)findViewById(R.id.tv_close_price);
        tv_open_price = (TextView)findViewById(R.id.tv_open_price);
        tv_total_amount = (TextView)findViewById(R.id.tv_total_amount);
        tv_high_price = (TextView)findViewById(R.id.tv_high_price);
        tv_low_price = (TextView)findViewById(R.id.tv_low_price);
        tv_total_volumn = (TextView)findViewById(R.id.tv_total_volumn);
        iv_right.setOnClickListener(this);
        findViewById(R.id.btn_buy).setOnClickListener(this);
        findViewById(R.id.btn_sale).setOnClickListener(this);
        myscrllview = (PullToRefreshScrollView) findViewById(R.id.myscrllview);
        myscrllview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        content_view = (CustomListView) findViewById(R.id.content_view);
//        listView.setFocusable(false);
        myscrllview.smoothScrollTo(0, 20);
        myscrllview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetStockRealtimeInfoTask(StockDetailActivity.this).execute(DataLoadDirection.Refresh);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
        adapter1 = new MatchAdapter1(this);
        adapter = new StockDetailAdapter(this);
        adapter.setList(list);
        content_view.setAdapter(adapter);

//        if(quotation!=null){
//            tv_name.setText(quotation.getName());
//            tv_symbol.setText(quotation.getSymbol());
//            tv_current.setText(quotation.getCurrent());
//
//            tv_right.setText(getString(R.string.del));
//            symbol = quotation.getSymbol();
//            new GetStockRealtimeInfoTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, DataLoadDirection.Refresh);
//        }

        initData();



    }

    private View getBuyView(final int index){
        View view = LayoutInflater.from(this).inflate(R.layout.stock_item,null);
        TextView tv_buy = (TextView) view.findViewById(R.id.tv_buy);
        TextView tv_buy_price = (TextView) view.findViewById(R.id.tv_buy_price);
        TextView tv_buy_num = (TextView) view.findViewById(R.id.tv_buy_num);
        TextView tv_sell = (TextView) view.findViewById(R.id.tv_sell);
        TextView tv_sell_price = (TextView) view.findViewById(R.id.tv_sell_price);
        TextView tv_sell_num = (TextView) view.findViewById(R.id.tv_sell_num);


        Stock s = list.get(index);
        tv_buy.setText(getString(R.string.buy)+nums[index]);
        tv_buy_price.setText(df.format(s.getBuy()));
        tv_buy_num.setText(s.getVolumn_buy());
        tv_sell.setText(getString(R.string.sale)+nums[index]);
        tv_sell_price.setText(df.format(s.getSell()));
        tv_sell_num.setText(s.getVolumn_sell());

        if(isup){
//            rl_title.setBackgroundColor(Color.rgb(255, 24, 0));
            tv_buy_price.setTextColor(Color.rgb(255, 24, 0));
            tv_sell_price.setTextColor(Color.rgb(255, 24, 0));
        }else{
//            rl_title.setBackgroundColor(Color.rgb(7, 152, 0));
            tv_buy_price.setTextColor(Color.rgb(7, 152, 0));
            tv_sell_price.setTextColor(Color.rgb(7, 152, 0));
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_buy:
                if(mlist!=null)
                     showDialog("1",this);
                else{
                    isFirstIn = false;
                    showDialog(this,R.string.m_getting);
                    new GetMyMatch(this).execute(1);
                }
                break;
            case R.id.btn_sale:
                if(mlist!=null)
                     showDialog("0",this);
                else{
                    isFirstIn = false;
                    showDialog(this,R.string.m_getting);
                    new GetMyMatch(this).execute(0);
                }

                break;
            case R.id.tv_right:
                if(getResources().getString(R.string.del).equals(tv_right.getText().toString())){//删除
                    new DefocusTask(this).execute(symbol);
                }else{//添加
                    new Focus2Task(this).execute(symbol);
                }
                break;
            case R.id.iv_right:
                refreshAnim();
                break;
        }
    }


    private void refreshAnim(){
        iv_refresh.setVisibility(View.VISIBLE);
        iv_right.setVisibility(View.GONE);
        RotateAnimation refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.rotating);
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
        iv_refresh.startAnimation(refreshingAnimation);


        refreshingAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                refresh_view.autoRefresh();
                new GetStockRealtimeInfoTask(StockDetailActivity.this).execute(DataLoadDirection.Refresh);
                showDialog(StockDetailActivity.this,R.string.refreshing);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                DialogDismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void showDialog(final String s,Context cxt){

        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        View view = View.inflate(cxt, R.layout.dialog_select_match, null);
        dialog.setContentView(view);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (UiCommon.widthPixels * 0.8);
        lp.height = (int)(UiCommon.higthPixels*0.65);
        win.setAttributes(lp);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        Log.e("tag",mlist.size()+"==============");
        adapter1.setList(mlist);
        listView.setAdapter(adapter1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MatchBean mm = (MatchBean) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("matchbean", mm);
                StockholdsBean sb = new StockholdsBean();
                sb.setSymbol(symbol);
                bundle.putSerializable("stockholdbean", sb);
                if("1".equals(s))
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_BUY, bundle);
                else
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_SALE, bundle);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.tvConfirm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    private boolean isFirstIn = true;

    private class GetStockRealtimeInfoTask extends LoadingDialog<Integer,Stock> {

        private int operation;

        public GetStockRealtimeInfoTask(Context activity) {
            super(activity,false,true);
        }

        @Override
        public Stock doInBackground(Integer... params) {
            operation = params[0];
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
               stock =  server.getStockRealtimeInfo(symbol);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return stock;
        }

        @Override
        public void onPostExecute(Stock ret) {
            super.onPostExecute(ret);
            myscrllview.onRefreshComplete();
            iv_refresh.clearAnimation();
            iv_refresh.setVisibility(View.GONE);
            iv_right.setVisibility(View.VISIBLE);
        }

        @Override
        public void doStuffWithResult(Stock result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    initData();
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(StockDetailActivity.this.getString(R.string.load_fail));
            }
        }
    }

    private void initData() {
        symbol = stock.getSymbol();
        tv_name.setText(stock.getName());
        tv_symbol.setText(symbol);
        tv_close_price.setText(df.format(stock.getClose()));
        tv_open_price.setText(df.format(stock.getOpen()));
        Log.e("tag", "stock.getTotal_amount():" + stock.getTotal_amount() + "-----------stock.getTotal_volumn():" + stock.getTotal_volumn());
//                    tv_total_amount.setText(df.format(stock.getTotal_amount()));
        tv_total_amount.setText((int)stock.getTotal_amount()/10000+"万");
        tv_total_volumn.setText((int)stock.getTotal_volumn()+"");

        tv_high_price.setText(df.format(stock.getHigh()));
        tv_low_price.setText(df.format(stock.getLow()));
//        tv_min_price.setText(df.format(stock.getCurrent()-stock.getClose()/stock.getClose()*100)+"%");

        float percent = (Float.parseFloat(stock.getCurrent()+"") - Float.parseFloat(stock.getClose()+"")) / Float.parseFloat(stock.getClose()+"") * 100;
        if (percent < 0) {
            str = String.format("%.2f%%", percent);
        } else if (percent >= 0) {
            str = "+" + String.format("%.2f%%", percent);
        } else {
            str = String.format("%.2f%%", percent);
        }
        tv_min_price.setText(str);



        tv_max_price.setText(df.format(stock.getCurrent()-stock.getClose()));
        if(stock.getCurrent()-stock.getClose()<0){
            ll_top.setBackgroundColor(Color.rgb(7, 152, 0));
            isup = false;
        }else{
            ll_top .setBackgroundColor(Color.rgb(255, 24, 0));
            isup = true;
        }

//                    adapter.setIsup(isup);
//                    list.clear();
//                    adapter.addList(stock.getList());

        list = stock.getList();
        ll_buy.removeAllViews();
        for (int index=0;index<5;index++){
            ll_buy.addView(getBuyView(index),index);
        }


        tv_current.setText(df.format(stock.getCurrent()));
        if(stock.isOptional_stock_user())
            tv_right.setText(getString(R.string.del));
        else
            tv_right.setText(getString(R.string.add));
    }

    private class DefocusTask extends LoadingDialog<String,General>{

        public DefocusTask(Context activity) {
            super(activity, false,true);
        }

        @Override
        public General doInBackground(String... params) {
            General temp = null;
            if(server == null){
                server = new Get2ApiImpl();
            }
            try {
                temp=  server.defocus(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return temp;
        }

        @Override
        public void doStuffWithResult(General result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    tv_right.setText(getString(R.string.add));
                    UiCommon.INSTANCE.showTip(getString(R.string.option_del));
                }else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else{
                UiCommon.INSTANCE.showTip(StockDetailActivity.this.getString(R.string.load_fail));
            }
        }
    }

    private class Focus2Task extends LoadingDialog<String,General>{

        public Focus2Task(Context activity) {
            super(activity, false,true);
        }

        @Override
        public General doInBackground(String... params) {
            General temp = null;
            if(server == null){
                server = new Get2ApiImpl();
            }
            try {
                temp=  server.focus2(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return temp;
        }

        @Override
        public void doStuffWithResult(General result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    tv_right.setText(getString(R.string.del));
                    UiCommon.INSTANCE.showTip(getString(R.string.option_add));
                }else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else{
                UiCommon.INSTANCE.showTip(StockDetailActivity.this.getString(R.string.load_fail));
            }
        }
    }


    private class GetMyMatch extends LoadingDialog<Integer, MatchBean> {
        private int type;
        public GetMyMatch(Context activity) {
            super(activity, false, true);
        }

        @Override
        public MatchBean doInBackground(Integer... params) {
            type = params[0];
            MatchBean ret = null;
            if(server == null){
                server = new Get2ApiImpl();
            }
            try {
                ret = server.getRunMatches();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public void doStuffWithResult(MatchBean result) {
            if (result != null && result.code != -1) {
                DialogDismiss();
                if (result.getStatus()==1) {
                    if(result.getList().size()>0){
                        mlist = result.getList();
                        if(isFirstIn){

                        }else{
                            isFirstIn  =true;
                            showDialog(type+"",StockDetailActivity.this);
                        }

                    }
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(StockDetailActivity.this.getString(R.string.load_fail));
            }
        }
    }

}
