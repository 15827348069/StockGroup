package com.zbmf.StockGroup.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.ShareBean;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.BitmapUtil;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;

/**
 * Created by xuhao on 2016/6/20.
 */
public class InviteShareLayout extends LinearLayout implements WbShareCallback{
    private Activity activity;
    private String share_title,share_desc,share_img;
    private String share_url="http://center.zbmf.com/register?uid=";
    private ImageButton share_wx_friend,share_wx_friend_circle,share_sina,share_qq;

    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;
    private WbShareHandler shareHandler;
    private int mShareType = SHARE_ALL_IN_ONE;
    public  Bitmap bitmap;
    public InviteShareLayout(Context context) {
        super(context);
        showShareLayout(context);
    }

    public InviteShareLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        showShareLayout(context);
    }

    public InviteShareLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        showShareLayout(context);
    }
    public void init(Activity activitys, ShareBean shareBean){
        this.activity=activitys;
        this.share_title=shareBean.getTitle();
        this.share_desc=shareBean.getDesc();
        this.share_url=shareBean.getShareUrl();
        this.share_img=shareBean.getImg();
        this.shareHandler = new WbShareHandler(activitys);
        this.shareHandler.registerApp();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                bitmap= BitmapUtil.getBitmap(share_img,activity);
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
                        webpage.webpageUrl=share_url;
                        WXMediaMessage msg=new WXMediaMessage(webpage);
                        msg.title=share_title;
                        if(bitmap!=null){
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                            byte[] bitmap_byte= BitmapUtil.bmpToByteArray(thumbBmp,true);
                            msg.thumbData=bitmap_byte;
                        }
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());
                        req.message = msg;
                        req.scene= SendMessageToWX.Req.WXSceneSession;
                        IWXAPI api = WXAPIFactory.createWXAPI(activity,Constants.WEI_APK_KEY,true);
                        api.sendReq(req);
                    }
                });
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
                        webpage.webpageUrl=share_url;
                        WXMediaMessage msg=new WXMediaMessage(webpage);
                        msg.title=share_title;
                        if(bitmap!=null){
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                            byte[] bitmap_byte=BitmapUtil.bmpToByteArray(thumbBmp,true);
                            msg.thumbData=bitmap_byte;
                        }
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());
                        req.message = msg;
                        req.scene= SendMessageToWX.Req.WXSceneTimeline;
                        IWXAPI api = WXAPIFactory.createWXAPI(activity, Constants.WEI_APK_KEY,true);
                        api.sendReq(req);

                    }
                });
            }
        });
        share_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享到新浪
                WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                weiboMessage.mediaObject = getWebpageObj();
                shareHandler.shareMessage(weiboMessage, mShareType == SHARE_CLIENT);
            }
        });
        share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享到QQ
                shareQQ();
            }
        });
    }
    public void showShareLayout(Context context){
        View aDialog = LayoutInflater.from(context).inflate(R.layout.linear_share, null);
        addView(aDialog, -1, -2);
        share_wx_friend =(ImageButton) aDialog.findViewById(R.id.share_wx_friend);
        share_wx_friend_circle =(ImageButton) aDialog.findViewById(R.id.share_wx_friend_circle);
        share_sina=(ImageButton) aDialog.findViewById(R.id.share_sina);
        share_qq=(ImageButton) aDialog.findViewById(R.id.share_qq);
    }
    public void shareQQ(){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, activity.getString(R.string.app_name));
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,share_title);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share_url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share_img);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getString(R.string.app_name));
        Tencent mTencent= Tencent.createInstance(Constants.TencentSDKAppKey,activity);
        mTencent.shareToQQ(activity, params, new IUiListener() {
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
        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
    }
    /**
     * 获取分享的文本模板。
     */
    private String getActivitySharedText(String activity_time) {
        String format = activity.getString(R.string.weibosdk_share_webpage_template);
        String text = String.format(format,activity.getString(R.string.app_name)+"分享【"+activity_time+"】");
        return text;
    }
    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title =getActivitySharedText(share_title);
        mediaObject.description =share_desc;
        Bitmap  bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_share_invite);
        // 设置 Bitmap 类型的图片到视频对象里  设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl =share_url+ SettingDefaultsManager.getInstance().UserId();
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }


    public void onNewIntent(Intent intent) {
        shareHandler.doResultIntent(intent,this);
    }

    @Override
    public void onWbShareSuccess() {
        Toast.makeText(activity, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWbShareCancel() {
        Toast.makeText(activity, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onWbShareFail() {
        Toast.makeText(activity, R.string.weibosdk_demo_toast_share_failed, Toast.LENGTH_LONG).show();
    }
}
