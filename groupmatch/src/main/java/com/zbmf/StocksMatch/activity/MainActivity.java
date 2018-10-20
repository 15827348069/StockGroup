package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.db.Database;
import com.zbmf.StocksMatch.db.DatabaseImpl;
import com.zbmf.StocksMatch.fragment.HomeFragment;
import com.zbmf.StocksMatch.fragment.MathcFragment;
import com.zbmf.StocksMatch.fragment.MoreFragment;
import com.zbmf.StocksMatch.fragment.QuotationFragment;
import com.zbmf.StocksMatch.fragment.TzPlusFragment;
import com.zbmf.StocksMatch.utils.GetTime;
import com.zbmf.StocksMatch.utils.SharedPreferencesUtils;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;

import java.util.Date;


public class MainActivity extends FragmentActivity {

    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {HomeFragment.class, QuotationFragment.class, MathcFragment.class, TzPlusFragment.class, MoreFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_home, R.drawable.tab_home1, R.drawable.tab_home2,
            R.drawable.tab_home3, R.drawable.tab_home4};

    //Tab选项卡的文字
    private String mTextviewArray[] = {"广场","行情","我的比赛","投资+","更多"};
    private Database db;
    private SharedPreferencesUtils shUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutInflater = LayoutInflater.from(this);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
//            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId) {
                    case "广场":

                        break;
                    case "行情":

                        break;
                    case "我的比赛":

                        break;
                    case "投资":

                        break;
                    case "更多":

                        break;
                }
            }
        });



        db = new DatabaseImpl(this);
        shUtils = new SharedPreferencesUtils(this);
        String update_time = shUtils.getUpdated();
        if(!update_time.equals(GetTime.format.format(new Date()))){
            new UpdateDB(this).execute();
        }

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey("user")){
                User user = (User)savedInstanceState.getSerializable("user");
                UiCommon.INSTANCE.setiUser(user);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (UiCommon.INSTANCE.getiUser() != null && !TextUtils.isEmpty(UiCommon.INSTANCE.getiUser().getAuth_token())){
            outState.putSerializable("user",UiCommon.INSTANCE.getiUser());
        }
    }

    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UiCommon.INSTANCE.doOnActivityDestroy(this);
    }

    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           /* UiCommon.INSTANCE.showDialog(this, this.getString(R.string.exit_prompt), new DialogListener() {
                @Override
                public void onCancl(Dialog dialog) {
                    dialog.cancel();
                }

                @Override
                public void onConfirm(Dialog dialog) {
                    UiCommon.INSTANCE.finishAppNow();
                    dialog.cancel();

                }
            }, R.string.cancel, R.string.exit);*/
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, R.string.exitPromot, Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
                UiCommon.INSTANCE.finishAppNow();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
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


    /**
     * 更新数据库股票
     */
    private class UpdateDB extends LoadingDialog<Void, Stock> {

        public UpdateDB(Context activity) {
            super(activity, false, true);
        }

        @Override
        public Stock doInBackground(Void... params) {
            shUtils.setUpdated(GetTime.format.format(new Date()));
            Get2ApiImpl server = new Get2ApiImpl();
            try {
                Stock stock = server.updateDB();
                if(stock!=null&&stock.getList().size()>0){
                    for (int index =0;index<stock.getList().size();index++){
                        db.addSymbol(stock.getList().get(index));
                    }
                }
                return stock;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(Stock general) {
            if (general != null && general.code != -1) {

            } else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }


}
