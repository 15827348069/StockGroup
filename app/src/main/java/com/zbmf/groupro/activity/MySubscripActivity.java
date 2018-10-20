package com.zbmf.groupro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.ViewPageFragmentadapter;
import com.zbmf.groupro.fragment.TieFansFragment;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.MessageType;
//import com.zbmf.StockGroup.fragment.TieFansFragment;
//import com.zbmf.StockGroup.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.groupro.R.id.viewpager;

/**
 * 我的订阅 -- 铁粉、视频。
 */
public class MySubscripActivity extends AppCompatActivity implements OnTabSelectListener {

    private SlidingTabLayout mTab;
    private ViewPager mViewpager;
    private List<Fragment> mList;
    private List<String> title_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initFragment();
        setupView();

        Intent answer_intent=new Intent(Constants.USER_RED_NEW_MESSAGE);
        answer_intent.putExtra("type", Constants.PUSH_BOX_TYPE);
        this.sendBroadcast(answer_intent);
    }

    private void initFragment() {
        mList = new ArrayList<>();
        title_list = new ArrayList<>();
        title_list.add("铁粉");
//        title_list.add("视频");
        TieFansFragment unusedFragment = TieFansFragment.newInstance();
//        VideoFragment usedFragment = VideoFragment.newInstance();
        mList.add(unusedFragment);
//        mList.add(usedFragment);
    }

    private void setupView() {
        TextView tv_title = ((TextView) findViewById(R.id.group_title_name));
        tv_title.setText("我的订阅");
        tv_title.setVisibility(View.VISIBLE);
        mTab = (SlidingTabLayout) findViewById(R.id.tablayout);
        mViewpager = (ViewPager) findViewById(viewpager);
        mTab.setVisibility(View.GONE);
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
