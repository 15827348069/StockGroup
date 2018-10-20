package com.zbmf.StockGTec.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.CsItemDecoration;
import com.zbmf.StockGTec.adapter.HomeAdapter;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.LoginInfo;
import com.zbmf.StockGTec.beans.Table;
import com.zbmf.StockGTec.beans.Vers;
import com.zbmf.StockGTec.service.DemoIntentService;
import com.zbmf.StockGTec.service.DemoPushService;
import com.zbmf.StockGTec.service.ScoketService;
import com.zbmf.StockGTec.utils.Constants;
import com.zbmf.StockGTec.utils.ImageLoaderOptions;
import com.zbmf.StockGTec.utils.ObjectUtil;
import com.zbmf.StockGTec.utils.ServiceUtil;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.ShowActivity;
import com.zbmf.StockGTec.utils.UpdateManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.zbmf.StockGTec.utils.ShowActivity.showActivity;

//主页
public class HomeActivity extends ExActivity implements View.OnClickListener {

    private TextView tv_name, tv_num, tv_date;
    private UpdateReceiver receiver;
    private Dialog mDialog;
    private List<LoginInfo.Group> mGroups;
    private LoginInfo info;
    private ImageView iv_head;
    private RecyclerView mRecyclerView;
    private List<Table> mTables = new ArrayList<>();
    private String mIs_fans;
    private SettingDefaultsManager mSharePrefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            ShowActivity.showWebViewActivity(HomeActivity.this, extras.getString("url"));

        mSharePrefrences = SettingDefaultsManager.getInstance();
        info = (LoginInfo) ObjectUtil.readCache(Constants.LOGIN_INFO);
        setupView();
        initDatas();
        initRecyclerView();

        vers();
    }

    private void setupView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_date = (TextView) findViewById(R.id.tv_date);
        findViewById(R.id.group_head_setting).setOnClickListener(this);
        findViewById(R.id.ll_live).setOnClickListener(this);
        tv_date.setText("老师您好，今天是" + getCurrentDW());
        iv_head = (ImageView) findViewById(R.id.iv_head);
        ImageLoader.getInstance().displayImage(mSharePrefrences.getGroupImg(), iv_head, ImageLoaderOptions.AvatarOptions());
        tv_name.setText(mSharePrefrences.getGroupName());
        tv_num.setText("圈号：" + mSharePrefrences.getGroupId());
        mSharePrefrences.UserId();
        getGroupInfo();
    }

    private void initDatas() {
        mTables.add(new Table("邀请好友", R.mipmap.invite));
        mTables.add(new Table("铁粉管理", R.mipmap.tiefmanager));
        mTables.add(new Table("empty", 13));
        if (!(info != null && info.getGroups() != null && info.getGroups().size() > 0)) {
            mTables.clear();
            mTables.add(new Table("邀请好友", R.mipmap.invite));
            mTables.add(new Table("铁粉管理", R.mipmap.tiefmanager));
            mTables.add(new Table("财富中心", R.mipmap.money));
            mTables.add(new Table("宝盒", R.mipmap.baohe));
            mTables.add(new Table("观点", R.mipmap.wodebowen));
            mTables.add(new Table("视频", R.mipmap.video));
            mTables.add(new Table("问股", R.mipmap.wengu));
            mTables.add(new Table("数据", R.mipmap.datas));
            mTables.add(new Table("直播公告", R.mipmap.liveset));
        }
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        HomeAdapter homeAdapter = new HomeAdapter(this, mTables);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2 + 1);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new CsItemDecoration(2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(homeAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        homeAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Table table = mTables.get(position);
                String name = table.name;
                if (!TextUtils.isEmpty(name)) {
                    if (name.equals("直播公告")) {
                        liveNotice();
                    } else if (name.equals("铁粉管理")) {
                        fansManager();
                    } else if (name.equals("财富中心")) {
                        showActivity(HomeActivity.this, MfbActivity.class);
                    } else if (name.equals("观点")) {
                        showActivity(HomeActivity.this, MyBlogActivity.class);
                    } else if (name.equals("视频")) {
                        showActivity(HomeActivity.this, VideoCourseActivity.class);
                    } else if (name.equals("邀请好友")) {
                        showActivity(HomeActivity.this, InviteActivity.class);
                    } else if (name.equals("数据")) {
                        showActivity(HomeActivity.this, RelDateActivity.class);
                    } else if (name.equals("宝盒")) {
                        inChatActivity(3);
                    } else if (name.equals("问股")) {
                        inChatActivity(2);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
        //初始化时启动服务
        if (!ServiceUtil.isServiceRunning(this, ScoketService.class.getName())) {
            Intent intent = new Intent(this, ScoketService.class);
            this.bindService(intent, conn, BIND_AUTO_CREATE);
        }
        receiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getPackageName() + "client_id");
        registerReceiver(receiver, filter);
    }

    public class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取service传过来的信息
            String client_id = intent.getStringExtra("client_id");
            mSharePrefrences.setClientId(client_id);
            if (mSharePrefrences.authToken() != null) {
                bind_client_id();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void bind_client_id() {

        WebBase.Bind(new JSONHandler(HomeActivity.this) {
            @Override
            public void onSuccess(JSONObject obj) {
                getWolle();
            }

            @Override
            public void onFailure(String err_msg) {
                Log.e("BindTAG", err_msg);
            }
        });
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void vers() {
        WebBase.vers(new JSONHandler(HomeActivity.this) {
            @Override
            public void onSuccess(JSONObject obj) {
                Vers vers = new Vers();
                vers.setUpdated_at(obj.optString("updated_at"));
                vers.setIntro(obj.optString("intro"));
                vers.setSubject(obj.optString("subject"));
                vers.setVersion(obj.optString("version"));
                vers.setDownload(obj.optString("download"));
                JSONArray array = obj.optJSONArray("logics");

                Vers.Logics logics = new Vers.Logics();
                logics.intro = array.optJSONObject(0).optString("intro");
                logics.state = array.optJSONObject(0).optString("state");
                vers.setLogics(logics);

                PackageManager pm = getPackageManager();
                PackageInfo pi;
                String version = "";
                try {
                    pi = pm.getPackageInfo(getPackageName(), 0);
                    version = pi.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                if (version.compareTo(vers.getVersion()) < 0) {
                    UpdateManager update = new UpdateManager(HomeActivity.this);
                    update.setApkUrl(vers.getDownload());
                    update.setForce(true);
                    update.setVersion(vers.getVersion());
                    update.checkUpdateInfo(true);
                }

            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_live://开始直播
                inChatActivity(0);
                break;
            case R.id.group_head_setting:
                if (info != null) {
                    mGroups = info.getGroups();
                    if (mDialog == null)
                        mDialog = showDialog();
                    mDialog.show();
                } else
                    Toast.makeText(this, "没有所管理的圈子哦", Toast.LENGTH_SHORT).show();
                break;
            case R.id.group_head_msg:
                showActivity(this, PrivateActivity.class);
                break;
        }
    }

    private void inChatActivity(int pos) {
        if (mSharePrefrences.authToken() == null) {
            Toast.makeText(this, "请登陆", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("is_fans", mIs_fans);
        bundle.putInt("pos", pos);
        ShowActivity.startActivity(this, bundle, Chat1Activity.class.getName());
    }

    private void fansManager() {
        if (info != null && info.getGroups() != null && info.getGroups().size() > 0 && getGroup() != null) {
            if (getGroup().getAuth().getData() == 0) {
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
            } else {
                ShowActivity.showActivity(this, TieFActivity.class);
            }
        } else {
            ShowActivity.showActivity(this, TieFActivity.class);
        }
    }

    private void liveNotice() {
        if (info != null && info.getGroups() != null && info.getGroups().size() > 0 && getGroup() != null) {
            if (getGroup().getAuth().getLive() == 0) {
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
            } else {
                ShowActivity.showActivity(this, LiveSettingActivity.class);
            }
        } else {
            ShowActivity.showActivity(this, LiveSettingActivity.class);
        }
    }

    private LoginInfo.Group getGroup() {
        for (LoginInfo.Group group : info.getGroups()) {
            if (mSharePrefrences.getGroupId().equals(group.getId() + "")) {
                return group;
            }
        }
        return null;
    }

    private Dialog showDialog() {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.dialog_commodity_list, null);

        ListView listView = (ListView) view.findViewById(R.id.listview);
        Button btn_exit = (Button) view.findViewById(R.id.btn_exit);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        LinearLayout ll_top = (LinearLayout) view.findViewById(R.id.ll_top);
        MyAdapter adapter = new MyAdapter(this, R.layout.item_commodity, mGroups);
        listView.setAdapter(adapter);
        if (mGroups.size() == 0) {
            ll_top.setVisibility(View.GONE);
        }

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoticeDialog();
                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSet = false;
                for (LoginInfo.Group g : mGroups) {
                    if (g.isCheck()) {
                        mSharePrefrences.setGroupId(g.getId() + "");
                        mSharePrefrences.setGroupImg(g.getAvatar() + "");
                        mSharePrefrences.setGroupName(g.getName() + "");
                        mSharePrefrences.setGroupContent(g.getContent() + "");

                        ImageLoader.getInstance().displayImage(mSharePrefrences.getGroupImg(), iv_head, ImageLoaderOptions.AvatarOptions());
                        tv_name.setText(mSharePrefrences.getGroupName());
                        tv_num.setText("圈号：" + mSharePrefrences.getGroupId());
                        isSet = true;
                        g.setCheck(false);
                        mSharePrefrences.setIsChatManager(false);
                        mSharePrefrences.setGroupManager(false);
                        mSharePrefrences.setSayFans(false);
                        getGroupInfo();
                        if (g.getAuth().getCreator() == 1) {
                            mSharePrefrences.setIsChatManager(true);
                            mSharePrefrences.setGroupManager(true);
                        } else {
                            mSharePrefrences.setIsChatManager(false);
                            mSharePrefrences.setGroupManager(false);
                        }

                        if (g.getAuth().getFans() == 1)
                            mSharePrefrences.setSayFans(true);
                        break;
                    }
                }

                if (!isSet) {
                    Toast.makeText(HomeActivity.this, "没有选择圈子", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        dialog.setCancelable(true);

        return dialog;
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private int resource;
        private List<LoginInfo.Group> mGroups;

        public MyAdapter(Context context, int resource, List<LoginInfo.Group> objects) {
            this.context = context;
            this.resource = resource;
            this.mGroups = objects;
        }

        @Override
        public int getCount() {
            return mGroups.size();
        }

        @Override
        public Object getItem(int position) {
            return mGroups.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(context, resource, null);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            final LoginInfo.Group item = mGroups.get(position);
            tv.setText(item.getName());
            RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root);
            if (item.isCheck()) {
                root.setBackgroundResource(R.color.se);
            } else {
                root.setBackgroundResource(R.color.white);
            }
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (LoginInfo.Group g : mGroups) {
                        g.setCheck(false);
                    }
                    item.setCheck(true);
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }

    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("确认退出登录");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mSharePrefrences.setAuthtoken("");
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void getWolle() {
        WebBase.getWalle(new JSONHandler(HomeActivity.this) {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject pays = obj.optJSONObject("pay");
                JSONObject point = obj.optJSONObject("point");
                mSharePrefrences.setPays(pays.optString("unfrozen"));
                mSharePrefrences.setPoint(point.optLong("unfrozen"));
            }

            @Override
            public void onFailure(String err_msg) {
                Log.e(">>>>", "错误" + err_msg);
            }
        });
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getGroupInfo() {
        WebBase.groupInfo(new JSONHandler(HomeActivity.this) {
            @Override
            public void onSuccess(JSONObject obj) {
                if (!obj.isNull("group")) {
                    JSONObject object = obj.optJSONObject("group");
                    mIs_fans = object.optString("is_fans");
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(HomeActivity.this, err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private String getCurrentDW() {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return year + "年" + month + "月" + day + "日 " + weeks[week - 1];
    }
}
