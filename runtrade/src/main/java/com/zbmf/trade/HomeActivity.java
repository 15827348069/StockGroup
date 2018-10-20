package com.zbmf.trade;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.trade.Commons.FIRST_RUN;

public class HomeActivity extends AppCompatActivity {
    private int soure_act = -1;
    private String web_url = "http://www.runtrader.com/";
    private WebView webView;
    private int mWidthPixels;
    private ImageView iv;
    private boolean isInit = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setFullscreen(true);
        setContentView(R.layout.activity_home);
        if(!SharedpreferencesUtil.getInstance().getBoolean(FIRST_RUN,true)){
            Intent Launchintent=new Intent(this, LaunchActivity.class);
            startActivity(Launchintent);
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidthPixels = dm.widthPixels;

        addBtnEvent();
    }
    private void setFullscreen(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |=  bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    private void addBtnEvent() {
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar); // 进度条
        final Animation alphaAnimation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.progress_bar_alpha); // 渐变动画
        mProgressBar.setMax(100);
        iv = (ImageView) findViewById(R.id.root);
        webView = (WebView) findViewById(R.id.webView);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false); // 设置是否支持缩放
        webView.getSettings().setSupportZoom(true);
        String ua = webView.getSettings().getUserAgentString();
        webView.addJavascriptInterface(new JsOperation(), "RunTraderAPP");
        webView.getSettings().setUserAgentString(ua+"; HFWSH /"+"RunTraderAPP");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    if (mProgressBar.getVisibility() == View.GONE)
                        mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(progress);
                } else {
                    Log.e("TAG", "onProgressChanged: " + isInit );
                    if (!isInit) {
                        hideIV();
                    }
                    mProgressBar.setProgress(100);
                    mProgressBar.startAnimation(alphaAnimation);
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @SuppressLint("MissingPermission")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("13375717531")) {
                    showDialog();
                    return true;
                }

                if (url.contains("4008129696")) {
                    if (isAvilible(HomeActivity.this, "com.tencent.mobileqq")) {
                        String urls = "mqqwpa://im/chat?chat_type=wpa&uin=4008129696";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urls)));
                    } else {
                        Toast.makeText(HomeActivity.this, "请先安装手机QQ...", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }


                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        SharedPreferences sharedPreferences=getSharedPreferences(getPackageName()+"SettingDefaultsManager", Context.MODE_PRIVATE);
        synCookies(web_url,"token="+sharedPreferences.getString(TOKEN,""));
        webView.loadUrl(web_url);
    }

    private void hideIV(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(500);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv.setVisibility(View.GONE);
                isInit = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv.startAnimation(alphaAnimation);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView != null && webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            webView.goBack();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private long exitTime = 0;



    public boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        List<String> packageNames = new ArrayList<String>();
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        return packageNames.contains(packageName);
    }

    public void showDialog() {
        final Dialog dialog1 = new Dialog(this, R.style.myDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_tip, null);
        TextView tvCancl = (TextView) layout.findViewById(R.id.tvCancl);
        TextView tvConfirm = (TextView) layout.findViewById(R.id.tvConfirm);
        tvCancl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog1.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View arg0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 111);
                } else {
                    Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "13375717531"));
                    intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    HomeActivity.this.startActivity(intentPhone);
                }
                dialog1.dismiss();
            }
        });
        dialog1.setContentView(layout);

        // 设置对话框的出现位置，借助于window对象
        Window win = dialog1.getWindow();
        win.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (mWidthPixels * 0.7);
        win.setAttributes(lp);
        dialog1.setCancelable(false);
        dialog1.show();
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "13375717531"));
                intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                HomeActivity.this.startActivity(intentPhone);
            } else {
                Toast.makeText(HomeActivity.this, "拒绝拨号权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * 设置Cookie
     * @param cookie  格式：uid=21233 如需设置多个，需要多次调用
     */
    private void synCookies(String url,String cookie) {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, cookie);//指定要修改的cookies
        CookieSyncManager.getInstance().sync();
    }
    private final String TOKEN="token";
    public class JsOperation {
        @JavascriptInterface
        public void commit(String url) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        }
        @JavascriptInterface
        public void saveToken(String token){
            SharedPreferences sharedPreferences =getSharedPreferences(getPackageName()+"SettingDefaultsManager", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TOKEN,token);
            editor.commit();
        }
    }
}
