package com.zbmf.StockGroup.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.RemoteViews;

import com.zbmf.StockGroup.GroupActivity;
import com.zbmf.StockGroup.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class UpdateManager {
    private static final String TAG = "UpdateManager";

    private Context mContext;

    NotificationManager notificationManager = null;
    Notification notification = null;
    int notifyId = 1012;

    Boolean isDownloading = false;

    // 提示语
    private String updateMsg = null;

    // 返回的安装包url
    private String apkUrl = null;

    private String version = null;

    private String name = "炒股圈子";

    private String apkName = "mcc_pro";

    private Boolean force = false;

    private Dialog noticeDialog = null;

    /* 下载包安装路径 */
    String savePath = null;
    String saveFileName = null;

    /* 进度条与通知ui刷新的handler和msg常量 */
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;

    private int progress = 0;

    private boolean interceptFlag = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE: {
                    Log.i("--TAG","--   下载进度  :"+progress);
                    notification.contentView.setTextViewText(R.id.tv_progress_title, "下载中...  " + name + version + "  " + progress + "%");
//                    notification.contentView.setProgressBar(R.id.progressbar, 100, progress, false);
                    mRemoteViews.setProgressBar(R.id.progressbar,100,progress,false);
                    mBuilder.setProgress(100,progress,false);
                    if (notificationManager!=null){
                        notificationManager.notify(notifyId, notification);
                    }
                }
                break;

                case DOWN_OVER: {
                    if (notificationManager!=null) {
                        notificationManager.cancelAll();
                    }
                    installApk();
                }
                break;

                default:

                    break;
            }
        }
    };
    private RemoteViews mRemoteViews;
    private Notification.Builder mBuilder;


    public UpdateManager(Context context) {
        this.mContext = context;
    }


    // 外部接口让主Activity调用
    public void checkUpdateInfo(boolean isConfirm) {
        if (apkUrl == null || version == null || isDownloading) {
            return;
        }
        String packageName = mContext.getPackageName() + "/downloads/";
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            savePath = "/sdcard/" + packageName;

        } else {

            savePath = "/data/data/" + packageName;
        }

        saveFileName = savePath + apkName + version + ".apk";

        if (isConfirm)
            showNoticeDialog();
        else
            showDownloadNotification();
    }


    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadNotification();
            }
        });

        String title = null;

        if (force == true) {
            title = "退出";
        } else {

            title = "以后再说";
        }


        builder.setNegativeButton(title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 强制更新
                if (force == true) {
                    ActivityUtil.removeAllActivity();
                } else {
                    dialog.dismiss();
                    if (mContext instanceof GroupActivity) {
                        GroupActivity activity = (GroupActivity) mContext;
                        activity.goNext();
                    }
                }
            }
        });

        noticeDialog = builder.create();
        noticeDialog.setCanceledOnTouchOutside(false);
        noticeDialog.show();
    }

    private void showDownloadNotification() {
//        NoticeUtil.showDownloadNotification(mContext,version);
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // 通知栏显示所用到的布局文件
        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.view_progress);
        mRemoteViews.setTextViewText(R.id.tv_progress_title, "下载中..." + name + version + " " + progress + "%");
        mRemoteViews.setProgressBar(R.id.progressbar, 100, progress, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel download = new NotificationChannel(String.valueOf(notifyId), "download",
                    NotificationManager.IMPORTANCE_DEFAULT);
            download.setName("下载");
            download.enableLights(false);
            download.enableVibration(false);
            download.setImportance(NotificationManager.IMPORTANCE_DEFAULT);//设置为low, 通知栏不会有声音
            notificationManager.createNotificationChannel(download);
            mBuilder = new Notification.Builder(mContext, String.valueOf(notifyId));
            mBuilder.setSmallIcon(R.drawable.logo_icon)
                    .setChannelId(String.valueOf(notifyId))
                    .setContentTitle("正在下载...")
                    .setContentText(name + version)
                    .setCustomContentView(mRemoteViews)
                    .setPriority(Notification.PRIORITY_HIGH) //设置该通知优先级
                    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                    .setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(),  PendingIntent.FLAG_UPDATE_CURRENT))
                    .setNumber(3);
            notification = mBuilder.build();
            notification.contentView = mRemoteViews;
        } else {
            notification = new Notification();
            notification.icon = R.drawable.logo_icon;
            notification.tickerText = "正在下载" + name + version;
            notification.contentView = mRemoteViews;

            notification.contentIntent = PendingIntent.getActivity(mContext, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        }
        notificationManager.notify(notifyId, notification);
        if (mContext instanceof GroupActivity) {//todo..........
            GroupActivity activity = (GroupActivity) mContext;
            activity.goNext();
        }
        downloadApk();
    }


    /**
     * 下载apk
     */
    private void downloadApk() {
        LogUtil.e("开始下载");
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apkUrl);

                    // 打开到url的连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    int length = connection.getContentLength();

                    // 以下为java IO部分，大体来说就是先检查文件夹是否存在，不存在则创建, 然后的文件名重复问题，没有考虑
                    InputStream inputStream = connection.getInputStream();

                    File file = new File(savePath);

                    LogUtil.e("文件目录为: " + savePath + ",url" + apkUrl);
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    String apkFile = saveFileName;

                    File ApkFile = new File(apkFile);


                    OutputStream outputStream = new FileOutputStream(ApkFile);

                    int count = 0;

                    byte buff[] = new byte[1024 * 4];

                    int nextNotification = 10;

                    do {
                        int numread = inputStream.read(buff);

                        count += numread;

                        progress = (int) (((float) count / length) * 100);

                        //Log.e(TAG, "当前下载进度:" + progress);

                        if (progress == nextNotification) {
                            nextNotification += 5;

                            mHandler.sendEmptyMessage(DOWN_UPDATE);
                        }


                        if (numread <= 0) {
                            // 下载完成通知安装
                            mHandler.sendEmptyMessage(DOWN_OVER);
                            break;
                        }

                        outputStream.write(buff, 0, numread);

                    } while (!interceptFlag); // 点击取消就停止下载.

                    isDownloading = true;

                    LogUtil.e("下载结束");
                    outputStream.close();
                    inputStream.close();

                    if (force == true) {
                        //System.exit(0);
//                        HomeActivity.quit();
                    }

                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 安装apk
     */
    public void installAPK(String file) {
        File apkFile = new File(file);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.wy.toy.FileProvider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }

    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File file = (new File(saveFileName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.zbmf.StockGroup.provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        } else {
            Uri uri = Uri.fromFile(new File(saveFileName));
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
        updateMsg = "已有新的版本V" + version + "提供下载";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public void setUpdateMsg(String updateMsg) {
        this.updateMsg = updateMsg;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }

}