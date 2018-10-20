package com.zbmf.StockGroup.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.StockModeMenuAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.StockModeMenu;
import com.zbmf.StockGroup.dialog.StockModeDescDialog;
import com.zbmf.StockGroup.fragment.NuggetsTenFragment;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao
 * on 2017/12/4.
 * 模型选股页面
 */

public class StockModeActivity extends BaseActivity implements View.OnClickListener, OnTabSelectListener {
//    ListView list_stock_mode_menu;
    StockModeMenuAdapter adapter;
    private List<String>titleList;
    private List<StockModeMenu> menuList;
    private List<Fragment> fragmentList;
//    private DrawerLayout drawable_layout_id;
    private StockModeDescDialog stockModeDescDialog;
    private ImageButton group_title_right_button;
    private SlidingTabLayout mTab;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_stock_mode_layout;
    }

    @Override
    public void initView() {
        initTitle(getString(R.string.mode_stock_zbmf));
        group_title_right_button=getView(R.id.imb_stock_mode);
        mTab =getView(R.id.tablayout);
        group_title_right_button.setVisibility(View.VISIBLE);


//        list_stock_mode_menu = getView(R.id.list_stock_mode_menu);
//        drawable_layout_id = getView(R.id.drawable_layout_id);
//        drawable_layout_id.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
//        drawable_layout_id.setScrimColor(0x00ffffff);
//        drawable_layout_id.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                LogUtil.e("slideOffset=="+slideOffset);
//                View mContent = drawable_layout_id.getChildAt(0);
//                View mMenu = drawerView;
//                float scale = 1 - slideOffset;
//                float rightScale = 0.8f + scale * 0.2f;
//                if (drawerView.getTag().equals("LEFT"))
//                {
//                    float leftScale = 1 - 0.1f * scale;
//                    //设置左边菜单滑动后的占据屏幕大小
//                    ViewHelper.setScaleX(mMenu, leftScale);
//                    ViewHelper.setScaleY(mMenu, leftScale);
//
//                    //设置内容界面水平和垂直方向偏转量
//                    //在滑动时内容界面的宽度为 屏幕宽度减去菜单界面所占宽度
//                    ViewHelper.setTranslationX(mContent,mMenu.getMeasuredWidth() * (1 - scale));
//                    mContent.invalidate();
//                    //设置右边菜单滑动后的占据屏幕大小
//                    ViewHelper.setScaleX(mContent, rightScale);
//                    ViewHelper.setScaleY(mContent, rightScale);
//                }
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imb_stock_mode:
                showDialog();
                break;
        }
    }

    /**
     * 侧滑菜单单击事件监听器
     */
//    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            selectItem(position);
//        }
//
//        public void selectItem(int position) {
//            adapter.setSelextPosition(position);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            for (Fragment fragment : fragmentList) {
//                if (fragment.isAdded()) {
//                    fragment.onPause();
//                    transaction.hide(fragment);
//                }
//            }
//            if (fragmentList.get(position).isAdded()) {
//                transaction.show(fragmentList.get(position));
//            } else {
//                transaction.add(R.id.stock_mode_content, fragmentList.get(position));
//            }
//            transaction.commit();
////            drawable_layout_id.closeDrawer(Gravity.LEFT);
//        }
//    }

    @Override
    public void initData() {
        adapter = new StockModeMenuAdapter(this);
//        list_stock_mode_menu.setAdapter(adapter);
        modelProduct();
    }

    private void setDefautFragment() {
        fragmentList = new ArrayList<>();
//        for (StockModeMenu stockModeMenu : menuList) {
//            fragmentList.add(StockModeContentFragment.newInstance(stockModeMenu));
//        }
        //添加fragment  自己重新写Fragment 掘金十点和掘金尾盘共用同一个fragment  掘金半时单独使用一个fragment
        for (int i = 0; i < menuList.size(); i++) {
            StockModeMenu stockModeMenu = menuList.get(i);
            fragmentList.add(NuggetsTenFragment.instance(stockModeMenu,i));
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.stock_mode_content, fragmentList.get(0));
        transaction.commit();
        adapter.setSelextPosition(0);
//        drawable_layout_id.openDrawer(Gravity.LEFT);
        if(!SettingDefaultsManager.getInstance().getStockModeDesc()){
            showDialog();
        }
    }
    private void showDialog(){
        if(stockModeDescDialog==null){
            stockModeDescDialog=StockModeDescDialog.createDialog(this);
        }
        stockModeDescDialog.show();
    }
    @Override
    public void addListener() {
        group_title_right_button.setOnClickListener(this);
        mTab.setOnTabSelectListener(this);
//        list_stock_mode_menu.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void modelProduct() {
        WebBase.modelProduct(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (menuList == null) {
                    menuList = new ArrayList<>();
                } else {
                    menuList.clear();
                }
                if (obj.has("products")) {
                    if(titleList==null){
                        titleList=new ArrayList<>();
                    }else{
                        titleList.clear();
                    }
                    menuList.addAll(JSONParse.getModeProductList(obj.optJSONArray("products")));
                    for(StockModeMenu stockModeMenu:menuList){
                        titleList.add(stockModeMenu.getName());
                    }
                    mTab.setmTitles(titleList);
                    if (menuList.size() > 0) {
                        adapter.setList(menuList);
                        setDefautFragment();
                    }
                }

            }

            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
            }
        });
    }

    @Override
    public void onTabSelect(int position) {
        mTab.setCurrentTab(position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            for (Fragment fragment : fragmentList) {
                if (fragment.isAdded()) {
                    fragment.onPause();
                    transaction.hide(fragment);
                }
            }
            if (fragmentList.get(position).isAdded()) {
                transaction.show(fragmentList.get(position));
            } else {
                transaction.add(R.id.stock_mode_content, fragmentList.get(position));
            }
            transaction.commit();
    }

    @Override
    public void onTabReselect(int position) {

    }
}
