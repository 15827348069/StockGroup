package com.zbmf.StocksMatch.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.MfbsPriceAdapter;
import com.zbmf.StocksMatch.bean.FansPrice;
import com.zbmf.StocksMatch.bean.PayListBean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.listener.PayList;
import com.zbmf.StocksMatch.presenter.PayPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.view.GridViewForScrollView;
import com.zbmf.worklibrary.util.DoubleFromat;
import com.zbmf.worklibrary.util.Logx;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by pq
 * on 2018/4/8.
 */

public class PayDetailActivity extends BaseActivity<PayPresenter> implements PayList, View.OnClickListener {
    @BindView(R.id.mfbs_price_gridview)
    GridViewForScrollView mfbs_price_gridview;
    @BindView(R.id.icon_wx_id)
    ImageView icon_wx_id;
    @BindView(R.id.wx_check_box)
    CheckBox wx_check_box;
    @BindView(R.id.wx_pay_layout)
    RelativeLayout wx_pay_layout;
    @BindView(R.id.pay_tv_mfb)
    TextView pay_tv_mfb;
    @BindView(R.id.add_fans_button_text)
    TextView add_fans_button_text;
    @BindView(R.id.need_pay_rmb_number)
    TextView need_pay_rmb_number;
    @BindView(R.id.add_fans_pay_message)
    LinearLayout add_fans_pay_message;
    @BindView(R.id.sure_add_fans_button)
    LinearLayout sure_add_fans_button;
    @BindView(R.id.add_fans_bottom_layout)
    LinearLayout add_fans_bottom_layout;
    @BindView(R.id.activity_pay_detail)
    RelativeLayout activity_pay_detail;
    private PayPresenter mPayPresenter;
    private double mMfbPrice;
    private DecimalFormat df = new DecimalFormat("");
    private int mPay_id;
    private String mPro_num;
    private Dialog mEdit_dialog;
    private List<FansPrice> mPayList;
    private MfbsPriceAdapter mAdapter;
    private IWXAPI mWx_api;

    @Override
    protected int getLayout() {
        return R.layout.activity_pay_detail;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.pay);
    }

    @Override
    protected void initData(Bundle bundle) {
        MyActivityManager.getMyActivityManager().pushAct(this);
        mPayList = new ArrayList<>();
        mAdapter = new MfbsPriceAdapter(this);
        mfbs_price_gridview.setAdapter(mAdapter);

        wx_pay_layout.setOnClickListener(this);
        sure_add_fans_button.setOnClickListener(this);
        mfbs_price_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setSelect(mPayList, position);
                if (position == mPayList.size() - 1) {
                    if (mEdit_dialog == null) {
                        mEdit_dialog = Editdialog1();
                    }
                    mEdit_dialog.show();
                } else {
                    setNeedPay(mPayList.get(position));
                }
            }
        });
    }

    @Override
    protected PayPresenter initPresent() {
        mPayPresenter = new PayPresenter();
        return mPayPresenter;
    }

    @Override
    public void payList(PayListBean payList) {
        if (payList != null) {
            int excessSize = 0;
            List<PayListBean.Products> products = payList.getProducts();
            int size = products.size();
            if (size > 8) {
                excessSize = size - 8;
            }
            for (int i = size; i >= excessSize; i--) {
                PayListBean.Products product = products.get(i - 1);
                FansPrice fp = new FansPrice();
                if (i != excessSize) {
                    fp.setTitle(product.getName());
                    fp.setPrice(product.getAmount());
                    fp.setId(product.getId());
                    if (i == size) {
                        fp.setIs_checked(true);
                        mMfbPrice = product.getAmount();
                        setNeedPay(fp);
                    } else {
                        fp.setIs_checked(false);
                    }
                } else {
                    fp.setTitle("其他金额");
                }
                mPayList.add(fp);
            }
            if (mAdapter.getList() == null) {
                mAdapter.setList(mPayList);
            }
        }
    }

    @Override
    public void payListErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    @Override
    public void wxPayResult(Map<String, String> map) {
        DissLoading();
        if (map != null) {
            if (mWx_api == null) {
                mWx_api = WXAPIFactory.createWXAPI(PayDetailActivity.this, Constans.WEI_APK_KEY);
                mWx_api.registerApp(Constans.WEI_APK_KEY/*"wxd930ea5d5a258f4f"*/);//将该app注册到微信
            }
            if (!mWx_api.isWXAppInstalled()) {
                //提醒用户没有按照微信
                Toast.makeText(getApplicationContext(), "没有安装微信客户端", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i("--TAG","--------------------- appID: "+map.get("appid"));
            PayReq request = new PayReq();
            request.appId = map.get("appid");
            request.partnerId = map.get("partnerid");
            request.prepayId = map.get("prepayid");
            request.packageValue = map.get("package");
            request.nonceStr = map.get("noncestr");
            request.timeStamp = map.get("timestamp");
            request.sign = map.get("sign");
            mWx_api.sendReq(request);
        }
    }

    @Override
    public void wxPayErr(String msg) {
        DissLoading();
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    public void setNeedPay(FansPrice fp) {
        pay_tv_mfb.setText(df.format(fp.getPrice()));
        need_pay_rmb_number.setText(df.format(fp.getPrice()));
        mPay_id = fp.getId();
        mPro_num = DoubleFromat.getDouble(fp.getPrice(), 0);
        Log.i("--TAG","---------------  mPay_id "+mPay_id);
        Log.i("--TAG","---------------  mPro_num "+mPro_num);
    }

    private Dialog Editdialog1() {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        final View layout = LayoutInflater.from(this).inflate(R.layout.eidt_layout, null);
        final EditText editText = (EditText) layout.findViewById(R.id.my_detail_nickname_edit);
        editText.setHint("");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int acition_id, KeyEvent keyEvent) {
                if (acition_id == EditorInfo.IME_ACTION_DONE) {
                    mEdit_dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!TextUtils.isEmpty(editText.getText())) {
                    String input_mfb_number = editText.getText().toString();
                    FansPrice fp = new FansPrice();
                    fp.setPrice(Double.valueOf(input_mfb_number));
                    fp.setTitle(input_mfb_number);
                    fp.setId(7);
                    setNeedPay(fp);
                }
                editText.setFocusable(false);
                editText.setFocusableInTouchMode(false);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(layout.getWindowToken(), 0); //强制隐藏键盘
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                showSoftInputFromWindow(editText);
            }
        });
        return dialog;
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        mEdit_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wx_pay_layout:
                wx_check_box.setChecked(!wx_check_box.isChecked());
                if (!wx_check_box.isChecked()) {
                    sure_add_fans_button.setEnabled(false);
                    sure_add_fans_button.setAlpha(0.5f);
                } else {
                    sure_add_fans_button.setEnabled(true);
                    sure_add_fans_button.setAlpha(1.0f);
                }
                break;
            case R.id.sure_add_fans_button:
                mPayPresenter.wxPay(String.valueOf(mPay_id), mPro_num);

                break;
        }
    }

    /***
     * 微信支付
     * @param
     */
    private void WxPay(JSONObject obj) {
        if (mWx_api == null) {
            mWx_api = WXAPIFactory.createWXAPI(PayDetailActivity.this, Constans.WEI_APK_KEY);
        }
        if (!mWx_api.isWXAppInstalled()) {
            //提醒用户没有按照微信
            Toast.makeText(getApplicationContext(), "没有安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        Logx.e(obj.toString());
        PayReq request = new PayReq();
        request.appId = obj.optString("appid");
        request.partnerId = obj.optString("partnerid");
        request.prepayId = obj.optString("prepayid");
        request.packageValue = obj.optString("package");
        request.nonceStr = obj.optString("noncestr");
        request.timeStamp = obj.optString("timestamp");
        request.sign = obj.optString("sign");
        mWx_api.sendReq(request);
    }
}
