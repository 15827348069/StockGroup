package com.zbmf.StockGroup.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.TextUtils;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.MfbsPriceAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.FansPrice;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.view.GridViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PayDetailActivity extends BaseActivity implements View.OnClickListener {
    private MfbsPriceAdapter adapter;
    private GridViewForScrollView mfbs_price_gridview;
    private List<FansPrice> infolist;
    private Dialog edit_dialog;
    private String input_mfb_number;
    private CheckBox wx_check_box;
    private TextView pay_tv_mfb, need_pay_rmb_number;
    private LinearLayout sure_add_fans_button;
    private double mfb_price;
    private int pay_id;
    private String pro_num;
    private IWXAPI wx_api;
    DecimalFormat df = new DecimalFormat("");
    private int mMoney;

    private void getPriceMessage() {
        WebBase.products(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONArray products = obj.optJSONArray("products");
                int size = products.length();
                int get_size = 0;
                if (size > 8) {
                    get_size = size - 8;
                }
                for (int i = size; i >= get_size; i--) {
                    JSONObject product = products.optJSONObject(i - 1);
                    FansPrice fp = new FansPrice();
                    if (i != get_size) {
                        fp.setTitle(product.optString("name"));
                        fp.setPrice(product.optDouble("amount"));
                        fp.setId(product.optInt("id"));
                        if (i == size) {
                            fp.setIs_checked(true);
                            mfb_price = product.optDouble("amount");
                            setNeedPay(fp);
                        } else {
                            fp.setIs_checked(false);
                        }
                    } else {
                        fp.setTitle("其他金额");
                    }
                    infolist.add(fp);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    public void setNeedPay(FansPrice fp) {
        pay_tv_mfb.setText(df.format(fp.getPrice()));
        need_pay_rmb_number.setText(df.format(fp.getPrice()));
        pay_id = fp.getId();
        pro_num = DoubleFromat.getDouble(fp.getPrice(), 0);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_pay_detail;
    }

    @Override
    public void initView() {
        initTitle("充值");
        mMoney = getIntent().getIntExtra(IntentKey.MONEY, 0);
        if (mMoney != 0) {
            //充值
            pro_num = String.valueOf(mMoney);
            wx_pay();
        }
        mfbs_price_gridview = (GridViewForScrollView) findViewById(R.id.mfbs_price_gridview);
        wx_check_box = (CheckBox) findViewById(R.id.wx_check_box);
        pay_tv_mfb = (TextView) findViewById(R.id.pay_tv_mfb);
        sure_add_fans_button = (LinearLayout) findViewById(R.id.sure_add_fans_button);
        need_pay_rmb_number = (TextView) findViewById(R.id.need_pay_rmb_number);
        if (edit_dialog == null) {
            edit_dialog = Editdialog1();
        }
        edit_dialog.show();
        edit_dialog.dismiss();
    }

    @Override
    public void initData() {
        infolist = new ArrayList<>();
        adapter = new MfbsPriceAdapter(this, infolist);
        mfbs_price_gridview.setAdapter(adapter);
        getPriceMessage();
    }

    @Override
    public void addListener() {
        findViewById(R.id.wx_pay_layout).setOnClickListener(this);
        sure_add_fans_button.setOnClickListener(this);
        mfbs_price_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSelect(i);
                if (i == infolist.size() - 1) {
                    if (edit_dialog == null) {
                        edit_dialog = Editdialog1();
                    }
                    edit_dialog.show();
                } else {
                    setNeedPay(infolist.get(i));
                }
            }
        });
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
                    edit_dialog.dismiss();
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
                    input_mfb_number = editText.getText().toString();
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
        edit_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
                wx_pay();
                break;
        }
    }

    public void wx_pay() {
        WebBase.wx_pay(String.valueOf(pay_id), pro_num, new JSONHandler(true, PayDetailActivity.this, "正在获取订单...") {
            @Override
            public void onSuccess(JSONObject obj) {
                WxPay(obj.optJSONObject("order"));
            }

            @Override
            public void onFailure(String err_msg) {
                LogUtil.e(err_msg);
            }
        });
    }

    /***
     * 微信支付
     * @param
     */
    private void WxPay(JSONObject obj) {
        if (wx_api == null) {
            wx_api = WXAPIFactory.createWXAPI(PayDetailActivity.this, Constants.WEI_APK_KEY);
        }
        if (!wx_api.isWXAppInstalled()) {
            //提醒用户没有按照微信
            Toast.makeText(getApplicationContext(), "没有安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        LogUtil.json(obj.toString());
        PayReq request = new PayReq();
        request.appId = obj.optString("appid");
        request.partnerId = obj.optString("partnerid");
        request.prepayId = obj.optString("prepayid");
        request.packageValue = obj.optString("package");
        request.nonceStr = obj.optString("noncestr");
        request.timeStamp = obj.optString("timestamp");
        request.sign = obj.optString("sign");
        wx_api.sendReq(request);
    }

    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if(getIntent().getIntExtra(IntentKey.FLAG,0)==1){
//            startActivity(new Intent(this,GroupActivity.class));
//        }
//    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
