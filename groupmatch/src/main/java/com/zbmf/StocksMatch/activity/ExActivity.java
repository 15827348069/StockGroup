package com.zbmf.StocksMatch.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

import com.baidu.mobstat.StatService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.utils.AssetsDatabaseManager;
import com.zbmf.StocksMatch.utils.UiCommon;

import java.text.SimpleDateFormat;


/**
 * 自定义Activity类，给无底部导航栏用
 *
 * @author Administrator
 */
public class ExActivity extends Activity {
    public Handler iHandler;
    //	private Get2Api server;
    public ImageLoader imageLoader;
    public SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DisplayImageOptions options;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_avatar)
                .showImageForEmptyUri(R.drawable.default_avatar)
                .showImageOnFail(R.drawable.default_avatar)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888) // 设置图片的解码类型
                .displayer(new RoundedBitmapDisplayer(20)).build();
        int iCurrActiIdx = UiCommon.INSTANCE.setCurrActivity(this);

        if (AssetsDatabaseManager.getManager() == null) {
            AssetsDatabaseManager.initManager(this);
        }

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey("user")){
                User user = (User)savedInstanceState.getSerializable("user");
                UiCommon.INSTANCE.setiUser(user);
            }
        }

        if(320 == UiCommon.widthPixels){
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            UiCommon.widthPixels = dm.widthPixels;
            UiCommon.higthPixels = dm.heightPixels;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        UiCommon.INSTANCE.setCurrActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (UiCommon.INSTANCE.getiUser() != null && !TextUtils.isEmpty(UiCommon.INSTANCE.getiUser().getAuth_token())){
            outState.putSerializable("user",UiCommon.INSTANCE.getiUser());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UiCommon.INSTANCE.doOnActivityDestroy(this);
    }

    /*
    * back键监听
    */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (UiCommon.INSTANCE.processBackKey()) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /***
     * 显示Dialog
     *
     * @param context
     */
    public void showDialog(final Context context, final int s) {
        String message = getString(s);
        String title = "";
        mProgressDialog = ProgressDialog.show(context, title, message, true, true, null);
    }

    public void DialogDismiss() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

}
