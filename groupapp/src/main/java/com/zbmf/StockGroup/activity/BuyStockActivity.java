package com.zbmf.StockGroup.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ScreenPriceAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.callback.ResultCallback;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.dialog.TextDialog;
import com.zbmf.StockGroup.interfaces.DialogNoClick;
import com.zbmf.StockGroup.interfaces.DialogYesClick;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;

import org.json.JSONObject;

/**
 * Created by xuhao on 2017/9/27
 * 购买股票产品
 */

public class BuyStockActivity extends BaseActivity implements View.OnClickListener {
    private TextView name, date, tv_pay_price,tv_commit;
    private ListViewForScrollView listview;
    private ScreenPriceAdapter adapter;
    private ResultCallback<Screen.Prce> callback;
    private String screen_id;
    private Screen screen;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_buy_stock;
    }

    @Override
    public void initView() {
        initTitle("服务订购");
        name = getView(R.id.tv_name);
        date = getView(R.id.tv_date);
        tv_commit=getView(R.id.tv_commit);
        listview = getView(R.id.stock_price_id);
        tv_pay_price = getView(R.id.tv_pay_price);
    }

    @Override
    public void initData() {
        getWolle();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            screen = (Screen) bundle.getSerializable(IntentKey.SCREEN);
            screen_id = screen.getScreen_id();
            name.setText(screen.getName());
            date.setText("至" + DateUtil.getTime(screen.getEnd_at() * 1000, Constants.YYYY年MM月dd日));
            adapter = new ScreenPriceAdapter(this, screen.getPrceList());
            listview.setAdapter(adapter);
            callback = new ResultCallback<Screen.Prce>() {
                @Override
                public void onSuccess(Screen.Prce price) {
                    tv_pay_price.setTag(price);
                    tv_pay_price.setText(DoubleFromat.getDouble(price.getPrice(),0));
                    double my_pays=Double.valueOf(SettingDefaultsManager.getInstance().getPays());
                    if(price.getPrice()>my_pays){
                        tv_commit.setTag(false);
                        tv_commit.setText("去充值");
                    }else{
                        tv_commit.setTag(true);
                        tv_commit.setText("立即支付");
                    }
                }

                @Override
                public void onError(String message) {

                }
            };
            adapter.setCheck(0, callback);
        }
    }

    @Override
    public void addListener() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setCheck(position, callback);
            }
        });
        tv_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit:
                if(ShowActivity.isLogin(this)){
                    boolean pay= (boolean) tv_commit.getTag();
                    if(!pay){
                        ShowActivity.showPayDetailActivity(this);
                    }else{
                        final Screen.Prce prce = (Screen.Prce) tv_pay_price.getTag();
                        long time=DateUtil.getScreenTimes(prce.getDays());
                        LogUtil.e("time"+time);
                        LogUtil.e("screen"+screen.getEnd_at());
                        if(time>screen.getEnd_at()*1000){
                            TextDialog.createDialog(BuyStockActivity.this)
                                    .setTitle("系统提示")
                                    .setMessage(getString(R.string.screen_date))
                                    .setLeftButton("取消")
                                    .setRightButton("继续")
                                    .setRightClick(new DialogYesClick() {
                                        @Override
                                        public void onYseClick() {
                                            commit(prce);
                                        }
                                    }).show();
                        }else{
                            commit(prce);
                        }
                    }
                }
                break;
        }
    }
    private void commit(final Screen.Prce prce){
        String str="";
        switch (prce.getDays()/31){
            case 1:
                str="1个月";
                break;
            case 3:
                str="1季度";
                break;
            case 12:
                str="1年";
                break;
        }
        TextDialog.createDialog(BuyStockActivity.this)
                .setMessage("您将花费"+DoubleFromat.getDouble(prce.getPrice(),0)+"魔方宝订购《"+screen.getName()+"》服务"+str)
                .setLeftButton("稍后支付")
                .setRightButton("立即支付")
                .setRightClick(new DialogYesClick() {
                    @Override
                    public void onYseClick() {
                        WebBase.PayScreen(screen_id,prce.getPrice_id(), new JSONHandler(true,BuyStockActivity.this,getString(R.string.commit_fans)) {
                            @Override
                            public void onSuccess(JSONObject obj) {
                                showToast(getString(R.string.commit_success));
                                setResult(RESULT_OK);
                                getWolle();
                                finish();
                            }
                            @Override
                            public void onFailure(String err_msg) {
                                showToast(err_msg);
                            }
                        });
                    }
                }).show();

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        getWolle();
    }

    public void getWolle() {
        WebBase.getWalle(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject pays = obj.optJSONObject("pay");
                JSONObject point = obj.optJSONObject("point");
                SettingDefaultsManager.getInstance().setPays(pays.optString("unfrozen"));
                SettingDefaultsManager.getInstance().setPoint(point.optLong("unfrozen"));
            }
            @Override
            public void onFailure(String err_msg) {
            }
        });
    }
}
