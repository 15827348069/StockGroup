package com.zbmf.StockGroup.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveLoginListener;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.PublishInfo;
import com.bokecc.sdk.mobile.live.pojo.RoomInfo;
import com.bokecc.sdk.mobile.live.pojo.TemplateInfo;
import com.bokecc.sdk.mobile.live.pojo.Viewer;
import com.zbmf.StockGroup.activity.BlogDetailActivity;
import com.zbmf.StockGroup.activity.GroupDetailActivity;
import com.zbmf.StockGroup.activity.PayDetailActivity;
import com.zbmf.StockGroup.activity.VideoEmptyActivity;
import com.zbmf.StockGroup.activity.VideoPlayActivity;
import com.zbmf.StockGroup.activity.WebViewActivity;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.TopticBean;
import com.zbmf.StockGroup.beans.Video;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuhao on 2017/8/2.
 */

public class WebNoticitionUitl {
    private static final String blogReg="people\\/(\\d+)\\/blog\\/(\\d+)";
    private static final String groupReg="people\\/(\\d+)\\/live";
    private static final String accountPay="account/trade/pay/";
    private static final String videoReg="school\\/(\\d+)\\/video_play";
    public static Intent ShowActivity(final Context activity, String url){
        Pattern blogP = Pattern.compile(blogReg);
        Matcher blogM = blogP.matcher(url);
        boolean message_find=blogM.find();
       if(message_find){
                Intent intent = new Intent(activity,BlogDetailActivity.class);
                BlogBean blogBean=new BlogBean();
                blogBean.setBlog_id(blogM.group(1)+"-"+blogM.group(2));
                intent.putExtra("blogBean",blogBean);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                return intent;
        }
        Pattern groupP = Pattern.compile(groupReg);
        Matcher groupM = groupP.matcher(url);
        if(groupM.find()){
            Group group_tp=new Group();
            group_tp.setId(groupM.group(1));
            Intent intent=new Intent(activity, GroupDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(IntentKey.GROUP,group_tp);
            return intent;
        }
        Pattern payP = Pattern.compile(accountPay);
        Matcher payM = payP.matcher(url);
        if(payM.find()){
            return new Intent(activity, PayDetailActivity.class);
        }
        Pattern video=Pattern.compile(videoReg);
        Matcher videoM=video.matcher(url);
        if(videoM.find()){
            Intent intent=new Intent(activity, VideoEmptyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(IntentKey.VIDEO_KEY,videoM.group(1));
            return intent;
        }
        Intent intent = new Intent(activity,WebViewActivity.class);
        intent.putExtra("web_url",url);
        return intent;
    }
}
