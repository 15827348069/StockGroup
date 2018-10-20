package com.zbmf.StocksMatch.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.weibo.AccessTokenKeeper;

public class ShareActivity extends ExActivity implements View.OnClickListener, IWeiboHandler.Response {

    public static IWXAPI api;
    private Tencent mTencent;
    private IWeiboShareAPI mWeiboShareAPI = null; // 微博微博分享接口实例
    private String id = "1039";
    private String desc,title;
    private String baseUrl = "http://m.zbmf.com/match/share/?match_id=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_layout);


        mTencent = Tencent.createInstance(com.zbmf.StocksMatch.utils.Constants.TencentSDKAppKey, this.getApplicationContext());
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, com.zbmf.StocksMatch.utils.Constants.WBSDKAppKey);
        mWeiboShareAPI.registerApp();	// 将应用注册到微博客户端
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }

        desc = getString(R.string.default_share_desc);
        title = getString(R.string.default_share_title);

        getData();
        setupView();
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            MatchBean  matchbean = (MatchBean) bundle.getSerializable("matchbean");
            title = matchbean.getTitle();
            id=matchbean.getId();
            desc = matchbean.getDesc();
        }
    }

    private void setupView() {

        findViewById(R.id.ll_s1).setOnClickListener(this);
        findViewById(R.id.ll_s2).setOnClickListener(this);
        findViewById(R.id.ll_s3).setOnClickListener(this);
        findViewById(R.id.ll_s4).setOnClickListener(this);
        findViewById(R.id.rl_root).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_s1://朋友圈
                shareToWebchat(SendMessageToWX.Req.WXSceneTimeline);
                finish();
                break;
            case R.id.ll_s2://QQ
                shareToQQ();
                break;
            case R.id.ll_s3://微信好友
                shareToWebchat(SendMessageToWX.Req.WXSceneSession);finish();
                break;
            case R.id.ll_s4://新浪微博
                sendMessage();
                break;
            case R.id.rl_root:
                finish();
                break;
        }
    }

    //QQ分享-----------------------
    private ShareListener listener;
    private void shareToQQ(){
        listener = new ShareListener();

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, desc);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  baseUrl+id);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://www.zbmf.com/img/app/new_iphone/cgds-icon.gif");
        mTencent.shareToQQ(this, params, listener);
    }
    private class ShareListener implements IUiListener {
        @Override
        public void onCancel() {
            UiCommon.INSTANCE.showTip("分享取消");
        }

        @Override
        public void onComplete(Object arg0) {
            UiCommon.INSTANCE.showTip("分享成功");
        }

        @Override
        public void onError(UiError arg0) {
            UiCommon.INSTANCE.showTip("分享出错");
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,listener);//QQ分享必须
        finish();
    }


    //微信分享------------------------
    private void shareToWebchat(int scene) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(this, com.zbmf.StocksMatch.utils.Constants.WEI_APK_KEY, false);
            api.registerApp(com.zbmf.StocksMatch.utils.Constants.WEI_APK_KEY);
        }
        if (!api.isWXAppInstalled()) {
            //提醒用户没有按照微信
            UiCommon.INSTANCE.showTip("没有安装微信，请先安装微信");
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = baseUrl+id;
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = title;
        msg.description = desc;
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);
        msg.thumbData = com.zbmf.StocksMatch.utils.Util.bmpToByteArray(thumb,true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //微博分享--------------------
    private int mShareType = 1;//仅限客户端分享

    private void sendMessage() {

        if (mShareType == 1) {
            if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
                if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                    sendMultiMessage();
                } else {
                    sendSingleMessage();
                }
            } else {
                Toast.makeText(this, R.string.weibosdk_demo_not_support_api_hint, Toast.LENGTH_SHORT).show();
            }
        }
        else if (mShareType == 2) {
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
        mWeiboShareAPI.sendRequest(this, request);
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
            mWeiboShareAPI.sendRequest(this, request);
        }
        else if (mShareType == 2) {
            AuthInfo authInfo = new AuthInfo(this, com.zbmf.StocksMatch.utils.Constants.WBSDKAppKey, com.zbmf.StocksMatch.utils.Constants.REDIRECT_URL, Constants.SCOPE);
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            mWeiboShareAPI.sendRequest(this, request, authInfo, token, new WeiboAuthListener() {

                @Override
                public void onWeiboException(WeiboException arg0) {
                }

                @Override
                public void onComplete(Bundle bundle) {
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
                    Toast.makeText(getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this); //当前应用唤起微博分享后，返回当前应用
    }

    @Override
    public void onResponse(BaseResponse baseResp) {//接收微客户端博请求的数据。
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                Toast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                Toast.makeText(this,
                        getString(R.string.weibosdk_demo_toast_share_failed)+"baseResp.code"+baseResp.errCode + "Error Message: " + baseResp.errMsg,
                        Toast.LENGTH_LONG).show();
                Log.e("tag", "Error Message: " + baseResp.errMsg);
                break;
        }
//        finish();
    }

    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = desc;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = baseUrl+id;
        mediaObject.defaultText = "";
        return mediaObject;
    }

    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = Constants.SINA_AT;
        return textObject;
    }

}
