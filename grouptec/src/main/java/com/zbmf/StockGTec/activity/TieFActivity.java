package com.zbmf.StockGTec.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGTec.fragment.ExpireTieFragment;
import com.zbmf.StockGTec.fragment.TieFragment;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.StockGTec.R.id.viewpager;

public class TieFActivity extends ExActivity implements OnTabSelectListener {
    private SlidingTabLayout mTab;
    private ViewPager mViewpager;
    private List<Fragment> mList;
    private List<String> title_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tie_f);

        initFragment();setupView();
    }

    private void initFragment() {
        mList = new ArrayList<>();
        title_list = new ArrayList<>();
        title_list.add("即将到期");
        title_list.add("全部");
        TieFragment tieFragment = TieFragment.newInstance();
        ExpireTieFragment expireTieFragment = ExpireTieFragment.newInstance();
        mList.add(expireTieFragment);
        mList.add(tieFragment);
    }

    private void setupView() {
        TextView tv_title = ((TextView) findViewById(R.id.group_title_name));
        tv_title.setText("铁粉管理");
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
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }
}
