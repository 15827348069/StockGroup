package com.zbmf.StockGroup.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.zbmf.StockGroup.beans.Rank;
import com.zbmf.StockGroup.beans.StockMode;
import com.zbmf.StockGroup.callback.ResultCallback;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.utils.BitmapUtil;
import com.zbmf.StockGroup.utils.DoubleFromat;
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

public class NuggetsShareActivity extends BaseActivity implements IUiListener,WbShareCallback, View.OnClickListener {

    private IWXAPI iwxapi;
    private WbShareHandler shareHandler;
    private Tencent mTencent;
    private CustomProgressDialog progressDialog;
    private RelativeLayout mShareView;
    private Bitmap bitmap;
    private Group group;
    private final int SHARE_CLIENT = 1;
    private final int SHARE_ALL_IN_ONE = 2;
    private ImageView mClose;

    @Override
    public int getLayoutResId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉状态栏
        return R.layout.activity_nuggets_share;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void initView() {
        mShareView = getView(R.id.shareView);
        mClose = getView(R.id.close);
        ImageView code = getView(R.id.code);
        TextView currentTime = getView(R.id.currentTime);
        TextView stockName = getView(R.id.stockName);
        TextView stockNumber = getView(R.id.stockNumber);
        TextView current_price_v = getView(R.id.current_price_v);
        TextView zhangfu_v = getView(R.id.zhangfu_v);
        TextView yidong_v = getView(R.id.yidong_v);
        TextView qiangruo_v = getView(R.id.qiangruo_v);
        TextView saoma = getView(R.id.saoma);

        LinearLayout selectStockShareMsg = getView(R.id.selectStockShareMsg);
        LinearLayout modeStockShareMsg = getView(R.id.modeStockShareMsg);
        TextView weekZf_v = getView(R.id.weekZf_v);
        TextView weekTotalZf_v = getView(R.id.weekTotalZf_v);
        LinearLayout userMsg = getView(R.id.userMsg);
        RoundedCornerImageView user_avatar = getView(R.id.user_avatar);
//        XCRoundImageView user_avatar = getView(R.id.user_avatar);
        TextView selectUserName = getView(R.id.selectUserName);
        TextView selectStockWeek = getView(R.id.selectStockWeek);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            StockMode stockMode= (StockMode)extras.getSerializable(IntentKey.STOCK_MODE);
            Rank rank= (Rank)extras.getSerializable(IntentKey.RANK);
            int flag = extras.getInt(IntentKey.FLAG);
            if (flag==1){
                selectStockShareMsg.setVisibility(View.GONE);
                userMsg.setVisibility(View.GONE);
                selectStockWeek.setVisibility(View.GONE);
                modeStockShareMsg.setVisibility(View.VISIBLE);
                code.setImageResource(R.drawable.er_code);
                String currentTime1 = getCurrentTime();
                currentTime.setText(currentTime1);
                assert stockMode != null;
                stockName.setText(stockMode.getStockNmae());
                stockNumber.setText(stockMode.getSymbol());
                current_price_v.setText(DoubleFromat.getStockDouble(stockMode.getPrice(),2));
                zhangfu_v.setText(DoubleFromat.getStockDouble(stockMode.getYield()*100,2)+"%");
                qiangruo_v.setText(DoubleFromat.getStockDouble(stockMode.getYellow()/10,2));
                yidong_v.setText(DoubleFromat.getStockDouble(stockMode.getAllYield(),2)+"%");
                if(stockMode.getYield()==0){
                    zhangfu_v.setTextColor(this.getResources().getColor(R.color.black_99));
                }else{
                    setTextColor(stockMode.getYield()>0,zhangfu_v);
                }
            }else if (flag==2){
                selectStockShareMsg.setVisibility(View.VISIBLE);
                userMsg.setVisibility(View.VISIBLE);
                selectStockWeek.setVisibility(View.VISIBLE);
                modeStockShareMsg.setVisibility(View.GONE);
                code.setImageResource(R.drawable.select_stock_code);
                assert rank != null;
                weekZf_v.setText(rank.getHigh_yield()>=0?String.format("%+.2f", rank.getHigh_yield()):
                        String.format("-%+.2f", rank.getHigh_yield()));
                weekTotalZf_v.setText(rank.getWeek_yield()>=0?String.format("%+.2f", rank.getWeek_yield()):
                        String.format("-%+.2f", rank.getWeek_yield()));
                weekTotalZf_v.setTextColor(rank.getWeek_yield()>=0?getResources()
                        .getColor(R.color.red):getResources().getColor(R.color.green));
                weekZf_v.setTextColor(rank.getHigh_yield()>=0?getResources()
                        .getColor(R.color.red):getResources().getColor(R.color.green));
//                ViewFactory.getRoundImgView(this,rank.getAvatar(),user_avatar);
                ImageLoader.getInstance().displayImage(rank.getAvatar(),user_avatar,
                        ImageLoaderOptions.RoundedBitMapoptios());
                selectUserName.setText(rank.getNickname());
                stockName.setText(rank.getStock_name());
                stockNumber.setText(rank.getSymbol());
                saoma.setText(getString(R.string.select_saoma));
            }
        }
    }
    private void setTextColor(boolean isRed,TextView textView){
        textView.setTextColor(isRed? this.getResources().getColor(R.color.red):this.getResources().getColor(R.color.green));
    }
    @SuppressLint("SimpleDateFormat")
    private String getCurrentTime(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return dateFormat.format(date);
    }
    @Override
    public void initData() {
        initShare();
    }

    @Override
    public void addListener() {
        getView(R.id.share_wx_friend_circle).setOnClickListener(this);
        getView(R.id.share_wx_friend).setOnClickListener(this);
        getView(R.id.share_sina).setOnClickListener(this);
        getView(R.id.share_qq).setOnClickListener(this);
        getView(R.id.close).setOnClickListener(this);
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

    private void createBitMapImg(final View v) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage(getString(R.string.loading_img));
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        if (mClose.getVisibility() == View.VISIBLE) {
            mClose.setVisibility(View.INVISIBLE);
        }
        BitmapUtil.takeScreenShot(mShareView, new ResultCallback<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bit) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                bitmap = bit;
                shareImage(v);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mClose.getVisibility() == View.INVISIBLE) {
                            mClose.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onError(String message) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mClose.getVisibility() == View.INVISIBLE) {
                            mClose.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
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
                shareHandler.shareMessage(weiboMessage, SHARE_ALL_IN_ONE == SHARE_CLIENT);
                break;
            case R.id.share_qq:
                String img_url = FileUtils.getIntence().DEFAULT_DATA_IMAGEPATH + getPhotoFileName();
                String url = saveBitmap(img_url, bitmap);
                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, url);
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
                mTencent.shareToQQ(NuggetsShareActivity.this, params, this);
                break;
        }
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
        return String.format(format, group.getNick_name());
    }
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        return dateFormat.format(date) + "_"/* + group.getId()*/ + ".jpg";
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.close){
            this.finish();
        }else {
            if (bitmap == null) {
                createBitMapImg(v);
            } else {
                shareImage(v);
            }
        }
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void sharetoWx(int scene) {
        WXImageObject wxImageObject = new WXImageObject();
        String img_url = FileUtils.getIntence().DEFAULT_DATA_IMAGEPATH + getPhotoFileName();
        String url = saveBitmap(img_url, bitmap);
        wxImageObject.setImagePath(url);
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
}
