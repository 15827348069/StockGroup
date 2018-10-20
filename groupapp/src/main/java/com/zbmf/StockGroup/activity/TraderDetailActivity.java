package com.zbmf.StockGroup.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.TraderDeailHistoryAdapter;
import com.zbmf.StockGroup.adapter.TraderHolderAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.DealSys;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.TraderYield;
import com.zbmf.StockGroup.beans.Traders;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.CustomMyCProgress;
import com.zbmf.StockGroup.view.ListViewForScrollView;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshScrollView;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/11/9.
 */

public class TraderDetailActivity extends BaseActivity implements View.OnClickListener {
    private Traders traders;
    private TraderYield traderYield;
    private TraderHolderAdapter holdadapter;
    private TraderDeailHistoryAdapter adapter;
    private boolean is_track;
    private List<DealSys>dealSysList,holdList;
    private ListViewForScrollView trader_deal_history_list,trader_deal_list;
    private TextView deal_days,deal_total,deal_success
            ,total_yield,win_index,index_yield
            ,total_money,hold_num,position;
    private RoundedCornerImageView imv_avatar;
    private TextView name,desc,date,tv_commit,tv_commit_look,tv_no_hold,tv_commit_look_hold,tv_commit_content;
    private PullToRefreshScrollView trader_scrollview;
    private LinearLayout hold_layout,layout_commit_look_hold,layout_commit_look_history;
    private MatchInfo matchInfo;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_trader_detail;
    }

    @Override
    public void initView() {
        initTitle("操盘高手");
        trader_deal_history_list=getView(R.id.trader_deal_history_list);
        trader_deal_list=getView(R.id.trader_deal_list);
        trader_scrollview=getView(R.id.trader_scrollview);
        trader_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        imv_avatar=getView(R.id.imv_avatar);
        name=getView(R.id.tv_name);
        date=getView(R.id.tv_date);
        desc=getView(R.id.tv_desc);
        tv_no_hold=getView(R.id.tv_no_hold);
        tv_commit_look_hold=getView(R.id.tv_commit_look_hold);
        tv_commit_content=getView(R.id.tv_commit_content);
        hold_layout=getView(R.id.hold_layout);
        layout_commit_look_hold=getView(R.id.layout_commit_look_hold);
        deal_days=getView(R.id.tv_deal_days);
        deal_total=getView(R.id.tv_deal_total);
        deal_success=getView(R.id.tv_deal_success);
        total_yield=getView(R.id.tv_total_yield);
        win_index=getView(R.id.tv_win_index);
        index_yield=getView(R.id.tv_index_yield);
        total_money=getView(R.id.tv_total_money);
        hold_num=getView(R.id.tv_hold_num);
        position=getView(R.id.tv_position);

        tv_commit=getView(R.id.tv_commit);
        tv_commit_look=getView(R.id.tv_commit_look);

        layout_commit_look_history=getView(R.id.layout_commit_look_history);
    }

    @Override
    public void initData() {
        dealSysList=new ArrayList<>();
        holdList=new ArrayList<>();
        holdadapter=new TraderHolderAdapter(this);
        adapter=new TraderDeailHistoryAdapter(this);
        holdadapter.setList(holdList);
        adapter.setList(dealSysList);
        trader_deal_list.setAdapter(holdadapter);
        trader_deal_history_list.setAdapter(adapter);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            traders= (Traders) bundle.getSerializable(IntentKey.TRADER);
            matchInfo= (MatchInfo) getIntent().getSerializableExtra(IntentKey.MATCH_BAEN);
            setUserMessage();
            getDetailMessage(true);
        }
    }

    @Override
    public void addListener() {
        trader_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getDetailMessage(false);
            }
        });
        trader_deal_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDealSys((DealSys) holdadapter.getItem(position));
            }
        });
        trader_deal_history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDealSys((DealSys) adapter.getItem(position));
            }
        });
        tv_commit.setOnClickListener(this);
        tv_commit_look.setOnClickListener(this);
        tv_commit_look_hold.setOnClickListener(this);
        getView(R.id.tv_commit_look_history).setOnClickListener(this);
    }

    /**
     * 查看股票详情
     * @param dealSys
     */
    private void showDealSys(DealSys dealSys){
        if(is_track){
            dealSys.setUser_img(traders.getAvatar());
            dealSys.setNickname(traders.getNickname());
            Bundle bundle=new Bundle();
            bundle.putSerializable(IntentKey.MATCH_BAEN,matchInfo);
            bundle.putSerializable(IntentKey.STOCKHOLDER,dealSys);
            ShowActivity.showActivity(this,bundle, SimulateOneStockCommitActivity.class);
        }else{
            showToast("订阅后可查看股票详情！");
        }
    }
    private void setUserMessage(){
        if(traders!=null){
            name.setText(traders.getNickname());
//            ViewFactory.imgCircleView(this,traders.getAvatar(),imv_avatar);
            ImageLoader.getInstance().displayImage(traders.getAvatar(),imv_avatar, ImageLoaderOptions.AvatarOptions());
            desc.setText(traders.getProfile()!=null?traders.getProfile():"");
            date.setText(traders.getJoint_at()!=null?(traders.getJoint_at()+"加入资本魔方"):"");
        }
    }
    private void setTraderYield(){
        if(traderYield!=null){
            deal_days.setText(traderYield.getDeal_days()+"");
            deal_total.setText(traderYield.getDeal_total()+"");
            deal_success.setText((traderYield.getDeal_success()>0?"+":"")+DoubleFromat.getStockDouble(traderYield.getDeal_success()*100,2)+"%");
            total_yield.setText((traderYield.getTotal_yideld()>0?"+":"")+DoubleFromat.getStockDouble(traderYield.getTotal_yideld()*100,2)+"%");
            win_index.setText((traderYield.getWin_index()>0?"+":"")+DoubleFromat.getStockDouble(traderYield.getWin_index()*100,2)+"%");
            index_yield.setText((traderYield.getIndex_yield()>0?"+":"")+DoubleFromat.getStockDouble(traderYield.getIndex_yield()*100,2)+"%");
            total_money.setText(DoubleFromat.getStockDouble(traderYield.getTotal_money(),2));
            hold_num.setText(traderYield.getHold_num()+"");
            position.setText((traderYield.getPosition()>0?"+":"")+DoubleFromat.getStockDouble(traderYield.getPosition()*100,2)+"%");
            index_yield.setTextColor(traderYield.getIndex_yield()>0?getResources().getColor(R.color.red):getResources().getColor(R.color.green));
            total_yield.setTextColor(traderYield.getTotal_yideld()>0?getResources().getColor(R.color.red):getResources().getColor(R.color.green));
        }
    }
    private void getDetailMessage(boolean is_show){
        WebBase.traderInfo(traders.getUser_id(), new JSONHandler(is_show,this,getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                if(trader_scrollview.isRefreshing()){
                    trader_scrollview.onRefreshComplete();
                }
                if(obj.has("trader")){
                    traders= JSONParse.getTraders(obj.optJSONObject("trader"));
                    setUserMessage();
                }
                if(obj.has("tracker")){
                    JSONObject tracker=obj.optJSONObject("tracker");
                    is_track=tracker.optInt("is_track")==1?true:false;
                    hold_layout.setVisibility(is_track?View.VISIBLE:View.GONE);
                    tv_commit_look.setVisibility(is_track?View.GONE:View.VISIBLE);
                    layout_commit_look_hold.setVisibility(is_track?View.GONE:View.VISIBLE);
                    tv_commit.setText(is_track?"续订":"订阅");
                    tv_commit_content.setText(is_track?"有效期至"+tracker.optString("expired_at"):"订阅后可查看操盘记录");
                    SettingDefaultsManager.getInstance().setPays(tracker.optString("mpays"));
                }
                if(is_track&&obj.has("holds")){
                    holdList.clear();
                    holdList.addAll(JSONParse.getDealSysList(obj.optJSONArray("holds")));
                    if(holdList.size()==0){
                        tv_no_hold.setVisibility(View.VISIBLE);
                        layout_commit_look_hold.setVisibility(View.GONE);
                    }else{
                        tv_no_hold.setVisibility(View.GONE);
                        layout_commit_look_hold.setVisibility(View.VISIBLE);
                        holdadapter.notifyDataSetChanged();
                    }
                }
                if(obj.has("yield")){
                    traderYield=JSONParse.getTraderYield(obj.optJSONObject("yield"));
                    setTraderYield();
                }
                if(obj.has("deals")){
                    dealSysList.clear();
                    dealSysList.addAll(JSONParse.getDealSysList(obj.optJSONArray("deals")));
                    adapter.notifyDataSetChanged();
                    if(is_track&&dealSysList.size()>0){
                        layout_commit_look_history.setVisibility(View.VISIBLE);
                    }else{
                        layout_commit_look_history.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(String err_msg) {
                if(trader_scrollview.isRefreshing()){
                    trader_scrollview.onRefreshComplete();
                }
                showToast(err_msg);
                if(err_msg.equals(Constants.NEED_LOGIN)){
                    ShowActivity.showActivity(TraderDetailActivity.this,LoginActivity.class);
                }
            }
        });
    }
    private Dialog dialog;
    private Dialog resetAccDialog() {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        final LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.commit_trader, null);
        final Button btn_confirm_reset = (Button) layout.findViewById(R.id.btn_confirm_reset);
        TextView tv_account_remain = (TextView) layout.findViewById(R.id.tv_account_remain);
        final TextView tv_finish_tip = (TextView) layout.findViewById(R.id.tv_finish_tip);
        TextView tv_paynum= (TextView) layout.findViewById(R.id.tv_paynum);
        final RelativeLayout rl_reset_state = (RelativeLayout) layout.findViewById(R.id.rl_reset_state);
        final Button btn_start_op = (Button) layout.findViewById(R.id.btn_start_op);
        final CustomMyCProgress mCustomProgress = (CustomMyCProgress) layout.findViewById(R.id.cc_progress);
        mCustomProgress.setdefaultTextStr("订阅中");
        tv_paynum.setText(DoubleFromat.getStockDouble(traders.getPrice_month(),0)+"魔方宝");
        tv_account_remain.setText("账户余额"+ SettingDefaultsManager.getInstance().getPays()+"魔方宝");
        final Double my_mfb=Double.valueOf(SettingDefaultsManager.getInstance().getPays());
        if(my_mfb<traders.getPrice_month()){
            btn_confirm_reset.setText("去充值");
        }
        layout.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_confirm_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(my_mfb<traders.getPrice_month()){
                    ShowActivity.showPayDetailActivity(TraderDetailActivity.this);
                    dialog.dismiss();
                }else{
                    WebBase.traders_buy(traders.getUser_id(),new JSONHandler() {
                        @Override
                        public void onSuccess(JSONObject obj) {
                            getDetailMessage(false);
                            rl_reset_state.setVisibility(View.VISIBLE);
                            ObjectAnimator anim = ObjectAnimator.ofFloat(mCustomProgress, "progress", 0f, 100f);
                            anim.setDuration(1000);
                            anim.start();
                            mCustomProgress.setStateListener(new CustomMyCProgress.StateListener() {
                                @Override
                                public void loadFinished() {
                                    tv_finish_tip.setVisibility(View.VISIBLE);
                                    btn_start_op.setEnabled(true);
                                }
                            });
                        }

                        @Override
                        public void onFailure(String err_msg) {
                            showToast(err_msg);
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        btn_start_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_commit_look:
            case R.id.tv_commit:
                if(dialog==null){
                    dialog=resetAccDialog();
                }
                dialog.show();
                break;
            case R.id.tv_commit_look_hold:
                Bundle bundle=new Bundle();
                bundle.putSerializable(IntentKey.MATCH_BAEN,matchInfo);
                bundle.putSerializable(IntentKey.TRADER,traders);
                bundle.putInt(IntentKey.FLAG,1);
                ShowActivity.showActivity(this,bundle,MatchHoldActivity.class);
                break;
            case R.id.tv_commit_look_history:
                if(is_track){
                    Bundle bundle2=new Bundle();
                    bundle2.putSerializable(IntentKey.MATCH_BAEN,matchInfo);
                    bundle2.putSerializable(IntentKey.TRADER,traders);
                    bundle2.putInt(IntentKey.FLAG,1);
                    ShowActivity.showActivity(this,bundle2,MatchLogListActivity.class);
                }else{
                    showToast("订阅后可查看完整记录");
                }
                break;
        }
    }
}
