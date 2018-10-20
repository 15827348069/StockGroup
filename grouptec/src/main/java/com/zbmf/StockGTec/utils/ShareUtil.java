package com.zbmf.StockGTec.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
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
import com.zbmf.StockGTec.R;


/**
 * Created by lulu on 14/9/28.
 */

public class ShareUtil {

    private Activity activity;
    private Tencent mTencent;
    public static IWXAPI api;
    private IWeiboShareAPI mWeiboShareAPI = null; // 微博微博分享接口实例

    private String description = "随身携带最新资讯，随时了解官方活动，实时知晓直播动态，一切尽在资本魔方！",
            title = "我在资本魔方分享了一只好股票，点击即可查看！";
    private String targetUrl = "http://m1.zbmf.com/register/?uid=" + SettingDefaultsManager.getInstance().UserId();
    private String textObj = Constants.SINA_AT;
    //以下常量自行抽出
    private static final String TencentSDKAppKey = "1106227095";
    private static final String WEI_APK_KEY = "wx244d1eb2221dfee2";//微信key
    public static final String WBSDKAppKey = "1779795475";//微博key

    public static final String REDIRECT_URL = "http://www.7878.com";
    private String shareImgUrl = "http://wx3.sinaimg.cn/large/72cb3717gy1fhrjeayo7ij203c03cq2x.jpg";
    Bitmap thumb = null;
    boolean isDefault = true;

    public ShareUtil(Activity activity) {
        this.activity = activity;
    }

    public ShareUtil(Activity activity, String title) {
        this.activity = activity;
        this.title = title;
    }

    public ShareUtil(Activity activity, String title, String description) {
        this.activity = activity;
        this.title = title;
        this.description = description;
    }

    public ShareUtil(Activity activity, String title, String description, final String shareImgUrl, String targetUrl) {
        isDefault = false;
        this.activity = activity;
        this.title = title;
        this.description = description;
        this.shareImgUrl = shareImgUrl;
        this.targetUrl = targetUrl;
        new Thread(new Runnable() {
            @Override
            public void run() {
                thumb = Bitmap.createScaledBitmap(ImageLoader.getInstance().loadImageSync(shareImgUrl), 150, 150, true);
            }
        }).start();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public void setTextObj(String textObj) {
        this.textObj = textObj;
    }

    public void setShareImgUrl(final String shareImgUrl) {
        this.shareImgUrl = shareImgUrl;
        new Thread(new Runnable() {
            @Override
            public void run() {
                thumb = Bitmap.createScaledBitmap(ImageLoader.getInstance().loadImageSync(shareImgUrl), 150, 150, true);
            }
        }).start();
    }

    public static ShareListener listener;

    /**
     * 分享到QQ
     */
    public void shareToQQ() {
        mTencent = Tencent.createInstance(TencentSDKAppKey, activity.getApplicationContext());

        listener = new ShareListener();

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://wx3.sinaimg.cn/large/72cb3717gy1fhrjeayo7ij203c03cq2x.jpg");
        mTencent.shareToQQ(activity, params, listener);
    }

    private class ShareListener implements IUiListener {
        @Override
        public void onCancel() {
            Toast.makeText(activity, activity.getString(R.string.weibosdk_demo_toast_share_canceled), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete(Object arg0) {
            Toast.makeText(activity, activity.getString(R.string.weibosdk_demo_toast_share_success), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError arg0) {
            Toast.makeText(activity, activity.getString(R.string.weibosdk_demo_toast_share_failed), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 微信分享
     *
     * @param scene
     */
    public void shareToWebchat(int scene) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(activity, WEI_APK_KEY, false);
            api.registerApp(WEI_APK_KEY);
        }
        if (!api.isWXAppInstalled()) {
            //提醒用户没有按照微信
            Toast.makeText(activity, "没有安装微信，请先安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = targetUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = title;
        msg.description = description;

        if (isDefault)
            thumb = BitmapFactory.decodeResource(activity.getResources(),
                    R.mipmap.sgare);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private int mShareType = 1;//仅限客户端分享

    /**
     * 新浪微博分享
     *
     * @param mWeiboShareAPI
     */
    public void sendMessage(IWeiboShareAPI mWeiboShareAPI) {
        this.mWeiboShareAPI = mWeiboShareAPI;


        if (mShareType == 1) {
            if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
                if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                    sendMultiMessage();
                } else {
                    sendSingleMessage();
                }
            } else {
                Toast.makeText(activity, R.string.weibosdk_demo_not_support_api_hint, Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        } else if (mShareType == 2) {
            sendMultiMessage();
        }
    }

    private void sendSingleMessage() {

        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();

        weiboMessage.mediaObject = getWebpageObj();
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(activity, request);
    }

    private void sendMultiMessage() {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        weiboMessage.mediaObject = getWebpageObj();
        weiboMessage.textObject = getTextObj();
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        if (mShareType == 1) {
            mWeiboShareAPI.sendRequest(activity, request);
        } else if (mShareType == 2) {
            AuthInfo authInfo = new AuthInfo(activity, WBSDKAppKey, REDIRECT_URL, Constants.SCOPE);
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity.getApplicationContext());
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            mWeiboShareAPI.sendRequest(activity, request, authInfo, token, new WeiboAuthListener() {

                @Override
                public void onWeiboException(WeiboException arg0) {
                }

                @Override
                public void onComplete(Bundle bundle) {
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(activity.getApplicationContext(), newToken);
                    Toast.makeText(activity.getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }

    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = description;
//        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.sgare);
        // 设置 Bitmap 类型的图片到视频对象里  设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。if (isDefault)
        if (isDefault)
            thumb = BitmapFactory.decodeResource(activity.getResources(),
                    R.mipmap.sgare);
        mediaObject.setThumbImage(thumb);
        mediaObject.actionUrl = targetUrl;
        mediaObject.defaultText = "";
        return mediaObject;
    }

    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = textObj;
        return textObject;
    }
}
