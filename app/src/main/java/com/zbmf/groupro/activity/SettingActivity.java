package com.zbmf.groupro.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.groupro.R;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.Vers;
import com.zbmf.groupro.db.DBManager;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.FileUtils;
import com.zbmf.groupro.utils.JSONParse;
import com.zbmf.groupro.utils.SendBrodacast;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.utils.UpdateManager;
import com.zbmf.groupro.view.CustomProgressDialog;

import org.json.JSONObject;

/**
 * 设置页
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_version;
    private String version;
    private boolean isTip = false;
    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setupView();
    }

    private void setupView() {
        TextView tv_title = ((TextView) findViewById(R.id.group_title_name));
        tv_title.setText("设置");
        tv_title.setVisibility(View.VISIBLE);

        findViewById(R.id.group_title_return).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        findViewById(R.id.tv_clear).setOnClickListener(this);
        findViewById(R.id.tv_bind).setOnClickListener(this);
        findViewById(R.id.tv_check).setOnClickListener(this);
        findViewById(R.id.about_us).setOnClickListener(this);
        findViewById(R.id.yj_back).setOnClickListener(this);
        findViewById(R.id.textView2).setOnClickListener(this);
        tv_version = (TextView)findViewById(R.id.tv_version);
        SwitchCompat switch_music = (SwitchCompat) findViewById(R.id.switch_music);
        switch_music.setChecked(SettingDefaultsManager.getInstance().getMessageAll());
        switch_music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingDefaultsManager.getInstance().setMessageAll(isChecked);
            }
        });

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

    private void vers(final boolean b) {
        WebBase.vers(new JSONHandler(b,this,"正在检查新版本") {
            @Override
            public void onSuccess(JSONObject obj) {
                Vers vers = JSONParse.vers(obj);
                if (version.compareTo(vers.getVersion()) < 0) {
                    UpdateManager update = new UpdateManager(SettingActivity.this);
                    update.setApkUrl(vers.getUrl());
                    update.setForce(true);
                    update.setVersion(vers.getVersion());
                    update.checkUpdateInfo(true);
                }else{
                    if(b)
                        Toast.makeText(SettingActivity.this,"已是最新版本!",0).show();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(SettingActivity.this,err_msg,0).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_title_return:
                finish();
                break;
            case R.id.tv_check:
                vers(true);
                break;
            case R.id.btn_logout:
                log_out();

                break;
            case R.id.tv_clear:
                new ClearCacheTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.tv_bind:
                ShowActivity.showActivity(this,AccountBindActivity.class);
                break;
            case R.id.about_us:
                ShowActivity.showWebViewActivity(this,"https://center.zbmf.com/app/system/about/");
                break;
            case R.id.yj_back:
                ShowActivity.showWebViewActivity(this,"https://center.zbmf.com/app/system/suggest/");
                break;
            case R.id.textView2:
                ShowActivity.showActivity(this,ResetPwdActivity.class);
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
            boolean cleared = FileUtils.deletAllCacheFiles();
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
        Intent intent = new Intent(Constants.NEW_LIVE_MSG_READ);
        sendBroadcast(intent);
        dbManager.closeDB();
        WebBase.logout(new JSONHandler(true,SettingActivity.this,"正在退出登陆...") {
            @Override
            public void onSuccess(JSONObject obj) {
                SettingDefaultsManager.getInstance().clearUserInfo();
                SendBrodacast.send(SettingActivity.this,Constants.LOGOUT);
                finish();
            }
            @Override
            public void onFailure(String err_msg) {
                SettingDefaultsManager.getInstance().clearUserInfo();
                SendBrodacast.send(SettingActivity.this,Constants.LOGOUT);
                finish();
            }
        });
    }

}
