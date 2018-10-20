package com.zbmf.StocksMatch.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.GenApiHashUrl;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.beans.VersionInfo;
import com.zbmf.StocksMatch.db.Database;
import com.zbmf.StocksMatch.db.DatabaseImpl;
import com.zbmf.StocksMatch.utils.AssetsDatabaseManager;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.SharedPreferencesUtils;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.utils.UpdateManager;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;

public class RootActivity extends ExActivity {

    private static final int MSG_REFERSH = 0x1230;

    private Database db;
    private SharedPreferencesUtils shUtils;

    private String curr;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        UiCommon.widthPixels = dm.widthPixels;
        UiCommon.higthPixels = dm.heightPixels;

        Log.e("tag",dm.widthPixels+"");
        shUtils = new SharedPreferencesUtils(this);
        db = new DatabaseImpl(this);
        curr = shUtils.getAccount();

        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        mg.getDatabase("StockList.db");//初始化股票数据库


        user = db.getUser(curr);
        UiCommon.INSTANCE.setiUser(user);



        insertDummyContactWrapper();

        UiCommon.VERSION_NAME = UiCommon.INSTANCE.getCurr_VersionName();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void goNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (TextUtils.isEmpty(UiCommon.INSTANCE.getiUser().getAuth_token())) {
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_LOGIN, null);
                    finish();
                } else {
                    UiCommon.INSTANCE.setiUser(user);
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_MAIN, null);
                    finish();
                }
            }
        }, 2500);
    }

    private void insertDummyContactWrapper() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},Constants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }else
            new Version(this).execute();
    }

        private void showMessageDialog() {
            showMessageOKCancel(getString(R.string.permission_tip),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                                    finish();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
    //                                goNext();
                                    finish();
                                    break;
                            }
                        }
                    });
        }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this).setTitle(R.string.permission_apply)
                .setMessage(message)
                .setPositiveButton(getString(R.string.goset), okListener)
                .setNegativeButton(getString(R.string.cancel), okListener)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goNext();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showMessageDialog();//用户拒绝给出一些提示
                    return;
                }
                showMessageDialog();
            }
        }
    }

    private class Version extends LoadingDialog<Void, VersionInfo> {

        public Version(Context activity) {
            super(activity, R.string.loading, R.string.load_fail1,false);
        }

        @Override
        public VersionInfo doInBackground(Void... params) {
            Get2ApiImpl server = new Get2ApiImpl();
            try {
                return server.version();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(VersionInfo versionInfo) {
            if (versionInfo != null) {
                if (versionInfo.getStatus()==1) {
//                    versionInfo.setVersion("1.2.10");
                    GenApiHashUrl.apiUrlm=versionInfo.getAppUrl();
                    GenApiHashUrl.loginUrl=versionInfo.getAppHost();
                    if (UiCommon.VERSION_NAME.compareTo(versionInfo.getVersion())<0) {
                        // 有新版本
                        UpdateManager update = new UpdateManager(RootActivity.this);
                        update.setApkUrl(versionInfo.getUrl());
                        update.setForce(Boolean.parseBoolean(versionInfo.getForce()));
                        update.setVersion(versionInfo.getVersion());
                        update.checkUpdateInfo();
                    }else{
                        goNext();
                    }
                }/*else if(versionInfo.getCode()==1004){
                    UiCommon.INSTANCE.showTip(getString(R.string.err_tip));
                    shUtils.setAccount("");
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_LOGIN,null);
                }*/else{
                    UiCommon.INSTANCE.showTip(versionInfo.msg);
                }

            } else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

}
