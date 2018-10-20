package com.zbmf.StockGTec.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.fragment.MfbFragment;
import com.zbmf.StockGTec.utils.DateUtil;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MfbActivity extends ExActivity implements View.OnClickListener {
    private ViewPager viewpager;
    private List<Fragment> mList;
    private List<String> title_list;
    private List<String> date_list;
    private PagerAdapter adapter;
    private SlidingTabLayout tablayout;
    private TextView group_title_name;
    DecimalFormat df   = new DecimalFormat("######0.00");
    private TextView my_mfb_text,can_use_mfb,tv_frozen_capital;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfb);
        initFragment();
        init();
    }
    public void initFragment(){
        title_list=new ArrayList<>();
        mList=new ArrayList<>();
        date_list=new ArrayList<>();
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMM");
        for(int i=0;i>-7;i--){
            Date date= DateUtil.afterMonth(i);
            int month=date.getMonth()+1;
            if(i==0){
                title_list.add("本月");
                date_list.add(format.format(date));
            }else if(i==-6){
                title_list.add("全部");
                date_list.add(null);
            }else{
                title_list.add(month+"月");
                date_list.add(format.format(date));
            }

        }
        for(String str:date_list){
            mList.add(MfbFragment.newIntence(str));
        }
    }

    private void init() {
        group_title_name= (TextView) findViewById(R.id.group_title_name);
        group_title_name.setVisibility(View.VISIBLE);
        group_title_name.setText("财富中心");
        my_mfb_text= (TextView) findViewById(R.id.my_mfb_text);

        can_use_mfb= (TextView) findViewById(R.id.can_use_mfb);
        can_use_mfb.setText(SettingDefaultsManager.getInstance().getPays());
        tv_frozen_capital= (TextView) findViewById(R.id.tv_frozen_capital);

        findViewById(R.id.group_title_return).setOnClickListener(this);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tablayout = (SlidingTabLayout)findViewById(R.id.tablayout);
        adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        viewpager.setAdapter(adapter);
        tablayout.setViewPager(viewpager);
        viewpager.setOffscreenPageLimit(title_list.size());
        getWolle();
    }
    public void getWolle() {
        WebBase.getWalle(new JSONHandler(MfbActivity.this) {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject pays = obj.optJSONObject("pay");
                JSONObject point = obj.optJSONObject("point");
                double total=pays.optDouble("total");
                double unfrozen=pays.optDouble("unfrozen");
                my_mfb_text.setText(df.format(total));
                tv_frozen_capital.setText(df.format(total-unfrozen));
                SettingDefaultsManager.getInstance().setPays(pays.optString("unfrozen"));
                SettingDefaultsManager.getInstance().setPoint(point.optLong("unfrozen"));
            }

            @Override
            public void onFailure(String err_msg) {
                Log.e(">>>>", "错误" + err_msg);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_title_return:
                finish();
                break;
        }
    }
}
