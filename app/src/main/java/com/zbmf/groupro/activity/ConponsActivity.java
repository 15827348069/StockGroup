package com.zbmf.groupro.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.ViewPageFragmentadapter;
import com.zbmf.groupro.beans.CouponsBean;
import com.zbmf.groupro.fragment.CanUseConponsFragment;
import com.zbmf.groupro.fragment.LoseTimeConponsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.groupro.R.id.viewpager;

public class ConponsActivity extends AppCompatActivity implements OnTabSelectListener {
    private SlidingTabLayout mTab;
    private ViewPager mViewpager;
    private List<Fragment> mList;
    private List<String> title_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        initFragment();
        setupView();

    }

    private void initFragment() {
        mList = new ArrayList<>();
        title_list = new ArrayList<>();
        title_list.add("未使用");
        title_list.add("已过期");
        mList.add(CanUseConponsFragment.newInstance((CouponsBean) getIntent().getSerializableExtra("CouponsBean")));
        mList.add(LoseTimeConponsFragment.newInstance());
    }

    private void setupView() {
        TextView tv_title = ((TextView) findViewById(R.id.group_title_name));
        tv_title.setText("优惠券");
        tv_title.setVisibility(View.VISIBLE);
        mTab = (SlidingTabLayout) findViewById(R.id.tablayout);
        mViewpager = (ViewPager) findViewById(viewpager);

        ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getSupportFragmentManager(), title_list, mList);
        mViewpager.setAdapter(adapter);
        mTab.setViewPager(mViewpager);
        mTab.setOnTabSelectListener(this);
        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.to_get_conpons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"领券中心敬请期待",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }
}
