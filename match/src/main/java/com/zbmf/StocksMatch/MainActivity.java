package com.zbmf.StocksMatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zbmf.StocksMatch.bean.Adverts;
import com.zbmf.StocksMatch.bean.MatchDescBean;
import com.zbmf.StocksMatch.common.IntentKey;
import com.zbmf.StocksMatch.fragment.DrillFragment;
import com.zbmf.StocksMatch.fragment.HomeFragment;
import com.zbmf.StocksMatch.fragment.MatchFragment;
import com.zbmf.StocksMatch.fragment.MineFragment;
import com.zbmf.StocksMatch.fragment.StockFragment;
import com.zbmf.StocksMatch.listener.IMainActivityCilck;
import com.zbmf.StocksMatch.listener.IlaunchView;
import com.zbmf.StocksMatch.util.MyActivityManager;
import com.zbmf.StocksMatch.util.ShowActivity;
import com.zbmf.worklibrary.adapter.ViewPageAdapter;
import com.zbmf.worklibrary.view.ViewPageNoScroll;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainActivity extends AppCompatActivity implements /*RadioGroup.OnCheckedChangeListener, */IMainActivityCilck, IlaunchView, View.OnClickListener {
    @BindView(R.id.vp_main)
    ViewPageNoScroll vpMain;
//        @BindView(R.id.rg_main_menu)
//        RadioGroup rgMainMenu;
    @BindView(R.id.rb_home)
    TextView rbHome;
    @BindView(R.id.rb_match)
    TextView rbMatch;
    @BindView(R.id.rb_drill)
    TextView rbDrill;
    @BindView(R.id.rb_stock)
    TextView rbStock;
    @BindView(R.id.rb_mine)
    TextView rbMine;
    private MatchFragment matchFragment;

    private Unbinder mUnBinder;
    private boolean isExited = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen(true);
        setContentView(R.layout.activity_main);
        MyActivityManager.getMyActivityManager().pushAct(this);
        Intent intent = getIntent();
        if (intent != null) {
            Adverts adverts = (Adverts) intent.getSerializableExtra(IntentKey.ADS);
            ShowActivity.showWebViewActivityJudge(this,adverts);
//            if (adverts != null) {
//                if (adverts.getJump_url().contains(Constans.MATCH_AD_TYPE)) {
//                    //如果是比赛类型的广告，则请求接口，判断是否已经参赛
//                    new LaunchPresenter().getMatchDesc();
//                } else {
//                    ShowActivity.showWebViewActivity(this, adverts.getJump_url(), "");
//                }
//            }
        }
        mUnBinder = ButterKnife.bind(this);
        setOnClick();
        setSelectHome();
//        rgMainMenu.setOnCheckedChangeListener(this);
//        initView();
        getFragmentList();
    }

    private void setOnClick() {
        rbHome.setOnClickListener(this);
        rbMatch.setOnClickListener(this);
        rbDrill.setOnClickListener(this);
        rbStock.setOnClickListener(this);
        rbMine.setOnClickListener(this);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            long l = System.currentTimeMillis() - exitTime;
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MyActivityManager.getMyActivityManager().removeAllAct();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setFullscreen(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

//    private void initView() {}

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private boolean b = false;

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "大赛MainActivity打开");
        if (ShowActivity.isLogin() && !b) {
            b = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPageEnd(this, "大赛MainActivity关闭");
    }

    private void getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        HomeFragment homeFragment = HomeFragment.newInstance();
        matchFragment = MatchFragment.newInstance();
        DrillFragment drillFragment = DrillFragment.newInstance();
        StockFragment stockFragment = StockFragment.newInstance();
        MineFragment mineFragment = MineFragment.newInstance();
        fragmentList.add(homeFragment);
        fragmentList.add(matchFragment);
        fragmentList.add(drillFragment);
        fragmentList.add(stockFragment);
        fragmentList.add(mineFragment);
        vpMain.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), fragmentList));
        vpMain.setOffscreenPageLimit(fragmentList.size());
    }

    private int selectNo = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_home:
                selectNo = 0;
                break;
            case R.id.rb_match:
                selectNo = 1;
                break;
            case R.id.rb_drill:
                selectNo = 2;
                break;
            case R.id.rb_stock:
                selectNo = 3;
                break;
            case R.id.rb_mine:
                selectNo = 4;
                break;
        }
        selectedWithFg(selectNo);
    }

    private void selectedWithFg(int select) {
        switch (select) {
            case 0:
                rbHome.setSelected(true);
                rbMatch.setSelected(false);
                rbDrill.setSelected(false);
                rbStock.setSelected(false);
                rbMine.setSelected(false);
                rbHome.setTextColor(getResources().getColor(R.color.share_red));
                rbMatch.setTextColor(getResources().getColor(R.color.black_33));
                rbDrill.setTextColor(getResources().getColor(R.color.black_33));
                rbStock.setTextColor(getResources().getColor(R.color.black_33));
                rbMine.setTextColor(getResources().getColor(R.color.black_33));
                break;
            case 1:
                rbHome.setSelected(false);
                rbMatch.setSelected(true);
                rbDrill.setSelected(false);
                rbStock.setSelected(false);
                rbMine.setSelected(false);
                rbHome.setTextColor(getResources().getColor(R.color.black_33));
                rbMatch.setTextColor(getResources().getColor(R.color.share_red));
                rbDrill.setTextColor(getResources().getColor(R.color.black_33));
                rbStock.setTextColor(getResources().getColor(R.color.black_33));
                rbMine.setTextColor(getResources().getColor(R.color.black_33));
                break;
            case 2:
                rbHome.setSelected(false);
                rbMatch.setSelected(false);
                rbDrill.setSelected(true);
                rbStock.setSelected(false);
                rbMine.setSelected(false);
                rbHome.setTextColor(getResources().getColor(R.color.black_33));
                rbMatch.setTextColor(getResources().getColor(R.color.black_33));
                rbDrill.setTextColor(getResources().getColor(R.color.share_red));
                rbStock.setTextColor(getResources().getColor(R.color.black_33));
                rbMine.setTextColor(getResources().getColor(R.color.black_33));
                break;
            case 3:
                rbHome.setSelected(false);
                rbMatch.setSelected(false);
                rbDrill.setSelected(false);
                rbStock.setSelected(true);
                rbMine.setSelected(false);
                rbHome.setTextColor(getResources().getColor(R.color.black_33));
                rbMatch.setTextColor(getResources().getColor(R.color.black_33));
                rbDrill.setTextColor(getResources().getColor(R.color.black_33));
                rbStock.setTextColor(getResources().getColor(R.color.share_red));
                rbMine.setTextColor(getResources().getColor(R.color.black_33));
                break;
            case 4:
                rbHome.setSelected(false);
                rbMatch.setSelected(false);
                rbDrill.setSelected(false);
                rbStock.setSelected(false);
                rbMine.setSelected(true);
                rbHome.setTextColor(getResources().getColor(R.color.black_33));
                rbMatch.setTextColor(getResources().getColor(R.color.black_33));
                rbDrill.setTextColor(getResources().getColor(R.color.black_33));
                rbStock.setTextColor(getResources().getColor(R.color.black_33));
                rbMine.setTextColor(getResources().getColor(R.color.share_red));
                break;
        }
        vpMain.setCurrentItem(select, false);
    }

    public void setIsExit(boolean b) {
        this.isExited = b;
    }

    public void setSelectHome() {
        this.selectNo = 0;
        rbHome.setSelected(true);
        rbHome.setTextColor(getResources().getColor(R.color.share_red));
        rbMatch.setSelected(false);
        rbDrill.setSelected(false);
        rbStock.setSelected(false);
        rbMine.setSelected(false);
        vpMain.setCurrentItem(0, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    @Override
    public void onCityClick() {
        selectedWithFg(1);
        selectNo=1;
        matchFragment.onCityClick();
    }

    @Override
    public void loadMore() {
    }

    @Override
    public void dissLoading() {
    }

    @Override
    public void showToastMsg(String msg) {
    }

    @Override
    public void onRefreshComplete() {
    }

    @Override
    public void toLogin() {
    }

    @Override
    public void rushLunchImage(List<Adverts> imgList) {
    }

    @Override
    public void rushLunchImageErr(String msg) {
    }

    @Override
    public void refreshMatchDesc(MatchDescBean.Result result) {
        if (result != null) {
            ShowActivity.showMatch4(MainActivity.this, result);
        }
    }

    @Override
    public void refreshMatchDescErr(String msg) {
    }

}
