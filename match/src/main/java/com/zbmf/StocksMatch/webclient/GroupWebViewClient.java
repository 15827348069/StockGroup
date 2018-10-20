package com.zbmf.StocksMatch.webclient;


import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zbmf.StocksMatch.bean.Group;
import com.zbmf.StocksMatch.common.RegConstans;
import com.zbmf.StocksMatch.listener.OnUrlClick;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuhao
 * on 2017/9/8.
 */

public class GroupWebViewClient extends WebViewClient {
    private OnUrlClick onUrlClick;

    public GroupWebViewClient setUrlClick(OnUrlClick urlClick) {
        this.onUrlClick = urlClick;
        return this;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        UrlMatch(url);
        return true;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//        super.onReceivedSslError(view, handler, error);
        // 接受所有网站的证书，忽略SSL错误，执行访问网页
        handler.proceed();
    }

    private void UrlMatch(String url){
        Pattern blogP = Pattern.compile(RegConstans.blogReg);
        Matcher blogM = blogP.matcher(url);
        boolean message_find=blogM.find();
        if(message_find){
            getBlogMessage(blogM.group(1)+"-"+blogM.group(2));
            return;
        }
        Pattern groupP = Pattern.compile(RegConstans.groupReg);
        Matcher groupM = groupP.matcher(url);
        if(groupM.find()){
            Group group=new Group();
            group.setId(groupM.group(1));
            if(onUrlClick!=null){
                onUrlClick.onGroup(group);
            }
            return;
        }
        Pattern payP = Pattern.compile(RegConstans.accountPay);
        Matcher payM = payP.matcher(url);
        if(payM.find()){
            if(onUrlClick!=null){
                onUrlClick.onPay();
            }
            return;
        }
        Pattern video=Pattern.compile(RegConstans.videoReg);
        Matcher videoM=video.matcher(url);
        if(videoM.find()){
//           getVideoMessage(videoM.group(1));
            return;
        }
        if(url.startsWith("img:")){
            if(onUrlClick!=null){
                onUrlClick.onImage(url.replace("img:",""));
            }
            return;
        }
        String[] web_url = url.split("/");
        if(web_url.length==4&&url.startsWith("app://group/")){
            //进入圈子
            Group group=new Group();
            group.setId(web_url[3]);
            if(onUrlClick!=null){
                onUrlClick.onGroup(group);
            }
            return;
        }
        if(web_url.length==7&&url.startsWith("app://app/people/")){
            getBlogMessage(web_url[4]+"-"+web_url[6]);
            return;
        }
        Pattern stockP = Pattern.compile(RegConstans.stockReg);
        Matcher stockM = stockP.matcher(url);
//        if(stockM.find()){
//            WebBase.getStockRealtimeInfo(stockM.group(1), new JSONHandler() {
//                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                @Override
//                public void onSuccess(JSONObject obj) {
//                    Stock stock=JSONParse.getStockRealtimeInfo(obj);
//                    if(onUrlClick!=null){
//                        onUrlClick.onStock(stock);
//                    }
//                }
//
//                @Override
//                public void onFailure(String err_msg) {
//
//                }
//            });
//            return;
//        }
        if(url.startsWith("http:")||url.startsWith("https:")){
            if(onUrlClick!=null){
                onUrlClick.onWeb(url);
            }
        }else{
            if(onUrlClick!=null){
                onUrlClick.onWeb("http://"+url);
            }
        }
    }
    private void getBlogMessage(String blog_id){
//        WebBase.searchUserBlogInfo(blog_id, new JSONHandler() {
//            @Override
//            public void onSuccess(JSONObject obj) {
//                BlogBean blogBean=new BlogBean();
//                blogBean.setBlog_id(obj.optString("blog_id"));
//                blogBean.setTitle(obj.optString("subject"));
//                blogBean.setImg(obj.optString("cover"));
//                blogBean.setLook_number(obj.optJSONObject("stat").optString("views"));
//                blogBean.setPinglun(obj.optJSONObject("stat").optString("replys"));
//                blogBean.setWap_link(obj.optJSONObject("link").optString("wap"));
//                blogBean.setApp_link(obj.optJSONObject("link").optString("app"));
//                blogBean.setDate(obj.optString("posted_at"));
//                if(onUrlClick!=null){
//                    onUrlClick.onBolg(blogBean);
//                }
//            }
//            @Override
//            public void onFailure(String err_msg) {
//
//            }
//        });
    }
//    private void getVideoMessage(String video_id){
//        WebBase.GetVideoDetail(video_id, new JSONHandler() {
//            @Override
//            public void onSuccess(JSONObject obj) {
//                if(!obj.isNull("video")){
//                    JSONObject jsonObject = obj.optJSONObject("video");
//                    if(onUrlClick!=null){
//                        onUrlClick.onVideo( JSONParse.getVideo(jsonObject));
//                    }
//                }
//            }
//            @Override
//            public void onFailure(String err_msg) {
//
//            }
//        });
//    }
}
