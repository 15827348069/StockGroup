package com.zbmf.groupro.activity;

import android.app.Activity;
import android.app.Dialog;
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

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.FansPriceAdapter;
import com.zbmf.groupro.adapter.UserCouponsAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.CouPonsRules;
import com.zbmf.groupro.beans.CouponsBean;
import com.zbmf.groupro.beans.FansPrice;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.ActivityUtil;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.SendBrodacast;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.GridViewForScrollView;
import com.zbmf.groupro.view.SwipeToFinishView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class AddFansActivity extends Activity implements View.OnClickListener {
    private FansPriceAdapter adapter;
    private GridViewForScrollView price_view;
    private List<FansPrice> infolist;
    private String GROUP_ID;
    private Dialog dialog, mz_dialog;
    private List<CouponsBean> couponslist;
    private UserCouponsAdapter userCouponsAdapter;
    private TextView coupns_textview, mfb_text_view, add_tf_date;
    private CheckedTextView jf_to_mfb_button;
    private LinearLayout add_fans_pay_message;
    private TextView add_fans_button_text, need_pay_mfb_number, my_tv_mfb, my_tv_jf;
    private boolean need_add_mfb, jf_to_mfb;
    private double jf_to_mfb_number;
    private double need_pay_mfb;
    private CouponsBean coupons;
    DecimalFormat df = new DecimalFormat("");
    DecimalFormat double_df = new DecimalFormat("######0.00");
    private Group group;
    private TextView group_name, group_name_id, add_day_fans_text_message;
    private boolean is_fans;
    private TextView group_title_name;
    private long use_jf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fans);
        new SwipeToFinishView(this);
        group = (Group) getIntent().getSerializableExtra("GROUP");
        ActivityUtil.getInstance().putActivity(ActivityUtil.AddFansActivity, this);
        GROUP_ID = group.getId();
        getUserCoupons(false);
    }

    private void init() {
        infolist = new ArrayList<>();
        adapter = new FansPriceAdapter(getBaseContext(), infolist);
        price_view = (GridViewForScrollView) findViewById(R.id.fans_price_gridview);
        price_view.setAdapter(adapter);

        findViewById(R.id.user_coupons_layout).setOnClickListener(this);
        findViewById(R.id.group_title_return).setOnClickListener(this);
        findViewById(R.id.sure_add_fans_button).setOnClickListener(this);

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
        group_title_name = (TextView) findViewById(R.id.group_title_name);
        group_title_name.setText(getString(R.string.tf_title));

        price_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSelect(i);
                coupons = null;
                getCanUseCoupons(infolist.get(i));
            }
        });
        setAddFansMessage();
        getPrice();
    }

    /**
     * 根据选择的类型获取可用优惠券
     */
    public void getCanUseCoupons(FansPrice fp) {
        setadd_tf_date(fp.getCount());
        need_pay_mfb = fp.getPrice();
        need_pay_mfb_number.setTag(fp);
        setJF(need_pay_mfb);
        if (fp.getUnit() == 1) {
            add_day_fans_text_message.setVisibility(View.VISIBLE);
        } else {
            add_day_fans_text_message.setVisibility(View.GONE);
        }
        setMFB_TEXT_VIEW(true);
        int can_use_coupons_size = can_user(fp);
        if (can_use_coupons_size != 0) {
            coupns_textview.setText(can_use_coupons_size + "张可用");
            Collections.sort(couponslist, new MyComparator());
        } else {
            coupns_textview.setText("无可用");
        }


    }

    public int can_user(FansPrice fp) {
        int can_use_coupons = 0;
        double price = fp.getPrice();
        double days = fp.getCount();
        if (couponslist == null) {
            return can_use_coupons;
        }
        int size = couponslist.size();
        for (int i = 0; i < size; i++) {
            CouponsBean cb = couponslist.get(i);
            boolean can_use_this = false;
            switch (cb.getCateogry()) {
                case 20:
                    //魔方宝
                    if(cb.getMaximum()!=0){
                        if (price >= cb.getMinimum()&&price<=cb.getMaximum()) {
                            can_use_this = true;
                            can_use_coupons += 1;
                        } else {
                            can_use_this = false;
                        }
                    }else{
                        if (price >= cb.getMinimum()) {
                            can_use_this = true;
                            can_use_coupons += 1;
                        } else {
                            can_use_this = false;
                        }
                    }
                    break;
                case 30:
                    //加入时间
                    if (is_fans) {
                        if(cb.getMaximum()!=0){
                            if (days >= cb.getMinimum() && cb.getKind() != 12&&days<=cb.getMaximum()) {
                                can_use_this = true;
                                can_use_coupons += 1;
                            } else {
                                can_use_this = false;
                            }
                        }else{
                            if (days >= cb.getMinimum() && cb.getKind() != 12) {
                                can_use_this = true;
                                can_use_coupons += 1;
                            } else {
                                can_use_this = false;
                            }
                        }

                    } else {
                        if(cb.getMaximum()!=0){
                            if (days >= cb.getMinimum()&&days<=cb.getMaximum()) {
                                can_use_this = true;
                                can_use_coupons += 1;
                            } else {
                                can_use_this = false;
                            }
                        }else{
                            if (days >= cb.getMinimum()) {
                                can_use_this = true;
                                can_use_coupons += 1;
                            } else {
                                can_use_this = false;
                            }
                        }

                    }
                    break;
            }
            couponslist.get(i).setCan_use(can_use_this);
            couponslist.get(i).setIs_take(false);
        }
        return can_use_coupons;
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
        my_tv_mfb.setText(SettingDefaultsManager.getInstance().getPays());
        my_tv_jf.setText(String.format("%d", SettingDefaultsManager.getInstance().getPoint()));
    }

    public void setJF(double price) {
        if(price<=0){
            price=0;
        }
        if (group.getEnable_point() == 0) {
            jf_to_mfb_button.setVisibility(View.GONE);
        } else {
            jf_to_mfb_button.setVisibility(View.VISIBLE);
            jf_to_mfb_button.setChecked(jf_to_mfb);
            jf_to_mfb_button.setOnClickListener(this);
            long my_jf = SettingDefaultsManager.getInstance().getPoint();
            long max_point = group.getMax_point();
            double jf_to_mfb_bi = group.getMax_mpay() / group.getMax_point();
            double need_jf = price / jf_to_mfb_bi;
            String countent = null;
            if (my_jf >= need_jf) {
                if (need_jf <= max_point) {
                    jf_to_mfb_number = price;
                    use_jf = Long.valueOf(getDoubleormat(need_jf));
                } else {
                    jf_to_mfb_number = group.getMax_mpay();
                    use_jf = max_point;
                }
            } else {
                jf_to_mfb_number = my_jf * jf_to_mfb_bi;
                if (jf_to_mfb_number > 0.01) {
                    use_jf = my_jf;
                } else {
                    jf_to_mfb_button.setVisibility(View.GONE);
                }
            }
            countent = use_jf + "积分可抵扣" + getDoubleormat(jf_to_mfb_number) + "魔方宝<br>";
            countent+= "<font color=\"#666666\"><small>(最高可用" + max_point + "积分抵扣)</small></font>";
            jf_to_mfb_button.setText(Html.fromHtml(countent));
        }
    }

    public String getDoubleormat(double vealue) {
        if (double_df.format(vealue).contains(".00")) {
            double ve = Double.valueOf(double_df.format(vealue));
            return df.format(ve);
        } else {
            return double_df.format(vealue);
        }
    }

    /***
     * 设置需要支付的魔方宝
     * @param
     */
    public void setMFB_TEXT_VIEW(boolean have_jf) {
        double need_mfb = need_pay_mfb;
        if (coupons != null) {
            List<CouPonsRules> rules = coupons.getRiles();
            if (rules.size() > 0) {
                CouPonsRules couPonsRules = rules.get(0);
                switch (coupons.getCateogry()) {
                    case 20:
                        //满减魔方宝
                        if (couPonsRules.getIs_incr() == 0) {
                            double amount = couPonsRules.getAmount();
                            need_mfb = need_mfb - amount;
                        }
                        break;
                    case 30:
                        //打折券
                        if(couPonsRules.getIs_ratio()==1){
                            if (couPonsRules.getIs_incr() == 0 && couPonsRules.getCateogry() == 20) {
                                double amount = (100 - couPonsRules.getAmount()) / 100;
                                need_mfb = need_mfb * amount;
                            } else {
                                int count = (int) (couPonsRules.getAmount() / 31);
                                need_mfb = need_mfb - count * group.getMonth_mapy();
                            }
                        }else{
                            //魔方宝
                            double amount = couPonsRules.getAmount();
                            need_mfb = need_mfb - amount;
                        }
                        break;
                }
            }
        }
        if(have_jf){
            setJF(need_mfb);
        }
        if (jf_to_mfb) {
            need_mfb = need_mfb - jf_to_mfb_number;
        }
        if (need_mfb > 0) {
            mfb_text_view.setTag(need_mfb);
            mfb_text_view.setText(getDoubleormat(need_mfb));
        } else {
            mfb_text_view.setTag(0);
            mfb_text_view.setText(df.format(0));
        }

        getNeedPay();
    }

    public void getNeedPay() {
        double need_mfb = Double.valueOf((Double) mfb_text_view.getTag());
        double my_mfb = Double.valueOf(SettingDefaultsManager.getInstance().getPays());
//
        if (need_mfb > my_mfb) {
            need_add_mfb = true;
        } else {
            need_add_mfb = false;
        }

        setButtonMessage(need_mfb - my_mfb);
    }

    public void setButtonMessage(double need_mfb) {
        if (need_add_mfb) {
            add_fans_pay_message.setVisibility(View.VISIBLE);
            add_fans_button_text.setText("去充值");
            need_pay_mfb_number.setText(getDoubleormat(need_mfb));
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
                    if (i == 0) {
                        fp.setIs_checked(true);
                        getCanUseCoupons(fp);
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
                    showMzDialig();
                }
                break;
            case R.id.user_coupons_layout:
                showDialig();
                break;
            case R.id.jf_to_mfb_button:
                jf_to_mfb = !jf_to_mfb;
                jf_to_mfb_button.setChecked(jf_to_mfb);
                setMFB_TEXT_VIEW(false);
                break;
            case R.id.close_coupons_dialog:
                if (dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                }
                break;
            case R.id.sure_button:
                checkUserInfo();
                break;
            case R.id.cause_button:
                if (mz_dialog != null && mz_dialog.isShowing()) {
                    mz_dialog.cancel();
                }
                break;
            case R.id.group_title_return:
                finish();
                break;
        }
    }

    private void checkUserInfo() {
        if (!TextUtils.isEmpty(SettingDefaultsManager.getInstance().getUserPhone())
                && !TextUtils.isEmpty(SettingDefaultsManager.getInstance().getTrueName())
                && !TextUtils.isEmpty(SettingDefaultsManager.getInstance().getIdcard())) {
            subFans();
        } else {
            startActivityForResult(new Intent(this, BindInfoActivity.class), Constants.BINDED);
        }
    }

    public void subFans() {

        FansPrice fp = (FansPrice) need_pay_mfb_number.getTag();
        String coupons_id = null;
        if (coupons != null) {
            coupons_id = coupons.getTake_id();
            SendBrodacast.send(getBaseContext(), Constants.UP_DATA_COUPONS);
        }
        if (fp.getUnit() == 1) {
            //按天加入铁粉
            WebBase.subGuest(GROUP_ID, fp.getCount(), jf_to_mfb, coupons_id, use_jf, new JSONHandler(true, AddFansActivity.this, "正在加入铁粉...") {
                @Override
                public void onSuccess(JSONObject obj) {
                    Toast.makeText(getBaseContext(), "加入铁粉成功", Toast.LENGTH_SHORT).show();
                    is_fans = true;
                    if (mz_dialog != null && mz_dialog.isShowing()) {
                        mz_dialog.cancel();
                    }
                    SendBrodacast.send(getBaseContext(), Constants.UP_DATA_MESSAGE);
                    finish();
                }

                @Override
                public void onFailure(String err_msg) {
                    Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
                    if (mz_dialog != null && mz_dialog.isShowing()) {
                        mz_dialog.cancel();
                    }
                }
            });
        } else if (fp.getUnit() == 2) {
            //按月加入铁粉
            WebBase.subFans(GROUP_ID, fp.getTime(), coupons_id, jf_to_mfb, use_jf, new JSONHandler(true, AddFansActivity.this, "正在加入铁粉...") {
                @Override
                public void onSuccess(JSONObject obj) {
                    Toast.makeText(getBaseContext(), "加入铁粉成功", Toast.LENGTH_SHORT).show();
                    is_fans = true;
                    if (mz_dialog != null && mz_dialog.isShowing()) {
                        mz_dialog.cancel();
                    }
                    finish();
                }

                @Override
                public void onFailure(String err_msg) {
                    Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
                    if (mz_dialog != null && mz_dialog.isShowing()) {
                        mz_dialog.cancel();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.BINDED) {
            subFans();
        }
    }

    public void getUserCoupons(final boolean is_show) {
        WebBase.getUserCoupons(GROUP_ID, new JSONHandler(true, AddFansActivity.this, "正在获取数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                if (couponslist == null) {
                    couponslist = new ArrayList<>();
                }
                if (!obj.isNull("coupons")) {
                    JSONArray coupons = obj.optJSONArray("coupons");
                    int size = coupons.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject coupon = coupons.optJSONObject(i);
                        if (coupon != null) {
                            CouponsBean cb = new CouponsBean();
                            cb.setCoupon_id(coupon.optString("coupon_id"));
                            cb.setSubject(coupon.optString("subject"));
                            cb.setSumary(coupon.optString("summary"));
                            cb.setKind(coupon.optInt("kind"));
                            cb.setTake_id(coupon.optString("take_id"));
                            cb.setType("coupons");
                            cb.setIs_take(false);
                            cb.setStart_at(coupon.optString("start_at"));
                            cb.setEnd_at(coupon.optString("end_at"));
                            cb.setMinimum(coupon.optDouble("minimum"));
                            cb.setMaximum(coupon.optDouble("maximum"));
                            cb.setCateogry(coupon.optInt("category"));
                            JSONArray rules = coupon.optJSONArray("rules");
                            List<CouPonsRules> riles = new ArrayList<CouPonsRules>();
                            int rules_size = rules.length();
                            for (int k = 0; k < rules_size; k++) {
                                CouPonsRules cr = new CouPonsRules();
                                JSONObject rule = rules.optJSONObject(k);
                                if (!rule.isNull("maximum")) {
                                    cr.setAmount(rule.optDouble("amount"));
                                    cr.setCateogry(rule.optDouble("category"));
                                    cr.setIs_ratio(rule.optInt("is_ratio"));
                                    cr.setIs_incr(rule.optInt("is_incr"));
                                    cr.setMaximum(rule.optDouble("maximum"));
                                    riles.add(cr);
                                }
                            }
                            cb.setRiles(riles);
                            couponslist.add(cb);
                        }
                    }
                    init();
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

    public void showDialig() {
        if (couponslist == null) {
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
            win.setWindowAnimations(R.style.cons_dialoganimstyle);
            win.setGravity(Gravity.BOTTOM);
            view.findViewById(R.id.close_coupons_dialog).setOnClickListener(this);
            ListView coupinslist = (ListView) view.findViewById(R.id.user_coupons_list_view_id);
            userCouponsAdapter = new UserCouponsAdapter(getBaseContext(), couponslist);
            coupinslist.setAdapter(userCouponsAdapter);
            coupinslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    CouponsBean cb = couponslist.get(i);
                    if (cb.isCan_use()) {
                        coupns_textview.setText(cb.getSumary());
                        userCouponsAdapter.setSelect(i);
                        coupons = cb;
                        setMFB_TEXT_VIEW(true);
                        dialog.cancel();
                    } else {
                        Toast.makeText(AddFansActivity.this, "优惠券不可使用", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
            win.setAttributes(lp);
            win.setWindowAnimations(R.style.dialoganimstyle);
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    public void showMzDialig() {
        if (mz_dialog != null) {
            mz_dialog.show();
        } else {
            mz_dialog = new Dialog(this, R.style.myDialogTheme);
            View view = LayoutInflater.from(this).inflate(R.layout.mz_message_layout, null);
            mz_dialog.setContentView(view);
            view.findViewById(R.id.sure_button).setOnClickListener(this);
            view.findViewById(R.id.cause_button).setOnClickListener(this);
            Window win = mz_dialog.getWindow();
            win.setGravity(Gravity.CENTER);
            win.setWindowAnimations(R.style.mz_dialoganimstyle);
            mz_dialog.setCancelable(true);
            mz_dialog.show();
        }
    }

    class MyComparator implements Comparator {
        //这里的o1和o2就是list里任意的两个对象，然后按需求把这个方法填完整就行了
        public int compare(Object o1, Object o2) {
            CouponsBean cb = (CouponsBean) o1;
            if (cb.isCan_use()) {
                return -1;
            }
            //比较规则
            return 1;
        }

    }

    @Override
    public void finish() {
        Intent intent = new Intent(AddFansActivity.this, FansActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_fans", is_fans);
        intent.putExtras(bundle);
        setResult(Constants.ADD_FANS, intent);
        super.finish();
    }
}
