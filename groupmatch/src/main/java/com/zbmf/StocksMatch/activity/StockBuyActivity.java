package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.BuyDetailAdapter;
import com.zbmf.StocksMatch.adapter.BuySGuPiaoCodeAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.beans.Stock1;
import com.zbmf.StocksMatch.beans.StockholdsBean;
import com.zbmf.StocksMatch.listener.MyTextWatcher;
import com.zbmf.StocksMatch.utils.AssetsDatabaseManager;
import com.zbmf.StocksMatch.utils.GetTime;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.CustomListView;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class StockBuyActivity extends ExActivity implements View.OnClickListener {
    private TextView tv_title,tv_getprice_low,tv_getprice_hight, can_buy_number_text;
    private MatchBean mb;
    private AutoCompleteTextView search_edittext;
    private BuySGuPiaoCodeAdapter adapter;
    private EditText buy_number, buy_price;
    private double can_buy_number;
    DecimalFormat dfzero = new DecimalFormat();
    private StockholdsBean sb;
    private Stock stock;
    private String buy_price_msg, buy_number_msg;
    DecimalFormat df = new DecimalFormat("#0.00");
    private CustomListView gu_piao_list;
    private BuyDetailAdapter buyadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_buy);

        dfzero.setMaximumFractionDigits(0);
        dfzero.setGroupingSize(0);
        dfzero.setRoundingMode(RoundingMode.FLOOR);
        getData();
        setupView();
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mb = (MatchBean) bundle.getSerializable("matchbean");
            sb = (StockholdsBean) bundle.getSerializable("stockholdbean");
            stock = (Stock)bundle.getSerializable("stock");
        }
    }

    private void setupView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(UiCommon.INSTANCE.subTitle(mb.getTitle()));
        findViewById(R.id.iv_back).setOnClickListener(this);
        search_edittext = (AutoCompleteTextView) findViewById(R.id.buy_edittext);
        gu_piao_list = (CustomListView) findViewById(R.id.gu_piao_list);
        findViewById(R.id.delete_price).setOnClickListener(this);
        findViewById(R.id.add_price).setOnClickListener(this);
        tv_getprice_low = (TextView) findViewById(R.id.tv_getprice_low);
        tv_getprice_hight = (TextView) findViewById(R.id.tv_getprice_hight);
        can_buy_number_text = (TextView) findViewById(R.id.can_buy_number);
        buy_number = (EditText) findViewById(R.id.buy_number);
        findViewById(R.id.all_button).setOnClickListener(this);
        findViewById(R.id.half_id).setOnClickListener(this);
        findViewById(R.id.three_id).setOnClickListener(this);
        findViewById(R.id.buy_code_id).setOnClickListener(this);
        buy_price = (EditText) findViewById(R.id.buy_price);
        findViewById(R.id.ll_hight).setOnClickListener(this);
        findViewById(R.id.ll_low).setOnClickListener(this);
        gu_piao_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stock1 stock1 = list.get(position);
                if (stock1.getPrice().contains("--"))
                    return;
                buy_price.setText(stock1.getPrice());
                buy_price.setSelection(buy_price.getText().length());
            }
        });
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!UiCommon.INSTANCE.isChineseChar(s.toString())) {
                    if (s.length() == 6) {//查询一次
                        update(s.toString());//定时更新
                    }
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        adapter = new BuySGuPiaoCodeAdapter(getApplicationContext());
        search_edittext.setAdapter(adapter);
        search_edittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                search_edittext.setSelection(search_edittext.getText().toString().length());
                update(adapter.getItem(position));
            }
        });

        buy_price.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() != 0) {
                    double price = Double.valueOf(s.toString());
                    if (price > 0) {
//                        can_buy_number = mb.getUnfrozen_money() / price;
                        can_buy_number = caculateMacBuyWithPrice((float)price);


                        Log.e(">>>>>", "可用资金" + mb.getUnfrozen_money() + "<>" + can_buy_number + "可买数量");
//                        if (can_buy_number % 100 != 0) {
//                            can_buy_number = Double.valueOf(dfzero.format(can_buy_number / 100)) * 100;
//                        }
                        can_buy_number_text.setText("可买:" + dfzero.format(can_buy_number));
                    } else {
                        UiCommon.INSTANCE.showTip("请输入正确的价格");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        buyadapter = new BuyDetailAdapter(this, list);
        gu_piao_list.setAdapter(buyadapter);

        initData();//未有具体股票初始空布局
        if(stock==null)
            initData();
        else
            initPart(stock);

        if (sb != null) {
            update(sb.getSymbol());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.delete_price:
                double ddd;
                if (TextUtils.isEmpty(buy_price.getText())) {
                    ddd = 0;
                } else {
                    ddd = Double.valueOf(buy_price.getText().toString());
                }
                if (ddd > 0) {
                    buy_price.setText(df.format((ddd - 0.01)));
                } else {
                    buy_price.setText(df.format((0.00)));
                }
                break;
            case R.id.add_price:
                double ddd1;
                if (TextUtils.isEmpty(buy_price.getText())) {
                    ddd1 = 0;
                } else {
                    ddd1 = Double.valueOf(buy_price.getText().toString());
                }
                buy_price.setText(df.format((ddd1 + 0.01)));
                break;
            case R.id.all_button:
                buy_number.setText(dfzero.format(can_buy_number));
                buy_number.setSelection(buy_number.getText().length());
                break;
            case R.id.half_id:
                double buy_number_two = can_buy_number / 2;
                if (buy_number_two % 100 != 0) {
                    buy_number_two = Double.valueOf(dfzero
                            .format(buy_number_two / 100)) * 100;
                }
                buy_number.setText(dfzero.format(buy_number_two));
                buy_number.setSelection(buy_number.getText().length());
                break;
            case R.id.three_id:
                double buy_number_three = can_buy_number / 3;
                if (buy_number_three % 100 != 0) {
                    buy_number_three = Double.valueOf(dfzero.format(buy_number_three / 100)) * 100;
                }
                buy_number.setText(dfzero.format(buy_number_three));
                buy_number.setSelection(buy_number.getText().length());
                break;
            case R.id.buy_code_id:

                if (buy_code == null || buy_code.equals("")) {
                    UiCommon.INSTANCE.showTip("股票代码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(buy_price.getText())) {
                    UiCommon.INSTANCE.showTip("购买价格不能为空");
                    return;
                }
                if (price_big_or_smail(
                        Double.valueOf(buy_price.getText().toString()),
                        tv_getprice_hight, tv_getprice_low) == 1) {
                    UiCommon.INSTANCE.showTip("价格不能高于今日涨停价");
                    return;
                }
                if (price_big_or_smail(
                        Double.valueOf(buy_price.getText().toString()),
                        tv_getprice_hight, tv_getprice_low) == -1) {
                    UiCommon.INSTANCE.showTip("价格不能低于今日跌停价");
                    return;
                }
                if (TextUtils.isEmpty(buy_number.getText())
                        || buy_number.getText().toString().equals("0")) {
                    UiCommon.INSTANCE.showTip("购买数量不能为空");
                    return;
                }
                if (Double.valueOf(buy_number.getText().toString()) % 100 != 0) {
                    UiCommon.INSTANCE.showTip("购买数量请输入100的倍数");
                    return;
                }
                buy_price_msg = buy_price.getText().toString();
                buy_number_msg = buy_number.getText().toString();
                if (Double.valueOf(buy_number_msg) > can_buy_number) {
                    UiCommon.INSTANCE.showTip("购买数量不能高于可买数量");
                    return;
                }

                //提交委托
                new BuyTask(this, R.string.trust_tip3, R.string.load_fail, true).execute();
                break;
            case R.id.ll_hight:
                if(tv_getprice_hight.getText().toString().contains("--")){
                    return;
                }
                buy_price.setText(tv_getprice_hight.getText());
                buy_price.setSelection(buy_price.getText().length());
                break;
            case R.id.ll_low:
                if(tv_getprice_hight.getText().toString().contains("--")){
                    return;
                }
                buy_price.setText(tv_getprice_low.getText());
                buy_price.setSelection(buy_price.getText().length());
                break;
        }
    }

    private Timer timer = null;

    private void update(final String symbol) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (GetTime.getData()) {
                    new GetStockRealtimeInfoTask(StockBuyActivity.this).execute(symbol);
                } else {
                    new GetStockRealtimeInfoTask(StockBuyActivity.this).execute(symbol);//最后一次更新
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                        return;
                    }
                }
            }
        }, 0, 100000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerCancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerCancel();
    }

    private void timerCancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private class GetStockRealtimeInfoTask extends LoadingDialog<String, Stock> {
        public GetStockRealtimeInfoTask(Context activity) {
            super(activity, false, true);
        }
        @Override
        public Stock doInBackground(String... params) {
            if (server == null) {
                server = new Get2ApiImpl();
            }
            Stock stock = null;
            try {
                stock = server.getStockRealtimeInfo(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return stock;
        }

        @Override
        public void doStuffWithResult(Stock result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
                    initPart(result);
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(StockBuyActivity.this.getString(R.string.load_fail));
            }
        }
    }

    /**
     * 初始列表数据
     * @param stock
     */
    private void initPart(Stock stock) {
        list.clear();
        for (int i = 0; i < 10; i++) {
            Stock1 stock1 = new Stock1();
            if (i < 5) {
                Stock s = stock.getList().get(4 - i);
                stock1.setType(StockBuyActivity.this.getString(R.string.sale) + nums[4 - i]);
                stock1.setPrice(df.format(s.getSell()));
                stock1.setNumber(s.getVolumn_sell());
                stock1.setIsup(stock.getCurrent() - stock.getClose() > 0 ? true : false);

            } else {
                Stock s = stock.getList().get(i - 5);
                stock1.setType(StockBuyActivity.this.getString(R.string.buy) + nums[i - 5]);
                stock1.setPrice(df.format(s.getBuy()));
                stock1.setNumber(s.getVolumn_buy());
                stock1.setIsup(stock.getCurrent() - stock.getClose() > 0 ? true : false);
            }
            list.add(stock1);
        }
        initDetail(stock);
    }

    /**
     * 初始股票详情
     * @param result
     */
    private void initDetail(Stock result) {
        buy_code = result.getSymbol();
        search_edittext.setText(result.getName() + "(" + result.getSymbol() + ")");
        search_edittext.setSelection(search_edittext.getText().length());
        tv_getprice_hight.setTextColor(Color.rgb(255, 24, 0));
        tv_getprice_low.setTextColor(Color.rgb(7, 152, 0));
        can_buy_number = caculateMacBuyWithPrice((float)result.getCurrent());
        buy_price.setText(df.format(result.getCurrent()));
        can_buy_number_text.setText(getString(R.string.can_buy_number) + dfzero.format(can_buy_number));
        tv_getprice_hight.setText(df.format(result.getClose() * 1.1));
        tv_getprice_low.setText(df.format(result.getClose() * 0.9));

        buyadapter.notifyDataSetChanged();//更新列表
    }

    /**
     * 计算可买数
     * @param currPrice
     * @return
     */
    private double caculateMacBuyWithPrice(float currPrice) {
        float volume = 0;
        float unfrozen = (float)mb.getUnfrozen_money();
        float turnover_per = 0.001f, commission_per = 0.001f;
        if (true) {
            volume = unfrozen / (currPrice + turnover_per + commission_per * currPrice);
            if (volume * currPrice < 1)
                volume = (unfrozen - 1) / (currPrice + commission_per * currPrice);
            if (volume * commission_per * currPrice < 5)
                volume = (unfrozen - 1 - 5) / currPrice;
            else if (volume * commission_per * currPrice < 5)
                volume = (unfrozen - 5) / (currPrice + turnover_per);
        } else {
            volume = unfrozen / (currPrice + commission_per * currPrice);
            if (commission_per * currPrice * volume < 5) {
                volume = (unfrozen - 5) / currPrice;
            }
        }

        int v = (int)volume % 100;

        return (int)volume-v;
    }

    private Get2Api server;

    private class BuyTask extends LoadingDialog<String, General> {

        public BuyTask(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public General doInBackground(String... params) {
            if (server == null) {
                server = new Get2ApiImpl();
            }

            //(String symbol, String price, String volumn, String match_id)

            try {
                return server.buy(buy_code, buy_price_msg, buy_number_msg, mb.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(General result) {
            if (result != null && result.code != -1) {
                if (result.getStatus() == 1) {
//                    initData();
                    finish();
                    UiCommon.INSTANCE.showTip(getString(R.string.trust_tip1));
                } else if (result.code == 1108) {
                    UiCommon.INSTANCE.showTip(getString(R.string.trust_tip2));
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

    private String[] nums = {"一", "二", "三", "四", "五"};
    private boolean isup = false;
    private List<Stock1> list = new ArrayList<Stock1>();
    private String buy_code;

    /**
     * 初始布局
     */
    private void initData() {
        buy_price.setText("");
        can_buy_number_text.setText("可买:" + "--");
        buy_number.setText("");
        search_edittext.setText("");
        tv_getprice_hight.setText("--");
        tv_getprice_low.setText("--");

        list.clear();
        for (int i = 0; i < 10; i++) {
            Stock1 stock1 = new Stock1();
            if (i < 5) {
                stock1.setType(StockBuyActivity.this.getString(R.string.sale) + nums[4 - i]);
                stock1.setPrice("--");
                stock1.setNumber("--");
                stock1.setIsup(true);

            } else {
                stock1.setType(StockBuyActivity.this.getString(R.string.buy) + nums[i - 5]);
                stock1.setPrice("--");
                stock1.setNumber("--");
                stock1.setIsup(true);
            }

            list.add(stock1);
        }
        buyadapter.notifyDataSetChanged();
        timerCancel();
    }

    private int price_big_or_smail(double price, TextView big, TextView smail) {
        int status = 0;
        double big_value = Double.valueOf(big.getText().toString());
        double smail_value = Double.valueOf(smail.getText().toString());
        if (price < smail_value) {
            status = -1;
        }
        if (price > big_value) {
            status = 1;
        }
        return status;
    }
}
