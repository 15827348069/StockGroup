package com.zbmf.groupro.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zbmf.groupro.R;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BlogBean;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.CusSeekbar;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by xuhao on 2016/6/20.
 */
public class WebViewActivity extends Activity {
    private WebView jianghu_webview;
    private String URL;
    private ImageButton web_view_back,web_view_close,web_share_id;
    private ProgressBar progressBar_loading;
    private TextView web_title;
    private Dialog mTextSetteingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        URL=getIntent().getStringExtra("web_url");
        jianghu_webview= (WebView) findViewById(R.id.jianghu_webview);
        final WebSettings settings=jianghu_webview.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        settings.setSupportZoom(true);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(false);
        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024*1024*8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        jianghu_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        jianghu_webview.loadUrl(URL);
        jianghu_webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String[] web_url = url.split("/");
                if(url.startsWith("mqqwpa://")){
                    //跳转到客服qq
                    String qq_url="mqqwpa://im/chat?chat_type=crm&uin=2852273339&version=1&src_type=web&web_src=http:://wpa.b.qq.com";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qq_url)));
                }else if(web_url.length==4&&url.startsWith("app://group/")){
                    //进入圈子
                    getGroupInfo(web_url[3]);
                }else if(web_url.length==7&&url.startsWith("app://app/people/")){
                    getBlogDetail(web_url[4]+"-"+web_url[6]);
                }else{
                    view.loadUrl(url);
                }
                return true;
            }
        });
        web_title= (TextView) findViewById(R.id.web_title);
        web_title.setText(getIntent().getStringExtra("web_title"));
        web_view_back= (ImageButton) findViewById(R.id.web_view_back);
        web_share_id= (ImageButton) findViewById(R.id.web_share_id);
        web_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                if(jianghu_webview.canGoBack())
//                {
//                    jianghu_webview.goBack();//返回上一页面
//                }
//                else
//                {
//                    finish();
//                }
            }
        });
        web_share_id.setVisibility(View.GONE);
//        web_share_id.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mTextSetteingDialog == null)
//                    mTextSetteingDialog = showSetText(settings);
//                mTextSetteingDialog.show();
//            }
//        });
        web_view_close= (ImageButton) findViewById(R.id.web_view_close);
        web_view_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressBar_loading= (ProgressBar) findViewById(R.id.progressBar_loading);
        jianghu_webview.setWebChromeClient(new WebChromeClient(){
            //设置加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress==100){
                    progressBar_loading.setVisibility(View.GONE);
                }else{
                    progressBar_loading.setVisibility(View.VISIBLE);
                    progressBar_loading.setProgress(newProgress);
                }
            }
            //设置头部
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                web_title.setText(title);
            }
        });
        jianghu_webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout= (SwipeRefreshLayout)findViewById(R.id.swiperefresh_id);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新刷新页面
                jianghu_webview.reload();
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(jianghu_webview.canGoBack())
            {
                jianghu_webview.goBack();//返回上一页面
                return true;
            }
            else
            {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
    private Dialog showSetText(final WebSettings settings) {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        View layout = getLayoutInflater().inflate(R.layout.setting_text_size, null);
        CusSeekbar seekbar = (CusSeekbar) layout.findViewById(R.id.seekbar);
        ArrayList<String> sections = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            if (i == 1)
                sections.add("标准");
            else
                sections.add("");
        }
        seekbar.initData(sections);
        seekbar.setCur_sections(SettingDefaultsManager.getInstance().getBlogTextSize());
        seekbar.setResponseOnTouch(new CusSeekbar.ResponseOnTouch() {
            @Override
            public void onTouchResponse(int volume) {
                SettingDefaultsManager.getInstance().setBlogTextSize(volume);
                switch (volume) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                }
            }
        });


        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        dialog.setCancelable(true);
        return dialog;
    }
    public void getGroupInfo(String id){
        Group groupbean=new Group();
        groupbean.setId(id);
        ShowActivity.showGroupDetailActivity(WebViewActivity.this,groupbean);
    }
    public void getBlogDetail(String blog_id){
        WebBase.searchUserBlogInfo(blog_id, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                BlogBean blogBean=new BlogBean();
                blogBean.setBlog_id(obj.optString("blog_id"));
                blogBean.setTitle(obj.optString("subject"));
                blogBean.setImg(obj.optString("cover"));
                blogBean.setLook_number(obj.optJSONObject("stat").optString("views"));
                blogBean.setPinglun(obj.optJSONObject("stat").optString("replys"));
                blogBean.setWap_link(obj.optJSONObject("link").optString("wap"));
                blogBean.setApp_link(obj.optJSONObject("link").optString("app"));
                blogBean.setDate(obj.optString("posted_at"));
                ShowActivity.showBlogDetailActivity(WebViewActivity.this,blogBean);
            }
            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
}
