package com.zbmf.StockGTec.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.BlogBean;
import com.zbmf.StockGTec.utils.FileUtils;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.ShowActivity;
import com.zbmf.StockGTec.view.CusSeekbar;
import com.zbmf.StockGTec.view.HProgressBarLoading;

import org.json.JSONObject;

import java.util.ArrayList;


public class BlogDetailActivity extends ExActivity implements View.OnClickListener {
    private WebView jianghu_webview;
    private String URL;
    private BlogBean blogBean;
    private HProgressBarLoading top_progress;
    private Dialog mTextSetteingDialog;
    private Dialog pinglun_dialog;
    private TextView blog_detail_pinglun, blog_pinglun_button;
    private ImageButton care_about, cause_care_about_blog_button;
    private boolean isContinue = false;
    private TextView mTvCenterBadnet;
    private ImageView loadingImageView;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_detail_layout);
        blogBean = (BlogBean) getIntent().getSerializableExtra("blogBean");
//        ShowWebShareLayout.getiInstance().init(getApplicationContext(), this);
        init();
        if (blogBean != null) {
            initData();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        getUserBlogInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init() {
        jianghu_webview = (WebView) findViewById(R.id.blog_detail_webview);
        blog_detail_pinglun = (TextView) findViewById(R.id.blog_detail_pinglun);
        blog_pinglun_button = (TextView) findViewById(R.id.blog_pinglun_button);
        mTvCenterBadnet = (TextView) findViewById(R.id.tv_center_badnet);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_id);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新刷新页面
                jianghu_webview.reload();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        findViewById(R.id.blog_back).setOnClickListener(this);
        findViewById(R.id.blog_textsize_setting).setOnClickListener(this);
        blog_detail_pinglun.setOnClickListener(this);
        findViewById(R.id.share_blog_detail).setOnClickListener(this);
        findViewById(R.id.blog_pinglun_button).setOnClickListener(this);
        findViewById(R.id.blog_back).setOnClickListener(this);
        care_about = (ImageButton) findViewById(R.id.care_about_blog_button);
        loadingImageView = (ImageView) findViewById(R.id.loadingImageView);
        animationDrawable = (AnimationDrawable) loadingImageView.getDrawable();
        animationDrawable.start();
        cause_care_about_blog_button = (ImageButton) findViewById(R.id.cause_care_about_blog_button);
        care_about.setOnClickListener(this);
        cause_care_about_blog_button.setOnClickListener(this);
        WebSettings settings = jianghu_webview.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setAppCachePath(FileUtils.getIntence().DEFAULT_DATA_FILE);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        jianghu_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        jianghu_webview.setWebChromeClient(new WebChromeClient() {
            //设置加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //如果进度条隐藏则让它显示
                if (View.INVISIBLE == top_progress.getVisibility()) {
                    top_progress.setVisibility(View.VISIBLE);
                }
                if (newProgress == 100) {
                    animationDrawable.stop();
                    loadingImageView.clearAnimation();
                    loadingImageView.setVisibility(View.GONE);
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
                if (newProgress >= 80) {
                    //拦截webView自己的处理方式
                    if (isContinue) {
                        return;
                    }
                    top_progress.setCurProgress(100, 3000, new HProgressBarLoading.OnEndListener() {
                        @Override
                        public void onEnd() {
                            finishOperation(true);
                            isContinue = false;
                        }
                    });
                    isContinue = true;
                } else {
                    top_progress.setNormalProgress(newProgress);
                }
            }

            //设置头部
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                web_title.setText(title);
            }
        });
        jianghu_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //app://blog/43-1083/posts/
                //app://blog/43-1083/
                //app://group/43/
                //app://group/43/blog/
                String[] web_url = url.split("/");
                if (url.startsWith("mqqwpa://")) {
                    //跳转到客服qq
                    String qq_url = "mqqwpa://im/chat?chat_type=crm&uin=2852273339&version=1&src_type=web&web_src=http:://wpa.b.qq.com";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qq_url)));
                } else if (url.endsWith("/posts/") && url.startsWith("app://blog/")) {
                    ShowActivity.showBlogPingActivity(BlogDetailActivity.this, blogBean);
                } else if (web_url.length == 4 && url.startsWith("app://group/")) {
                    //进入圈子
//                    getGroupInfo(web_url[3]);
                } else if (web_url.length == 7 && url.startsWith("app://app/people/")) {
                    getBlogDetail(web_url[4] + "-" + web_url[6]);
                } else if (url.startsWith("app://image/")) {
                    ShowActivity.ShowBigImage(BlogDetailActivity.this, url.replace("app://image/", "http://"));
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                errorOperation();
            }
        });
        top_progress = (HProgressBarLoading) findViewById(R.id.top_progress);
        jianghu_webview.addJavascriptInterface(new ScrollTop(swipeRefreshLayout), "blogdetail");
        jianghu_webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
//        if(pinglun_dialog==null){
//            pinglun_dialog=Editdialog1();
//            pinglun_dialog.show();
//        }
//        pinglun_dialog.dismiss();
    }

    /**
     * 结束进行的操作
     */
    private void finishOperation(boolean flag) {
        //最后加载设置100进度
        top_progress.setNormalProgress(100);
        //显示网络异常布局
        mTvCenterBadnet.setVisibility(flag ? View.INVISIBLE : View.VISIBLE);
        //点击重新连接网络
        mTvCenterBadnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvCenterBadnet.setVisibility(View.INVISIBLE);
                //重新加载网页
                jianghu_webview.reload();
            }
        });
        hideProgressWithAnim();
    }

    /**
     * 隐藏加载对话框
     */
    private void hideProgressWithAnim() {
        AnimationSet animation = getDismissAnim(BlogDetailActivity.this);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                top_progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        top_progress.startAnimation(animation);
    }

    /**
     * 获取消失的动画
     *
     * @param context
     * @return
     */
    private AnimationSet getDismissAnim(Context context) {
        AnimationSet dismiss = new AnimationSet(context, null);
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setDuration(1000);
        dismiss.addAnimation(alpha);
        return dismiss;
    }

    public void initData() {
        blog_pinglun_button.setText(blogBean.getPinglun());
        switch (SettingDefaultsManager.getInstance().getBlogTextSize()) {
            case 0:
                URL = blogBean.getApp_link() + "?size=s";
                break;
            case 1:
                URL = blogBean.getApp_link() + "?size=m";
                break;
            case 2:
                URL = blogBean.getApp_link() + "?size=l";
                break;
            case 3:
                URL = blogBean.getApp_link() + "?size=xl";
                break;
            case 4:
                URL = blogBean.getApp_link() + "?size=xxl";
                break;
        }
        jianghu_webview.loadUrl(URL);
    }

    //    private void getUserBlogInfo(){
//        WebBase.getUserBlogInfo(blogBean.getBlog_id(), new JSONHandler() {
//            @Override
//            public void onSuccess(JSONObject obj) {
//                if(blogBean.is_myself()){
//                    care_about.setVisibility(View.GONE);
//                    cause_care_about_blog_button.setVisibility(View.GONE);
//                }else{
//                    if(obj.optInt("is_collect")==1){
//                        care_about.setVisibility(View.GONE);
//                        cause_care_about_blog_button.setVisibility(View.VISIBLE);
//                    }else{
//                        care_about.setVisibility(View.VISIBLE);
//                        cause_care_about_blog_button.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(String err_msg) {
//
//            }
//        });
//    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (jianghu_webview.canGoBack()) {
                jianghu_webview.goBack();//返回上一页面
                return true;
            } else {
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

    /**
     * 错误的时候进行的操作
     */
    private void errorOperation() {
        //隐藏webview
        jianghu_webview.setVisibility(View.INVISIBLE);

        if (View.INVISIBLE == top_progress.getVisibility()) {
            top_progress.setVisibility(View.VISIBLE);
        }

        top_progress.setCurProgress(80, 1500, new HProgressBarLoading.OnEndListener() {
            @Override
            public void onEnd() {
                //3.5s 加载 80->100 进度的加载
                top_progress.setCurProgress(100, 3500, new HProgressBarLoading.OnEndListener() {
                    @Override
                    public void onEnd() {
                        finishOperation(false);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    private Dialog showSetText() {
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
                String vealue = null;
                switch (volume) {
                    case 0:
                        vealue = "s";
                        break;
                    case 1:
                        vealue = "m";
                        break;
                    case 2:
                        vealue = "l";
                        break;
                    case 3:
                        vealue = "xl";
                        break;
                    case 4:
                        vealue = "xxl";
                        break;
                }
                URL = blogBean.getApp_link() + "?size=" + vealue;
                jianghu_webview.loadUrl("javascript:changeSize('" + vealue + "')");
            }
        });


        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        dialog.setCancelable(true);
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.blog_detail_pinglun:
//                if(ShowActivity.isLogin(this)){
//                    if(pinglun_dialog==null){
//                        pinglun_dialog=Editdialog1();
//                    }
//                    pinglun_dialog.show();
//                }
//                break;
//            case R.id.share_blog_detail:
//                ShowWebShareLayout.getiInstance().showShareLayout(blogBean);
//                break;
//            case R.id.blog_pinglun_button:
//                ShowActivity.showBlogPingActivity(this,blogBean);
//                break;
//            case R.id.care_about_blog_button:
//                if(ShowActivity.isLogin(this)){
//                    createBlogCollect();
//                }
//                break;
//            case R.id.cause_care_about_blog_button:
//                if(ShowActivity.isLogin(this)){
//                    removeBlogCollect();
//                }
//                break;
            case R.id.web_view_back:
                if (jianghu_webview.canGoBack()) {
                    jianghu_webview.goBack();//返回上一页面
                } else {
                    finish();
                }
                break;
            case R.id.blog_textsize_setting:
                if (mTextSetteingDialog == null) {
                    mTextSetteingDialog = showSetText();
                }
                mTextSetteingDialog.show();
                break;
            case R.id.blog_back:
                finish();
                break;
        }
    }
//    private void createBlogCollect(){
//        WebBase.createBlogCollect(blogBean.getBlog_id(), new JSONHandler(true,BlogDetailActivity.this,"正在加入收藏...") {
//            @Override
//            public void onSuccess(JSONObject obj) {
//                Toast.makeText(getBaseContext(),"收藏成功",Toast.LENGTH_SHORT).show();
//                cause_care_about_blog_button.setVisibility(View.VISIBLE);
//                care_about.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(String err_msg) {
//                Toast.makeText(getBaseContext(),"收藏失败"+err_msg,Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    private void removeBlogCollect(){
//        WebBase.removeBlogCollect(blogBean.getBlog_id(), new JSONHandler(true,BlogDetailActivity.this,"正在取消收藏...") {
//            @Override
//            public void onSuccess(JSONObject obj) {
//                Toast.makeText(getBaseContext(),"取消成功",Toast.LENGTH_SHORT).show();
//                cause_care_about_blog_button.setVisibility(View.GONE);
//                care_about.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onFailure(String err_msg) {
//                Toast.makeText(getBaseContext(),"取消失败"+err_msg,Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    private Dialog Editdialog1(){
//        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
//        final View layout = LayoutInflater.from(this).inflate(R.layout.blog_detail_pinglun_layout, null);
//        final EditText blog_detail_pinglun_edit=(EditText) layout.findViewById(R.id.blog_detail_pinglun_edit);
//        final Button send= (Button) layout.findViewById(R.id.send_pinglun_layout);
//        send.setEnabled(false);
//        send.setAlpha(0.5f);
//        blog_detail_pinglun_edit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(charSequence.equals("")||charSequence.length()==0){
//                    send.setEnabled(false);
//                    send.setAlpha(0.5f);
//                }else{
//                    send.setEnabled(true);
//                    send.setAlpha(1.0f);
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendPing(blog_detail_pinglun_edit);
//            }
//        });
//        dialog.setContentView(layout);
//        Window win = dialog.getWindow();
//        win.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams lp = win.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        win.setAttributes(lp);
//        win.setWindowAnimations(R.style.dialoganimstyle);
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                blog_detail_pinglun_edit.setFocusable(false);
//                blog_detail_pinglun_edit.setFocusableInTouchMode(false);
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(layout.getWindowToken(), 0); //强制隐藏键盘
//            }
//        });
//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                showSoftInputFromWindow(blog_detail_pinglun_edit);
//            }
//        });
//        return  dialog;
//    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        pinglun_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void sendPing(final EditText editText) {
//        WebBase.createUserBlogPost(blogBean.getBlog_id(),editText.getText().toString(), new JSONHandler(true,this,"正在提交...") {
//            @Override
//            public void onSuccess(JSONObject obj) {
//                pinglun_dialog.dismiss();
//                editText.setText("");
//                Toast.makeText(getBaseContext(),"评论成功",Toast.LENGTH_SHORT).show();
//                String number=blog_pinglun_button.getText().toString();
//                int count=0;
//                if(!TextUtils.isEmpty(number)){
//                    count=Integer.valueOf(number);
//                }
//                count+=1;
//                blog_pinglun_button.setText(count+"");
//                jianghu_webview.loadUrl("javascript:getBlogPosts()");
//            }
//
//            @Override
//            public void onFailure(String err_msg) {
//                pinglun_dialog.dismiss();
//                Toast.makeText(getBaseContext(),"评论失败",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void getBlogDetail(String blog_id) {
        WebBase.searchUserBlogInfo(blog_id, new JSONHandler(false, BlogDetailActivity.this, "") {
            @Override
            public void onSuccess(JSONObject obj) {
                BlogBean blogBean = new BlogBean();
                blogBean.setBlog_id(obj.optString("blog_id"));
                blogBean.setTitle(obj.optString("subject"));
                blogBean.setImg(obj.optString("cover"));
                blogBean.setLook_number(obj.optJSONObject("stat").optString("views"));
                blogBean.setPinglun(obj.optJSONObject("stat").optString("replys"));
                blogBean.setWap_link(obj.optJSONObject("link").optString("wap"));
                blogBean.setApp_link(obj.optJSONObject("link").optString("app"));
                blogBean.setDate(obj.optString("posted_at"));
                ShowActivity.showBlogDetailActivity(BlogDetailActivity.this, blogBean);
            }

            @Override
            public void onFailure(String err_msg) {
                Log.e("err_msg", err_msg);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (jianghu_webview != null) {
            jianghu_webview.loadUrl("javascript:getBlogPosts()");
        }
    }

    public void getGroupInfo(String id) {
//        Group groupbean=new Group();
//        groupbean.setId(id);
//        ShowActivity.showGroupDetailActivity(BlogDetailActivity.this,groupbean);
    }

    public class ScrollTop {
        private SwipeRefreshLayout swipeRefreshLayout;

        public ScrollTop(SwipeRefreshLayout swipeRefreshLayout) {
            this.swipeRefreshLayout = swipeRefreshLayout;
        }

        public void setCanScroll(boolean canScroll) {
            swipeRefreshLayout.setEnabled(canScroll);
        }
    }
}