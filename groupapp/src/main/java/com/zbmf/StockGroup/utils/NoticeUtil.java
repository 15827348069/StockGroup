package com.zbmf.StockGroup.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.zbmf.StockGroup.GroupActivity;
import com.zbmf.StockGroup.R;

/**
 * Created by pq
 * on 2018/8/15.
 */

public class NoticeUtil {

//    private static String version = null;

    public static void showDownloadNotification(Context mContext,String version) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // 通知栏显示所用到的布局文件
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.view_progress);
        String name = "炒股圈子";
        int progress = 0;
        remoteViews.setTextViewText(R.id.tv_progress_title, "下载中..." + name + version + " " + progress + "%");
        remoteViews.setProgressBar(R.id.progressbar, 100, progress, false);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel download = new NotificationChannel("1", "download", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(download);
            Notification.Builder builder = new Notification.Builder(mContext, "1");
            builder.setSmallIcon(R.drawable.logo_icon)
                    .setContentTitle("正在下载...")
                    .setContentText(name + version)
                    .setCustomContentView(remoteViews)
                    .setPriority(Notification.PRIORITY_HIGH) //设置该通知优先级
                    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                    .setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(), 0))
                    .setNumber(3);
            notification = builder.build();
        } else {
            notification = new Notification();
            notification.icon = R.drawable.logo_icon;
            notification.tickerText = "正在下载" + name + version;
            notification.contentView = remoteViews;

            notification.contentIntent = PendingIntent.getActivity(mContext, 0, new Intent(), 0);
        }
        int notifyId = 1012;
        notificationManager.notify(notifyId, notification);
        if (mContext instanceof GroupActivity) {//todo..........
            GroupActivity activity = (GroupActivity) mContext;
            activity.goNext();
        }
//        downloadApk();
    }

    /*public static void showNoticeLayout(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(getId()),
                    "notice", NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(context, String.valueOf(getId()));
            builder.setSmallIcon(R.drawable.logo_icon)
                    .setContentTitle(getSubject())
                    .setContentText(getContent())
                    .setContentIntent(getPushIntent(context))
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)
                    .setPriority(Notification.PRIORITY_HIGH) //设置该通知优先级
                    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                    .setTicker(getContent()) //通知首次出现在通知栏，带上升动画效果的
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.drawable.turtle);//设置通知小ICON
            notification = builder.build();
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setContentTitle(getSubject())
                    .setContentText(getContent())
                    .setContentIntent(getPushIntent(context))
                    // .setNumber(number)
                    .setTicker(getContent())
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.drawable.logo_icon);
            notification = mBuilder.build();
        }
        mNotificationManager.notify(getId(), notification);
    }*/
}
