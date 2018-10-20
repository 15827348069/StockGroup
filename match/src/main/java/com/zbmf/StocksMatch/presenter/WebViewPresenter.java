package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.common.Constans;
import com.zbmf.StocksMatch.common.RegConstans;
import com.zbmf.StocksMatch.listener.IWebView;
import com.zbmf.StocksMatch.listener.OnUrlClick;
import com.zbmf.StocksMatch.model.GroupWebViewClientMode;
import com.zbmf.worklibrary.presenter.BasePresenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pq
 * on 2018/3/29.
 */

public class WebViewPresenter extends BasePresenter<GroupWebViewClientMode,IWebView> {
    private OnUrlClick onUrlClick;
    private String url;

    public WebViewPresenter(OnUrlClick onUrlClick, String url) {
        this.onUrlClick = onUrlClick;
        this.url = url;
    }

    @Override
    public void getDatas() {
     if (isFirst()){
         setFirst(false);
     }
     if (onUrlClick!=null&& !TextUtils.isEmpty(url)){
         UrlClick(onUrlClick,url);
     }
    }

    @Override
    public GroupWebViewClientMode initMode() {
        return new GroupWebViewClientMode();
    }
    public void UrlClick(OnUrlClick onUrlClick, String url){
        Pattern blogP = Pattern.compile(RegConstans.blogReg);
        Matcher blogM = blogP.matcher(url);
        boolean message_find=blogM.find();
//        if(message_find){
//            getMode().searchUserBlogInfo(blogM.group(1) + "-" + blogM.group(2), new CallBack() {
//                @Override
//                public void onSuccess(Object o) {
//
//                }
//
//                @Override
//                public void onFail(String msg) {
//
//                }
//            });
//        }
        if(url.startsWith("http:")||url.startsWith("https:")){
            if(onUrlClick!=null){
                onUrlClick.onWeb(url);
                if (getView()!=null){
                    getView().loadResult(Constans.GAIN_DATA_SUCCESS);
                }
            }
        }else{
            if(onUrlClick!=null){
                onUrlClick.onWeb("http://"+url);
                if (getView()!=null){
                    getView().loadResult(Constans.GAIN_DATA_SUCCESS);
                }
            }
        }
    }
}
