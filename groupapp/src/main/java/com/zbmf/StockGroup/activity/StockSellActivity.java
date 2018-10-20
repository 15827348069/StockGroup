package com.zbmf.StockGroup.activity;

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
import com.zbmf.StockGroup.beans.Stock;
import com.zbmf.StockGroup.beans.Stock1;
import com.zbmf.StockGroup.beans.StockholdsBean;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.view.ListViewForScrollView;

import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StockSellActivity extends BaseActivity implements View.OnClickListener{
    private AutoCompleteTextView search_edittext;
    private StockAdapter adapter;
    private TextView tv_getprice_low;
    private TextView tv_getprice_hight, can_buy_number_text;
    private EditText buy_number, buy_price;
    private double can_buy_number;
    DecimalFormat dfzero = new DecimalFormat();
    private StockholdsBean sb;
    private String buy_price_msg, buy_number_msg;
    DecimalFormat df = new DecimalFormat("#0.00");
    private ListViewForScrollView gu_piao_list;
    private BuyDetailAdapter buyadapter;
    private boolean isFirst=true;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_stock_sale;
    }

    @Override
    public void initView() {
        dfzero.setMaximumFractionDigits(0);
        dfzero.setGroupingSize(0);
        dfzero.setRoundingMode(RoundingMode.FLOOR);
        initTitle("卖出");
        search_edittext =getView(R.id.buy_edittext);
        gu_piao_list =getView(R.id.gu_piao_list);
        tv_getprice_low =  getView(R.id.tv_getprice_low);
        tv_getprice_hight =  getView(R.id.tv_getprice_hight);
        can_buy_number_text = getView(R.id.can_buy_number);
        buy_number = getView(R.id.buy_number);
        buy_price =  getView(R.id.buy_price);
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
        getView(R.id.sale_code_id).setOnClickListener(this);
        getView(R.id.ll_hight).setOnClickListener(this);
        getView(R.id.ll_low).setOnClickListener(this);
        gu_piao_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stock1 stock1 = list.get(position);
                if(stock1.getPrice().contains("--"))
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
                if(isFirst&&timer!=null){
                    timer.cancel();
                    timer=null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        search_edittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isFirst=true;
                update(adapter.getSymbolAtPosition(position));
            }
        });

        buy_price.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() != 0) {
                    double price = Double.valueOf(s.toString());
                    if(price<0)
                        showToast("请输入正确的价格");
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

        buy_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    double number=Double.valueOf(s.toString());
                    if(number>can_buy_number){
                        showToast("卖出数量不能超过可卖数量");
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
    }
    @Override
    public void initData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            sb = (StockholdsBean)bundle.getSerializable(IntentKey.STOCKHOLDER);
        }
        if(sb!=null){
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
            case R.id.sale_code_id:
                if (buy_code == null || buy_code.equals("")) {
                    showToast("股票代码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(buy_price.getText())) {
                    showToast("卖出价格不能为空");
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
                    showToast("卖出数量不能为空");
                    return;
                }
                if (Double.valueOf(buy_number.getText().toString()) % 100 != 0) {
                    showToast("卖出数量请输入100的倍数");
                    return;
                }
                buy_price_msg = buy_price.getText().toString();
                buy_number_msg = buy_number.getText().toString();
                if (Double.valueOf(buy_number_msg) > can_buy_number) {
                    showToast("卖出数量不能高于可卖数量");
                    return;
                }
                //提交委托
                sellMatchStock(buy_code,buy_price_msg,buy_number_msg);
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

    private void update(final String code) {
        if(timer==null){
            timer = new Timer();
        }
        TimerTask   task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getSymbol(code);
                    }
                });
            }
        };
        timer.schedule(task,0,5000);
    }
    private void getSymbol(String symbol){
        WebBase.getRealtimeInfo(symbol, new JSONHandler() {
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
     * 初始股票详情
     * @param result
     */
    private void initDetail(Stock result) {
        buy_code = result.getSymbol();
        search_edittext.setText(result.getName() + "(" + result.getSymbol() + ")");
        search_edittext.setSelection(search_edittext.getText().length());
        tv_getprice_hight.setTextColor(Color.rgb(255, 24, 0));
        tv_getprice_low.setTextColor(Color.rgb(7, 152, 0));

        can_buy_number = result.getVolumnunfrozen();
        if(can_buy_number % 100!=0){
            can_buy_number = Double.valueOf(dfzero.format(can_buy_number/100))*100;
        }
        buy_price.setText(df.format(result.getCurrent()));
        buy_price.setSelection(buy_price.getText().length());
        can_buy_number_text.setText("可卖" + dfzero.format(can_buy_number));
        tv_getprice_hight.setText(df.format(result.getClose() * 1.1));
        tv_getprice_low.setText(df.format(result.getClose() * 0.9));
        buyadapter.notifyDataSetChanged();
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
                stock1.setType(StockSellActivity.this.getString(R.string.sale) + nums[4 - i]);
                stock1.setPrice(df.format(s.getSell()));
                stock1.setNumber(s.getVolumn_sell());
                stock1.setIsup(stock.getCurrent() - stock.getClose() > 0 ? true : false);
            } else {
                Stock s = stock.getList().get(i - 5);
                stock1.setType(StockSellActivity.this.getString(R.string.buy) + nums[i - 5]);
                stock1.setPrice(df.format(s.getBuy()));
                stock1.setNumber(s.getVolumn_buy());
                stock1.setIsup(stock.getCurrent() - stock.getClose() > 0 ? true : false);
            }
            list.add(stock1);
        }
        if(isFirst){
            initDetail(stock);
            isFirst=false;
        }

    }


    private String[] nums = {"一", "二", "三", "四", "五"};
    private boolean isup = false;
    private List<Stock1> list = new ArrayList<Stock1>();
    private String buy_code;

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
    private void sellMatchStock(String symbol,String price,String volumn){
        WebBase.sellMatchStock(symbol,price,volumn,new JSONHandler() {
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
