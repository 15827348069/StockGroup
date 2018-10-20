package com.zbmf.StockGroup.activity;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.Types;
import com.zbmf.StockGroup.beans.User;
import com.zbmf.StockGroup.db.DBManager;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VipActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mL0,mL1, mL2, mL3, mL4;
    private Button mBtn1, mBtn2, mBtn3, mBtn4;
    private Button mSubscribeVIPBtn;
    private TextView mTime;
    private MatchInfo mMatchInfo;
    //    private IWXAPI wx_api;
    private int mPrice;
    private TextView mBottomMFB;
    private Dialog mDialog;
    private Button mEnter0;
    private ArrayList<Types> typeList = new ArrayList<>();
    private int mOnReadMsgCount;
    private TextView mMark;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_vip;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.member));
        mL0 = getView(R.id.r0);
        mL1 = getView(R.id.r1);
        mL2 = getView(R.id.r2);
        mL3 = getView(R.id.r3);
        mL4 = getView(R.id.r4);
        mBtn1 = getView(R.id.enter1);
        mBtn2 = getView(R.id.enter2);
        mBtn3 = getView(R.id.enter3);
        mBtn4 = getView(R.id.enter4);
        mSubscribeVIPBtn = getView(R.id.subscribeVIPBtn);
        mTime = getView(R.id.time);
        mBottomMFB = getView(R.id.bottomMFB);

        mMark = getView(R.id.mark);
        mEnter0 = getView(R.id.enter0);
        int isVip = SettingDefaultsManager.getInstance().getIsVip();
        if (isVip == 1) {
            String vipAtEnd = SettingDefaultsManager.getInstance().getVipAtEnd();
            mTime.setText(String.format("有效期至:%s", vipAtEnd));
            mTime.setVisibility(View.VISIBLE);
            mSubscribeVIPBtn.setText(getString(R.string.xf));
        } else {
            mSubscribeVIPBtn.setText(getString(R.string.open_vip));
            mTime.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        getMatchInfo();
        getSubscribeVipPrice(0);//获取开通VIP的价格
        getTopicList();//获取话题列表
        getNoReadMsgCount();
    }

    @Override
    public void addListener() {
        mL0.setOnClickListener(this);
        mL1.setOnClickListener(this);
        mL2.setOnClickListener(this);
        mL3.setOnClickListener(this);
        mL4.setOnClickListener(this);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
        mEnter0.setOnClickListener(this);
        mSubscribeVIPBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.r1:
            case R.id.enter1:
                //跳转只能选股产品查看页
                ShowActivity.skipNewSmartStockActivity(this);
                break;
            case R.id.r2:
            case R.id.enter2:
                //跳转优惠券页面
                if (ShowActivity.isLogin(this)) {
                    ShowActivity.showConponsActivity(this);
                }
                break;
            case R.id.r3:
            case R.id.enter3:
                //跳转选股大赛页面
                ShowActivity.skipZbmfSelectStockActivity(this);
                break;
            case R.id.r4:
            case R.id.enter4:
                if (ShowActivity.isLogin(this)) {
                    if (mMatchInfo == null) {
                        getMatchInfo();
                    } else {
                        ShowActivity.skipMatchRankActivity(this, mMatchInfo);
                    }
                }
                break;
            case R.id.subscribeVIPBtn:
                //先判断用户是否登录，余额是否足够，不足的话直接跳转到充值页面并选中900元
                showDialog();
                break;
            case R.id.r0:
            case R.id.enter0:
                //跳转话题分类页面
                int isVip = SettingDefaultsManager.getInstance().getIsVip();
                if (ShowActivity.isLogin(this)) {
                    if (isVip == 1) {
                        ShowActivity.skipHtActivity(this, typeList, mOnReadMsgCount);
                    } else {
                        showDialog();
                    }
                }
                break;
        }
    }

    //开通VIP或者续费
    private void openOrxfVIP() {
        int isVip = SettingDefaultsManager.getInstance().getIsVip();
        float pays = Float.parseFloat(SettingDefaultsManager.getInstance().getPays());//用户账户余额
        if (isVip == 1) {
            if (pays < mPrice) {
                ShowActivity.showPayDetailActivity1(this, mPrice);
            } else {
                //续费
                xfVIP();
            }
        } else if (isVip == 0) {
            //开通会员
            if (pays < mPrice) {
                ShowActivity.showPayDetailActivity1(this, mPrice);
            } else {
                //如果用户账户余额足够的话，直接扣除
                subscribeVIP();
            }
        }
    }

    private void showDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(this, R.style.dialogTheme);
        }
        View dialogView = LayoutInflater.from(this).inflate(R.layout.open_vip_dialog_tip_cntent, null);
        //获得dialog的window窗口
        Window window = getWindow();
        //设置dialog在屏幕底部
        assert window != null;
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        mDialog.setContentView(dialogView, lp);
        Button callBtn = (Button) dialogView.findViewById(R.id.callBtn);
        Button cancelBtnBtn = (Button) dialogView.findViewById(R.id.cancelBtn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShowActivity.isLogin(VipActivity.this)) {
                    if (mPrice == 0) {
                        getSubscribeVipPrice(1);
                    } else {
                        openOrxfVIP();
                    }
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        cancelBtnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    private void getMatchInfo() {
        WebBase.getMatchPlayer(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                mMatchInfo = JSONParse.getMatchMessage1(obj);
            }

            @Override
            public void onFailure(String err_msg) {
                Log.e("--TAG", err_msg);
            }
        });
    }

    private void getSubscribeVipPrice(final int flag) {
        WebBase.getSubscribeVipPrice(new JSONHandler() {

            @Override
            public void onSuccess(JSONObject obj) {
                String status = obj.optString("status");
                if (status.equals("ok")) {
                    JSONObject vip = obj.optJSONObject("vip");
                    if (vip != null) {
                        mPrice = vip.optInt("price");
                        mBottomMFB.setText(String.format(String.valueOf(mPrice) + "%s", "魔方宝/年"));
                        if (flag == 1) {
                            openOrxfVIP();
                        }
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
            }
        });
    }

    private void getTopicList() {
        WebBase.getTopicList(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                String status = obj.optString("status");
                if (status.equals("ok")) {
                    int total = obj.optInt("total");
                    JSONArray types = obj.optJSONArray("types");
                    for (int i = 0; i < types.length(); i++) {
                        JSONObject jsonObject = types.optJSONObject(i);
                        int id = jsonObject.optInt("id");
                        String name = jsonObject.optString("name");
                        String created_at = jsonObject.optString("created_at");
                        typeList.add(new Types(total, id, name, created_at));
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Log.e("--TAG", err_msg);
            }
        });
    }

    //获取未读消息数量
    private void getNoReadMsgCount() {
        WebBase.getNoReadMsgCount(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")) {
                    mOnReadMsgCount = obj.optInt("result");
                    if (mOnReadMsgCount > 0) {
                        mMark.setText(String.valueOf(mOnReadMsgCount));
                        mMark.setVisibility(View.VISIBLE);
                    } else {
                        mMark.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
            }
        });
    }

    private void subscribeVIP() {
        WebBase.subscribeVIP(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                String status = obj.optString("status");
                if (status.equals("ok")) {
                    JSONObject vip = obj.optJSONObject("vip");
                    if (vip != null) {
                        String vip_end_at = vip.optString("vip_end_at");
                        SettingDefaultsManager.getInstance().setVipAtEnd(vip_end_at);
                        if (!TextUtils.isEmpty(vip_end_at)) {
                            mTime.setText(String.format("有效期至:%s", vip_end_at));
                            mTime.setVisibility(View.VISIBLE);
                        }
//                        getMatchInfo();
                        userInfo();
                        mSubscribeVIPBtn.setText(getString(R.string.xf));
                    }
                    showToast("VIP开通成功");
                }
            }

            @Override
            public void onFailure(String err_msg) {
                showToast("VIP开通失败");
            }
        });
    }

    private void xfVIP() {
        WebBase.xfVIP(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")) {
                    JSONObject vip = obj.optJSONObject("vip");
                    if (vip != null) {
                        String vip_end_at = vip.optString("vip_end_at");
                        mTime.setText(String.format("有效期至:%s", vip_end_at));
                    }
                    userInfo();
                    showToast("VIP续费成功");
                }
            }

            @Override
            public void onFailure(String err_msg) {
                showToast("VIP续费失败");
            }
        });
    }

    private void userInfo() {
        WebBase.userInfo(SettingDefaultsManager.getInstance().authToken(), new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject user = obj.optJSONObject("user");
                User userbean = new User();
                userbean.setAuth_token(SettingDefaultsManager.getInstance().authToken());
                userbean.setAvatar(user.optString("avatar"));
                userbean.setUsername(user.optString("username"));
                userbean.setUser_id(user.optString("user_id"));
                userbean.setTruename(user.optString("truename"));
                userbean.setRole(user.optString("role"));
                userbean.setNickname(user.optString("nickname"));
                userbean.setPhone(user.optString("phone"));
                userbean.setIdcard(user.optString("idcard"));
                userbean.setIs_super(user.optInt("is_super"));
                userbean.setIs_vip(user.optInt("is_vip"));
                userbean.setVip_end_at(user.optString("vip_end_at"));
                new DBManager(getBaseContext()).addUser(userbean);
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void wx_pay() {
//        WebBase.wx_pay(String.valueOf(pay_id), pro_num, new JSONHandler(true,PayDetailActivity.this,"正在获取订单...") {
//            @Override
//            public void onSuccess(JSONObject obj) {
//                WxPay(obj.optJSONObject("order"));
//            }
//
//            @Override
//            public void onFailure(String err_msg) {
//                LogUtil.e(err_msg);
//            }
//        });
//    }

//    /**
//     * 微信支付
//     * @param
//     */
//    private void WxPay(JSONObject obj) {
//        if (wx_api == null) {
//            wx_api = WXAPIFactory.createWXAPI(VipActivity.this, Constants.WEI_APK_KEY);
//        }
//        if (!wx_api.isWXAppInstalled()) {
//            //提醒用户没有按照微信
//            Toast.makeText(getApplicationContext(), "没有安装微信客户端", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        LogUtil.json(obj.toString());
//        PayReq request = new PayReq();
//        request.appId = obj.optString("appid");
//        request.partnerId = obj.optString("partnerid");
//        request.prepayId = obj.optString("prepayid");
//        request.packageValue = obj.optString("package");
//        request.nonceStr = obj.optString("noncestr");
//        request.timeStamp = obj.optString("timestamp");
//        request.sign = obj.optString("sign");
//        wx_api.sendReq(request);
//    }
}
