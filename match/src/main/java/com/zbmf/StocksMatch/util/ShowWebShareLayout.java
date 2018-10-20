package com.zbmf.StocksMatch.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.ShareBean;
import com.zbmf.StocksMatch.common.Constans;

import java.io.IOException;
import java.net.URL;

/**
 * Created by xuhao
 * on 2016/6/20.
 */
public class ShowWebShareLayout implements WbShareCallback {
    private Activity webActivity;
    private static final int SHARE_CLIENT = 1;
    private static final int SHARE_ALL_IN_ONE = 2;
    private WbShareHandler shareHandler;
    private int mShareType = SHARE_ALL_IN_ONE;
    public ShowWebShareLayout(Activity activity){
        this.webActivity=activity;
        this.shareHandler = new WbShareHandler(activity);
        this.shareHandler.registerApp();
    }
    public void showShareLayout(final ShareBean ab){
        final Dialog aDialog = new Dialog(webActivity, R.style.myDialogTheme);
        aDialog.getWindow().setContentView(R.layout.activity_share);
        aDialog.setCanceledOnTouchOutside(true);
        aDialog.setCancelable(true);
        Window win = aDialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        Button share_wx_friend= aDialog.findViewById(R.id.share_wx_friend);
        Button share_wx_friend_circle= aDialog.findViewById(R.id.share_wx_friend_circle);
        Button share_sina=aDialog.findViewById(R.id.share_sina);
        Button share_qq= aDialog.findViewById(R.id.share_qq);
        Button cause_share= aDialog.findViewById(R.id.cause_share);
        cause_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aDialog.dismiss();
            }
        });
        share_wx_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享到微信
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        WXWebpageObject webpage=new WXWebpageObject();
                        webpage.webpageUrl=ab.getShareUrl();
                        WXMediaMessage msg=new WXMediaMessage(webpage);
                        msg.title=ab.getTitle();
                        Bitmap bmp ;
                        try {
                            bmp = BitmapFactory.decodeStream(new URL(ab.getImg()).openStream());
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                            msg.thumbData= BitmapUtil.bmpToByteArray(thumbBmp,true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());
                        req.message = msg;
                        req.scene= SendMessageToWX.Req.WXSceneSession;
                        IWXAPI api = WXAPIFactory.createWXAPI(webActivity, Constans.WEI_APK_KEY,true);
                        api.sendReq(req);
                    }
                });
                aDialog.dismiss();
            }
        });
        share_wx_friend_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享到朋友圈
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        WXWebpageObject webpage=new WXWebpageObject();
                        webpage.webpageUrl=ab.getShareUrl();
                        WXMediaMessage msg=new WXMediaMessage(webpage);
                        msg.title=ab.getTitle();
                        Bitmap bmp ;
                        try {
                            bmp = BitmapFactory.decodeStream(new URL(ab.getImg()).openStream());
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                            msg.thumbData= BitmapUtil.bmpToByteArray(thumbBmp,true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());
                        req.message = msg;
                        req.scene= SendMessageToWX.Req.WXSceneTimeline;

                        IWXAPI api = WXAPIFactory.createWXAPI(webActivity, Constans.WEI_APK_KEY,true);
                        api.sendReq(req);

                    }
                });
                aDialog.dismiss();
            }
        });
        share_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        //分享到新浪
                        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                        weiboMessage.mediaObject = getWebpageObj(ab);
                        shareHandler.shareMessage(weiboMessage, mShareType == SHARE_CLIENT);
                        aDialog.dismiss();
                    }
                });

            }
        });
        share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享到QQ
                shareQQ(ab);
                aDialog.dismiss();
            }
        });
        aDialog.show();
    }
    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(final ShareBean ab) {
         WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = getActivitySharedText(ab.getTitle());
        mediaObject.description=ab.getDesc();
        Bitmap bitmap=BitmapUtil.getBitmap(ab.getImg(),webActivity);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl =ab.getShareUrl();
        mediaObject.defaultText ="Webpage 默认文案";
        return mediaObject;
    }
    private String getActivitySharedText(String activity_time) {
        String format = webActivity.getString(R.string.weibosdk_share_webpage_template);
        return String.format(format,webActivity.getString(R.string.app_name)+"分享【"+activity_time+"】");
    }
    public void shareQQ(ShareBean ab){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, webActivity.getString(R.string.app_name));
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, ab.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, ab.getShareUrl());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ab.getImg());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, webActivity.getString(R.string.app_name));
        Tencent mTencent= Tencent.createInstance(Constans.TencentSDKAppKey,webActivity);
        mTencent.shareToQQ(webActivity, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                ShowToast("分享成功");
            }

            @Override
            public void onError(UiError uiError) {
                ShowToast("分享失败");
            }

            @Override
            public void onCancel() {
                ShowToast("取消分享");
            }
        });
    }

    public void ShowToast(String message){
        Toast.makeText(webActivity,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWbShareSuccess() {
        Toast.makeText(webActivity, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onWbShareCancel() {
        Toast.makeText(webActivity, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onWbShareFail() {
        Toast.makeText(webActivity, R.string.weibosdk_demo_toast_share_failed, Toast.LENGTH_LONG).show();
    }
    public void onNewIntent(Intent intent){
        shareHandler.doResultIntent(intent,this);
    }
}
