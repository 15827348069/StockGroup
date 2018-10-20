package com.zbmf.StocksMatch.activity;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
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
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.weibo.AccessTokenKeeper;

public class AboutActivity extends ExActivity implements View.OnClickListener {

    private TextView tv_title;
    private WindowManager.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        lp = getWindow().getAttributes();
        setupView();
    }
    private void setupView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.about));
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.ll_about).setOnClickListener(this);
        findViewById(R.id.ll_share).setOnClickListener(this);
        findViewById(R.id.ll_feed).setOnClickListener(this);
        findViewById(R.id.ll_question).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_about:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ABOUTUS, null);
                break;

            case R.id.ll_share:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_SHARE, null);
                lp.alpha=0.3f;
                getWindow().setAttributes(lp);
                break;

            case R.id.ll_feed:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_FEEDBACK, null);
                break;
            case R.id.ll_question:
//              UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ISSUE,null);
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.issue));
                bundle.putInt("soure_act", 3);
                bundle.putString("web_url", "http://www.7878.com/apps/contact/");
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ACCOUNT_Web, bundle);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        lp.alpha=1.0f;
        getWindow().setAttributes(lp);
    }

}
