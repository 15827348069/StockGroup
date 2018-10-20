package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.callback.ResultCallback;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.BitmapUtil;
import com.zbmf.StockGroup.utils.DisplayUtil;
import com.zbmf.StockGroup.utils.FileUtils;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.CustomProgressDialog;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zbmf.StockGroup.utils.BitmapUtil.CImage;
import static com.zbmf.StockGroup.utils.BitmapUtil.bmpToByteArray;
import static com.zbmf.StockGroup.utils.BitmapUtil.compressImage;
import static com.zbmf.StockGroup.utils.BitmapUtil.saveBitmap;

/**
 * Created by xuhao on 2018/3/29.
 */

public class ShareMessageActivity extends BaseActivity implements View.OnClickListener, IUiListener, WbShareCallback {
    TextView tvContent;
    TextView tvDate;
    RoundedCornerImageView imvAvatar;
    TextView tvName;
    TextView tvCircleId;
    LinearLayout llShareContent,llShareLayout;
    ImageView imvEwm;
    private Group group;
    private WbShareHandler shareHandler;
    private final int SHARE_CLIENT = 1;
    private final int SHARE_ALL_IN_ONE = 2;
    private int mShareType = SHARE_ALL_IN_ONE;
    private IWXAPI iwxapi;
    private Tencent mTencent;
    private CustomProgressDialog progressDialog;
    private Bitmap bitmap;

    private final String shareUrl = "http://www.zbmf.com/register?uid=%1$s";
    @Override
    public int getLayoutResId() {
        return R.layout.activity_share_message_layout;
    }

    @Override
    public void initView() {
        tvContent=getView(R.id.tv_content);
        tvDate=getView(R.id.tv_date);
        imvAvatar=getView(R.id.imv_avatar);
        tvName=getView(R.id.tv_name);
        tvCircleId=getView(R.id.tv_circle_id);
        llShareContent=getView(R.id.ll_share_content);
        imvEwm=getView(R.id.imv_erm);
        llShareLayout=getView(R.id.ll_share);
        llShareContent.post(new Runnable() {
            @Override
            public void run() {
                Log.e("top===","=="+llShareLayout.getTop());
                Log.e("llShareContent===","=="+llShareContent.getBottom());
                int height=llShareLayout.getTop()-llShareContent.getBottom();
                if(height>0){
                    llShareLayout.setTag(height/2);
                    setMargins(llShareContent,0,height/2,0,0);
                }
            }
        });
    }
    public  void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
    @Override
    public void initData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            group= (Group) bundle.getSerializable(IntentKey.GROUP);
//            ViewFactory.imgCircleView(this,group.getAvatar(), imvAvatar);
            ImageLoader.getInstance().displayImage(group.getAvatar(), imvAvatar, ImageLoaderOptions.AvatarOptions());
            imvEwm.setImageBitmap(BitmapUtil.createQRCode(String.format(shareUrl,group.getId()), DisplayUtil.dip2px(this, 100)));
            tvName.setText(group.getNick_name());
            tvCircleId.setText(String.format("圈号：%s",group.getId()));
            tvContent.setText(group.getShareMessage());
            tvDate.setText(group.getMessageDate());
        }
        initShare();
    }
    @Override
    public void addListener() {
        getView(R.id.share_wx_friend_circle).setOnClickListener(this);
        getView(R.id.share_wx_friend).setOnClickListener(this);
        getView(R.id.share_sina).setOnClickListener(this);
        getView(R.id.share_qq).setOnClickListener(this);
    }
    private void createBitMapImg(final View v) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage(getString(R.string.loading_img));
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        BitmapUtil.takeScreenShot(llShareContent, new ResultCallback<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bit) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                bitmap = bit;
                shareImage(v);
            }

            @Override
            public void onError(String message) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(ShareMessageActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initShare() {
        if (iwxapi == null) {
            iwxapi = WXAPIFactory.createWXAPI(this, Constants.WEI_APK_KEY, true);
        }
        if (shareHandler == null) {
            this.shareHandler = new WbShareHandler(this);
            this.shareHandler.registerApp();
        }
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Constants.TencentSDKAppKey, this);
        }
    }

    private void shareImage(View v) {
        switch (v.getId()) {
            case R.id.share_wx_friend_circle:
                sharetoWx(SendMessageToWX.Req.WXSceneTimeline);
                break;
            case R.id.share_wx_friend:
                sharetoWx(SendMessageToWX.Req.WXSceneSession);
                break;
            case R.id.share_sina:
                WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                weiboMessage.imageObject = getSinaImageObject();
                weiboMessage.textObject = getSinaTextObject();
                shareHandler.shareMessage(weiboMessage, mShareType == SHARE_CLIENT);
                break;
            case R.id.share_qq:
                String img_url = FileUtils.getIntence().DEFAULT_DATA_IMAGEPATH + getPhotoFileName();
                String url = saveBitmap(img_url, bitmap);
                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, url);
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
                mTencent.shareToQQ(ShareMessageActivity.this, params, this);
                break;
        }
    }
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        return dateFormat.format(date) + "_" + group.getId()+"-"+group.getMessageId() + ".jpg";
    }

    private ImageObject getSinaImageObject() {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(CImage(bitmap));
        imageObject.setThumbImage(compressImage(bitmap));
        return imageObject;
    }

    private TextObject getSinaTextObject() {
        TextObject textObject = new TextObject();
        textObject.text = getActivitySharedText();
        return textObject;
    }

    private String getActivitySharedText() {
        String format = getString(R.string.weibosdk_share_webpage_template);
        String text = String.format(format, group.getNick_name());
        return text;
    }

    private void sharetoWx(int scene) {
        WXImageObject wxImageObject = new WXImageObject();
        String img_url = FileUtils.getIntence().DEFAULT_DATA_IMAGEPATH + getPhotoFileName();
        String url = saveBitmap(img_url, bitmap);
        wxImageObject.setImagePath(url);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxImageObject;
        //设置缩略图
        Bitmap mBp =Bitmap.createScaledBitmap(bitmap, 120, 120, true);
        msg.thumbData = bmpToByteArray(mBp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");//  transaction字段用
        req.message = msg;
        req.scene = scene;
        iwxapi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onComplete(Object o) {
        showToast("分享成功");
    }

    @Override
    public void onError(UiError uiError) {
        showToast("分享失败");
    }

    @Override
    public void onCancel() {
        showToast("取消分享");
    }

    @Override
    public void onWbShareSuccess() {
        showToast("分享成功");
    }

    @Override
    public void onWbShareCancel() {
        showToast("取消分享");
    }

    @Override
    public void onWbShareFail() {
        showToast("分享失败");
    }

    @Override
    public void onClick(View v) {
        if (bitmap == null) {
            createBitMapImg(v);
        } else {
            shareImage(v);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data,this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(shareHandler!=null){
            shareHandler.doResultIntent(intent, this);
        }
    }
}
