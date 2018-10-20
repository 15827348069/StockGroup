package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.PayInfo;
import com.zbmf.StocksMatch.beans.StockholdsBean;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;
import org.w3c.dom.Text;

public class PayTipActivity extends ExActivity {

    private TextView tv_num,tv_tip,tv_pay,tv_title;
    private String match_id;
    private StockholdsBean stockholdsBean;
    private Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        match_id = (String) getIntent().getSerializableExtra("match_id");
        stockholdsBean = (StockholdsBean) getIntent().getSerializableExtra("sb");
        group = (Group) getIntent().getSerializableExtra("group");

        tv_num = (TextView)findViewById(R.id.tv_num);
        tv_tip = (TextView)findViewById(R.id.tv_tip);
        tv_pay = (TextView)findViewById(R.id.tv_pay);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(R.string.seesee);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btn_see).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PayStock(PayTipActivity.this,R.string.loading,R.string.load_fail,true).execute();
            }
        });

        tv_num.setText(getString(R.string.pay_tip1,UiCommon.INSTANCE.getiUser().getMpay()));
        tv_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.shengmi));
                bundle.putInt("soure_act", 3);
                bundle.putString("web_url", "file:///android_asset/disclaimer.html");
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ACCOUNT_Web, bundle);
            }
        });
        new PayStockTem(this,R.string.loading,R.string.load_fail,true).execute();
    }
    private Get2Api server;
    private class PayStockTem extends LoadingDialog<Void,PayInfo>{

        public PayStockTem(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public PayInfo doInBackground(Void... params) {
            if(server == null){
                server = new Get2ApiImpl();
            }
            try {
                return server.PayStockTem();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(PayInfo result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    if(!result.getMpay().equals(UiCommon.INSTANCE.getiUser().getMpay())){
                        tv_num.setText(getString(R.string.pay_tip1,result.getMpay()));
                        UiCommon.INSTANCE.getiUser().setMpay(result.getMpay());
                    }
                    double a = 10 * (Double.valueOf(result.getDiscount()));
                    String html = "<font size=\"40px\" color=\"#000000\">查看需支付10个魔方宝（会员价：</font><font size=\"40px\" color=\"#ff0000\">"+"&nbsp;&nbsp;&nbsp;"+a+"</font><font size=\"40px\" color=\"#000000\">个魔方宝）</font>";;
                    Spanned text = Html.fromHtml(html);
                    tv_pay.setText(text);


                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }


    private class PayStock extends LoadingDialog<String,General>{


        public PayStock(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public General doInBackground(String... params) {

            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                return server.PayStock(group.getId(), match_id,stockholdsBean.getSymbol());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(General result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    UiCommon.INSTANCE.showTip("魔方宝支付成功");
                    Intent intent = new Intent();
                    intent.setAction(Constants.PAY_SUCCESS);
                    intent.putExtra("symbol",stockholdsBean.getSymbol());
                    sendBroadcast(intent);
                    finish();
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }
}
