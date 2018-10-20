package com.zbmf.StocksMatch.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.bean.ShareBean;
import com.zbmf.StocksMatch.common.AppConfig;
import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.SharedKey;
import com.zbmf.StocksMatch.util.BitmapUtil;
import com.zbmf.StocksMatch.util.SettingDefaultsManager;
import com.zbmf.worklibrary.util.SharedpreferencesUtil;

/**
 * Created by pq
 * on 2018/4/10.
 */

public class ShareDialog extends Dialog implements WbShareCallback/*, IWeiboHandler.Response */ {
    private Context context;
    private Button share_wx_friend, share_wx_friend_circle, share_sina, share_qq, mCancelShare;
    private Activity activity;
    private String share_title, share_desc, share_img;
    private String share_url = "http://center.zbmf.com/register?uid=";
    private String baseUrl = "http://m.zbmf.com/match/share/?match_id=";//
    private String id = "1039";
    public static IWXAPI api;
    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;
    private WbShareHandler shareHandler;
    private int mShareType = SHARE_ALL_IN_ONE;
    public Bitmap bitmap, imgBitmap, textBit;
    private String shareUrl;
    private String imgUrl;
    private int flag;
    private String path;
    private String shareText;
    private String webShareUrl;

    public ShareDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public ShareDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected ShareDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public ShareDialog setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    public ShareDialog setPath(String path) {
        this.path = path;
        return this;
    }

    public ShareDialog setShareWebUrl(String webShareUrl) {
        this.webShareUrl = webShareUrl;
        return this;
    }

    public ShareDialog setShareImg(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
        return this;
    }

    public ShareDialog setShareText(String text, Bitmap textBit) {
        this.shareText = text;
        this.textBit = textBit;
        return this;
    }

    public ShareDialog createDialog(Activity activity, String id) {
        this.activity = activity;
        this.id = id;
        View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_share, null);
        //获得dialog的window窗口
        Window window = getWindow();
        //设置dialog在屏幕底部
        assert window != null;
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        this.setContentView(dialogView);
        share_wx_friend = (Button) dialogView.findViewById(R.id.share_wx_friend);
        share_wx_friend_circle = (Button) dialogView.findViewById(R.id.share_wx_friend_circle);
        share_sina = (Button) dialogView.findViewById(R.id.share_sina);
        share_qq = (Button) dialogView.findViewById(R.id.share_qq);
        mCancelShare = (Button) dialogView.findViewById(R.id.cause_share);
        String uID = SharedpreferencesUtil.getInstance()
                .getString(SharedKey.USER_ID, "");
        init(activity, new ShareBean(context.getString(R.string.invite_share_title),
                AppConfig.SHARE_IMG, AppConfig.SHARE_URL + uID,
                context.getString(R.string.invite_share_content)));
        return this;
    }

    public void init(Activity activitys, ShareBean shareBean) {
        this.activity = activitys;
        this.shareHandler = new WbShareHandler(activitys);
        this.shareHandler.registerApp();
        this.share_title = shareBean.getTitle();
        this.share_desc = shareBean.getDesc();
        this.share_url = shareBean.getShareUrl();
        this.share_img = shareBean.getImg();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                bitmap = BitmapUtil.getBitmap(share_img, activity);
            }
        });
        //分享到微信
        share_wx_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == Constans.WX_SHARE_WEB) {
                    shareToWebchat(SendMessageToWX.Req.WXSceneSession);
                } else if (flag == Constans.WX_SHARE_TEXT) {
                    createWXApi();
                    iWXShareText(shareText, SendMessageToWX.Req.WXSceneSession, textBit);
                } else if (flag == Constans.WX_SHARE_IMG) {
                    createWXApi();
                    iWXPicShare(SendMessageToWX.Req.WXSceneSession, imgBitmap);
                }
                dissMissI();
            }
        });
        share_wx_friend_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == Constans.WX_SHARE_WEB) {
                    shareToWebchat(SendMessageToWX.Req.WXSceneTimeline);
                } else if (flag == Constans.WX_SHARE_TEXT) {
                    createWXApi();
                    iWXShareText(shareText, SendMessageToWX.Req.WXSceneTimeline, textBit);
                } else if (flag == Constans.WX_SHARE_IMG) {
                    createWXApi();
                    iWXPicShare(SendMessageToWX.Req.WXSceneTimeline, imgBitmap);
                }
                //分享到朋友圈
                dissMissI();
            }
        });
        share_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享到新浪
                WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                weiboMessage.mediaObject = getWebpageObj();
                shareHandler.shareMessage(weiboMessage, mShareType == SHARE_CLIENT);
                dissMissI();
            }
        });
        share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享到QQ
                shareQQ();
                dissMissI();
            }
        });
        mCancelShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissMissI();
            }
        });
    }

    public void shareQQ() {
        final Bundle params = new Bundle();
        if (flag == Constans.WX_SHARE_WEB) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, activity.getString(R.string.app_name));
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, context.getString(R.string.invite_share_title));
            if (TextUtils.isEmpty(shareUrl)) {
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share_url);
            } else {
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
            }
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share_img);
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getString(R.string.app_name));
        } else if (flag == Constans.WX_SHARE_IMG) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,path);
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getString(R.string.app_name));
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        }
        Tencent mTencent = Tencent.createInstance(Constans.TencentSDKAppKey, activity);
        mTencent.shareToQQ(activity, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Toast.makeText(context, context.getString(R.string.share_success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(context, context.getString(R.string.share_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, context.getString(R.string.cancel_share), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    微信分享------------------------

    /**
     * 微信网页类型分享
     *
     * @param scene
     */
    private void shareToWebchat(int scene) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(context, Constans.WEI_APK_KEY, false);
            api.registerApp(Constans.WEI_APK_KEY);
        }
        if (!api.isWXAppInstalled()) {
            //提醒用户没有按照微信
            Toast.makeText(activity, "没有安装微信，请先安装微信", Toast.LENGTH_LONG).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        if (TextUtils.isEmpty(webShareUrl)) {
            webpage.webpageUrl = baseUrl + id;
        } else {
            webpage.webpageUrl = webShareUrl;
        }
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = context.getString(R.string.invite_share_title);
        msg.description = context.getString(R.string.invite_share_content);
//        if (bitmap != null) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.luanch_logo);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);
//        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }

    public void createWXApi() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(context, Constans.WEI_APK_KEY, false);
            api.registerApp(Constans.WEI_APK_KEY);
        }
        if (!api.isWXAppInstalled()) {
            //提醒用户没有按照微信
            Toast.makeText(context, "没有安装微信，请先安装微信", Toast.LENGTH_LONG).show();
            return;
        }
    }

    /**
     * 微信分享文字
     */
    public void iWXShareText(String text, int scene, Bitmap bitmap) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = text;
        WXMediaMessage msg = new WXMediaMessage();
        msg.setThumbImage(bitmap);
        msg.mediaObject = textObject;
        msg.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }

    private int THUMB_SIZE = 120;
    private IWXAPI iwxapi;

    /**
     * 微信图片分享
     */
    public void iWXPicShare(int scene, Bitmap mBitmapTop) {
        if (iwxapi == null) {
            iwxapi = WXAPIFactory.createWXAPI(context, Constans.WEI_APK_KEY, true);
        }
        WXImageObject wxImageObject = new WXImageObject(mBitmapTop);
        WXMediaMessage wxMediaMessage = new WXMediaMessage();
        wxMediaMessage.mediaObject = wxImageObject;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mBitmapTop, THUMB_SIZE, THUMB_SIZE, true);
//        mBitmapTop.recycle();
        wxMediaMessage.thumbData = BitmapUtil.bmpToByteArray(scaledBitmap, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "msg";
        req.message = wxMediaMessage;
        req.scene = scene;
        iwxapi.sendReq(req);
    }

    /**
     * 回收bitmap
     */
    public void recyclerBitmap(){
        if (bitmap!=null&&!bitmap.isRecycled()){
            bitmap.recycle();
        }
        if (imgBitmap!=null&&!imgBitmap.isRecycled()){
            imgBitmap.recycle();
        }
        if (textBit!=null&&!textBit.isRecycled()){
            textBit.recycle();
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 获取分享的文本模板。
     */
    private String getActivitySharedText(String activity_time) {
        String format = activity.getString(R.string.weibosdk_share_webpage_template);
        return String.format(format, activity.getString(R.string.app_name) + "分享【" + activity_time + "】");
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = getActivitySharedText(share_title);
        if (flag==Constans.WX_SHARE_WEB){
            mediaObject.description = share_desc;
            if (TextUtils.isEmpty(shareUrl)) {
                mediaObject.actionUrl = share_url + SettingDefaultsManager.getInstance().UserId();
            } else {
                mediaObject.actionUrl = shareUrl;
            }
        }
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.luanch_logo);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        // 设置 Bitmap 类型的图片到视频对象里  设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(scaledBitmap);
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }

    public ShareDialog showI() {
        show();
        return this;
    }

    public ShareDialog dissMissI() {
        if (isShowing()) {
            dismiss();
        }
        return this;
    }

    public void onNewIntent(Intent intent) {
        shareHandler.doResultIntent(intent, this);
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
