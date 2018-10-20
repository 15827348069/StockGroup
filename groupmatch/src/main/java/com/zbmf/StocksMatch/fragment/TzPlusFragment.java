package com.zbmf.StocksMatch.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshBase;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshScrollView;

public class TzPlusFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean isFirst = true;
    private WebView webView;
    private View rootView;
    public TzPlusFragment() {
    }

    public static TzPlusFragment newInstance(String param1, String param2) {
        TzPlusFragment fragment = new TzPlusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        Log.e("tag","init this");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("tag", "create this");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("tag", "onAttach this");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("tag", "onCreateView this");
        if(rootView==null){
            rootView=inflater.inflate(R.layout.web, null);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("tag", "onViewCreated this");
        ImageView btnLeft = (ImageView) view.findViewById(R.id.iv_back);
        btnLeft.setVisibility(View.INVISIBLE);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.tz);

        final String web_url = "http://m.zbmf.com/match/investment/";

        final ProgressBar mProgressBar = (ProgressBar) view.findViewById(R.id.mProgressBar); // 进度条
        final Animation alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.progress_bar_alpha); // 渐变动画
        mProgressBar.setMax(100);

        webView = (WebView) view.findViewById(R.id.webView);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false); // 设置是否支持缩放
        webView.getSettings().setSupportZoom(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    if (mProgressBar.getVisibility() == View.GONE)
                        mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(progress);
                } else {
                    mProgressBar.setProgress(100);
                    mProgressBar.startAnimation(alphaAnimation);
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("app:trading-")) {

                    Bundle bundle1 = new Bundle();
                    bundle1.putString("web_url",url.substring("app:trading-".length(), url.length()));
                    bundle1.putInt("soure_act",9);
                    UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ACCOUNT_Web,bundle1);
                }else if (url.startsWith("app:group")) {
                    UiCommon.INSTANCE.openOther("com.zbmf.StockGroup",getContext(),getActivity(),"http://www.zbmf.com/bin/stocksgroup.apk");
                }else if (url.startsWith("app:jcp")) {
                    UiCommon.INSTANCE.openOther("com.colin.jincaopan",getContext(),getActivity(),"https://www.jincaopan.com/soft/jincaopan_ds.apk");
                }else{
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isFirst = false;
            }

        });
        if(isFirst)
        webView.loadUrl(web_url);
    }

}
