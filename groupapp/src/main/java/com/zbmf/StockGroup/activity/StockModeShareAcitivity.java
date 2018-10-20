package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.zbmf.StockGroup.beans.StockMode;
import com.zbmf.StockGroup.callback.ResultCallback;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.BitmapUtil;
import com.zbmf.StockGroup.utils.DateUtil;
import com.zbmf.StockGroup.utils.DoubleFromat;
import com.zbmf.StockGroup.utils.FileUtils;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.view.CustomProgressDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zbmf.StockGroup.utils.BitmapUtil.bmpToByteArray;
import static com.zbmf.StockGroup.utils.BitmapUtil.compressImage;
import static com.zbmf.StockGroup.utils.BitmapUtil.saveBitmap;

/**
 * Created by xuhao on 2017/12/5.
 */

public class StockModeShareAcitivity extends BaseActivity implements View.OnClickListener, WbShareCallback, IUiListener {
    private StockMode stockMode;
    private TextView price, yield, vrsi, ywpi, name, symbol, tv_date;
    private LinearLayout share_content_layout;
    private Bitmap bitmap;
    private CustomProgressDialog progressDialog;

    private final int SHARE_CLIENT = 1;
    private final int SHARE_ALL_IN_ONE = 2;
    private WbShareHandler shareHandler;
    private int mShareType = SHARE_ALL_IN_ONE;
    private IWXAPI iwxapi;
    private Tencent mTencent;

    @Override
    public int getLayoutResId() {
        return R.layout.acitivity_share_stock_layout;
    }

    @Override
    public void initView() {
        name = getView(R.id.tv_stock_name);
        symbol = getView(R.id.tv_stock_sybmol);
        price = getView(R.id.tv_price);
        yield = getView(R.id.tv_yield);
        vrsi = getView(R.id.tv_vrsi);
        ywpi = getView(R.id.tv_ywpi);
        tv_date = getView(R.id.tv_date);
        share_content_layout = getView(R.id.share_content_layout);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            stockMode = (StockMode) bundle.getSerializable(IntentKey.STOCKHOLDER);
            name.setText(stockMode.getStockNmae());
            symbol.setText(stockMode.getSymbol());
            price.setText(DoubleFromat.getStockDouble(stockMode.getPrice(), 2));
            yield.setText(DoubleFromat.getStockDouble(stockMode.getYield() * 100, 2) + "%");
            ywpi.setText(DoubleFromat.getStockDouble(stockMode.getYellow()/10 , 2) );
            vrsi.setText(DoubleFromat.getStockDouble(stockMode.getAllYield(), 2)+"%");
        }
        tv_date.setText(DateUtil.getTime(DateUtil.getTimes(), Constants.yy年MM月dd日HH_mm));
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.WEI_APK_KEY, true);
        this.shareHandler = new WbShareHandler(this);
        this.shareHandler.registerApp();
        mTencent = Tencent.createInstance(Constants.TencentSDKAppKey, this);
    }

    @Override
    public void addListener() {
        getView(R.id.share_wx_friend_circle).setOnClickListener(this);
        getView(R.id.share_wx_friend).setOnClickListener(this);
        getView(R.id.share_sina).setOnClickListener(this);
        getView(R.id.share_qq).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (bitmap == null) {
            shareImg(v);
        } else {
            shareImgToFriend(v);
        }
    }

    private void shareImg(final View v) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage(getString(R.string.loading_img));
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        BitmapUtil.takeScreenShot(share_content_layout, new ResultCallback<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bit) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                bitmap = bit;
                shareImgToFriend(v);
            }

            @Override
            public void onError(String message) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                showToast(message);
            }
        });
    }

    private void shareImgToFriend(View v) {
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
                weiboMessage.textObject= getSinaTextObject();
                shareHandler.shareMessage(weiboMessage, mShareType == SHARE_CLIENT);
                break;
            case R.id.share_qq:
                String img_url = FileUtils.getIntence().DEFAULT_DATA_IMAGEPATH + getPhotoFileName();
                String url=saveBitmap(img_url, bitmap);
                LogUtil.e(url);
                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, url);
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
                mTencent.shareToQQ(StockModeShareAcitivity.this, params,this);
                break;
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void sharetoWx(int scene) {
        WXImageObject wxImageObject = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxImageObject;
        //设置缩略图
        Bitmap mBp = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
        msg.thumbData = bmpToByteArray(mBp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");//  transaction字段用
        req.message = msg;
        req.scene = scene;
        iwxapi.sendReq(req);
    }

    private ImageObject getSinaImageObject() {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        imageObject.setThumbImage(compressImage(bitmap));
        return imageObject;
    }
    private TextObject getSinaTextObject(){
        TextObject textObject=new TextObject();
        textObject.text=getActivitySharedText();
        return textObject;
    }
    private String getActivitySharedText() {
        String format = getString(R.string.weibosdk_share_webpage_template);
        String text = String.format(format,getString(R.string.app_name)+"分享【模型选股】");
        return text;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent, this);
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

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        return dateFormat.format(date) + "_" + stockMode.getSymbol() + ".jpg";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode,resultCode,data,this);
    }
    @Override
    public void onComplete(Object o) {
        showToast("分享成功");
    }

    @Override
    public void onError(UiError uiError) {
        LogUtil.e(uiError.errorMessage);
        showToast("分享失败");
    }

    @Override
    public void onCancel() {
        showToast("取消分享");
    }
}
