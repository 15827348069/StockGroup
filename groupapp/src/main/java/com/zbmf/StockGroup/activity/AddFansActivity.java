package com.zbmf.StockGroup.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.FansPriceAdapter;
import com.zbmf.StockGroup.adapter.UserCouponsAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.CouponsOrSystem;
import com.zbmf.StockGroup.beans.FansPrice;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.Rules;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.SendBrodacast;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.GridViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class AddFansActivity extends BaseActivity implements View.OnClickListener {
    private FansPriceAdapter adapter;
    private GridViewForScrollView price_view;
    private List<FansPrice> infolist;
    private String GROUP_ID;
    private Dialog dialog/*, mz_dialog*/;
    //    private List<CouponsBean> couponslist;
    private List<CouponsOrSystem> couponslist1;
    private UserCouponsAdapter userCouponsAdapter;
    private TextView coupns_textview, mfb_text_view, add_tf_date;
    private CheckedTextView jf_to_mfb_button;
    private LinearLayout add_fans_pay_message;
    private TextView add_fans_button_text, need_pay_mfb_number, my_tv_mfb, my_tv_jf;
    private boolean need_add_mfb, jf_to_mfb=false;
    private double jf_to_mfb_number;
    //    private CouponsBean coupons;
    private CouponsOrSystem couponsOrSystem;
    private Group group;
    private TextView group_name, group_name_id, add_day_fans_text_message;
    private boolean is_fans, is_add;
    private long use_jf;
    private FansPrice fansPrice;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_add_fans;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.tf_title));
        price_view = (GridViewForScrollView) findViewById(R.id.fans_price_gridview);
        coupns_textview = (TextView) findViewById(R.id.coupns_textview);
        mfb_text_view = (TextView) findViewById(R.id.mfb_text_view);
        add_tf_date = (TextView) findViewById(R.id.add_tf_date);
        add_fans_pay_message = (LinearLayout) findViewById(R.id.add_fans_pay_message);
        add_fans_button_text = (TextView) findViewById(R.id.add_fans_button_text);
        need_pay_mfb_number = (TextView) findViewById(R.id.need_pay_mfb_number);
        add_day_fans_text_message = (TextView) findViewById(R.id.add_day_fans_text_message);
        jf_to_mfb_button = (CheckedTextView) findViewById(R.id.jf_to_mfb_button);
        group_name = (TextView) findViewById(R.id.group_name);
        group_name_id = (TextView) findViewById(R.id.group_name_id);
        my_tv_mfb = (TextView) findViewById(R.id.my_tv_mfb);
        my_tv_jf = (TextView) findViewById(R.id.my_tv_jf);
    }

    @Override
    public void initData() {
        group = (Group) getIntent().getSerializableExtra(IntentKey.GROUP);
        GROUP_ID = group.getId();
        infolist = new ArrayList<>();
        adapter = new FansPriceAdapter(getBaseContext(), infolist);
        price_view.setAdapter(adapter);
        getPrice();
        getWolle();
        getUserCoupons(false);
    }

    @Override
    public void addListener() {
        findViewById(R.id.user_coupons_layout).setOnClickListener(this);
        findViewById(R.id.sure_add_fans_button).setOnClickListener(this);
        //点击选择魔方宝套餐类型
        price_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSelect(i);
                couponsOrSystem = null;
                FansPrice fansPrice = infolist.get(i);
                getCanUseCoupons(fansPrice);
            }
        });
    }

    public void getWolle() {
        WebBase.getWalle(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject pays = obj.optJSONObject("pay");
                JSONObject point = obj.optJSONObject("point");
                SettingDefaultsManager.getInstance().setPays(pays.optString("unfrozen"));
                SettingDefaultsManager.getInstance().setPoint(point.optLong("unfrozen"));
                setAddFansMessage();
            }

            @Override
            public void onFailure(String err_msg) {
            }
        });
    }

    /**
     * 根据选择的类型获取可用优惠券
     */
    public void getCanUseCoupons(FansPrice fp) {
        fansPrice = fp;
        int can_use_coupons_size = can_user(fp);
        setadd_tf_date(fp.getCount());
        need_pay_mfb_number.setTag(fp);
        if (fp.getUnit() == 1) {
            add_day_fans_text_message.setVisibility(View.VISIBLE);
        } else {
            add_day_fans_text_message.setVisibility(View.GONE);
        }
        setJF(fp.getPrice());
        if (can_use_coupons_size != 0) {
            coupns_textview.setText(String.format(can_use_coupons_size + "%s", "张可用"));
            Collections.sort(couponslist1, new MyComparator());
        } else {
            coupns_textview.setText("无可用");
        }
        getCouponsMfb();
    }

    public int can_user(FansPrice fp) {
        double price = fp.getPrice();
        double days = fp.getCount();
        int can_use_coupons = 0;
        if (couponslist1 == null) {
            return can_use_coupons;
        }
        int size = couponslist1.size();
        for (int i = 0; i < size; i++) {
            CouponsOrSystem couponsOrSystem = couponslist1.get(i);
            int kind = couponsOrSystem.getKind();
            int unit = fp.getUnit();
            boolean can_use_this = false;
            int category = couponsOrSystem.getCategory();
            switch (category) {
                case 20:
                    //魔方宝
                    int minimum = couponsOrSystem.getMinimum();
                    int maximum = couponsOrSystem.getMaximum();
                    if (maximum != 0) {
                        if (price >= minimum && price <= maximum) {
                            boolean b = compileTimeIsUse(couponsOrSystem);
                            if (b) {
                                if (kind == 12) {
                                    if (unit == 1) {
                                        //体验券&&体验套餐
                                        can_use_this = true;
                                        can_use_coupons += 1;
                                    } else {
                                        can_use_this = false;
                                    }
                                } else {
                                    can_use_this = true;
                                    can_use_coupons += 1;
                                }
                            }
                        } else {
                            can_use_this = false;
                        }
                    } else {
                        if (price >= minimum) {
                            boolean b = compileTimeIsUse(couponsOrSystem);
                            if (b) {
                                if (kind == 12) {
                                    if (unit == 1) {
                                        //体验券&&体验套餐
                                        can_use_this = true;
                                        can_use_coupons += 1;
                                    } else {
                                        can_use_this = false;
                                    }
                                } else {
                                    can_use_this = true;
                                    can_use_coupons += 1;
                                }
                            }
                        } else {
                            can_use_this = false;
                        }
                    }
                    break;
                case 30:
                    //加入时间
                    int maximum1 = couponsOrSystem.getMaximum();
                    int minimum1 = couponsOrSystem.getMinimum();
                    if (is_fans) {
                        if (maximum1 != 0) {
                            if (days >= minimum1 && kind != 12 && days <= maximum1) {
                                boolean b = compileTimeIsUse(couponsOrSystem);
                                if (b) {
                                        can_use_this = true;
                                        can_use_coupons += 1;
                                }
                            } else {
                                can_use_this = false;
                            }
                        } else {
                            if (days >= minimum1 && kind != 12) {
                                boolean b = compileTimeIsUse(couponsOrSystem);
                                if (b) {
                                        can_use_this = true;
                                        can_use_coupons += 1;
                                }
                            } else {
                                can_use_this = false;
                            }
                        }
                    } else {
                        if (maximum1 != 0) {
                            if (days >= minimum1 /*&& kind != 12 */ && days <= maximum1) {
                                boolean b = compileTimeIsUse(couponsOrSystem);
                                if (b) {
                                    if (kind == 12) {
                                        if (unit == 1) {
                                            //体验券&&体验套餐
                                            can_use_this = true;
                                            can_use_coupons += 1;
                                        } else {
                                            can_use_this = false;
                                        }
                                    } else {
                                        can_use_this = true;
                                        can_use_coupons += 1;
                                    }
                                }
                            } else {
                                can_use_this = false;
                            }
                        } else {
                            if (days >= minimum1 /*&& kind != 12*/) {
                                boolean b = compileTimeIsUse(couponsOrSystem);
                                if (b) {
                                    if (kind == 12) {
                                        if (unit == 1) {
                                            //体验券&&体验套餐
                                            can_use_this = true;
                                            can_use_coupons += 1;
                                        } else {
                                            can_use_this = false;
                                        }
                                    } else {
                                        can_use_this = true;
                                        can_use_coupons += 1;
                                    }
                                }
                            } else {
                                can_use_this = false;
                            }
                        }

                    }
                    break;
            }
            couponslist1.get(i).setCanUse(can_use_this);
            couponslist1.get(i).setCheck(false);
        }
        return can_use_coupons;
    }

    private boolean setQuanType(boolean can_use_this, int kind, int
            unit) {
        if (kind == 12) {
            if (unit == 1) {
                //体验券&&体验套餐
                can_use_this = true;
//                can_use_coupons += 1;
            } else {
                can_use_this = false;
            }
        } else if (kind == 3) {
            if (unit == 2) {
                //非体验券&&费体验套餐
                can_use_this = true;
//                can_use_coupons += 1;
            } else {
                can_use_this = false;
            }
        }
        return can_use_this;
    }

    @SuppressLint("SimpleDateFormat")
    private boolean compileTimeIsUse(CouponsOrSystem couponsOrSystem) {
        String start_at = couponsOrSystem.getStart_at();
        String end_at = couponsOrSystem.getEnd_at();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String currentTime = format.format(date);
        String s1 = start_at.replaceAll("-", "");
        String s3 = end_at.replaceAll("-", "");
        String s2 = currentTime.replaceAll("-", "");
        int start_time = Integer.parseInt(s1);
        int current_time = Integer.parseInt(s2);
        int end_time = Integer.parseInt(s3);
        return current_time >= start_time && current_time < end_time;
    }

    public void setadd_tf_date(int count) {
        String message = null;
        switch (count) {
            case Constants.DAYS:
                message = count + "天";
                break;
            case Constants.DAYS * 7:
                message = count + "天";
                break;
            case Constants.MONTH_DAYS:
                message = 1 + "个月";
                break;
            case Constants.MONTH_DAYS * 3:
                message = 3 + "个月";
                break;
            case Constants.MONTH_DAYS * 6:
                message = 6 + "个月";
                break;
            case Constants.MONTH_DAYS * 12:
                message = "1年";
                break;
        }
        add_tf_date.setText(message);
    }

    /**
     * 设置用户可用魔方宝
     */
    public void setAddFansMessage() {
        group_name.setText("【" + group.getName() + "】");
        group_name_id.setText("【" + group.getName() + "】");
        my_tv_mfb.setText(DoubleFromat.getDouble(Double.valueOf(SettingDefaultsManager.getInstance().getPays()), 2));
        my_tv_jf.setText(String.format("%d", SettingDefaultsManager.getInstance().getPoint()));
    }

    public void setJF(double price) {
        if (price <= 0) {
            price = 0;
        }
        int enable_point = group.getEnable_point();
        if (enable_point == 0) {
            jf_to_mfb_button.setVisibility(View.GONE);
        } else {
            jf_to_mfb_button.setVisibility(View.VISIBLE);
            jf_to_mfb_button.setChecked(jf_to_mfb);
            jf_to_mfb_button.setOnClickListener(this);
            long my_jf = SettingDefaultsManager.getInstance().getPoint();//我自己的积分
            long max_point = group.getMax_point();//规定最高可使用的积分
            double jf_to_mfb_bi = group.getMax_mpay() / group.getMax_point();
            double need_jf = price / jf_to_mfb_bi;//兑换成魔方宝所需要的积分
            String countent = null;
            if (my_jf >= need_jf) {
                if (need_jf <= max_point) {
                    jf_to_mfb_number = price;
                    use_jf = Long.valueOf(DoubleFromat.getDouble(need_jf, 2));
                } else {
                    jf_to_mfb_number = group.getMax_mpay();
                    use_jf = max_point;
                }
            } else {
                if (my_jf <= max_point) {
                    jf_to_mfb_number = my_jf * jf_to_mfb_bi;
                    if (jf_to_mfb_number > 0.01) {
                        use_jf = my_jf;
                    } else {
                        jf_to_mfb_button.setVisibility(View.GONE);
                    }
                } else {
                    jf_to_mfb_number = group.getMax_mpay();
                    if (jf_to_mfb_number > 0.01) {
                        use_jf = max_point;
                    } else {
                        jf_to_mfb_button.setVisibility(View.GONE);
                    }
                }
            }
            countent = use_jf + "积分可抵扣" + DoubleFromat.getDouble(jf_to_mfb_number, 2) + "魔方宝<br>";
            countent += "<font color=\"#666666\"><small>(最高可用" + max_point + "积分抵扣)</small></font>";
            jf_to_mfb_button.setText(Html.fromHtml(countent));
        }
    }

    private int point = 0;

    private void getCouponsMfb() {
        int coupon_id = 0, product_id = 0;
        if (couponsOrSystem != null) {
            coupon_id = couponsOrSystem.getTake_id();
        }
        if (fansPrice != null) {
            product_id = fansPrice.getId();
        }
        if (jf_to_mfb) {
            point = 1;
        }else {
            point=0;
        }
        if (coupon_id == 0 && point == 0) {
            double price = fansPrice.getPrice();
            String aDouble = DoubleFromat.getDouble(price, 2);
            mfb_text_view.setText(aDouble);

            setButtonMessage(fansPrice.getPrice());
        } else {
            WebBase.ruleCouponFans(GROUP_ID, coupon_id, product_id, point, new JSONHandler(true,
                    AddFansActivity.this, getString(R.string.loading)) {
                @Override
                public void onSuccess(JSONObject obj) {
                    double mpays = obj.optDouble("mpays");
                    if (point == 1) {
                        mpays = mpays - jf_to_mfb_number;
                    }
                    if (mpays > 0) {
                        mfb_text_view.setText(DoubleFromat.getDouble(mpays, 2));
                    } else {
                        mfb_text_view.setText("0");
                    }
                    setButtonMessage(mpays);
                }

                @Override
                public void onFailure(String err_msg) {
                    setButtonMessage(fansPrice.getPrice());
                }
            });
        }
    }

    public void setButtonMessage(double mpays) {
        double my_mfb = Double.valueOf(SettingDefaultsManager.getInstance().getPays());
        double need_pay_mfb = mpays - my_mfb;
        if (need_pay_mfb > 0) {
            need_add_mfb = true;
        } else {
            need_add_mfb = false;
        }
        if (need_add_mfb) {
            add_fans_pay_message.setVisibility(View.VISIBLE);
            add_fans_button_text.setText("去充值");
            need_pay_mfb_number.setText(DoubleFromat.getDouble(need_pay_mfb, 2));
        } else {
            add_fans_pay_message.setVisibility(View.GONE);
            if (group.getFans_level() < 5) {
                add_fans_button_text.setText("加入铁粉");
                is_fans = false;
            } else {
                add_fans_button_text.setText("续费铁粉");
                is_fans = true;
            }
        }
    }

    public void getPrice() {
        WebBase.fansProduct(GROUP_ID, new JSONHandler(false, AddFansActivity.this, "正在加载数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                if (couponslist1 == null) {
//                        couponslist = new ArrayList<>();
                    couponslist1 = new ArrayList<>();
                }
                JSONArray products = obj.optJSONArray("products");
                int size = products.length();
                for (int i = 0; i < size; i++) {
                    JSONObject oj = products.optJSONObject(i);
                    FansPrice fp = new FansPrice();
                    fp.setPrice(oj.optDouble("mpays"));
                    fp.setTitle(oj.optString("title"));
                    fp.setCount(oj.optInt("days"));
                    fp.setType(oj.optInt("type"));
                    fp.setUnit(oj.optInt("unit"));
                    fp.setTime(oj.optInt("time"));
                    fp.setId(oj.optInt("product_id"));
                    if (i == 0) {
                        fp.setIs_checked(true);
                        if (couponslist1 != null && couponslist1.size() > 0) {
                            getCanUseCoupons(fp);
                        }
                    }
                    infolist.add(fp);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sure_add_fans_button:
                if (need_add_mfb) {
                    ShowActivity.showPayDetailActivity(this);
                } else {
                    checkUserInfo();
//                    showMzDialig();
                }
                break;
            case R.id.user_coupons_layout:
                showDialig();
                break;
            case R.id.jf_to_mfb_button:
                if (!jf_to_mfb){
                    jf_to_mfb=true;
                }else {
                    jf_to_mfb=false;
                }
//                jf_to_mfb = !jf_to_mfb;
                jf_to_mfb_button.setChecked(jf_to_mfb);
                getCouponsMfb();
                break;
            case R.id.close_coupons_dialog:
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                break;
           /* case R.id.sure_button:
                checkUserInfo();
                break;
            case R.id.cause_button:
                if (mz_dialog != null && mz_dialog.isShowing()) {
                    mz_dialog.cancel();
                }
                break;*/
        }
    }

    private void checkUserInfo() {
        String userPhone = SettingDefaultsManager.getInstance().getUserPhone();
        String trueName = SettingDefaultsManager.getInstance().getTrueName();
        String idCard = SettingDefaultsManager.getInstance().getIdcard();
        boolean userPhoneEmpty = TextUtils.isEmpty(userPhone);
        boolean trueNameEmpty = TextUtils.isEmpty(trueName);
        boolean idCardEmpty = TextUtils.isEmpty(idCard);
        if (!userPhoneEmpty && !trueNameEmpty && !idCardEmpty) {
            subFans();
        } else {
            startActivityForResult(new Intent(this, BindInfoActivity.class), RequestCode.BINDED);
//            mz_dialog.dismiss();
        }
    }

    public void subFans() {
        FansPrice fp = (FansPrice) need_pay_mfb_number.getTag();
        int coupons_id = 0;
        if (couponsOrSystem != null) {
            coupons_id = couponsOrSystem.getTake_id();
            SendBrodacast.send(getBaseContext(), Constants.UP_DATA_COUPONS);
        }
        if (fp.getUnit() == 1) {
            //按天加入铁粉
            WebBase.subGuest(GROUP_ID, fp.getCount(), jf_to_mfb, String.valueOf(coupons_id), use_jf,
                    new JSONHandler(true, AddFansActivity.this, "正在加入铁粉...") {
                        @Override
                        public void onSuccess(JSONObject obj) {
                            showToast("加入铁粉成功");
                            is_fans = true;
                            is_add = true;
                            /*if (mz_dialog != null && mz_dialog.isShowing()) {
                                mz_dialog.cancel();
                            }*/
                            SendBrodacast.send(getBaseContext(), Constants.UP_DATA_MESSAGE);
                            setResultOk();
                        }

                        @Override
                        public void onFailure(String err_msg) {
                            showToast(err_msg);
                            /*if (mz_dialog != null && mz_dialog.isShowing()) {
                                mz_dialog.cancel();
                            }*/
                        }
                    });
        } else if (fp.getUnit() == 2) {
            //按月加入铁粉
            WebBase.subFans(GROUP_ID, fp.getTime(), String.valueOf(coupons_id), jf_to_mfb, use_jf,
                    new JSONHandler(true, AddFansActivity.this, "正在加入铁粉...") {
                        @Override
                        public void onSuccess(JSONObject obj) {
                            showToast("加入铁粉成功");
                            is_fans = true;
                            is_add = true;
                          /*  if (mz_dialog != null && mz_dialog.isShowing()) {
                                mz_dialog.cancel();
                            }*/
                            SendBrodacast.send(getBaseContext(), Constants.UP_DATA_MESSAGE);
                            setResultOk();
                        }

                        @Override
                        public void onFailure(String err_msg) {
                            showToast(err_msg);
                           /* if (mz_dialog != null && mz_dialog.isShowing()) {
                                mz_dialog.cancel();
                            }*/
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RequestCode.BINDED) {
            subFans();
        }
    }

    public void getUserCoupons(final boolean is_show) {
        if (couponslist1 != null) {
            couponslist1.clear();
        }
        WebBase.getUserCoupons(GROUP_ID, new JSONHandler(true, AddFansActivity.this, "正在获取数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                if (couponslist1 == null) {
                    couponslist1 = new ArrayList<>();
                }
                if (!obj.isNull("coupons")) {
                    JSONArray coupons = obj.optJSONArray("coupons");
                    int length = coupons.length();
                    List<CouponsOrSystem> couponsList = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject = coupons.optJSONObject(i);
                        JSONArray rules = jsonObject.optJSONArray("rules");
                        List<Rules> rulesList = new ArrayList<>();
                        for (int i1 = 0; i1 < rules.length(); i1++) {
                            int category = rules.optJSONObject(i1).optInt("category");
                            Rules rules1 = new Rules(
                                    rules.optJSONObject(i1).optInt("category"),
                                    rules.optJSONObject(i1).optInt("is_incr"),
                                    rules.optJSONObject(i1).optInt("is_ratio"),
                                    rules.optJSONObject(i1).optDouble("amount"),
                                    rules.optJSONObject(i1).optInt("maximum"));
                            rulesList.add(rules1);
                        }
                        couponsList.add(new CouponsOrSystem(jsonObject.optInt("take_id"),
                                jsonObject.optInt("coupon_id"),
                                jsonObject.optInt("id"), jsonObject.optString("subject"),
                                jsonObject.optString("summary"), jsonObject.optInt("kind"),
                                jsonObject.optString("start_at"), jsonObject.optString("end_at"),
                                jsonObject.optInt("is_taken"), jsonObject.optInt("rule_valid"),
                                jsonObject.optInt("minimum"), jsonObject.optInt("maximum"),
                                jsonObject.optInt("category"), jsonObject.optInt("total"),
                                jsonObject.optInt("havings"), jsonObject.optInt("days"),
                                jsonObject.optInt("is_ratio"), jsonObject.optInt("is_hide"),
                                jsonObject.optInt("is_delete"), jsonObject.optInt("user_id"),
                                jsonObject.optString("nickname"), jsonObject.optString("avatar"),
                                rulesList));
                    }
//                    List<CouponsBean> couponsList1 = JSONParse.getCouponsList(coupons);
                    couponslist1.addAll(couponsList);
                    if (infolist.size() > 0) {
                        for (FansPrice fansPrice : infolist) {
                            if (fansPrice.is_checked()) {
                                getCanUseCoupons(fansPrice);
                            }
                        }
                    }
                    if (is_show) {
                        showDialig();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });


    }

    private void setResultOk() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_add", is_add);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showDialig() {
        if (couponslist1 == null) {
            getPrice();
            getUserCoupons(true);
            return;
        }
        if (dialog != null) {
            userCouponsAdapter.notifyDataSetChanged();
            dialog.show();
        } else {
            dialog = new Dialog(this, R.style.myDialogTheme);
            View view = LayoutInflater.from(this).inflate(R.layout.user_coupons_list_view, null);
            dialog.setContentView(view);
            Window win = dialog.getWindow();
            if (win != null) {
                win.setWindowAnimations(R.style.cons_dialoganimstyle);
                win.setGravity(Gravity.BOTTOM);
                view.findViewById(R.id.close_coupons_dialog).setOnClickListener(this);
                WindowManager.LayoutParams lp = win.getAttributes();
                lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
                win.setAttributes(lp);
                win.setWindowAnimations(R.style.dialoganimstyle);
            }
            ListView coupinslist = (ListView) view.findViewById(R.id.user_coupons_list_view_id);
            userCouponsAdapter = new UserCouponsAdapter(getBaseContext(), couponslist1);
            coupinslist.setAdapter(userCouponsAdapter);
            coupinslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    CouponsBean cb = couponslist.get(i);
                    CouponsOrSystem cb = couponslist1.get(i);
                    if (cb.isCanUse()) {
                        coupns_textview.setText(cb.getSubject());
                        userCouponsAdapter.setSelect(i);
                        couponsOrSystem = cb;
                    } else {
                        Toast.makeText(AddFansActivity.this, "优惠券不可使用", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            dialog.setCancelable(true);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    getCouponsMfb();
                }
            });
            dialog.show();
        }
    }

   /* public void showMzDialig() {
        if (mz_dialog != null) {
            mz_dialog.show();
        } else {
            mz_dialog = new Dialog(this, R.style.myDialogTheme);
            View view = LayoutInflater.from(this).inflate(R.layout.mz_message_layout, null);
            mz_dialog.setContentView(view);
            view.findViewById(R.id.sure_button).setOnClickListener(this);
            view.findViewById(R.id.cause_button).setOnClickListener(this);
            Window win = mz_dialog.getWindow();
            if (win != null) {
                win.setGravity(Gravity.CENTER);
                win.setWindowAnimations(R.style.mz_dialoganimstyle);
            }
            mz_dialog.setCancelable(true);
            mz_dialog.show();
        }
    }*/

    private class MyComparator implements Comparator<CouponsOrSystem> {
        public int compare(CouponsOrSystem o1, CouponsOrSystem/*CouponsBean*/ o2) {
            if (o1.isCanUse()) {
                return -1;
            } else if (o1.isCanUse() && o2.isCanUse() && o1.getMinimum() < o2.getMinimum()) {
                return -1;
            } else if (!o1.isCanUse() && o2.isCanUse()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
