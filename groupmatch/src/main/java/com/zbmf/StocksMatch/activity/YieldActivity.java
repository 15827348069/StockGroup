package com.zbmf.StocksMatch.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.YieldPageAdapter;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.fragment.YieldFragment;
import com.zbmf.StocksMatch.utils.UiCommon;

import java.util.ArrayList;
import java.util.List;

/**
 * 榜单
 */
public class YieldActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private TextView tv_title,tv_total,tv_week,tv_day;
    private ImageView iv_underline;
    private ViewPager viewpager;
    private YieldFragment dayFrag,weekFrag,totalFrag;
    private MatchBean matchBean;
    private List<Fragment> list ;
    private YieldPageAdapter adapter;
    private int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;

        getData();
        initFragment();
        setupView();
        init();
    }

    private void init() {
        tv_day.setTextAppearance(this, R.style.TextAppear_Theme_main_Size14);
        tv_week.setTextAppearance(this,R.style.TextAppear_Theme_c6_Size14);
        tv_total.setTextAppearance(this,R.style.TextAppear_Theme_c6_Size14);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x);

        int offset = (width / 3 - bitmap.getWidth()) / 2;

        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        iv_underline.setImageMatrix(matrix);
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            matchBean = (MatchBean) bundle.getSerializable("matchbean");
        }
    }

    private void initFragment() {
        list = new ArrayList<Fragment>();
        dayFrag = new YieldFragment();
        weekFrag = new YieldFragment();
        totalFrag = new YieldFragment();
        dayFrag = YieldFragment.newInstance(matchBean);
        weekFrag = YieldFragment.newInstance(matchBean);
        totalFrag = YieldFragment.newInstance(matchBean);
        dayFrag.setOrder("1");
        weekFrag.setOrder("7");
        totalFrag.setOrder("0");
        list.add(dayFrag);
        list.add(weekFrag);
        list.add(totalFrag);
    }

    private void setupView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(UiCommon.INSTANCE.subTitle(matchBean.getTitle()));
        tv_total = (TextView)findViewById(R.id.tv_total);
        tv_week = (TextView)findViewById(R.id.tv_week);
        tv_day = (TextView)findViewById(R.id.tv_day);
        iv_underline = (ImageView)findViewById(R.id.iv_underline);
        viewpager = (ViewPager)findViewById(R.id.viewpager);
        findViewById(R.id.iv_back).setOnClickListener(this);
        tv_day.setOnClickListener(this);
        tv_week.setOnClickListener(this);
        tv_total.setOnClickListener(this);
        adapter = new YieldPageAdapter(getSupportFragmentManager(),list);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(this);
        viewpager.setOffscreenPageLimit(2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_total:
                viewpager.setCurrentItem(2);
                break;
            case R.id.tv_week:
                viewpager.setCurrentItem(1);
                break;
            case R.id.tv_day:
                viewpager.setCurrentItem(0);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private int lastPosition;
    @Override
    public void onPageSelected(int position) {
        TranslateAnimation animation = new TranslateAnimation(lastPosition
                * width / 3, position * width / 3, 0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);

        iv_underline.startAnimation(animation);
        lastPosition = position;

        tv_week.setTextAppearance(this, R.style.TextAppear_Theme_c6_Size14);
        tv_day.setTextAppearance(this, R.style.TextAppear_Theme_c6_Size14);
        tv_total.setTextAppearance(this,R.style.TextAppear_Theme_c6_Size14);
        switch (position){
            case 0:
                tv_day.setTextAppearance(this, R.style.TextAppear_Theme_main_Size14);
                break;
            case 1:
                tv_week.setTextAppearance(this, R.style.TextAppear_Theme_main_Size14);
                break;
            case 2:
                tv_total.setTextAppearance(this, R.style.TextAppear_Theme_main_Size14);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        UiCommon.INSTANCE.setCurrActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UiCommon.INSTANCE.doOnActivityDestroy(this);
    }
}
