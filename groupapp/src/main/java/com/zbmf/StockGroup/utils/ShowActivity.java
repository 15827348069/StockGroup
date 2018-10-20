package com.zbmf.StockGroup.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.zbmf.StockGroup.GroupActivity;
import com.zbmf.StockGroup.activity.AddFansActivity;
import com.zbmf.StockGroup.activity.BigImageActivity;
import com.zbmf.StockGroup.activity.BlogDetailActivity;
import com.zbmf.StockGroup.activity.BlogPingActivity;
import com.zbmf.StockGroup.activity.BoxDetailActivity;
import com.zbmf.StockGroup.activity.BoxDetailWebActivity;
import com.zbmf.StockGroup.activity.Chat1Activity;
import com.zbmf.StockGroup.activity.ConponsActivity;
import com.zbmf.StockGroup.activity.FB_GD_Activity;
import com.zbmf.StockGroup.activity.FansActivity;
import com.zbmf.StockGroup.activity.GDDetailActivity;
import com.zbmf.StockGroup.activity.GroupDetailActivity;
import com.zbmf.StockGroup.activity.HistoryDateActivity;
import com.zbmf.StockGroup.activity.HtActivity;
import com.zbmf.StockGroup.activity.LiveHistoryActivity;
import com.zbmf.StockGroup.activity.LoginActivity;
import com.zbmf.StockGroup.activity.MatchRankActivity;
import com.zbmf.StockGroup.activity.MyDetailActivity;
import com.zbmf.StockGroup.activity.NoReadMsgActivity;
import com.zbmf.StockGroup.activity.NuggetsShareActivity;
import com.zbmf.StockGroup.activity.PayDetailActivity;
import com.zbmf.StockGroup.activity.RedPackgedActivity;
import com.zbmf.StockGroup.activity.ScreenDetailActivity;
import com.zbmf.StockGroup.activity.SettingActivity;
import com.zbmf.StockGroup.activity.SimulateStockChatActivity;
import com.zbmf.StockGroup.activity.SmartSelectStockActivity;
import com.zbmf.StockGroup.activity.StockArgumentActivity;
import com.zbmf.StockGroup.activity.ThirdPartyLogin;
import com.zbmf.StockGroup.activity.TopicDetailActivty;
import com.zbmf.StockGroup.activity.UserDetailActivity;
import com.zbmf.StockGroup.activity.VideoListActivity;
import com.zbmf.StockGroup.activity.VipActivity;
import com.zbmf.StockGroup.activity.WebViewActivity;
import com.zbmf.StockGroup.activity.ZbmfSelectStockMatch;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.BoxBean;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.MatchInfo;
import com.zbmf.StockGroup.beans.Rank;
import com.zbmf.StockGroup.beans.RedPackgedBean;
import com.zbmf.StockGroup.beans.StockMode;
import com.zbmf.StockGroup.beans.Types;
import com.zbmf.StockGroup.beans.VideoTeacher;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.HtmlUrl;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2016/12/14.
 */

public class ShowActivity {
    public  static Intent intent=null;
    public static void showActivity(Activity activity, Class<?> c){
        intent=new Intent(activity,c);
        StartActivity(activity,intent);
    }
    public static void showActivity(Context context, Class<?> c){
        intent=new Intent(context,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        StartActivity(context,intent);
    }
    public static void StartActivity(Activity activity,Intent intent){
        activity.startActivity(intent);
        if(intent!=null){
            intent=null;
        }
    }
    public static void StartActivityForResult(Activity activity,Intent intent,int code){
        activity.startActivityForResult(intent,code);
        if(intent!=null){
            intent=null;
        }
    }
    public static void showActivity(Activity activity, Bundle aBundle,  Class<?> c){
        Intent intent = new Intent(activity,c);
        if (null != aBundle) {
            intent.putExtras(aBundle);
        }
        activity.startActivity(intent);
    }
    public static void showActivityForResult(Activity activity, Bundle aBundle,Class<?> c,int code){
        Intent intent = new Intent(activity,c);
        if (null != aBundle) {
            intent.putExtras(aBundle);
        }
        activity.startActivityForResult(intent,code);
    }
    public static void showActivity(Activity activity, Bundle aBundle, String targetName){
        Intent intent = new Intent();
        intent.setClassName(activity.getPackageName(),targetName);
        if (null != aBundle) {
            intent.putExtras(aBundle);
        }
        activity.startActivity(intent);
    }

    public static void StartActivity(Context context,Intent intent){
        context.startActivity(intent);
        if(intent!=null){
            intent=null;
        }
    }
    public static void showPayDetailActivity(Activity activity)
    {
        intent=new Intent(activity, PayDetailActivity.class);
        StartActivity(activity,intent);
    }
    public static void showPayDetailActivity(Activity activity,int flag)
    {
        intent=new Intent(activity, PayDetailActivity.class);
        intent.putExtra(IntentKey.FLAG,flag);
        StartActivity(activity,intent);
    }
    public static void showPayDetailActivity1(Activity activity,float money)
    {
        intent=new Intent(activity, PayDetailActivity.class);
        intent.putExtra(IntentKey.MONEY,money);
        StartActivity(activity,intent);
    }

    /**
     * 大图
     * @param context
     * @param img_url
     */
    public static void ShowBigImage(Context context,String img_url){
        intent=new Intent(context, BigImageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("img_url",img_url);
        StartActivity(context,intent);
    }

    /**
     * 红包页面
     * @param activity
     * @param red
     */
    public static void showRedBagActivity(FragmentActivity activity, RedPackgedBean red) {
        intent=new Intent(activity, RedPackgedActivity.class);
        intent.putExtra("redpackged",red);
        StartActivity(activity,intent);
    }

    /**
     * 直播室
     * @param activity
     */
    public static void showChatActivity(Activity activity,Group group){
        intent=new Intent(activity, Chat1Activity.class);
        intent.putExtra(IntentKey.GROUP,group);
        StartActivity(activity,intent);
    }
    /**
     * 圈子简介
     * @param activity
     */
    public static void showGroupDetailActivity(Activity activity,Group group){
        intent=new Intent(activity, GroupDetailActivity.class);
        intent.putExtra(IntentKey.GROUP,group);
        StartActivity(activity,intent);
    }
    public static void showGroupDetailActivity(Activity activity,String  group_id){
        intent=new Intent(activity, GroupDetailActivity.class);
        Group group=new Group();
        group.setId(group_id);
        intent.putExtra(IntentKey.GROUP,group);
        StartActivity(activity,intent);
    }
    public static void showGroupDetailActivity(Activity activity,Group group,int flag){
        intent=new Intent(activity, GroupDetailActivity.class);
        intent.putExtra(IntentKey.GROUP,group);
        intent.putExtra(IntentKey.FLAG,flag);
        StartActivity(activity,intent);
    }
    public static void showVideoListActivity(Activity activity,VideoTeacher videoTeacher){
        intent=new Intent(activity, VideoListActivity.class);
        intent.putExtra(IntentKey.VIDEO_TEACHER,videoTeacher);
        StartActivity(activity,intent);
    }
    /**
     * 我的资料
     * @param activity
     */
    public static void showMyDetail(Activity activity){
        intent=new Intent(activity, MyDetailActivity.class);
        StartActivity(activity,intent);
    }
    /**
     * 优惠券
     * @param activity
     */
    public static void showConponsActivity(Activity activity){
        intent=new Intent(activity, ConponsActivity.class);
        StartActivity(activity,intent);
    }
    /**
     * 直播室历史纪录
     * @param activity
     */
    public static void showLiveHistoryActivity(Activity activity,Group group){
        intent=new Intent(activity, LiveHistoryActivity.class);
        intent.putExtra(IntentKey.GROUP,group);
        StartActivity(activity,intent);
    }
    /**
     * 直播室历史纪录日期
     * @param activity
     */
    public static void showLiveHistoryDateActivity(Activity activity,Group group){
        intent=new Intent(activity, HistoryDateActivity.class);
        intent.putExtra(IntentKey.GROUP,group);
        StartActivity(activity,intent);
    }
    /**
     * 铁粉专区
     * @param activity
     * @param
     */
    public static void showFansActivity(Activity activity,Group group){
        intent=new Intent(activity, FansActivity.class);
        intent.putExtra(IntentKey.GROUP,group);
        StartActivityForResult(activity,intent, Constants.FANS_MESSAGE);
    }
    /**
     * 加入铁粉专区
     * @param activity
     * @param
     */
    public static void showAddFansActivity(Activity activity,Group group){
        intent=new Intent(activity, AddFansActivity.class);
        intent.putExtra(IntentKey.GROUP,group);
        StartActivityForResult(activity,intent,RequestCode.ADD_FANS);
    }
    /**
     * 铁粉宝盒简介专区
     * @param activity
     * @param boxBean
     */
    public static void showBoxDetailDescActivity(Activity activity, BoxBean boxBean){
        intent=new Intent(activity, BoxDetailActivity.class);
        intent.putExtra("BoxBean",boxBean);
        StartActivity(activity,intent);
    }
    /**
     * 铁粉宝盒详情专区
     * @param activity
     * @param boxBean
     */
    public static void showBoxDetailActivity(Activity activity, BoxBean boxBean){
        intent=new Intent(activity, BoxDetailWebActivity.class);
        intent.putExtra("BoxBean",boxBean);
        StartActivity(activity,intent);
    }
    /**
     * 铁粉宝盒详情专区
     * @param activity
     */
    public static void showBoxDetailActivity(Context activity, String group_id,String box_id){
        intent=new Intent(activity, BoxDetailWebActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("group_id",group_id);
        intent.putExtra("box_id",box_id);
        StartActivity(activity,intent);
    }
    /**
     * WebView页面
     * @param activity
     * @param url
     */
    public static void showWebViewActivity(Activity activity, String url){
        intent=new Intent(activity, WebViewActivity.class);
        intent.putExtra(IntentKey.WEB_URL,url);
        StartActivity(activity,intent);
    }
    /**
     * WebView页面
     * @param activity
     * @param url
     */
    public static void showWebViewActivity(Activity activity, String url,int flag){
        intent=new Intent(activity, WebViewActivity.class);
        intent.putExtra(IntentKey.WEB_URL,url);
        intent.putExtra(IntentKey.FLAG,flag);
        StartActivity(activity,intent);
    }
    /**
     * 博文详情
     * @param activity
     * @param blogBean
     */
    public static void showBlogDetailActivity(Activity activity, BlogBean blogBean){
        intent=new Intent(activity, BlogDetailActivity.class);
        intent.putExtra("blogBean",blogBean);
        StartActivity(activity,intent);
    }
    /**
     * 博文详情
     * @param activity
     * @param blogBean
     */
    public static void showBlogDetailActivity(Activity activity, BlogBean blogBean,int flag){
        intent=new Intent(activity, BlogDetailActivity.class);
        intent.putExtra("blogBean",blogBean);
        intent.putExtra(IntentKey.FLAG,flag);
        StartActivity(activity,intent);
    }
    /**
     * 博文详情
     * @param activity
     * @param blogBean
     */
    public static void showBlogDetailActivity(Context activity, BlogBean blogBean){
        intent=new Intent(activity, BlogDetailActivity.class);
        intent.putExtra("blogBean",blogBean);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        StartActivity(activity,intent);
    }
    /**
     * 第三方登陆绑定手机号
     * @param activity
     */
    public static void showBindphoneActivity(Activity activity){
        intent=new Intent(activity, ThirdPartyLogin.class);
        StartActivity(activity,intent);
    }

    /**
     * 设置页面
     * @param activity
     */
    public static void showSettingActivity(Activity activity){
        intent=new Intent(activity, SettingActivity.class);
        StartActivity(activity,intent);
    }

    /**
     * 博文评论
     * @param blogDetailActivity
     * @param blogBean
     */
    public static void showBlogPingActivity(BlogDetailActivity blogDetailActivity, BlogBean blogBean) {
        intent=new Intent(blogDetailActivity, BlogPingActivity.class);
        intent.putExtra("blogBean",blogBean);
        StartActivity(blogDetailActivity,intent);
    }
    public static boolean isLogin(Activity activity){
        if(TextUtils.isEmpty(SettingDefaultsManager.getInstance().authToken())){
            intent=new Intent(activity, LoginActivity.class);
            activity.startActivityForResult(intent,RequestCode.LOGIN);
            return false;
        }else{
            return true;
        }
    }
    public static void showQQ(Activity activity){
        if(isAvilible(activity.getBaseContext(),"com.tencent.mobileqq")){
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(HtmlUrl.QQ_KF_URL);
            intent.setData(content_url);
            activity.startActivity(intent);
        }else{
            Toast.makeText(activity,"请安装手机版QQ",Toast.LENGTH_SHORT).show();
        }
    }
    private static boolean isAvilible(Context context, String packageName) {
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

    public static void showNuggetsShareActivity(Activity activity, StockMode stockMode, Rank rank, int flag){
        Intent intent = new Intent(activity, NuggetsShareActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.STOCK_MODE,stockMode);
        bundle.putSerializable(IntentKey.RANK,rank);
        bundle.putInt(IntentKey.FLAG,flag);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public static void showStockArgumentActivity(Activity activity){
        Intent intent = new Intent(activity, StockArgumentActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
    //跳转选股用户详情页面
    public static void showUserDetailActivity(Activity activity,String userId,int flag){
        Intent intent = new Intent(activity, UserDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.USER_ID,userId);
        bundle.putInt(IntentKey.FLAG,flag);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
    //跳转VIP服务订阅页面
    public static void skipVIPActivity(Activity activity/*, Screen screen*/){
        Intent intent = new Intent(activity, VipActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putSerializable(IntentKey.SCREEN,screen);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
    //跳转VIP订阅页面
    public static void skipSubscribeActivity(Activity activity){
        Intent intent = new Intent(activity, ScreenDetailActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
    //跳转新的只能选股页面
    public static void skipNewSmartStockActivity(Activity activity){
        Intent intent = new Intent(activity, SmartSelectStockActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
    //跳转选股大赛页面
    public static void skipZbmfSelectStockActivity(Activity activity){
        Intent intent = new Intent(activity, ZbmfSelectStockMatch.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
    //跳转模拟交易排行榜页面
    public static void skipMatchRankActivity(Activity activity, MatchInfo mMatchInfo){
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.SELECT, 1);
        bundle.putSerializable(IntentKey.MATCH_BAEN, mMatchInfo);
        ShowActivity.showActivity(activity, bundle, SimulateStockChatActivity.class);
    }
    //跳转单页比赛排行详情的页面
    public static void skipMatchRank(Activity activity,int flag){
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.FLAG,flag);
        ShowActivity.showActivity(activity,bundle, MatchRankActivity.class);
    }
    //跳转首页
    public static void skipGroupActivity(Activity activity,int flag){
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.FLAG,flag);
        ShowActivity.showActivity(activity,bundle, GroupActivity.class);
    }
    //跳转话题
    public static void skipHtActivity(Activity activity, ArrayList<Types> typeList,int noReadMsgCount) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(IntentKey.TYPES,typeList);
        bundle.putInt("no_read_msg_count",noReadMsgCount);
        showActivity(activity,bundle, HtActivity.class);
    }
    //跳转话题详情
    public static void skipTopicDetailActivty(Activity activity,String topic_id,String avatar,int status) {
        Bundle bundle = new Bundle();
        bundle.putString("topic_id",topic_id);
        bundle.putString("avatar",avatar);
        bundle.putInt("status",status);
        showActivity(activity,bundle, TopicDetailActivty.class);
    }
    //跳转观点详情页面
    public static void skipGDDetailActivity(Activity activity,String viewpoint_id) {
        Bundle bundle = new Bundle();
        bundle.putString("viewpoint_id",viewpoint_id);
        showActivity(activity,bundle, GDDetailActivity.class);
    }
    //跳转话题发表
    public static void skipFB_GD_Activity(Activity activity,String topic_id,String title) {
        Bundle bundle = new Bundle();
        bundle.putString("topic_id",topic_id);
        bundle.putString("title",title);
        showActivity(activity,bundle, FB_GD_Activity.class);
    }
    //跳转未读消息列表
    public static void skipNoReadActivity(Activity activity) {
        Bundle bundle = new Bundle();
        showActivity(activity,bundle, NoReadMsgActivity.class);
    }
}
