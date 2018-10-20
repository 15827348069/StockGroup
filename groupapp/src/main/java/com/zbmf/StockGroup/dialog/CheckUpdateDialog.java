package com.zbmf.StockGroup.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.zbmf.StockGroup.R;

public class CheckUpdateDialog {
    private Activity activity;
    private boolean isDebug;

    public CheckUpdateDialog(Activity activity, boolean isDebug) {
        this.activity = activity;
        this.isDebug = isDebug;
    }

    public void checkUpdate() {
        if (isDebug) {
            PgyUpdateManager.register(activity,"com.zbmf.StockGroup.fileprovider", new UpdateManagerListener() {
                @Override
                public void onNoUpdateAvailable() {

                }

                @Override
                public void onUpdateAvailable(String s) {
                    final AppBean appBean = getAppBeanFromString(s);
                    if (Integer.parseInt(appBean.getVersionCode()) >
                            getVersionCode(activity, activity.getPackageName())) {
                        new AlertDialog.Builder(activity)
                                .setTitle(getVersionName(activity, activity.getPackageName())+activity.getResources().getString(R.string.update_hint))
                                .setMessage(appBean.getReleaseNote())
                                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startDownloadTask(activity, appBean.getDownloadURL());
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }
                }
            });
        }
    }
    private  final int getVersionCode(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    private  final String getVersionName(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
