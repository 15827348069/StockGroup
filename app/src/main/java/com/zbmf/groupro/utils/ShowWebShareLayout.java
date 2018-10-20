package com.zbmf.groupro.utils;

import android.app.Dialog;
import android.content.Context;
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

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
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
import com.zbmf.groupro.R;
import com.zbmf.groupro.activity.BlogDetailActivity;
import com.zbmf.groupro.api.AppUrl;
import com.zbmf.groupro.beans.BlogBean;

import java.io.IOException;
import java.net.URL;

/**
 * Created by xuhao on 2016/6/20.
 */
public class ShowWebShareLayout {
    public String excu_id;
    private Context context;
    private BlogDetailActivity webActivity;
    public IWeiboShareAPI Web__mWeiboShareAPI;
    public static ShowWebShareLayout instance;
    public static ShowWebShareLayout getiInstance(){
        if(instance==null){
            instance=new ShowWebShareLayout();
        }
        return instance;
    }
    private ShowWebShareLayout(){

    }
    public void init(Context context,BlogDetailActivity activity){
        this.webActivity=activity;
        this.context=context;
        this.Web__mWeiboShareAPI= WeiboShareSDK.createWeiboAPI(activity, Constants.WBSDKAppKey);;
        Web__mWeiboShareAPI.registerApp();
    }
    public void closeActivity(){
        if(webActivity!=null){
            webActivity.finish();
        }
    }
    public void showShareLayout(final BlogBean ab){
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
        Button share_wx_friend=(Button) aDialog.findViewById(R.id.share_wx_friend);
        Button share_wx_friend_circle=(Button) aDialog.findViewById(R.id.share_wx_friend_circle);
        Button share_sina=(Button) aDialog.findViewById(R.id.share_sina);
        Button share_qq=(Button) aDialog.findViewById(R.id.share_qq);
        Button cause_share= (Button) aDialog.findViewById(R.id.cause_share);
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
                        webpage.webpageUrl=ab.getWap_link();
                        WXMediaMessage msg=new WXMediaMessage(webpage);
                        msg.title=ab.getTitle();
                        Bitmap bmp = null;
                        try {
                            bmp = BitmapFactory.decodeStream(new URL(ab.getImg()).openStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                        byte[] bitmap_byte=BitmapUtil.bmpToByteArray(thumbBmp,true);
                        msg.thumbData=bitmap_byte;
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());
                        req.message = msg;
                        req.scene= SendMessageToWX.Req.WXSceneSession;
                        IWXAPI api = WXAPIFactory.createWXAPI(context,Constants.WEI_APK_KEY,true);
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
                        webpage.webpageUrl=ab.getWap_link();
                        WXMediaMessage msg=new WXMediaMessage(webpage);
                        msg.title=ab.getTitle();
                        Bitmap bmp ;
                        try {
                            bmp = BitmapFactory.decodeStream(new URL(ab.getImg()).openStream());
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                            byte[] bitmap_byte=BitmapUtil.bmpToByteArray(thumbBmp,true);
                            msg.thumbData=bitmap_byte;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());
                        req.message = msg;
                        req.scene= SendMessageToWX.Req.WXSceneTimeline;

                        IWXAPI api = WXAPIFactory.createWXAPI(context, Constants.WEI_APK_KEY,true);
                        api.sendReq(req);

                    }
                });
                aDialog.dismiss();
            }
        });
        share_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享到新浪
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                        weiboMessage.textObject = getTextObj(ab.getTitle());
                        weiboMessage.imageObject = getImageObj(ab.getImg());
                        weiboMessage.mediaObject=getWebpageObj(ab);
                        // 2. 初始化从第三方到微博的消息请求
                        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
                        // 用transaction唯一标识一个请求
                        request.transaction = String.valueOf(System.currentTimeMillis());
                        request.multiMessage = weiboMessage;
                        if(!Web__mWeiboShareAPI.isWeiboAppSupportAPI()){
                            sendWebSina(request);
                        }else{
                            Web__mWeiboShareAPI.sendRequest(webActivity, request);
                        }
                    }
                });
                aDialog.dismiss();
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
    public void sendWebSina(SendMultiMessageToWeiboRequest request){
            AuthInfo authInfo = new AuthInfo(webActivity, Constants.WBSDKAppKey, AppUrl.REDIRECT_URL, AppUrl.SCOPE);
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context);
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            Web__mWeiboShareAPI.sendRequest(webActivity, request, authInfo, token, new WeiboAuthListener() {
                @Override
                public void onWeiboException( WeiboException arg0 ) {

                }
                @Override
                public void onComplete( Bundle bundle ) {
                    // TODO Auto-generated method stub
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(context, newToken);
                }
                @Override
                public void onCancel() {

                }
            });
    }
    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(String avatar) {
        ImageObject imageObject = new ImageObject();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap bitmap=BitmapUtil.getBitmap(avatar,context);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }
    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(BlogBean ab) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = ab.getTitle();
        Bitmap bitmap=BitmapUtil.getBitmap(ab.getImg(),context);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl =ab.getWap_link();
        mediaObject.defaultText ="Webpage 默认文案";
        return mediaObject;
    }
    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String name) {
        TextObject textObject = new TextObject();
        if(name.length()>100){
            textObject.text = getActivitySharedText(name.substring(0,100));
        }else{
            textObject.text = getActivitySharedText(name);
        }
        return textObject;
    }
    private String getActivitySharedText(String activity_time) {
        String appUrl = context.getString(R.string.weibosdk_app_url);
        String format = context.getString(R.string.weibosdk_share_webpage_template);
        String text = String.format(format,context.getString(R.string.app_name)+"分享【"+activity_time+"】");
        return text;
    }
    public void shareQQ(BlogBean ab){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, context.getString(R.string.app_name));
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, ab.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, ab.getWap_link());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ab.getImg());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getString(R.string.app_name));
        Tencent mTencent= Tencent.createInstance(Constants.TencentSDKAppKey,context);
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
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
