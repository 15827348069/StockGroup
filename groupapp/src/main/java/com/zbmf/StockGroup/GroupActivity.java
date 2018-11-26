package com.zbmf.StockGroup;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveLoginListener;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.PublishInfo;
import com.bokecc.sdk.mobile.live.pojo.RoomInfo;
import com.bokecc.sdk.mobile.live.pojo.TemplateInfo;
import com.bokecc.sdk.mobile.live.pojo.Viewer;
import com.igexin.sdk.PushManager;
import com.zbmf.StockGroup.activity.DongAskActivity;
import com.zbmf.StockGroup.activity.LoginActivity;
import com.zbmf.StockGroup.activity.ScreenActivity;
import com.zbmf.StockGroup.activity.ScreenDetailActivity;
import com.zbmf.StockGroup.activity.SimulateOneStockCommitActivity;
import com.zbmf.StockGroup.activity.StockModeActivity;
import com.zbmf.StockGroup.activity.VideoPlayActivity;
import com.zbmf.StockGroup.adapter.GroupViewPageAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.LiveMessage;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.beans.Stock;
import com.zbmf.StockGroup.beans.User;
import com.zbmf.StockGroup.beans.Vers;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.constans.AppConfig;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.db.DBManager;
import com.zbmf.StockGroup.dialog.CheckUpdateDialog;
import com.zbmf.StockGroup.fragment.CareFragments;
import com.zbmf.StockGroup.fragment.HomeFragment;
import com.zbmf.StockGroup.fragment.MyDetailFragment;
import com.zbmf.StockGroup.interfaces.OnUrlClick;
import com.zbmf.StockGroup.interfaces.UpUserMessage;
import com.zbmf.StockGroup.service.DemoIntentService;
import com.zbmf.StockGroup.service.DemoPushService;
import com.zbmf.StockGroup.service.ScoketService;
import com.zbmf.StockGroup.utils.ActivityUtil;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.MessageType;
import com.zbmf.StockGroup.utils.ServiceUtil;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.utils.UpdateManager;
import com.zbmf.StockGroup.utils.WebClickUitl;
import com.zbmf.StockGroup.view.HomeViewPage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import com.igexin.sdk.PushManager;


public class GroupActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        View.OnClickListener, OnUrlClick{
    private List<Fragment> infolist;
    private RadioGroup group_main_munu;
    private UpdateReceiver receiver;
    private HomeViewPage viewpage;
    private MyDetailFragment myDetailFragment;
    private CareFragments careFragement;
    private HomeFragment homeFragment;
    private ImageView my_detail_point;
    private TextView home_menu_point, care_menu_point;
    private int roffine = 0;
    private int select;
    private DBManager dbManager;
    private boolean url, isFirst = true;
    private UpUserMessage upUserMessage;
    private RadioButton care_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.addActivity(this, getClass());
//        applyPermissions();
        if (isFirst) {
            isFirst = false;
            setContentView(R.layout.activity_group);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && !url) {
                url = true;
                String url_link = bundle.getString(IntentKey.WELCOME_URL);
                if (url_link != null || !TextUtils.isEmpty(url_link)) {
                    WebClickUitl.UrlClick(this, url_link);
                }
            }
            dbManager = new DBManager(getBaseContext());
            init();
            setDefaultFragment();
            new CheckUpdateDialog(this, AppConfig.IS_DEBUG).checkUpdate();
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
            filter.addAction(Constants.UPDATE_VIDEO_LIST);
            registerReceiver(receiver, filter);
            if (!TextUtils.isEmpty(SettingDefaultsManager.getInstance().authToken())) {
                userInfo();
            }
            if (!AppConfig.IS_DEBUG) {
                vers();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void setUpUserMessage(UpUserMessage upUserMessage) {
        this.upUserMessage = upUserMessage;
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
                userbean.setIs_super(user.optInt("is_super"));
                userbean.setIs_vip(user.optInt("is_vip"));
                userbean.setVip_end_at(user.optString("vip_end_at"));
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
//        study_menu_point = (TextView) findViewById(R.id.study_menu_point);//学院页面新消息提醒
        findViewById(R.id.home_menu).setOnClickListener(this);
        findViewById(R.id.care_menu).setOnClickListener(this);
//        findViewById(R.id.study_menu).setOnClickListener(this);
        findViewById(R.id.my_detail).setOnClickListener(this);
        care_menu = (RadioButton) findViewById(R.id.care_menu);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            int anInt = extras.getInt(IntentKey.FLAG);
            if (anInt==0){
                group_main_munu.check(R.id.care_menu);
                viewpage.setCurrentItem(1, false);
            }
        }
    }

    private void setDefaultFragment() {
        myDetailFragment = MyDetailFragment.newInstance();
        careFragement = CareFragments.newInstance();
        homeFragment = HomeFragment.newInstance();
        infolist = new ArrayList<>();
        infolist.add(homeFragment);
        infolist.add(careFragement);
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
                if (TextUtils.isEmpty(SettingDefaultsManager.getInstance().authToken())) {
                    ShowActivity.showActivity(this, LoginActivity.class);
                    group_main_munu.check(R.id.home_menu);
                    viewpage.setCurrentItem(0, false);
                } else {
                    group_main_munu.check(R.id.care_menu);
                }
                break;
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

    public void checked() {
        if (select == 1 || select == 3) {
            viewpage.setCurrentItem(0, false);
        }
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
                }
                break;
//            case R.id.study_menu:
//                //学院
//                index = 2;
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
                }
                break;
        }
        viewpage.setCurrentItem(index, false);
    }

    @Override
    public void onGroup(Group group) {
        ShowActivity.showGroupDetailActivity(this, group);
    }

    @Override
    public void onBolg(BlogBean blogBean) {
        ShowActivity.showBlogDetailActivity(this, blogBean);
    }

    @Override
    public void onVideo(Video video) {
        toVideo(video);
    }

    @Override
    public void onWeb(String url) {
        ShowActivity.showWebViewActivity(this, url);
    }

    @Override
    public void onImage(String url) {
        ShowActivity.ShowBigImage(this, url);
    }


    @Override
    public void onStock(Stock stock) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.STOCKHOLDER, stock);
        ShowActivity.showActivity(this, bundle, SimulateOneStockCommitActivity.class);
    }

    @Override
    public void onScreen() {
        ShowActivity.showActivity(this, ScreenActivity.class);
    }

    @Override
    public void onScreenDetail(Screen screen) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.SCREEN, screen);
        ShowActivity.showActivity(this, bundle, ScreenDetailActivity.class);
    }

    @Override
    public void onModeStock() {
        ShowActivity.showActivity(this, StockModeActivity.class);
    }

    @Override
    public void onDongAsk() {
        ShowActivity.showActivity(this, DongAskActivity.class);
    }

    @Override
    public void onQQ(String url) {
        ShowActivity.showQQ(this);
    }

    @Override
    public void onPay() {
        ShowActivity.showPayDetailActivity(this);
    }

    private void toVideo(final Video video) {
        if (video.getIs_live()) {
            DWLive.getInstance().setDWLiveLoginParams(new DWLiveLoginListener() {
                @Override
                public void onLogin(TemplateInfo templateInfo, Viewer viewer, RoomInfo roomInfo, PublishInfo publishInfo) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY, video);
                            ShowActivity.showActivityForResult(GroupActivity.this, bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                        }
                    });
                }

                @Override
                public void onException(final DWLiveException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY, video);
                            ShowActivity.showActivityForResult(GroupActivity.this, bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                        }
                    });
                }
            }, Constants.CC_USERID, video.getBokecc_id() + "", SettingDefaultsManager.getInstance().NickName(), "");
            DWLive.getInstance().startLogin();
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.VIDEO_KEY, video);
            ShowActivity.showActivity(GroupActivity.this, bundle, VideoPlayActivity.class);
        }
    }

    private class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取service传过来的信息
            switch (intent.getAction()) {
                case Constants.BIND_CLIENT_ID:
                    String client_id = intent.getStringExtra("client_id");
                    Log.i("===TAG","---   存储  聊天的socketID："+client_id);
                    SettingDefaultsManager.getInstance().setClientId(client_id);
                    if (SettingDefaultsManager.getInstance().authToken() != null) {
                        bind_client_id();
                    }
                    break;
                case Constants.UP_DATA_MESSAGE:
                    if (upUserMessage != null) {
                        upUserMessage.upMessage();
                    }
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
                    dbManager.setUnablemessage(null);
                    setCare_menu_point();
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
                    if (myDetailFragment.getPoint()) {
                        if (my_detail_point.getVisibility() == View.INVISIBLE) {
                            my_detail_point.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case Constants.NEW_LIVE_MSG:
                    //直播室新消息
                    LiveMessage msg = (LiveMessage) intent.getSerializableExtra("new_live_msg");
                    if (SettingDefaultsManager.getInstance().authToken() != null && !msg.getMessage_type().equals(MessageType.SYSTEM)) {
                        roffine += 1;
                        setUnRead();
                    }
                    break;
                case Constants.NEW_LIVE_MSG_READ:
                    roffine = dbManager.getAllUnredCount();
                    setCare_menu_point();
                    break;
                case Constants.UNREADNUM:
                    //设置菜单未读
                    if (careFragement != null) {
                        careFragement.updateDate(false, roffine);
                    }
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
                roffine = 0;
                roffine = dbManager.getAllUnredCount();
                setCare_menu_point();
            }

            @Override
            public void onFailure(String err_msg) {
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
            if (careFragement != null) {
                careFragement.updateDate(true, roffine);
            }
        } else {
            care_menu_point.setVisibility(View.GONE);
        }

    }


    public void setCare_menu_point() {
        roffine = dbManager.getAllUnredCount();
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
        dbManager.closeDB();
    }

    public void selectViewPage(int select) {
        switch (select) {
            case 0:
                group_main_munu.check(R.id.home_menu);
                break;
            case 1:
                if (SettingDefaultsManager.getInstance().authToken() == null || SettingDefaultsManager.getInstance().authToken().isEmpty()) {
                    ShowActivity.showActivity(this, LoginActivity.class);
                    group_main_munu.check(R.id.home_menu);
                    viewpage.setCurrentItem(0, false);
                } else {
                    viewpage.setCurrentItem(1, false);
                    care_menu.toggle();
                }
                break;
            case 2:
                if (SettingDefaultsManager.getInstance().authToken() == null || SettingDefaultsManager.getInstance().authToken().isEmpty()) {
                    ShowActivity.showActivity(this, LoginActivity.class);
                    group_main_munu.check(R.id.home_menu);
                } else {
                    my_detail_point.setVisibility(View.INVISIBLE);
                    group_main_munu.check(R.id.my_detail);
                    viewpage.setCurrentItem(2, false);
                }
                break;
        }
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
                deleteDatabase("webview.db");
                deleteDatabase("webviewCache.db");
                ActivityUtil.removeAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
//                case STUDY:
//                    group_main_munu.clearCheck();
//                    group_main_munu.check(R.id.study_menu);
//                    viewpage.setCurrentItem(2, false);
//                    break;
                case RequestCode.SCREEN:
                    if (homeFragment != null) {
                        homeFragment.getScreenList();
                    }
                    break;
            }

        }
    }

    private void vers() {
        WebBase.vers(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                Vers vers = JSONParse.vers(obj);
                PackageManager pm = getPackageManager();
                PackageInfo pi;
                try {
                    pi = pm.getPackageInfo(getPackageName(), 0);
                    String version = pi.versionName;
                    if (version.compareTo(vers.getVersion()) < 0 && vers.getLogics() != null) {
                        UpdateManager update = new UpdateManager(GroupActivity.this);
                        update.setApkUrl(vers.getUrl());
                        update.setForce(true);
                        update.setVersion(vers.getVersion());
                        update.checkUpdateInfo(true);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
}
