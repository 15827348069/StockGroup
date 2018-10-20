package com.zbmf.StockGroup.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.ViewPageFragmentadapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.fragment.GoldStockFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/11/8.
 */

public class GoldStockActivity extends BaseActivity implements IUiListener, WbShareCallback {
    private ViewPager mViewpager;
    private List<GoldStockFragment> mList;
    private IUiListener iUiListener;
    private WbShareHandler shareHandler;
    public IUiListener getiUiListener() {
        return iUiListener;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_gold_stock_layout;
    }

    @Override
    public void initView() {
        initTitle("金股金句");
        mViewpager = getView(R.id.viewpager_gold_stock);
        iUiListener=this;
        this.shareHandler = new WbShareHandler(this);
        this.shareHandler.registerApp();
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        WebBase.getDictumNums(new JSONHandler(true,this,getString(R.string.loading)) {
            @Override
            public void onSuccess(JSONObject obj) {
                if(obj.has("result")){
                    JSONObject result=obj.optJSONObject("result");
                    if(result.has("data")){
                        JSONArray jsonArray=result.optJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++){
                            mList.add(GoldStockFragment.newInstance(jsonArray.optString(i)));
                        }
                        if(mList!=null&&mList.size()>0){
                            ViewPageFragmentadapter adapter = new ViewPageFragmentadapter(getSupportFragmentManager(),null, mList);
                            mViewpager.setAdapter(adapter);
                        }
                    }
                }

            }

            @Override
            public void onFailure(String err_msg) {

            }
        });

    }

    @Override
    public void addListener() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(shareHandler!=null){
            shareHandler.doResultIntent(intent, this);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data,this);
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
}
