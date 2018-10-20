package com.zbmf.StocksMatch.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.BuyDetailAdapter;
import com.zbmf.StocksMatch.adapter.StockAdapter;
import com.zbmf.StocksMatch.bean.IStock;
import com.zbmf.StocksMatch.bean.MatchInfo;
import com.zbmf.StocksMatch.bean.Stock1;
import com.zbmf.StocksMatch.bean.StockInfo;
import com.zbmf.StocksMatch.bean.StockMode;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.RefreshStockRealInfo;
import com.zbmf.StocksMatch.presenter.BuyStockPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.StockSellEvent;
import com.zbmf.worklibrary.view.ListViewForScrollView;

import org.greenrobot.eventbus.EventBus;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StockBuyActivity extends BaseActivity<BuyStockPresenter> implements RefreshStockRealInfo, View.OnClickListener {
    @BindView(R.id.imb_title_return)
    ImageButton titleReturn;
    @BindView(R.id.buy_edittext)
    AutoCompleteTextView buyEdittext;
    @BindView(R.id.delete_price)
    TextView delete_price;
    @BindView(R.id.buy_price)
    EditText buyPrice;
    @BindView(R.id.add_price)
    TextView add_price;
    @BindView(R.id.buy_number)
    EditText buy_number;
    @BindView(R.id.can_buy_number)
    TextView canBuyNumber;
    @BindView(R.id.all_button)
    Button all_button;
    @BindView(R.id.half_id)
    Button half_id;
    @BindView(R.id.three_id)
    Button three_id;
    @BindView(R.id.buy_code_id)
    Button buy_code_id;
    @BindView(R.id.tv_getprice_hight)
    TextView tv_getprice_hight;
    @BindView(R.id.ll_hight)
    LinearLayout ll_hight;
    @BindView(R.id.tv_getprice_low)
    TextView tv_getprice_low;
    @BindView(R.id.ll_low)
    LinearLayout ll_low;
    @BindView(R.id.gu_piao_list)
    ListViewForScrollView gu_piao_list;
    @BindView(R.id.ll_sp_trade_tip)
    LinearLayout ll_sp_trade_tip;
    private MatchInfo mMatchInfo;
    private BuyStockPresenter mBuyStockPresenter;
    private DecimalFormat df = new DecimalFormat("#0.00");
    //    private DecimalFormat df1 = new DecimalFormat("#");
    private DecimalFormat dfzero = new DecimalFormat();
    private StockAdapter mAdapter;
    private BuyDetailAdapter mBuyAdapter;
    private double mCan_buy_number;
    private String mSymbol;
    private int mFlag;
    private String mMatchId;

    @Override
    protected int getLayout() {
        mFlag = getIntent().getExtras().getInt(IntentKey.FLAG);
        return mFlag == Constans.BUY_FLAG ? R.layout.activity_stock_buy : R.layout.activity_stock_sell;
    }

    @Override
    protected String initTitle() {
        return mFlag == Constans.BUY_FLAG ? getString(R.string.buy_enter) : getString(R.string.sell);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        mAdapter = new StockAdapter(getApplicationContext());
        buyEdittext.setAdapter(mAdapter);
        mBuyAdapter = new BuyDetailAdapter(this, list);
        gu_piao_list.setAdapter(mBuyAdapter);

        dfzero.setMaximumFractionDigits(0);
        dfzero.setGroupingSize(0);
        dfzero.setRoundingMode(RoundingMode.FLOOR);

        if (bundle != null) {
//            Bundle bundle1 = bundle;
            mFlag = bundle.getInt(IntentKey.FLAG);
            mCan_buy_number = bundle.getInt(IntentKey.VUF);
            if (mFlag == Constans.SELL_FLAG) {
                canBuyNumber.setText(getString(R.string.can_sell_number) + (int) mCan_buy_number);
            }
            mMatchId = bundle.getString(IntentKey.MATCH_ID);
            mMatchInfo = (MatchInfo) bundle.getSerializable(IntentKey.MATCH_INFO);
            if (bundle.getSerializable(IntentKey.STOCK_HOLDER) instanceof StockMode) {
                StockMode sm = (StockMode) bundle.getSerializable(IntentKey.STOCK_HOLDER);
                assert sm != null;
                mSymbol = sm.getSymbol();
                String stockNmae = sm.getStockNmae();
                if (!TextUtils.isEmpty(stockNmae) && !TextUtils.isEmpty(mSymbol) && mFlag == Constans.SELL_FLAG) {
                    buyEdittext.setText(stockNmae + "(" + mSymbol + ")");
                }
            }
        }
        buy_code_id.setText(mFlag == Constans.BUY_FLAG ? getString(R.string.buy_enter) : getString(R.string.sell));
        initBuyData();
        addClickListener();
    }

    @Override
    protected BuyStockPresenter initPresent() {
        mBuyStockPresenter = new BuyStockPresenter(mFlag);
        mBuyStockPresenter.setSymbol(mSymbol);
        buyPrice.addTextChangedListener(new TextWatcher() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() != 0) {
                    double price = Double.valueOf(s.toString());
                    if (price > 0) {
                        if (mFlag==Constans.BUY_FLAG){
                            mCan_buy_number = caculateMacBuyWithPrice((float) price);
                            Log.e("--TAG", "可用资金" + mMatchInfo.getResult().getUnfrozen() + "<>" + mCan_buy_number + "可买数量");
                            int can_buy_number = (int) mCan_buy_number;
                            canBuyNumber.setText(getString(R.string.can_buy_number) + can_buy_number);
                        }else if (mFlag==Constans.SELL_FLAG){
                            canBuyNumber.setText(getString(R.string.can_sell_number) + (int)mCan_buy_number);
                        }
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
        buyEdittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isFirst = true;
                mSymbol = mAdapter.getSymbolAtPosition(position);
                getSymbolStock();
                buyEdittext.setText(mSymbol);
            }
        });
        gu_piao_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stock1 stock1 = list.get(position);
                if (stock1.getPrice().contains("--"))
                    return;
                buyPrice.setText(stock1.getPrice());
                buyPrice.setSelection(buyPrice.getText().length());
            }
        });
        buyEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO: 2018/3/27 取消股票数据更新
//                if (!isFirst && timer != null) {
//                    timer.cancel();
//                    timer=null;
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        buy_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    double number = Double.valueOf(s.toString());
                    if (number > mCan_buy_number) {
                        if (mFlag==Constans.SELL_FLAG){
                            showToast("卖出数量不能超过可卖数量");
                        }
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
        return mBuyStockPresenter;
    }

    //该数据是实时更新的
    private void getSymbolStock() {
        mBuyStockPresenter.setSymbol(mSymbol);
        mBuyStockPresenter.getStockRealInfo(mSymbol);
    }

    private void addClickListener() {
        titleReturn.setOnClickListener(this);
        delete_price.setOnClickListener(this);
        add_price.setOnClickListener(this);
        all_button.setOnClickListener(this);
        half_id.setOnClickListener(this);
        three_id.setOnClickListener(this);
        buy_code_id.setOnClickListener(this);
        ll_hight.setOnClickListener(this);
        ll_low.setOnClickListener(this);
    }

    /**
     * 初始布局
     */
    @SuppressLint("SetTextI18n")
    private void initBuyData() {
        buyPrice.setText("");
        canBuyNumber.setText(mFlag == Constans.BUY_FLAG ? getString(R.string.can_buy_number) + "--" : getString(R.string.can_sell_number) + "--");
        buy_number.setText("");
        buyEdittext.setText("");
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
        mBuyAdapter.notifyDataSetChanged();
        // TODO: 2018/3/27 取消股票数据更新
    }

    private String[] nums = {"一", "二", "三", "四", "五"};
    private boolean isup = false;
    private List<Stock1> list = new ArrayList<Stock1>();
    private String buy_code;
    private boolean isFirst = true;

    /**
     * 初始股票详情
     */
    @SuppressLint("SetTextI18n")
    private void initDetail(StockInfo.Result result) {
        buyEdittext.setText(result.getName() + "(" + result.getSymbol() + ")");
        buyEdittext.setSelection(buyEdittext.getText().length());
        String format = df.format(result.getCurrent());
        buyPrice.setText(format);
        buy_code = String.valueOf(result.getSymbol());
        tv_getprice_hight.setTextColor(Color.rgb(255, 24, 0));
        tv_getprice_low.setTextColor(Color.rgb(7, 152, 0));
        tv_getprice_hight.setText(df.format(result.getClose() * 1.1));
        tv_getprice_low.setText(df.format(result.getClose() * 0.9));
        isFirst = false;
//        mCan_buy_number = mFlag == Constans.BUY_FLAG ? caculateMacBuyWithPrice((float) result.getCurrent()) : result.getVolumnunfrozen();
//        mCan_buy_number = caculateMacBuyWithPrice((float) result.getCurrent());
        if (mFlag == Constans.BUY_FLAG) {
            canBuyNumber.setText(getString(R.string.can_buy_number) +
                    (int) caculateMacBuyWithPrice((float) result.getCurrent()));
        }else if (mFlag==Constans.SELL_FLAG){
            canBuyNumber.setText(getString(R.string.can_sell_number) + (int)mCan_buy_number);
        }
    }

    /**
     * 计算可买数
     *
     * @param currPrice
     * @return
     */
    private double caculateMacBuyWithPrice(float currPrice) {
        if (mMatchInfo == null) {
            return 0;
        }
        float volume = 0;
        float unfrozen = (float) mMatchInfo.getResult().getUnfrozen();//1000000
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

    @Override
    public void refreshStockInfo(StockInfo.Result stock) {
        list.clear();
        for (int i = 0; i < 10; i++) {
            Stock1 stock1 = new Stock1();
            if (stock != null) {
//                StockInfo.Result stock4 = stock;
                if (i < 5) {
                    IStock stock2 = stock.getStock();
                    double iSell = getISell(i, stock2);
                    stock1.setType("卖" + nums[4 - i]);
                    stock1.setPrice(df.format(iSell));
                    double volumnSell = getVolumnSell(i, stock2);
                    stock1.setNumber(String.valueOf(volumnSell));
                    stock1.setIsup(stock.getCurrent() - stock.getClose() > 0);
                } else {
                    IStock stock3 = stock.getStock();
                    double iSell = getBuy(i, stock3);
                    stock1.setType("买" + nums[i - 5]);
                    stock1.setPrice(df.format(iSell));
                    double volumnBuy = getVolumnBuy(i, stock3);
                    stock1.setNumber(String.valueOf(volumnBuy));
                    stock1.setIsup(stock.getCurrent() - stock.getClose() > 0);
                }
                list.add(stock1);
            }
        }
        mBuyAdapter.notifyDataSetChanged();//更新列表
        if (isFirst) {
            initDetail(stock);
            isFirst = false;
        }
    }

    @Override
    public void refreshStockInfoErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    private double getVolumnSell(int i, IStock stock2) {
        double vlumnSell = 0.00;
        if (i == 0) {
            vlumnSell = stock2.getVolumn_sell5();
        } else if (i == 1) {
            vlumnSell = stock2.getVolumn_sell4();
        } else if (i == 2) {
            vlumnSell = stock2.getVolumn_sell3();
        } else if (i == 3) {
            vlumnSell = stock2.getVolumn_sell2();
        } else if (i == 4) {
            vlumnSell = stock2.getVolumn_sell1();
        }
        return vlumnSell;
    }

    private double getISell(int i, IStock stock2) {
        double mSell = 0.00;
        if (i == 0) {
            mSell = stock2.getSell5();
        } else if (i == 1) {
            mSell = stock2.getSell4();
        } else if (i == 2) {
            mSell = stock2.getSell3();
        } else if (i == 3) {
            mSell = stock2.getSell2();
        } else if (i == 4) {
            mSell = stock2.getSell1();
        }
        return mSell;
    }

    private double getVolumnBuy(int i, IStock stock) {
        double volumnBuy = 0.00;
        if (i == 5) {
            volumnBuy = stock.getVolumn_buy1();
        } else if (i == 6) {
            volumnBuy = stock.getVolumn_buy2();
        } else if (i == 7) {
            volumnBuy = stock.getVolumn_buy3();
        } else if (i == 8) {
            volumnBuy = stock.getVolumn_buy4();
        } else if (i == 9) {
            volumnBuy = stock.getVolumn_buy5();
        }
        return volumnBuy;
    }

    private double getBuy(int i, IStock stock2) {
        double mSell = 0.00;
        if (i == 5) {
            mSell = stock2.getBuy1();
        } else if (i == 6) {
            mSell = stock2.getBuy2();
        } else if (i == 7) {
            mSell = stock2.getBuy3();
        } else if (i == 8) {
            mSell = stock2.getBuy4();
        } else if (i == 9) {
            mSell = stock2.getBuy5();
        }
        return mSell;
    }

    @Override
    public void entrustStock(String msg) {
        Log.i("--TAG","---- 买入 委托 ----"+msg);
        showToast(msg);
        if (msg.equals(getString(R.string.entrust_success))) {
            finishStockBuyAct();
        }
    }

    @Override
    public void sellStockStatus(String msg) {
        Log.i("--TAG","---- 卖出 委托 ----"+msg);
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
            if (msg.equals(getString(R.string.entrust_success))) {
                finishStockBuyAct();
            }
        }
    }

    private void finishStockBuyAct() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new StockSellEvent(IntentKey.SELL_STOCK_MSG));
                StockBuyActivity.this.finish();
            }
        }, 100);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_title_return:
                finish();
                break;
            case R.id.delete_price:
                double ddd;
                if (TextUtils.isEmpty(buyPrice.getText())) {
                    ddd = 0;
                } else {
                    ddd = Double.valueOf(buyPrice.getText().toString());
                }
                if (ddd > 0) {
                    buyPrice.setText(df.format((ddd - 0.01)));
                } else {
                    buyPrice.setText(df.format((0.00)));
                }
                break;
            case R.id.add_price:
                double ddd1;
                if (TextUtils.isEmpty(buyPrice.getText())) {
                    ddd1 = 0;
                } else {
                    ddd1 = Double.valueOf(buyPrice.getText().toString());
                }
                buyPrice.setText(df.format((ddd1 + 0.01)));
                break;
            case R.id.all_button:
                buy_number.setText(String.valueOf((int) mCan_buy_number));
                buy_number.setSelection(buy_number.getText().length());
                break;
            case R.id.half_id:
                double buy_number_two = mCan_buy_number / 2;
                if (buy_number_two % 100 != 0) {
                    double d = buy_number_two / 100;
                    int i = (int) (d);
                    if (mFlag == Constans.BUY_FLAG) {
                        buy_number_two = (i + 1) * 100;
                    } else if (mFlag == Constans.SELL_FLAG) {
                        buy_number_two = (i - 1) * 100;
                    }
                }
                buy_number.setText(String.valueOf((int) buy_number_two));
                buy_number.setSelection(buy_number.getText().length());
                break;
            case R.id.three_id:
                double buy_number_three = mCan_buy_number / 3;
                if (buy_number_three % 100 != 0) {
                    double d = buy_number_three / 100;
                    int i = (int) (d);
                    if (mFlag == Constans.BUY_FLAG) {
                        buy_number_three = (i + 1) * 100;
                    } else if (mFlag == Constans.SELL_FLAG) {
                        buy_number_three = (i - 1) * 100;
                    }
                }
                buy_number.setText(String.valueOf((int) buy_number_three));
                buy_number.setSelection(buy_number.getText().length());
                break;
            case R.id.buy_code_id:
                if (buy_code == null || buy_code.equals("")) {
                    showToast("股票代码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(buyPrice.getText())) {
                    showToast("购买价格不能为空");
                    return;
                }
                if (price_big_or_smail(
                        Double.valueOf(buyPrice.getText().toString()),
                        tv_getprice_hight, tv_getprice_low) == 1) {
                    showToast("价格不能高于今日涨停价");
                    return;
                }
                if (price_big_or_smail(
                        Double.valueOf(buyPrice.getText().toString()),
                        tv_getprice_hight, tv_getprice_low) == -1) {
                    showToast("价格不能低于今日跌停价");
                    return;
                }
                if (TextUtils.isEmpty(buy_number.getText())
                        || buy_number.getText().toString().equals("0")) {
                    if (mFlag == Constans.BUY_FLAG) {
                        showToast("购买数量不能为空");
                    } else if (mFlag == Constans.SELL_FLAG) {
                        showToast("卖出数量不能为空");
                    }
                    return;
                }
                String s = buy_number.getText().toString();
                if (Double.valueOf(s) % 100 != 0) {
                    showToast("购买数量请输入100的倍数");
                    return;
                }
                String buy_price_msg = buyPrice.getText().toString();
                String buy_number_msg = buy_number.getText().toString();
                if (Double.valueOf(buy_number_msg) > mCan_buy_number) {
                    showToast("购买数量不能高于可买数量");
                    return;
                }
                if (mFlag == Constans.BUY_FLAG) {
                    Log.i("--TAG","-----  提交买入   ---");
                    //提交买入
                    mBuyStockPresenter.buyMatchStock(String.valueOf(mSymbol), buy_price_msg, buy_number_msg, mMatchId);
                } else if (mFlag == Constans.SELL_FLAG) {
                    Log.i("--TAG","-----  提交卖出   ---");
                    //提交卖出
                    mBuyStockPresenter.sellMatchStock(String.valueOf(mSymbol), buy_number_msg, buy_price_msg, mMatchId);
                }
                break;
            case R.id.ll_hight:
                if (tv_getprice_hight.getText().toString().contains("--")) {
                    return;
                }
                buyPrice.setText(tv_getprice_hight.getText());
                buyPrice.setSelection(buyPrice.getText().length());
                break;
            case R.id.ll_low:
                if (tv_getprice_hight.getText().toString().contains("--")) {
                    return;
                }
                buyPrice.setText(tv_getprice_low.getText());
                buyPrice.setSelection(buyPrice.getText().length());
                break;
        }
    }
}
