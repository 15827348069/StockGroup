package com.zbmf.groupro.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.MfbsPriceAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.FansPrice;
import com.zbmf.groupro.utils.ActivityUtil;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.view.GridViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PayDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private MfbsPriceAdapter adapter;
    private GridViewForScrollView mfbs_price_gridview;
    private List<FansPrice> infolist;
    private Dialog edit_dialog;
    private String input_mfb_number;
    private CheckBox wx_check_box;
    private TextView pay_tv_mfb,group_title_name,need_pay_rmb_number;
    private LinearLayout sure_add_fans_button;
    private double mfb_price;
    private String pay_id,pro_num;
    private IWXAPI wx_api;
    DecimalFormat df=new DecimalFormat("");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_detail);
        ActivityUtil.getInstance().putActivity(ActivityUtil.PayDetailActivity,this);
        init();
        getPriceMessage();
    }

    private void getPriceMessage() {
        WebBase.products(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONArray products=obj.optJSONArray("products");
                int size=products.length();
                for(int i=size;i>=0;i--){
                    JSONObject product=products.optJSONObject(i-1);
                    FansPrice fp=new FansPrice();
                    if(i!=0){
                        fp.setTitle(product.optString("name"));
                        fp.setPrice(product.optDouble("amount"));
                        fp.setId(product.optString("id"));
                        if(i==size){
                            fp.setIs_checked(true);
                            mfb_price=product.optDouble("amount");
                            setNeedPay(fp);
                        }else{
                            fp.setIs_checked(false);
                        }
                    }else{
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
    public void setNeedPay(FansPrice fp){
        pay_tv_mfb.setText(df.format(fp.getPrice()));
        need_pay_rmb_number.setText(df.format(fp.getPrice()));
        pay_id=fp.getId();
        pro_num=df.format(fp.getPrice());
    }
    private void init(){
        infolist=new ArrayList<>();
        adapter=new MfbsPriceAdapter(this,infolist);
        mfbs_price_gridview= (GridViewForScrollView) findViewById(R.id.mfbs_price_gridview);
        wx_check_box= (CheckBox) findViewById(R.id.wx_check_box);
        pay_tv_mfb= (TextView) findViewById(R.id.pay_tv_mfb);
        group_title_name= (TextView) findViewById(R.id.group_title_name);
        findViewById(R.id.group_title_return).setOnClickListener(this);
        findViewById(R.id.wx_pay_layout).setOnClickListener(this);
        sure_add_fans_button= (LinearLayout) findViewById(R.id.sure_add_fans_button);
        sure_add_fans_button.setOnClickListener(this);
        need_pay_rmb_number= (TextView) findViewById(R.id.need_pay_rmb_number);
        group_title_name.setText("充值");
        mfbs_price_gridview.setAdapter(adapter);
        if(edit_dialog==null){
            edit_dialog=Editdialog1();
        }
        edit_dialog.show();
        edit_dialog.dismiss();
        mfbs_price_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSelect(i);
                if(i==infolist.size()-1){
                    if(edit_dialog==null){
                        edit_dialog=Editdialog1();
                    }
                    edit_dialog.show();
                }else{
                    setNeedPay(infolist.get(i));
                }
            }
        });
    }
    private Dialog Editdialog1(){
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        final View layout = LayoutInflater.from(this).inflate(R.layout.eidt_layout, null);
        final EditText editText=(EditText) layout.findViewById(R.id.my_detail_nickname_edit);
        editText.setHint("");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int acition_id, KeyEvent keyEvent) {
                if(acition_id== EditorInfo.IME_ACTION_DONE){
                    edit_dialog.dismiss();
                    if(!TextUtils.isEmpty(editText.getText())){
                        input_mfb_number=editText.getText().toString();
                        FansPrice fp=new FansPrice();
                        fp.setPrice(Double.valueOf(input_mfb_number));
                        fp.setTitle(input_mfb_number);
                        fp.setId("7");
                        setNeedPay(fp);
                    }
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
        return  dialog;
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
        switch (view.getId()){
            case R.id.wx_pay_layout:
                wx_check_box.setChecked(!wx_check_box.isChecked());
                if(!wx_check_box.isChecked()){
                    sure_add_fans_button.setEnabled(false);
                    sure_add_fans_button.setAlpha(0.5f);
                }else{
                    sure_add_fans_button.setEnabled(true);
                    sure_add_fans_button.setAlpha(1.0f);
                }
                break;
            case R.id.group_title_return:
                finish();
                break;
            case R.id.sure_add_fans_button:
                wx_pay();
                break;
        }
    }
    public void wx_pay(){
        WebBase.wx_pay(pay_id, pro_num, new JSONHandler(true,PayDetailActivity.this,"正在获取订单...") {
            @Override
            public void onSuccess(JSONObject obj) {
                WxPay(obj.optJSONObject("order"));
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
    /***
     * 微信支付
     * @param
     */
    private void WxPay(JSONObject obj) {
        // TODO Auto-generated method stub
        if (wx_api == null) {
            wx_api = WXAPIFactory.createWXAPI(PayDetailActivity.this, Constants.WEI_APK_KEY);
        }
        if (!wx_api.isWXAppInstalled()) {
            //提醒用户没有按照微信
            Toast.makeText(getApplicationContext(), "没有安装微信客户端",Toast.LENGTH_SHORT).show();
            return;
        }
        PayReq request = new PayReq();
        request.appId = obj.optString("appid");
        request.partnerId = obj.optString("partnerid");
        request.prepayId= obj.optString("prepayid");
        request.packageValue = obj.optString("package");
        request.nonceStr=obj.optString("noncestr");
        request.timeStamp= obj.optString("timestamp");
        request.sign=obj.optString("sign");
        wx_api.sendReq(request);
    }
}
