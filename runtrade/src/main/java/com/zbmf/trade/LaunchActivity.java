package com.zbmf.trade;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.trade.adapter.ViewPageFragmentadapter;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.trade.Commons.FIRST_RUN;

public class LaunchActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager viewPager;
    private LinearLayout llPoint;
    private List<Fragment> fragmentList;
    private List<ImageView> pointImage;
    private ViewPageFragmentadapter pageFragmentadapter;
    private TextView tv_jump;
    private int page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setFullscreen(true);
        setContentView(R.layout.activity_launch);
        viewPager = (ViewPager) findViewById(R.id.img_vp);
        llPoint = (LinearLayout) findViewById(R.id.ll_point);
        tv_jump = (TextView) findViewById(R.id.tv_jump);
        tv_jump.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);

        initFragment();
        initPoint();
    }

    private void initFragment() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        fragmentList.add(LaunchFragment.newsIntance(R.mipmap.icon_one, "无息杠杆", "八戒资金 盈利翻倍"));
        fragmentList.add(LaunchFragment.newsIntance(R.mipmap.icon_two, "资金安全", "支付托管 三方监控"));
        fragmentList.add(LaunchFragment.newsIntance(R.mipmap.icon_three, "财务自由", "祝您实现 投资成就"));
        pageFragmentadapter = new ViewPageFragmentadapter(getSupportFragmentManager(), null, fragmentList);
        viewPager.setAdapter(pageFragmentadapter);

    }

    private void initPoint() {
        if (fragmentList != null) {
            pointImage = new ArrayList<>();
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(20, 20);
            mParams.setMargins(10, 0, 10, 0);//设置小圆点左右之间的间隔
            for (int i = 0; i < fragmentList.size(); i++) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(mParams);
                imageView.setImageResource(R.drawable.button_select);
                imageView.setSelected(i == 0);//默认启动时，选中第一个小圆点
                pointImage.add(imageView);
                llPoint.addView(imageView);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        page=position;
        if (pointImage != null && pointImage.size() > 0) {
            if(pointImage.size()-1==position){
                if(tv_jump.getVisibility()==View.GONE){
                    tv_jump.setVisibility(View.VISIBLE);
                    llPoint.setVisibility(View.GONE);
                }
            }else{
                if(tv_jump.getVisibility()==View.VISIBLE){
                    tv_jump.setVisibility(View.GONE);
                    llPoint.setVisibility(View.VISIBLE);
                }
            }
            for (ImageView imageView : pointImage) {
                imageView.setSelected(false);
            }
            pointImage.get(position).setSelected(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    private void setFullscreen(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |=  bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    @Override
    public void onClick(View view) {
//        Intent intent = new Intent();
//        intent.setClassName(getPackageName(), HomeActivity.class.getName());
//        startActivity(intent);
        SharedpreferencesUtil.getInstance().putBoolean(FIRST_RUN,false);
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(page!=0){
                viewPager.setCurrentItem(page-1,true);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
