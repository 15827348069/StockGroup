package com.zbmf.StockGroup.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Vers;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.db.DBManager;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.FileUtils;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SendBrodacast;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.utils.UpdateManager;
import com.zbmf.StockGroup.view.CustomProgressDialog;

import org.json.JSONObject;

import java.io.File;

/**
 * 设置页
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_version;
    private String version;
    private boolean isTip = false;
    private DBManager dbManager;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        initTitle("设置");
        tv_version = (TextView)findViewById(R.id.tv_version);
        SwitchCompat switch_music = (SwitchCompat) findViewById(R.id.switch_music);
        switch_music.setChecked(SettingDefaultsManager.getInstance().getMessageAll());
        switch_music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingDefaultsManager.getInstance().setMessageAll(isChecked);
            }
        });
    }

    @Override
    public void initData() {
        PackageManager pm = getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(getPackageName(), 0);
            version = pi.versionName;
            tv_version.setText("炒股圈子"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        vers(false);
        dbManager=new DBManager(getBaseContext());
    }

    @Override
    public void addListener() {
        findViewById(R.id.btn_logout).setOnClickListener(this);
        findViewById(R.id.tv_clear).setOnClickListener(this);
        findViewById(R.id.tv_bind).setOnClickListener(this);
        findViewById(R.id.tv_check).setOnClickListener(this);
        findViewById(R.id.about_us).setOnClickListener(this);
        findViewById(R.id.yj_back).setOnClickListener(this);
        findViewById(R.id.textView2).setOnClickListener(this);
    }


    private void vers(final boolean b) {
        WebBase.vers(new JSONHandler(b,this,"正在检查新版本") {
            @Override
            public void onSuccess(JSONObject obj) {
                Vers vers = JSONParse.vers(obj);
                if (version.compareTo(vers.getVersion()) < 0) {
                    UpdateManager update = new UpdateManager(SettingActivity.this);
                    update.setApkUrl(vers.getUrl());
                    update.setForce(false);
                    update.setVersion(vers.getVersion());
                    update.checkUpdateInfo(true);
                }else{
                    if(b)
                        Toast.makeText(SettingActivity.this,"已是最新版本!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(SettingActivity.this,err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_check:
                vers(true);
                break;
            case R.id.btn_logout:
                log_out();
                break;
            case R.id.tv_clear:
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(SettingActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            RequestCode.WRITE_EXTERNAL_STORAGE);
                } else {
                    new ClearCacheTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                break;
            case R.id.tv_bind:
                ShowActivity.showActivity(this,AccountBindActivity.class);
                break;
            case R.id.about_us:
                ShowActivity.showWebViewActivity(this, HtmlUrl.ABOUT_US);
                break;
            case R.id.yj_back:
                ShowActivity.showWebViewActivity(this,HtmlUrl.MESSAGE_BACK);
                break;
            case R.id.textView2:
                ShowActivity.showActivity(this,ResetPwdActivity.class);
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case RequestCode.WRITE_EXTERNAL_STORAGE:
                new ClearCacheTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
        }
    }
    private CustomProgressDialog progressDialog;

    class ClearCacheTask extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog==null){
                progressDialog= CustomProgressDialog.createDialog(SettingActivity.this);
            }
            progressDialog.setMessage("正在清除缓存...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean cleared = FileUtils.getIntence().deletAllCacheFiles(SettingActivity.this);
            try {
                if(cleared)
                    Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cleared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            if(aBoolean)
            Toast.makeText(SettingActivity.this,"缓存清理成功",Toast.LENGTH_SHORT).show();
            else  Toast.makeText(SettingActivity.this,"清理失败",Toast.LENGTH_SHORT).show();
        }
    }
    public void log_out(){
        dbManager.setUnablemessage(null);
        WebBase.logout(new JSONHandler(true,SettingActivity.this,"正在退出登录...") {
            @Override
            public void onSuccess(JSONObject obj) {
                finish();
            }
            @Override
            public void onFailure(String err_msg) {
                finish();
            }
        });
        Intent intent = new Intent(Constants.NEW_LIVE_MSG_READ);
        sendBroadcast(intent);
        Intent intent2 = new Intent(Constants.UPDATE_VIDEO_LIST);
        sendBroadcast(intent2);
        SettingDefaultsManager.getInstance().clearUserInfo();
        SendBrodacast.send(SettingActivity.this,Constants.LOGOUT);
        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");
    }
}
