package com.zbmf.StocksMatch.activity;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zbmf.StocksMatch.MainActivity;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.Adverts;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.listener.IlaunchView;
import com.zbmf.StocksMatch.listener.IvClickListener;
import com.zbmf.StocksMatch.presenter.LaunchPresenter;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.ShowActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuhao
 * on 2017/11/24.
 */

public class LaunchActivity extends BaseActivity<LaunchPresenter> implements IlaunchView, IvClickListener {
    @BindView(R.id.lunchViewPager)
    ViewPager lunchViewPager;
    private List<ImageView> views;
    private List<Adverts> mAdverts;

    @Override
    protected int getLayout() {
        return R.layout.activity_lanch_layout;
    }

    @Override
    protected String initTitle() {
        return null;
    }

    @Override
    protected void initData(Bundle bundle) {
        // //是否全屏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Window window = getWindow();
        //使得布局延伸到状态栏和导航栏区域
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //透明状态栏/导航栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }



        MyActivityManager.getMyActivityManager().pushAct(this);
        iCheckPermission();
    }
    @OnClick(R.id.skip)
    public void onViewClicked(View view){
        if (view.getId()==R.id.skip){
            b=true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            LaunchActivity.this.finish();
        }
    }

    private void initHandle() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {//直接跳转首页
//                if (SharedpreferencesUtil.getInstance().getString(SharedKey.AUTH_TOKEN, null) != null) {
                if (!b){
                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
//                } else {
//                    Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }
            }
        }, 2000);
    }

    //校验当前APP的版本
    @Override
    protected LaunchPresenter initPresent() {
        return new LaunchPresenter();
    }

    @Override
    public void toLogin() {
            initHandle();
    }

    @Override
    public void rushLunchImage(List<Adverts> imgList) {
        if (views == null) {
            views = new ArrayList<ImageView>();
        }
        if (views.size() > 0) {
            views.clear();
        }
        if (imgList != null && imgList.size() > 0) {
            this.mAdverts = imgList;
            for (int i = 0; i < imgList.size(); i++) {
                ImageView imageView = new ImageView(this);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(this).load(imgList.get(i).getImg_url()).into(imageView);
                views.add(imageView);
            }
        }
        if (imgList != null && imgList.size() > 0) {
            lunchViewPager.setVisibility(View.VISIBLE);
            LunchAdapter lunchAdapter = new LunchAdapter(views, lunchViewPager);
            lunchViewPager.setAdapter(lunchAdapter);
            lunchAdapter.setIvClickListener(this);
        } else {
            lunchViewPager.setVisibility(View.GONE);
        }
        initHandle();
    }

    @Override
    public void rushLunchImageErr(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            initHandle();
            if (ShowActivity.checkLoginStatus(this, msg)) {
                showToast(getString(R.string.login_err_tip));
            }
        }
    }

    @Override
    public void refreshMatchDesc(MatchDescBean.Result result) {
        if(result!=null){
            ShowActivity.showMatch4(this,result);
            finish();
        }
    }

    @Override
    public void refreshMatchDescErr(String msg) {
    }

    @Override
    public void onRefreshComplete() {
    }

    private void iCheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Constans.permissions.length > 0) {
                for (int i = 0; i < Constans.permissions.length; i++) {
                    if (checkSelfPermission(Constans.permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        //需要授权
                        ActivityCompat.requestPermissions(this, Constans.permissions,
                                IntentKey.REQUEST_PERMISSION_CODE);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case IntentKey.REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
            break;
        }
    }

    private boolean b;

    @Override
    public void skipToView(int position) {
        if (mAdverts != null && mAdverts.size() > 0) {
            b = true;
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.ADS,mAdverts.get(position));
            ShowActivity.showActivity(LaunchActivity.this,bundle,MainActivity.class);
            this.finish();
        }
    }

    private class LunchAdapter extends PagerAdapter {
        private List<ImageView> img;
        private ViewPager mViewPager;
        private IvClickListener mIvClickListener;

        void setIvClickListener(IvClickListener ivClickListener) {
            this.mIvClickListener = ivClickListener;
        }

        LunchAdapter(List<ImageView> img, ViewPager viewPager) {
            this.img = img;
            this.mViewPager = viewPager;
        }

        @Override
        public int getCount() {
            return img.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            // 把position对应位置的ImageView添加到ViewPager中
            ImageView iv = img.get(position);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIvClickListener != null) {
                        mIvClickListener.skipToView(position);
                    }
                }
            });
            mViewPager.addView(iv);
            // 把当前添加ImageView返回回去.
            return iv;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            // 把ImageView从ViewPager中移除掉
            mViewPager.removeView(img.get(position));
        }
    }
}
