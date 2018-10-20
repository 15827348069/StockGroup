package com.zbmf.groupro.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.MfbAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.MFBean;
import com.zbmf.groupro.utils.ActivityUtil;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.ListViewForScrollView;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/2/20.
 */

public class MfbActivity extends Activity implements View.OnClickListener {
    private TextView my_mfb_message,group_title_name,no_message_text;
    private ListViewForScrollView mfb_list;
    private MfbAdapter adapter;
    private List<MFBean>info;
    private PullToRefreshScrollView mfb_scrollview;
    private int page,pages;
    private LinearLayout ll_none;
    DecimalFormat df=new DecimalFormat("");
    DecimalFormat double_df =new DecimalFormat("######0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_mfb_layout);
        ActivityUtil.getInstance().putActivity(ActivityUtil.MfbActivitys,this);
         init();
        initData();
    }

    public void initData() {
        page=1;
        pages=0;
        info.clear();
        my_mfb_message.setText(SettingDefaultsManager.getInstance().getPays());
        getmfbMessage(true);
    }
    public void updaData() {
        page=1;
        pages=0;
        info.clear();
        my_mfb_message.setText(SettingDefaultsManager.getInstance().getPays());
        getmfbMessage(false);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    public void init(){
        mfb_list= (ListViewForScrollView) findViewById(R.id.mfb_list);
        mfb_scrollview= (PullToRefreshScrollView) findViewById(R.id.mfb_scrollview);
        my_mfb_message= (TextView) findViewById(R.id.my_mfb_message);
        group_title_name= (TextView) findViewById(R.id.group_title_name);
        ll_none= (LinearLayout) findViewById(R.id.ll_none);
        no_message_text= (TextView) findViewById(R.id.no_message_text);
        findViewById(R.id.group_title_right_button).setVisibility(View.VISIBLE);
        findViewById(R.id.group_title_right_button).setOnClickListener(this);
        findViewById(R.id.group_title_return).setOnClickListener(this);
        group_title_name.setText("魔方宝");
        info=new ArrayList<>();
        adapter=new MfbAdapter(this,info);
        mfb_list.setAdapter(adapter);
        mfb_scrollview.setMode(PullToRefreshBase.Mode.BOTH);
        mfb_scrollview.getLoadingLayoutProxy().setPullLabel("加载更多数据");
        mfb_scrollview.getLoadingLayoutProxy().setRefreshingLabel("正在加载新数据...");
        mfb_scrollview.getLoadingLayoutProxy().setReleaseLabel("松开加载数据");
        mfb_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                updaData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if(page==pages){
                    Toast.makeText(MfbActivity.this,"已加载全部数据",Toast.LENGTH_SHORT).show();
                    mfb_scrollview.onRefreshComplete();
                }else{
                    page+=1;
                    getmfbMessage(false);
                }
            }
        });
    }
    public String getDoubleormat(double vealue){
        if(double_df.format(vealue).contains(".00")){
            double ve=Double.valueOf(double_df.format(vealue));
            return df.format(ve);
        }else{
            return double_df.format(vealue);
        }
    }
    private void getmfbMessage(boolean is_show){
        WebBase.payLogs(page, new JSONHandler(is_show,MfbActivity.this,"正在加载...") {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                JSONArray pay_logs=result.optJSONArray("pay_logs");
                int size=pay_logs.length();
                page=result.optInt("page");
                pages=result.optInt("pages");
                mfb_scrollview.onRefreshComplete();
                if(page==1&&size==0&&info.size()==0){
                    if(mfb_list.getVisibility()==View.VISIBLE){
                        mfb_list.setVisibility(View.GONE);
                    }
                    if(ll_none.getVisibility()==View.GONE){
                        ll_none.setVisibility(View.VISIBLE);
                    }
                    no_message_text.setText("还没有任何明细哦");
                }else{
                    if(mfb_list.getVisibility()==View.GONE){
                        mfb_list.setVisibility(View.VISIBLE);
                    }
                    if(ll_none.getVisibility()==View.VISIBLE){
                        ll_none.setVisibility(View.GONE);
                    }
                    for(int i=0;i<size;i++){
                        JSONObject pay=pay_logs.optJSONObject(i);
                        MFBean pb=new MFBean();
                        pb.setDate(pay.optString("created_at"));
                        double pay_count=pay.optDouble("pay");
                        if(pay_count>0){
                            pb.setCount("+"+getDoubleormat(pay_count));
                        }else{
                            pb.setCount(""+getDoubleormat(pay_count));
                        }
                        pb.setTitle(pay.optString("notes"));
                        info.add(pb);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                mfb_scrollview.onRefreshComplete();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.group_title_return:
                finish();
                break;
            case R.id.group_title_right_button:
                ShowActivity.showPayDetailActivity(this);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.getInstance().removeActivity(ActivityUtil.MfbActivitys);
    }
}
