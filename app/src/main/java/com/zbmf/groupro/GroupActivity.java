package com.zbmf.groupro;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.zbmf.groupro.activity.Chat1Activity;
import com.zbmf.groupro.activity.LoginActivity;
import com.zbmf.groupro.adapter.GroupViewPageAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.LiveMessage;
import com.zbmf.groupro.beans.Offine;
import com.zbmf.groupro.beans.User;
import com.zbmf.groupro.db.DBManager;
import com.zbmf.groupro.fragment.CareFragement;
import com.zbmf.groupro.fragment.HomeFragment;
import com.zbmf.groupro.fragment.MyDetailFragment;
import com.zbmf.groupro.service.DemoIntentService;
import com.zbmf.groupro.service.DemoPushService;
import com.zbmf.groupro.service.ScoketService;
import com.zbmf.groupro.utils.ActivityUtil;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.JSONParse;
import com.zbmf.groupro.utils.MessageType;
import com.zbmf.groupro.utils.ServiceUtil;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.HomeViewPage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GroupActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private List<Fragment> infolist;
    private RadioGroup group_main_munu;
    private UpdateReceiver receiver;
    private HomeViewPage viewpage;
    private MyDetailFragment myDetailFragment;
    private CareFragement careFragement;
    private HomeFragment homeFragment;
    private ImageView my_detail_point;
    private TextView home_menu_point, care_menu_point, study_menu_point;
    private int roffine = 0;
    private int live_roffine = 0, chat_roffine = 0;
    private int select;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        dbManager = new DBManager(getBaseContext());
        init();
        setDefaultFragment();
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
        //初始化时启动服务
        if (!ServiceUtil.isServiceRunning(this, ScoketService.class.getName())) {
            Intent intent = new Intent(this, ScoketService.class);
            this.bindService(intent, conn, BIND_AUTO_CREATE);
        }
        receiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BIND_CLIENT_ID);
        filter.addAction(Constants.UP_DATA_MESSAGE);
        filter.addAction(Constants.UP_DATA_COUPONS);
        filter.addAction(Constants.LOGOUT);
        filter.addAction(Constants.USER_NEW_MESSAGE);
        filter.addAction(Constants.NEW_LIVE_MSG);
        filter.addAction(Constants.NEW_LIVE_MSG_READ);
        filter.addAction(Constants.UNREADNUM);
        filter.addAction(Constants.NETWORK_IS_UNREACHABLE);
        filter.addAction(Constants.USER_RED_NEW_MESSAGE);
        registerReceiver(receiver, filter);
        ActivityUtil.getInstance().putActivity(ActivityUtil.GROUPACTIVITY, this);

        if (!TextUtils.isEmpty(SettingDefaultsManager.getInstance().authToken()))
            userInfo();
    }

    private void userInfo() {
        WebBase.userInfo(SettingDefaultsManager.getInstance().authToken(), new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject user = obj.optJSONObject("user");
                User userbean = new User();
                userbean.setAuth_token(SettingDefaultsManager.getInstance().authToken());
                userbean.setAvatar(user.optString("avatar"));
                userbean.setUsername(user.optString("username"));
                userbean.setUser_id(user.optString("user_id"));
                userbean.setTruename(user.optString("truename"));
                userbean.setRole(user.optString("role"));
                userbean.setNickname(user.optString("nickname"));
                userbean.setPhone(user.optString("phone"));
                userbean.setIdcard(user.optString("idcard"));
                dbManager.addUser(userbean);
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void init() {
        group_main_munu = (RadioGroup) findViewById(R.id.group_main_munu);
        viewpage = (HomeViewPage) findViewById(R.id.home_viewPager);
        my_detail_point = (ImageView) findViewById(R.id.my_detail_point);//个人页面新消息提醒
        home_menu_point = (TextView) findViewById(R.id.home_menu_point);//首页页面新消息提醒
        care_menu_point = (TextView) findViewById(R.id.care_menu_point);//关注页面新消息提醒
        study_menu_point = (TextView) findViewById(R.id.study_menu_point);//学院页面新消息提醒
        findViewById(R.id.home_menu).setOnClickListener(this);
        findViewById(R.id.care_menu).setOnClickListener(this);
//        findViewById(R.id.study_menu).setOnClickListener(this);
        findViewById(R.id.my_detail).setOnClickListener(this);
//        viewpage.setChildId(R.id.homt_tuijian);
    }

    private void setDefaultFragment() {
        myDetailFragment = MyDetailFragment.newInstance();
        careFragement = CareFragement.newInstance();
        homeFragment = HomeFragment.newInstance();
        infolist = new ArrayList<>();
        infolist.add(homeFragment);
        infolist.add(careFragement);
//        infolist.add(StudyFragment.newInstance());
        infolist.add(myDetailFragment);
        group_main_munu.check(R.id.home_menu);
        GroupViewPageAdapter adapter = new GroupViewPageAdapter(getSupportFragmentManager(), infolist);
        viewpage.setAdapter(adapter);
        viewpage.setOnPageChangeListener(this);
        viewpage.setOffscreenPageLimit(infolist.size());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                group_main_munu.check(R.id.home_menu);
                break;
            case 1:
                if (SettingDefaultsManager.getInstance().authToken() == null || SettingDefaultsManager.getInstance().authToken().isEmpty()) {
                    ShowActivity.showActivity(this, LoginActivity.class);
                    group_main_munu.check(R.id.home_menu);
                    viewpage.setCurrentItem(0, false);
                } else {
                    group_main_munu.check(R.id.care_menu);
                }
                break;
//            case 2:
//                group_main_munu.check(R.id.study_menu);
//                break;
            case 2:
                if (SettingDefaultsManager.getInstance().authToken() == null || SettingDefaultsManager.getInstance().authToken().isEmpty()) {
                    ShowActivity.showActivity(this, LoginActivity.class);
                    group_main_munu.check(R.id.home_menu);
                } else {
                    my_detail_point.setVisibility(View.INVISIBLE);
                    group_main_munu.check(R.id.my_detail);
                }
                break;
        }
        select = position;
    }

    public void checked(){
        viewpage.setCurrentItem(0, false);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        int index = 0;
        switch (view.getId()) {
            case R.id.home_menu:
                //首页
                index = 0;
                if (select == index) {
                    homeFragment.rushData();
                }
                break;
            case R.id.care_menu:
                //关注
                if (SettingDefaultsManager.getInstance().authToken() == null || SettingDefaultsManager.getInstance().authToken().isEmpty()) {
                    ShowActivity.showActivity(this, LoginActivity.class);
                    index = 0;
                    group_main_munu.clearCheck();
                    group_main_munu.check(R.id.home_menu);
                } else {
                    index = 1;
                    if (select == index) {
                        careFragement.rushData();
                    }
                }
                break;
//            case R.id.study_menu:
//                //学院
////                index = 2;
//                break;
            case R.id.my_detail:
                //个人
                if (SettingDefaultsManager.getInstance().authToken() == null || SettingDefaultsManager.getInstance().authToken().isEmpty()) {
                    ShowActivity.showActivity(this, LoginActivity.class);
                    index = 0;
                    group_main_munu.clearCheck();
                    group_main_munu.check(R.id.home_menu);
                } else {
                    my_detail_point.setVisibility(View.INVISIBLE);
                    index = 2;
                    if (select == index) {
                        myDetailFragment.runshData();
                    }
                }
                break;
        }
        viewpage.setCurrentItem(index, false);
    }

    private class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取service传过来的信息
            switch (intent.getAction()) {
                case Constants.BIND_CLIENT_ID:
                    String client_id = intent.getStringExtra("client_id");
                    SettingDefaultsManager.getInstance().setClientId(client_id);
                    if (SettingDefaultsManager.getInstance().authToken() != null) {
                        bind_client_id();
                    }
                    break;
                case Constants.UP_DATA_MESSAGE:
                    getWolle();
                    break;
                case Constants.UP_DATA_COUPONS:
                    myDetailFragment.setData(false);
                    break;
                case Constants.LOGOUT:
                    myDetailFragment.setData(false);
                    careFragement.setData(false);
                    group_main_munu.clearCheck();
                    group_main_munu.check(R.id.home_menu);
                    viewpage.setCurrentItem(0, false);
                    break;
                case Constants.USER_NEW_MESSAGE:
                    //个人中心新消息point提示
                    if (my_detail_point.getVisibility() == View.INVISIBLE) {
                        my_detail_point.setVisibility(View.VISIBLE);
                    }
                    myDetailFragment.setPointVisi(intent.getStringExtra("type"));
                    break;
                case Constants.USER_RED_NEW_MESSAGE:
                    myDetailFragment.setPointGone(intent.getStringExtra("type"));
                    if(myDetailFragment.getPoint()){
                        if (my_detail_point.getVisibility() == View.INVISIBLE) {
                            my_detail_point.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case Constants.NEW_LIVE_MSG:
                    //直播室新消息
                    LiveMessage msg = (LiveMessage) intent.getSerializableExtra("new_live_msg");
                    if (SettingDefaultsManager.getInstance().authToken() != null && !msg.getMessage_type().equals(MessageType.SYSTEM)) {
                        live_roffine += 1;
                        setCare_menu_point();
                    }
                    break;
                case Constants.NEW_LIVE_MSG_READ:
                    live_roffine = dbManager.getAllUnredCount();
                    setCare_menu_point();
                    break;
                case Constants.UNREADNUM:
                    careFragement.updateDate(false);
                    //设置菜单未读
                    setCare_menu_point();
                    break;
                case Constants.NETWORK_IS_UNREACHABLE:
                    Toast.makeText(GroupActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

    public void bind_client_id() {
        WebBase.Bind(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                live_roffine = 0;
                Chat1Activity activity = (Chat1Activity) ActivityUtil.getInstance().getActivity(ActivityUtil.CHATACTIVITY);
                Offine offine = JSONParse.Bind(obj);
                if (offine != null && offine.getList() != null && offine.getList().size() > 0)
                    for (int i = 0; i < offine.getList().size(); i++) {
                        live_roffine += offine.getList().get(i).getLive();
                        chat_roffine += offine.getList().get(i).getRoom();
                        dbManager.setOfflineMsgCount(offine.getList().get(i));
                    }
                live_roffine =  dbManager.getAllUnredCount();
                setCare_menu_point();
                if (activity != null) {
                    activity.enterGroup();
                } else {
                    if (!TextUtils.isEmpty(SettingDefaultsManager.getInstance().authToken())) {
                        getWolle();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Log.e("BindTAG", err_msg);
            }
        });
    }

    private void setUnRead() {
        if (SettingDefaultsManager.getInstance().authToken() != null) {
            if (roffine > 0) {
                if (roffine == 99) {
                    care_menu_point.setText(roffine + "+");
                } else {
                    care_menu_point.setText(roffine + "");
                }
                care_menu_point.setVisibility(View.VISIBLE);
            } else {
                care_menu_point.setVisibility(View.GONE);
            }
            careFragement.updateDate(true);
        } else {
            care_menu_point.setVisibility(View.GONE);
        }

    }

    public void setCare_menu_point() {
        roffine = live_roffine;
        roffine = Math.max(0, Math.min(roffine, 99));
        setUnRead();
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public void getWolle() {
        WebBase.getWalle(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject pays = obj.optJSONObject("pay");
                JSONObject point = obj.optJSONObject("point");
                SettingDefaultsManager.getInstance().setPays(pays.optString("unfrozen"));
                SettingDefaultsManager.getInstance().setPoint(point.optLong("unfrozen"));
                myDetailFragment.updataUserMessage();
                ActivityUtil.getInstance().UpdaUserMfbMessage();
            }

            @Override
            public void onFailure(String err_msg) {
                Log.e(">>>>", "错误" + err_msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束服务
        if (conn != null) {
            unbindService(conn);
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        ActivityUtil.getInstance().clearAll();

        dbManager.closeDB();
    }

    public void selectViewPage(int select) {
        viewpage.setCurrentItem(select, false);
    }

    public void goNext() {
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityUtil.getInstance().clearAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void updateCareData() {
        if (careFragement != null) {
            careFragement.setFirstIn();
        }
    }
}
