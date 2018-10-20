package com.zbmf.StockGroup.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.BuyDetailAdapter;
import com.zbmf.StockGroup.adapter.StockAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.DealSys;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.Stock;
import com.zbmf.StockGroup.beans.Stock1;
import com.zbmf.StockGroup.beans.StockMode;
import com.zbmf.StockGroup.beans.StockholdsBean;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.view.ListViewForScrollView;

import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class StockBuyActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_getprice_low, tv_getprice_hight, can_buy_number_text;
    private MatchInfo mb;
    private AutoCompleteTextView search_edittext;
    private StockAdapter adapter;
    private EditText buy_number, buy_price;
    private double can_buy_number;
    DecimalFormat dfzero = new DecimalFormat();
    private String buy_price_msg, buy_number_msg;
    DecimalFormat df = new DecimalFormat("#0.00");
    private ListViewForScrollView gu_piao_list;
    private BuyDetailAdapter buyadapter;
    private boolean isFirst = true;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_stock_buy;
    }

    @Override
    public void initView() {
        dfzero.setMaximumFractionDigits(0);
        dfzero.setGroupingSize(0);
        dfzero.setRoundingMode(RoundingMode.FLOOR);
        setupView();
    }

    @Override
    public void initData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            initBuyData();
            mb = (MatchInfo) bundle.getSerializable(IntentKey.MATCH_BAEN);
            if (bundle.getSerializable(IntentKey.STOCKHOLDER) instanceof DealSys) {
                DealSys dealSys = (DealSys) bundle.getSerializable(IntentKey.STOCKHOLDER);
                update(dealSys.getSumbol());
            } else if (bundle.getSerializable(IntentKey.STOCKHOLDER) instanceof StockholdsBean) {
                StockholdsBean sb = (StockholdsBean) bundle.getSerializable(IntentKey.STOCKHOLDER);
                update(sb.getSymbol());
            }else if(bundle.getSerializable(IntentKey.STOCKHOLDER)instanceof StockMode){
                StockMode sm= (StockMode) bundle.getSerializable(IntentKey.STOCKHOLDER);
                update(sm.getSymbol());
            }
        }

    }

    private void setupView() {
        initTitle("买入");
        search_edittext = getView(R.id.buy_edittext);
        gu_piao_list = getView(R.id.gu_piao_list);
        tv_getprice_low = getView(R.id.tv_getprice_low);
        tv_getprice_hight = getView(R.id.tv_getprice_hight);
        can_buy_number_text = getView(R.id.can_buy_number);
        buy_number = getView(R.id.buy_number);
        buy_price = getView(R.id.buy_price);
        adapter = new StockAdapter(getApplicationContext());
        search_edittext.setAdapter(adapter);
        buyadapter = new BuyDetailAdapter(this, list);
        gu_piao_list.setAdapter(buyadapter);

    }

    @Override
    public void addListener() {
        getView(R.id.delete_price).setOnClickListener(this);
        getView(R.id.add_price).setOnClickListener(this);
        getView(R.id.all_button).setOnClickListener(this);
        getView(R.id.half_id).setOnClickListener(this);
        getView(R.id.three_id).setOnClickListener(this);
        getView(R.id.buy_code_id).setOnClickListener(this);
        getView(R.id.ll_hight).setOnClickListener(this);
        getView(R.id.ll_low).setOnClickListener(this);

        buy_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() != 0) {
                    double price = Double.valueOf(s.toString());
                    if (price > 0) {
                        can_buy_number = caculateMacBuyWithPrice((float) price);
                        LogUtil.e("可用资金" + mb.getMoneyunfrozen() + "<>" + can_buy_number + "可买数量");
                        String format = dfzero.format(can_buy_number);
                        can_buy_number_text.setText("可买:" + format);
                    } else {
                        showToast("请输入正确的价格");
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
        search_edittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isFirst = true;
                update(adapter.getSymbolAtPosition(position));
            }
        });
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
                if (!isFirst && timer != null) {
                    timer.cancel();
                    timer=null;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                    showToast("股票代码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(buy_price.getText())) {
                    showToast("购买价格不能为空");
                    return;
                }
                if (price_big_or_smail(
                        Double.valueOf(buy_price.getText().toString()),
                        tv_getprice_hight, tv_getprice_low) == 1) {
                    showToast("价格不能高于今日涨停价");
                    return;
                }
                if (price_big_or_smail(
                        Double.valueOf(buy_price.getText().toString()),
                        tv_getprice_hight, tv_getprice_low) == -1) {
                    showToast("价格不能低于今日跌停价");
                    return;
                }
                if (TextUtils.isEmpty(buy_number.getText())
                        || buy_number.getText().toString().equals("0")) {
                    showToast("购买数量不能为空");
                    return;
                }
                if (Double.valueOf(buy_number.getText().toString()) % 100 != 0) {
                    showToast("购买数量请输入100的倍数");
                    return;
                }
                buy_price_msg = buy_price.getText().toString();
                buy_number_msg = buy_number.getText().toString();
                if (Double.valueOf(buy_number_msg) > can_buy_number) {
                    showToast("购买数量不能高于可买数量");
                    return;
                }
                //提交委托--买入
                buyMatchStock(buy_code, buy_price_msg, buy_number_msg);
                break;
            case R.id.ll_hight:
                if (tv_getprice_hight.getText().toString().contains("--")) {
                    return;
                }
                buy_price.setText(tv_getprice_hight.getText());
                buy_price.setSelection(buy_price.getText().length());
                break;
            case R.id.ll_low:
                if (tv_getprice_hight.getText().toString().contains("--")) {
                    return;
                }
                buy_price.setText(tv_getprice_low.getText());
                buy_price.setSelection(buy_price.getText().length());
                break;
        }
    }

    private Timer timer = null;

    private void update(final String symbol) {
        if (timer == null) {
            timer = new Timer();
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getSymbol(symbol);
                    }
                });
            }
        };
        timer.schedule(task, 0, 5000);
    }

    private void getSymbol(String symbol) {
        WebBase.getStockRealtimeInfo(symbol, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                initPart(JSONParse.getStockRealtimeInfo(obj));
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
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


    /**
     * 初始列表数据
     *
     * @param stock
     */
    private void initPart(Stock stock) {
        list.clear();
        for (int i = 0; i < 10; i++) {
            Stock1 stock1 = new Stock1();
            if (i < 5) {
                Stock s = stock.getList().get(4 - i);
                stock1.setType("卖" + nums[4 - i]);
                stock1.setPrice(df.format(s.getSell()));
                stock1.setNumber(s.getVolumn_sell());
                stock1.setIsup(stock.getCurrent() - stock.getClose() > 0 ? true : false);

            } else {
                Stock s = stock.getList().get(i - 5);
                stock1.setType("买" + nums[i - 5]);
                stock1.setPrice(df.format(s.getBuy()));
                stock1.setNumber(s.getVolumn_buy());
                stock1.setIsup(stock.getCurrent() - stock.getClose() > 0 ? true : false);
            }
            list.add(stock1);
        }
        buyadapter.notifyDataSetChanged();//更新列表
        if (isFirst) {
            initDetail(stock);
            isFirst = false;
        }

    }

    /**
     * 初始股票详情
     *
     * @param result
     */
    @SuppressLint("SetTextI18n")
    private void initDetail(Stock result) {
        search_edittext.setText(result.getName() + "(" + result.getSymbol() + ")");
        search_edittext.setSelection(search_edittext.getText().length());
        String format = df.format(result.getCurrent());
        buy_price.setText(format);
        buy_code = result.getSymbol();
        tv_getprice_hight.setTextColor(Color.rgb(255, 24, 0));
        tv_getprice_low.setTextColor(Color.rgb(7, 152, 0));
        tv_getprice_hight.setText(df.format(result.getClose() * 1.1));
        tv_getprice_low.setText(df.format(result.getClose() * 0.9));
        isFirst = false;
        can_buy_number = caculateMacBuyWithPrice((float) result.getCurrent());
        can_buy_number_text.setText(getString(R.string.can_buy_number) + dfzero.format(can_buy_number));
    }

    /**
     * 计算可买数
     *
     * @param currPrice
     * @return
     */
    private double caculateMacBuyWithPrice(float currPrice) {
        if (mb == null) {
            return 0;
        }
        double volume = 0;
        double unfrozen = mb.getUnfrozen();//500541
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

        int v = (int) volume % 100;

        return (int) volume - v;
    }

    private String[] nums = {"一", "二", "三", "四", "五"};
    private boolean isup = false;
    private List<Stock1> list = new ArrayList<Stock1>();
    private String buy_code;

    /**
     * 初始布局
     */
    private void initBuyData() {
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
                stock1.setType("卖" + nums[4 - i]);
                stock1.setPrice("--");
                stock1.setNumber("--");
                stock1.setIsup(true);

            } else {
                stock1.setType("买" + nums[i - 5]);
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

    private void buyMatchStock(String symbol, String price, String volumn) {
        WebBase.buyMatchStock(symbol, price, volumn, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                showToast("委托成功");
                finish();
            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
            }
        });
    }

}
