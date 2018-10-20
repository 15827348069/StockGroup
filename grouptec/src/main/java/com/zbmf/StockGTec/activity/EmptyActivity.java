package com.zbmf.StockGTec.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.Videos;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.ShareUtil;

public class EmptyActivity extends ExActivity implements IWeiboHandler.Response {

    private IWeiboShareAPI mWeiboShareAPI = null; // 微博微博分享接口实例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_empty);
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, ShareUtil.WBSDKAppKey);
        mWeiboShareAPI.registerApp();    // 将应用注册到微博客户端
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }

        Bundle extras = getIntent().getExtras();
        Videos videos = null;
        if(extras!=null)
            videos = (Videos) extras.getSerializable("videos");
        ShareUtil shareUtil = new ShareUtil(this);
        if(videos!=null){
            shareUtil.setShareImgUrl(videos.getShare_img());
            shareUtil.setTargetUrl(videos.getShare_url());
            shareUtil.setTextObj("上资本魔方炒股圈子，学技术，看直播，炒股其实很简单！");
            shareUtil.setDescription("上资本魔方炒股圈子，学技术，看直播，炒股其实很简单！");
            shareUtil.setTitle("【" + SettingDefaultsManager.getInstance().getGroupName() + "】" + videos.getTitle() +  "，一起来看吧");

        }

        shareUtil.sendMessage(mWeiboShareAPI);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this); //当前应用唤起微博分享后，返回当前应用
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        finish();
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                Toast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                Toast.makeText(this,
                        getString(R.string.weibosdk_demo_toast_share_failed) + "baseResp.code" + baseResponse.errCode + "Error Message: " + baseResponse.errMsg,
                        Toast.LENGTH_LONG).show();
                break;
        }
    }
}
