package com.zbmf.StockGroup.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.BoxItemAdapter;
import com.zbmf.StockGroup.adapter.DialogQuanAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BoxBean;
import com.zbmf.StockGroup.beans.CouponsOrSystem;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.dialog.QuanBottomDialog;
import com.zbmf.StockGroup.interfaces.GainQuan;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.SendBrodacast;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;
import com.zbmf.StockGroup.view.SwipeToFinishView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao
 * on 2017/1/3.
 */

public class FansActivity extends BaseActivity implements View.OnClickListener, GainQuan {
    private ListViewForScrollView box_list;
    private List<BoxBean> infolist;
    private BoxItemAdapter adapter;
    private PullToRefreshScrollView fans_activity_scrollview;
    private int page, pages;
    private String GROUP_ID;
    private TextView fabs_desc_text, /*look_all_text,*/
            add_box_price_month, add_box_price_day/*, actitivy_text_message*/;
    //    private ImageView iv_arrow;
    private LinearLayout /*look_all_desc, */fans_message_layout, fans_message_linear;
    private Group group_bean;
    private Button add_fans_button, mClickGainBtn;
    private boolean is_fans;
    private TextView tv_fans_type, tv_fans_day;
    private boolean isFirst = true;
    private DecimalFormat df = new DecimalFormat("");
    private LinearLayout mLoyalFens;
    private List<CouponsOrSystem> couponList;
    private Dialog mDialog;
    private DialogQuanAdapter mDialogQuanAdapter;
    private QuanBottomDialog mQuanBottomDialog;
    private Dialog mz_dialog;
    private LinearLayout mFans_bottom_layout;

    @Override
    public int getLayoutResId() {
        return R.layout.fans_activity_layout;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.tf_title));
        new SwipeToFinishView(this);

        couponList = new ArrayList<>();

        box_list = (ListViewForScrollView) findViewById(R.id.box_item_message);
        fabs_desc_text = (TextView) findViewById(R.id.fabs_desc_text);
        add_box_price_month = (TextView) findViewById(R.id.add_box_price_month);
        add_box_price_day = (TextView) findViewById(R.id.add_box_price_day);
        mClickGainBtn = getView(R.id.clickGainBtn);
//        RelativeLayout topQuanTip = getView(R.id.topQuanTip);
        mLoyalFens = getView(R.id.loyalFens);
//        actitivy_text_message = (TextView) findViewById(R.id.actitivy_text_message);
//        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
//        look_all_desc = (LinearLayout) findViewById(R.id.look_all_desc);
//        look_all_text = (TextView) findViewById(R.id.look_all_text);
        add_fans_button = (Button) findViewById(R.id.add_fans_button);
        mFans_bottom_layout = findViewById(R.id.fans_bottom_layout);
        fans_message_layout = (LinearLayout) findViewById(R.id.fans_message_layout);
        fans_message_linear = (LinearLayout) findViewById(R.id.fans_message_linear);
        tv_fans_type = (TextView) findViewById(R.id.tv_fans_type);
        tv_fans_day = (TextView) findViewById(R.id.tv_fans_day);
        fans_activity_scrollview = (PullToRefreshScrollView) findViewById(R.id.fans_activity_scrollview);
        mQuanBottomDialog = new QuanBottomDialog(this, R.style.Theme_Light_Dialog);
//        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//        mFans_bottom_layout.measure(w, h);  //在onCreate方法里边获取控件大小
//        int height = mFans_bottom_layout.getMeasuredHeight();
//        int width = mFans_bottom_layout.getMeasuredWidth();
    }

    @Override
    public void initData() {
        group_bean = (Group) getIntent().getSerializableExtra(IntentKey.GROUP);
        GROUP_ID = group_bean.getId();
        infolist = new ArrayList<>();
        adapter = new BoxItemAdapter(getBaseContext(), infolist);
        box_list.setAdapter(adapter);
        setFansMessage();
        page = 1;
        mQuanBottomDialog.showDialog(FansActivity.this, FansActivity.this,/*height,*/
                couponList, group_bean, FansActivity.this);
        getBox_Message();
        //获取优惠券
        getCoupons();
    }

    @Override
    public void addListener() {
//        findViewById(R.id.coupons_countent_layout_id).setOnClickListener(this);
        mClickGainBtn.setOnClickListener(this);
        add_fans_button.setOnClickListener(this);
        box_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ShowActivity.isLogin(FansActivity.this)) {
                    BoxBean boxBean = infolist.get(i);
                    int level = Integer.parseInt(boxBean.getBox_level());
                    if (group_bean.getFans_level() >= level) {
                        boxBean.setId(GROUP_ID);
                        ShowActivity.showBoxDetailActivity(FansActivity.this, boxBean);
                    } else {
                        String message = "您为【***】用户，升级成为【**】即可查";
                        switch (group_bean.getFans_level()) {
                            case 0:
                                message = message.replace("【***】", "非铁粉");
                                break;
                            case 5:
                                message = message.replace("【***】", "体验铁粉");
                                break;
                            case 10:
                                message = message.replace("【***】", "非年粉");
                                break;
                        }
                        if (level == 10) {
                            message = message.replace("【**】", "包月铁粉");
                        } else if (level == 20) {
                            message = message.replace("【**】", "年粉");
                        }
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //可展开的文字
//        StretchTextUtil.getInstance(fabs_desc_text, look_all_text, 2, look_all_desc, iv_arrow,
//                R.drawable.icon_all_message_top, R.drawable.icon_all_messagebottom).initStretch();
        fans_activity_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //执行刷新函数
                page += 1;
                getBox_Message();
            }
        });
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
            if (win != null) {
                win.setGravity(Gravity.CENTER);
                win.setWindowAnimations(R.style.mz_dialoganimstyle);
            }
            mz_dialog.setCancelable(true);
            mz_dialog.show();
        }
    }
    public void setFansMessage() {
        int level = group_bean.getFans_level();
        switch (level) {
            case 0:
                setAddFanseMessage();
                break;
            case 5:
                setFanseMessage("体验铁粉");
                break;
            case 10:
                setFanseMessage("包月铁粉");
                break;
            case 20:
                setFanseMessage("年粉");
                break;
        }
    }

    private void setFanseMessage(String message) {
        is_fans = true;
        fans_message_layout.setVisibility(View.VISIBLE);
        fans_message_linear.setVisibility(View.GONE);
        add_fans_button.setText("续费铁粉");
        tv_fans_type.setText(message);
        tv_fans_day.setText(String.format(group_bean.getFans_date() + "%s", "到期"));
//        fabs_desc_text.setText(group_bean.getFans_countent());
//        actitivy_text_message.setText(group_bean.getFans_activity());
        //是铁粉则显示宝盒
        box_list.setVisibility(View.VISIBLE);
        //史铁粉则隐藏铁粉介绍文字
        mLoyalFens.setVisibility(View.GONE);
    }

    private void setAddFanseMessage() {
        LogUtil.e(group_bean.toString());
        is_fans = false;
        fans_message_layout.setVisibility(View.GONE);
        fans_message_linear.setVisibility(View.VISIBLE);
        add_fans_button.setText("加入铁粉");
        add_box_price_month.setText(df.format(group_bean.getMonth_mapy()) + "魔方宝/月");
        add_box_price_day.setText(df.format(group_bean.getDay_mapy()) + "魔方宝/日");
        fabs_desc_text.setText(group_bean.getFans_countent());
//        actitivy_text_message.setText(group_bean.getFans_activity());
        //不是铁粉则隐藏列表
        box_list.setVisibility(View.GONE);
        //史铁粉则显示铁粉介绍文字
        mLoyalFens.setVisibility(View.VISIBLE);
    }

    public void getBox_Message() {
        WebBase.getGroupBoxs(GROUP_ID, "0", page, new JSONHandler(false, FansActivity.this, "正在加载数据...") {
            @Override
            public void onSuccess(JSONObject object) {
                BoxBean bb = JSONParse.getGroupBoxs(object);
                if (bb != null && bb.getList() != null)
                    infolist.addAll(bb.getList());
                adapter.notifyDataSetChanged();
                fans_activity_scrollview.onRefreshComplete();
                page = bb.getPage();
                pages = bb.getPages();
                if (page == pages && !isFirst) {
                    Toast.makeText(FansActivity.this, "已加载全部数据", Toast.LENGTH_SHORT).show();
                    fans_activity_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
                }
                if (isFirst) {
                    isFirst = false;
                }
            }

            @Override
            public void onFailure(String err_msg) {
                fans_activity_scrollview.onRefreshComplete();
                if (isFirst) {
                    isFirst = false;
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_fans_button:
                showMzDialig();
                break;
//            case R.id.coupons_countent_layout_id:
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(IntentKey.GROUP, group_bean);
//                ShowActivity.showActivityForResult(this, bundle, GroupConponsActivity.class, RequestCode.ADD_FANS);
//                break;
            case R.id.clickGainBtn:
                if (mQuanBottomDialog != null) {
                    mQuanBottomDialog.show();
                }
                break;
            case R.id.sure_button:
                ShowActivity.showAddFansActivity(this, group_bean);
                if (mz_dialog!=null&&mz_dialog.isShowing()){
                    mz_dialog.dismiss();
                }
                break;
            case R.id.cause_button:
                if (mz_dialog != null && mz_dialog.isShowing()) {
                    mz_dialog.cancel();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e("resultCode==" + resultCode);
        LogUtil.e("requestCode==" + requestCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.ADD_FANS:
                    Bundle b = data.getExtras();
                    boolean is_add = b.getBoolean("is_add");
                    if (is_add) {
                        getFansInfo();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent(this, Chat1Activity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_fans", is_fans);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    private void getFansInfo() {
        WebBase.fansInfo(GROUP_ID, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (!obj.isNull("group")) {
                    JSONObject group = obj.optJSONObject("group");
                    group_bean = JSONParse.getGroup(group);
                    setFansMessage();
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    //获取系统和该圈子的优惠券
    private void getCoupons() {
        WebBase.getGroupCoupons(GROUP_ID, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")) {
                    couponList.clear();
                    JSONArray coupons = obj.optJSONArray("groups");
                    int couponsLength = coupons.length();
                    for (int i = 0; i < couponsLength; i++) {
                        JSONObject couponsObj = coupons.optJSONObject(i);
                        couponList.add(new CouponsOrSystem(couponsObj.optInt("take_id"),
                                couponsObj.optInt("coupon_id"),
                                couponsObj.optInt("id"), couponsObj.optString("subject"),
                                couponsObj.optString("summary"), couponsObj.optInt("kind"),
                                couponsObj.optString("start_at"), couponsObj.optString("end_at"),
                                couponsObj.optInt("is_taken"), couponsObj.optInt("rule_valid"),
                                couponsObj.optInt("minimum"), couponsObj.optInt("maximum"),
                                couponsObj.optInt("category"), couponsObj.optInt("total"),
                                couponsObj.optInt("havings"), couponsObj.optInt("days"),
                                couponsObj.optInt("is_ratio"), couponsObj.optInt("is_hide"),
                                couponsObj.optInt("is_delete"), couponsObj.optInt("user_id"),
                                couponsObj.optString("nickname"), couponsObj.optString("avatar")));
                    }
                    JSONArray systems = obj.optJSONArray("systems");
                    int systemLength = systems.length();
                    for (int i = 0; i < systemLength; i++) {
                        JSONObject systemObject = systems.optJSONObject(i);
                        couponList.add(new CouponsOrSystem(systemObject.optInt("take_id"),
                                systemObject.optInt("coupon_id"),
                                systemObject.optInt("id"), systemObject.optString("subject"),
                                systemObject.optString("summary"), systemObject.optInt("kind"),
                                systemObject.optString("start_at"), systemObject.optString("end_at"),
                                systemObject.optInt("is_taken"), systemObject.optInt("rule_valid"),
                                systemObject.optInt("minimum"), systemObject.optInt("maximum"),
                                systemObject.optInt("category"), systemObject.optInt("total"),
                                systemObject.optInt("havings"), systemObject.optInt("days"),
                                systemObject.optInt("is_ratio"), systemObject.optInt("is_hide"),
                                systemObject.optInt("is_delete"), systemObject.optInt("user_id"),
                                systemObject.optString("nickname"), systemObject.optString("avatar")));
                    }
                }
                notifyAdaterL();
            }

            @Override
            public void onFailure(String err_msg) {
            }
        });
    }

    private void notifyAdaterL() {
        if (couponList.size() > 0) {
                mQuanBottomDialog.notifyAdapter();
        } else {
            mQuanBottomDialog.setShowOrHideView();
        }
    }

    private void gainQuan(int coupons_id) {
        WebBase.takeCoupon(String.valueOf(coupons_id), new JSONHandler(true, this, "正在领取...") {
            @Override
            public void onSuccess(JSONObject obj) {
                SendBrodacast.send(getBaseContext(), Constants.UP_DATA_COUPONS);
                if (obj.optString("status").equals("ok")) {
                    showToast("领取成功");
                    mQuanBottomDialog.dismissI();
                    getCoupons();
                    ShowActivity.showAddFansActivity(FansActivity.this, group_bean);
                }
            }

            @Override
            public void onFailure(String err_msg) {
            }
        });
    }

    @Override
    public void gainQuanI(int coupon_id) {
        gainQuan(coupon_id);
    }
}
