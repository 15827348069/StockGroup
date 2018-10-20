package com.zbmf.StockGroup.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.StockMatchAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Rank;
import com.zbmf.StockGroup.beans.Round;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ZbmfSelectStockMatch extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        StockMatchAdapter.UserDetail {
    private PullToRefreshScrollView mStock_match_scrollview;
    private TextView mTv2, firstAddTv, mTv1, open_tip;
    private StockMatchAdapter mStockMatchAdapter;
    private List<Round> mRoundList;
    private List<String> mSpinnerList;
    private List<Rank> rankList;
    private String selectWeek = "0";
    private ArrayAdapter<String> mAdapter;
    private TextView mWeekTime;
    private Dialog mDialog;
    private RelativeLayout shade_view;
    private ListViewForScrollView mStock_match_list;
    private Button mOpenVIPBtn;
    private boolean isFirst;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_zbmf_select_stock_match;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.select_stock_match_zbmf));
        Spinner spinner = getView(R.id.spinner);
        mTv1 = getView(R.id.tv1);
        mTv2 = getView(R.id.tv2);
        firstAddTv = getView(R.id.firstAddTv);

        mRoundList = new ArrayList<>();
        mSpinnerList = new ArrayList<>();
        rankList = new ArrayList<>();
        mStock_match_scrollview = getView(R.id.stock_match_scrollview);
        mWeekTime = getView(R.id.weekTime);
        shade_view = getView(R.id.shade_view);
        mOpenVIPBtn = getView(R.id.openVIPBtn);
        open_tip = getView(R.id.open_tip);
        ImageView t4Img = getView(R.id.t4Img);
        mStock_match_list = getView(R.id.stock_match_list);
        t4Img.setImageResource(R.drawable.t4);
        mStock_match_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, mSpinnerList);
        mAdapter.setDropDownViewResource(R.layout.my_drop_down_item);
        spinner.setAdapter(mAdapter);
        spinner.setOnItemSelectedListener(this);
        mStockMatchAdapter = new StockMatchAdapter(this, this);

        mStockMatchAdapter.setUserDetail(this);
        mStock_match_list.setAdapter(mStockMatchAdapter);

    }

    @Override
    public void initData() {
        isFirst = true;
        getSelectStockYieldList(isFirst);//选股大赛周期列表
        getWeekRankList(selectWeek);//选股周排行
    }

    @Override
    public void addListener() {
        mTv1.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        mOpenVIPBtn.setOnClickListener(this);
        mStock_match_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mRoundList.clear();
                rankList.clear();
                isFirst = true;
                getSelectStockYieldList(isFirst);
                getWeekRankList(selectWeek);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:
                //跳大赛介绍H5
                ShowActivity.showWebViewActivity(this, HtmlUrl.FIND_STOCK1);
                break;
            case R.id.tv2:
                //如果不是老师，提示：经认证后才可选股，认证请联系:4000607878
                //如果是老师点击进入  UserDetailActivity 页面
                //判断是否为老师
                // 时间段内才可以选股
                int isSuperVip = SettingDefaultsManager.getInstance().getIsSuperVip();
                String userId = SettingDefaultsManager.getInstance().UserId();//获取UserID
                if (isSuperVip == 0) {
                    //不是老师
                    showDialog();
                } else if (isSuperVip == 1) {
                    ShowActivity.showUserDetailActivity(this, userId, IntentKey.MINE_TYPE);
                }
                break;
            case R.id.openVIPBtn:
                ShowActivity.skipVIPActivity(this);
                break;
        }
    }

    @Override
    public void skipUserDetail(int userID, int position) {
        //跳转选中的用户的选股的详情
//        if (shade_view.getVisibility()==View.VISIBLE&&position>2){
        ShowActivity.showUserDetailActivity(this, String.valueOf(userID), IntentKey.OTHER_TYPE);
//        }else if (shade_view.getVisibility()==View.GONE){
//            ShowActivity.showUserDetailActivity(this, String.valueOf(userID), IntentKey.OTHER_TYPE);
//        }
    }

    //周数
    private void getSelectStockYieldList(boolean is_first) {
        WebBase.getSelectStockYieldList(new JSONHandler(is_first, this, getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                String status = obj.optString("status");
                if (status.equals("ok")) {
                    JSONArray rounds = obj.optJSONArray("rounds");
                    int length = rounds.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject = rounds.optJSONObject(i);
                        int round_id = jsonObject.optInt("round_id");
                        int round = jsonObject.optInt("round");
                        String start_apply = jsonObject.optString("start_apply");
                        String end_apply = jsonObject.optString("end_apply");
                        String start_at = jsonObject.optString("start_at");
                        String end_at = jsonObject.optString("end_at");
                        Round round1 = new Round();
                        round1.setRound_id(round_id);
                        round1.setRound("第" + round + "周");
                        round1.setEnd_apply(end_apply);
                        round1.setEnd_at(end_at);
                        round1.setStart_apply(start_apply);
                        round1.setStart_at(start_at);
                        mRoundList.add(round1);
                        mSpinnerList.add(round1.getRound());
                    }
                    mAdapter.notifyDataSetChanged();
                    mWeekTime.setText(String.format(mRoundList.get(mRoundList.size() - 1).getStart_at()
                            + "~%s", mRoundList.get(mRoundList.size() - 1).getEnd_at()));
                }
                if (isFirst) {
                    isFirst = false;
                }
                mStock_match_scrollview.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
                if (isFirst) {
                    isFirst = false;
                }
                mStock_match_scrollview.onRefreshComplete();
            }
        });
    }

    //选股大赛周排行
    private void getWeekRankList(String round_id) {
        WebBase.getWeekRankList(round_id, new JSONHandler() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")) {
                    JSONArray ranksArray = obj.optJSONArray("ranks");
                    int length = ranksArray.length();
                    rankList.clear();
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject = ranksArray.optJSONObject(i);
                        rankList.add(new Rank(jsonObject.optInt("week_rank"), jsonObject.optString("symbol"),
                                jsonObject.optString("stock_name"), jsonObject.optDouble("week_yield")
                                , jsonObject.optDouble("week_score"), jsonObject.optInt("user_id"),
                                jsonObject.optString("nickname"), jsonObject.optString("avatar"),
                                jsonObject.optString("reason"), jsonObject.optDouble("high_yield")));
                    }

                    if (SettingDefaultsManager.getInstance().getIsVip() == 0 ||
                            TextUtils.isEmpty(SettingDefaultsManager.getInstance().authToken())) {
                        List<Rank> rankDataList = new ArrayList<>();
                        for (int i = 0; i < rankList.size(); i++) {
                            if (i > 2) {
                                rankDataList.add(rankList.get(i));
                            }
                        }
                        mStockMatchAdapter.setList(rankDataList);
                        firstAddTv.setText(rankList.get(0).getWeek_yield() >= 0 ? "第一名累计涨幅:+" +
                                (DoubleFromat.getDouble(rankList.get(0).getWeek_yield() * 100, 2)) + "%" :
                                "第一名累计涨幅:" + (DoubleFromat.getDouble(rankList.get(0).getWeek_yield() * 100, 2)) + "%");
                        shade_view.setVisibility(View.VISIBLE);
                    } else {
                        shade_view.setVisibility(View.GONE);
                        mStockMatchAdapter.setList(rankList);
                    }
                }
                ListAdapter listAdapter = mStock_match_list.getAdapter();
                if (isFirst) {
                    isFirst = false;
                }
            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
                if (isFirst) {
                    isFirst = false;
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = mAdapter.getItem(position);
        for (int i = 0; i < mRoundList.size(); i++) {
            String round = mRoundList.get(i).getRound();
            if (round.equals(item)) {
                selectWeek = String.valueOf(mRoundList.get(i).getRound_id());
                mWeekTime.setText(String.format(mRoundList.get(i).getStart_at() + "~%s", mRoundList.get(i).getEnd_at()));
            }
        }
        if (!isFirst) {
            getWeekRankList(selectWeek);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void showDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(this, R.style.dialogTheme);
        }
        View dialogView = LayoutInflater.from(this).inflate(R.layout.verify_teacher_dialog, null);
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
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000607878"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                mDialog.dismiss();
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
}
