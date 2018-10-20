package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.GroupCouponsAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.CouponsBean;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SendBrodacast;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.PullToRefreshBase;
import com.zbmf.StockGroup.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/9/5.
 */

public class GroupConponsActivity extends BaseActivity implements View.OnClickListener {
    private List<CouponsBean>couponsList;
    private Group group;
    private String groupId;
    private PullToRefreshListView couponsListView;
    private GroupCouponsAdapter adapter;
    private boolean isRush=false;
    private LinearLayout ll_none;
    private TextView no_message_text;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_coupons_layout;
    }

    @Override
    public void initView() {
        initTitle("优惠券");
        couponsListView=getView(R.id.coupons_list);
        getView(R.id.btn_one_key).setOnClickListener(this);
        ll_none=getView(R.id.ll_none);
        no_message_text=getView(R.id.no_message_text);
        no_message_text.setText(getString(R.string.no_coupons_msg));
    }

    @Override
    public void initData() {
        group= (Group) getIntent().getSerializableExtra(IntentKey.GROUP);
        groupId=group.getId();
        couponsList=new ArrayList<>();
        adapter=new GroupCouponsAdapter(this,couponsList);
        couponsListView.setAdapter(adapter);
        getCoupons();
    }

    @Override
    public void addListener() {
        couponsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                isRush=true;
                getCoupons();
            }
        });
        couponsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                takeCoupon(position-1);
            }
        });
    }
    private void getCoupons(){
        WebBase.getGroupCoupons(groupId, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if(isRush){
                    isRush=false;
                    couponsList.clear();
                }
                if(!obj.isNull("groups")){
                    couponsList.addAll(JSONParse.getCouponsList(obj.optJSONArray("groups")));
                }
                if(!obj.isNull("systems")){
                    couponsList.addAll(JSONParse.getCouponsList(obj.optJSONArray("systems")));
                }
                if(couponsList.size()==0){
                    getView(R.id.btn_one_key).setVisibility(View.GONE);
                    if(ll_none.getVisibility()==View.GONE){
                        ll_none.setVisibility(View.VISIBLE);
                    }
                }else{
                    getView(R.id.btn_one_key).setVisibility(View.VISIBLE);
                    if(ll_none.getVisibility()==View.VISIBLE){
                        ll_none.setVisibility(View.GONE);
                    }
                }
                couponsListView.onRefreshComplete();
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(String err_msg) {
                couponsListView.onRefreshComplete();
            }
        });
    }
    public void takeCoupon(final int positon){
        CouponsBean couponsBean=couponsList.get(positon);
        if(couponsBean.is_take()){
            ShowActivity.showAddFansActivity(GroupConponsActivity.this,group);
            return;
        }
        int coupons_id=couponsList.get(positon).getCoupon_id();
        WebBase.takeCoupon(String.valueOf(coupons_id), new JSONHandler(true,this,"正在领取...") {
            @Override
            public void onSuccess(JSONObject obj) {
                SendBrodacast.send(getBaseContext(), Constants.UP_DATA_COUPONS);
                if(obj.optInt("is_taken")==0){
                    showToast("领取成功");
                    couponsList.get(positon).setIs_take(false);
                }else{
                    couponsList.get(positon).setIs_take(true);
                    couponsList.get(positon).setAmin_show(true);
                }
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(),err_msg,Toast.LENGTH_SHORT).show();
                couponsList.get(positon).setIs_take(false);
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_one_key:
                WebBase.takeAllCoupon(groupId, new JSONHandler() {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        showToast("领取成功");
                        ShowActivity.showAddFansActivity(GroupConponsActivity.this,group);
                    }

                    @Override
                    public void onFailure(String err_msg) {
                        showToast(err_msg);
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode,data);
        finish();
    }
}
