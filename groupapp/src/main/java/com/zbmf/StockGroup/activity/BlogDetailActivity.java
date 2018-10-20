package com.zbmf.StockGroup.activity;

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
import android.text.TextUtils;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveLoginListener;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.PublishInfo;
import com.bokecc.sdk.mobile.live.pojo.RoomInfo;
import com.bokecc.sdk.mobile.live.pojo.TemplateInfo;
import com.bokecc.sdk.mobile.live.pojo.Viewer;
import com.zbmf.StockGroup.GroupActivity;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.beans.ShareBean;
import com.zbmf.StockGroup.beans.Stock;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.dialog.EditTextDialog;
import com.zbmf.StockGroup.dialog.TextDialog;
import com.zbmf.StockGroup.interfaces.DialogYesClick;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.webclient.GroupWebViewClient;
import com.zbmf.StockGroup.interfaces.OnUrlClick;
import com.zbmf.StockGroup.utils.FileUtils;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.utils.ShowWebShareLayout;
import com.zbmf.StockGroup.view.CusSeekbar;
import com.zbmf.StockGroup.view.HProgressBarLoading;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.zbmf.StockGroup.utils.ShowActivity.showActivityForResult;

/**
 * Created by xuhao on 2016/6/20.
 */
public class BlogDetailActivity extends BaseActivity implements View.OnClickListener, OnUrlClick {
    private WebView jianghu_webview;
    private String  URL;
    private BlogBean blogBean;
    private HProgressBarLoading top_progress;
    private Dialog mTextSetteingDialog;
    private Dialog pinglun_dialog;
    private TextView blog_detail_pinglun,blog_pinglun_button;
    private ImageButton care_about,cause_care_about_blog_button;
    private boolean isContinue = false;
    private TextView mTvCenterBadnet;
    private ImageView loadingImageView;
    private AnimationDrawable animationDrawable;
    private ShowWebShareLayout showWebShareLayout;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(showWebShareLayout==null){
            showWebShareLayout=new ShowWebShareLayout(this);
        }
        showWebShareLayout.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.blog_detail_layout;
    }

    @Override
    public void initView() {
        if(showWebShareLayout==null){
            showWebShareLayout=new ShowWebShareLayout(this);
        }
        init();
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init(){
        initTitle();
        jianghu_webview= (WebView) findViewById(R.id.blog_detail_webview);
        blog_detail_pinglun= (TextView) findViewById(R.id.blog_detail_pinglun);
        blog_pinglun_button= (TextView) findViewById(R.id.blog_pinglun_button);
        mTvCenterBadnet = (TextView) findViewById(R.id.tv_center_badnet);
        final SwipeRefreshLayout swipeRefreshLayout= (SwipeRefreshLayout)findViewById(R.id.swiperefresh_id);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新刷新页面
                jianghu_webview.reload();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        care_about= (ImageButton) findViewById(R.id.care_about_blog_button);
        loadingImageView= (ImageView) findViewById(R.id.loadingImageView);
        animationDrawable = (AnimationDrawable) loadingImageView.getDrawable();
        animationDrawable.start();
        cause_care_about_blog_button= (ImageButton) findViewById(R.id.cause_care_about_blog_button);

        WebSettings settings=jianghu_webview.getSettings();
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
        String agent=settings.getUserAgentString();
        settings.setUserAgentString(agent.replace("Android", "StockGroup"));
        jianghu_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        jianghu_webview.setWebChromeClient(new WebChromeClient(){
            //设置加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //如果进度条隐藏则让它显示
                if (View.INVISIBLE == top_progress.getVisibility()) {
                    top_progress.setVisibility(View.VISIBLE);
                }
                if(newProgress==100){
                    animationDrawable.stop();
                    loadingImageView.clearAnimation();
                    loadingImageView.setVisibility(View.GONE);
                    if(swipeRefreshLayout.isRefreshing()){
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
                }else{
                    top_progress.setNormalProgress(newProgress);
                }
            }
            //设置头部
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        jianghu_webview.setWebViewClient(new GroupWebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                errorOperation();
            }
        }
        .setUrlClick(this));
        top_progress= (HProgressBarLoading) findViewById(R.id.top_progress);
        jianghu_webview.addJavascriptInterface(new ScrollTop(swipeRefreshLayout),"blogdetail");
        jianghu_webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        if(pinglun_dialog==null){
            pinglun_dialog=Editdialog1();
            pinglun_dialog.show();
        }
        pinglun_dialog.dismiss();
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
    public void initData(){
        blogBean= (BlogBean) getIntent().getSerializableExtra("blogBean");
        if(blogBean.getApp_link()!=null){
            loadUrl();
        }else{
            searchUserBlogInfo();
        }
    }
    private void loadUrl(){
        switch (SettingDefaultsManager.getInstance().getBlogTextSize()) {
            case 0:
                URL=blogBean.getApp_link()+"?size=s";
                break;
            case 1:
                URL=blogBean.getApp_link()+"?size=m";
                break;
            case 2:
                URL=blogBean.getApp_link()+"?size=l";
                break;
            case 3:
                URL=blogBean.getApp_link()+"?size=xl";
                break;
            case 4:
                URL=blogBean.getApp_link()+"?size=xxl";
                break;
        }
        LogUtil.e(URL);
        jianghu_webview.loadUrl(URL);
        getUserBlogInfo();
        blog_pinglun_button.setText(blogBean.getPinglun());
    }
    @Override
    public void addListener() {
        findViewById(R.id.blog_textsize_setting).setVisibility(View.VISIBLE);
        findViewById(R.id.blog_textsize_setting).setOnClickListener(this);
        blog_detail_pinglun.setOnClickListener(this);
        findViewById(R.id.share_blog_detail).setVisibility(View.VISIBLE);
        findViewById(R.id.share_blog_detail).setOnClickListener(this);
        findViewById(R.id.blog_pinglun_button).setOnClickListener(this);
        care_about.setOnClickListener(this);
        cause_care_about_blog_button.setOnClickListener(this);
    }
    private void searchUserBlogInfo(){
        WebBase.searchUserBlogInfo(blogBean.getBlog_id(), new JSONHandler(
                true,
                BlogDetailActivity.this,
                getString(R.string.loading)
        ) {
            @Override
            public void onSuccess(JSONObject obj) {
                blogBean= JSONParse.getBlogDetail(obj);
                loadUrl();
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
    private void getUserBlogInfo(){
        WebBase.getUserBlogInfo(blogBean.getBlog_id(), new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if(blogBean.is_myself()){
                    care_about.setVisibility(View.GONE);
                    cause_care_about_blog_button.setVisibility(View.GONE);
                }else{
                    if(obj.optInt("is_collect")==1){
                        care_about.setVisibility(View.GONE);
                        cause_care_about_blog_button.setVisibility(View.VISIBLE);
                    }else{
                        care_about.setVisibility(View.VISIBLE);
                        cause_care_about_blog_button.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
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
                String vealue=null;
                switch (volume) {
                    case 0:
                        vealue="s";
                        break;
                    case 1:
                        vealue="m";
                        break;
                    case 2:
                        vealue="l";
                        break;
                    case 3:
                        vealue="xl";
                        break;
                    case 4:
                        vealue="xxl";
                        break;
                }
                URL=blogBean.getApp_link()+"?size="+vealue;
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
        switch (view.getId()){
            case R.id.blog_detail_pinglun:
                if(ShowActivity.isLogin(this)){
                    if(TextUtils.isEmpty(SettingDefaultsManager.getInstance().getUserPhone())){
                        TextDialog.createDialog(this)
                                .setTitle("")
                                .setMessage("为响应国家政策，请于操作前绑定手机信息！")
                                .setLeftButton("下次再说")
                                .setRightButton("前往绑定")
                                .setRightClick(new DialogYesClick() {
                                    @Override
                                    public void onYseClick() {
                                        Bundle bundle=new Bundle();
                                        bundle.putInt(IntentKey.FLAG,1);
                                        showActivityForResult(BlogDetailActivity.this,bundle, BindPhoneActivity.class,RequestCode.BINDED);
                                    }
                                })
                                .show();
                    }else{
                        if(pinglun_dialog==null){
                            pinglun_dialog=Editdialog1();
                        }
                        pinglun_dialog.show();
                    }
                }
                break;
            case R.id.share_blog_detail:
                if(showWebShareLayout==null){
                    showWebShareLayout=new ShowWebShareLayout(this);
                }
                showWebShareLayout.showShareLayout(new ShareBean(blogBean.getTitle(),blogBean.getImg(),blogBean.getWap_link()));
                break;
            case R.id.blog_pinglun_button:
                ShowActivity.showBlogPingActivity(this,blogBean);
                break;
            case R.id.care_about_blog_button:
                if(ShowActivity.isLogin(this)){
                    createBlogCollect();
                }
                break;
            case R.id.cause_care_about_blog_button:
                if(ShowActivity.isLogin(this)){
                    removeBlogCollect();
                }
                break;
            case R.id.blog_textsize_setting:
                if (mTextSetteingDialog == null){
                    mTextSetteingDialog = showSetText();
                }
                mTextSetteingDialog.show();
                break;
        }
    }
    private void createBlogCollect(){
        WebBase.createBlogCollect(blogBean.getBlog_id(), new JSONHandler(true,BlogDetailActivity.this,"正在加入收藏...") {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getBaseContext(),"收藏成功",Toast.LENGTH_SHORT).show();
                cause_care_about_blog_button.setVisibility(View.VISIBLE);
                care_about.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(),"收藏失败"+err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void removeBlogCollect(){
        WebBase.removeBlogCollect(blogBean.getBlog_id(), new JSONHandler(true,BlogDetailActivity.this,"正在取消收藏...") {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getBaseContext(),"取消成功",Toast.LENGTH_SHORT).show();
                cause_care_about_blog_button.setVisibility(View.GONE);
                care_about.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(),"取消失败"+err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Dialog Editdialog1(){
        return EditTextDialog.createDialog(this, R.style.myDialogTheme)
                .setRightButton(getString(R.string.send_button))
                .setEditHint(getString(R.string.blog_message))
                .setSendClick(new EditTextDialog.OnSendClick() {
                    @Override
                    public void onSend(String message, int type) {
                        sendPing(message);
                    }
                });
    }
    public void sendPing(final String message){
        WebBase.createUserBlogPost(blogBean.getBlog_id(),message, new JSONHandler(true,this,"正在提交...") {
            @Override
            public void onSuccess(JSONObject obj) {
                pinglun_dialog.dismiss();
                Toast.makeText(getBaseContext(),"评论成功",Toast.LENGTH_SHORT).show();
                String number=blog_pinglun_button.getText().toString();
                int count=0;
                if(!TextUtils.isEmpty(number)){
                    count=Integer.valueOf(number);
                }
                count+=1;
                blog_pinglun_button.setText(count+"");
                jianghu_webview.loadUrl("javascript:getBlogPosts()");
            }

            @Override
            public void onFailure(String err_msg) {
                pinglun_dialog.dismiss();
                Toast.makeText(getBaseContext(),"评论失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(jianghu_webview!=null){
            jianghu_webview.loadUrl("javascript:getBlogPosts()");
        }
    }

    @Override
    public void onGroup(Group group) {
        ShowActivity.showGroupDetailActivity(this, group);
    }

    @Override
    public void onBolg(BlogBean blogBean) {
        ShowActivity.showBlogDetailActivity(this, blogBean);
    }

    @Override
    public void onVideo(Video video) {
        toVideo(video);
    }

    @Override
    public void onWeb(String url) {
        if(url.endsWith("/posts/")&&url.startsWith("http://app://blog/")){
            ShowActivity.showBlogPingActivity(BlogDetailActivity.this,blogBean);
        }else{
            ShowActivity.showWebViewActivity(this,url);
        }
    }

    @Override
    public void onImage(String url) {
        ShowActivity.ShowBigImage(this, url);
    }

    @Override
    public void onPay() {
        ShowActivity.showPayDetailActivity(this);
    }

    @Override
    public void onStock(Stock stock) {
        Bundle bundle=new Bundle();
        bundle.putSerializable(IntentKey.STOCKHOLDER,stock);
        ShowActivity.showActivity(this,bundle, StockDetailActivity.class);
    }

    @Override
    public void onScreen() {
        ShowActivity.showActivity(this,ScreenActivity.class);
    }

    @Override
    public void onScreenDetail(Screen screen) {
        Bundle bundle=new Bundle();
        bundle.putSerializable(IntentKey.SCREEN,screen);
        ShowActivity.showActivity(this,bundle,ScreenDetailActivity.class);
    }
    @Override
    public void onQQ(String url) {
        ShowActivity.showQQ(this);
    }
    @Override
    public void onModeStock() {
        ShowActivity.showActivity(this,StockModeActivity.class);
    }

    @Override
    public void onDongAsk() {
        ShowActivity.showActivity(this,DongAskActivity.class);
    }

    private void toVideo(final Video video) {
        if (video.getIs_live()) {
            DWLive.getInstance().setDWLiveLoginParams(new DWLiveLoginListener() {
                @Override
                public void onLogin(TemplateInfo templateInfo, Viewer viewer, RoomInfo roomInfo, PublishInfo publishInfo) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY, video);
                            ShowActivity.showActivityForResult(BlogDetailActivity.this, bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                        }
                    });
                }

                @Override
                public void onException(final DWLiveException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle=new Bundle();
                            bundle.putSerializable(IntentKey.VIDEO_KEY,video);
                            ShowActivity.showActivityForResult(BlogDetailActivity.this,bundle, VideoPlayActivity.class, RequestCode.COMIT_VIDEO);
                        }
                    });
                }
            }, Constants.CC_USERID, video.getBokecc_id() + "", SettingDefaultsManager.getInstance().NickName(), "");
            DWLive.getInstance().startLogin();
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.VIDEO_KEY, video);
            ShowActivity.showActivity(BlogDetailActivity.this, bundle, VideoPlayActivity.class);
        }
    }

    public class ScrollTop{
        private SwipeRefreshLayout swipeRefreshLayout;
        public ScrollTop(SwipeRefreshLayout swipeRefreshLayout){
            this.swipeRefreshLayout=swipeRefreshLayout;
        }
        public void setCanScroll(boolean canScroll){
            swipeRefreshLayout.setEnabled(canScroll);
        }
    }

}