package com.zbmf.StockGroup.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zbmf.StockGroup.GroupActivity;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.CycleViewPager;
import com.zbmf.StockGroup.api.AppUrl;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.HomeImage;
import com.zbmf.StockGroup.beans.Vers;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.utils.ViewFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener {
    private List<HomeImage> url_list;
    private CycleViewPager cycleViewPager;
    private List<ImageView> views;
    private boolean commit;

    private static final int MY_PERMISSION_REQUEST_CODE = 10000;

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setInstallPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        findViewById(R.id.tv_jump).setOnClickListener(this);
        vers();
    }

    /**
     * 8.0以上系统设置安装未知来源权限
     */
    public void setInstallPermission() {
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先判断是否有安装未知来源应用的权限
            haveInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                //弹框提示用户手动打开
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("安装权限")
                        .setMessage("需要打开允许此应用来源，请去设置中开启此权限")
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    //此方法需要API>=26才能使用
                                    toInstallPermissionSettingIntent();
                                    dialog.dismiss();
                                }
                            }
                        })
                        .create().show();
            }
        }
        /*
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }
        );

        if (!isAllGranted) {
            // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    MY_PERMISSION_REQUEST_CODE
            );
        }
    }

    /**
     * alert 消息提示框显示
     *
     * @param context  上下文
     * @param title    标题
     * @param message  消息
     * @param listener 监听器
     */
    public static void showAlert(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", listener);
        builder.setCancelable(false);
        builder.setIcon(R.mipmap.ic_launcher);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 开启安装未知来源权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toInstallPermissionSettingIntent() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, Constants.INSTALL_PERMISS_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.INSTALL_PERMISS_CODE) {
            Toast.makeText(this, "安装应用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            ArrayList<String> list = new ArrayList<>();
            String[] a = new String[]{};
            String[] strings = new String[0];
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this,
                        permission) != PackageManager.PERMISSION_GRANTED) {//如果没有同意授权
                    list.add(permission);
                    strings = list.toArray(a);
                }
            }
            if (strings.length > 0) {
                ActivityCompat.requestPermissions(this, strings, MY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void initHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!commit) {
                    startActivity(new Intent(getApplication(), GroupActivity.class));
                    LaunchActivity.this.finish();
                }
            }
        }, 3000);
    }

    public void getImageUrl() {
        WebBase.getWelcomeAdverts(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                findViewById(R.id.tv_jump).setVisibility(View.VISIBLE);
                JSONArray adverts = obj.optJSONArray("adverts");
                int size = adverts.length();
                if (url_list == null) {
                    url_list = new ArrayList<HomeImage>();
                } else {
                    url_list.clear();
                }
                if (views == null) {
                    views = new ArrayList<ImageView>();
                } else {
                    views.clear();
                }
                for (int i = 0; i < size; i++) {
                    JSONObject oj = adverts.optJSONObject(i);
                    if (oj != null) {
                        HomeImage h = new HomeImage();
                        h.setType("link");
                        h.setId(oj.optString("advert_id"));
                        h.setTitle(oj.optString("subject"));
                        h.setLink_url(oj.optString("jump_url"));
                        h.setImg_url(oj.optString("img_url"));
                        url_list.add(h);
                    }
                }
                initHandler();
                up_data_img();
            }

            @Override
            public void onFailure(String err_msg) {
                initHandler();
            }
        });
    }

    public void up_data_img() {
        if (cycleViewPager == null && getFragmentManager() != null) {
            cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
        }
        views.clear();
        for (int i = 0; i < url_list.size(); i++) {
            views.add(ViewFactory.getLanchImageView(LaunchActivity.this, url_list.get(i).getImg_url()));
//            views.add(ViewFactory.getImgView(LaunchActivity.this, url_list.get(i).getImg_url()));
        }
        cycleViewPager.setCycle(false);
        cycleViewPager.setData(views, url_list, mAdCycleViewListener);
        cycleViewPager.setWheel(false);
        cycleViewPager.setTime(2000);
        cycleViewPager.setIndicatorLayout(View.GONE);
    }

    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {
        @Override
        public void onImageClick(HomeImage info, int position, View imageView) {
            String url = info.getLink_url();
            if (url != null || !TextUtils.isEmpty(url)) {
                if (url.equals(Constants.VIP_SKIP_URL)) {
                    ShowActivity.skipVIPActivity(LaunchActivity.this);
                } else if (url.equals(Constants.SELECT_STOCK_SKIP_URL)) {
                    ShowActivity.skipZbmfSelectStockActivity(LaunchActivity.this);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(IntentKey.WELCOME_URL, info.getLink_url());
                    Intent intent = new Intent(LaunchActivity.this, GroupActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
            commit = true;
            /*Bundle bundle = new Bundle();
            bundle.putString(IntentKey.WELCOME_URL, info.getLink_url());
            Intent intent = new Intent(LaunchActivity.this, GroupActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);*/
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    private void vers() {
        WebBase.vers(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                Vers vers = JSONParse.vers(obj);
                AppUrl.ALL_URL_PREFIX = vers.getPassport().getHost() + vers.getPassport().getApi();
                AppUrl.ALL_URL = vers.getGroup().getHost() + vers.getGroup().getApi();
                AppUrl.Walle_URL = vers.getWww().getHost() + vers.getWww().getApi();
                AppUrl.MATCH_URL = vers.getMatch().getHost() + vers.getMatch().getApi();
                AppUrl.STOCK_URL = vers.getStock().getHost() + vers.getStock().getApi();
                getImageUrl();
            }

            @Override
            public void onFailure(String err_msg) {
                initHandler();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jump:
                commit = true;
                startActivity(new Intent(getApplication(), GroupActivity.class));
                LaunchActivity.this.finish();
                break;
        }
    }
}
