package com.zbmf.StocksMatch.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StocksMatch.AppContext;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.activity.AboutActivity;
import com.zbmf.StocksMatch.activity.AboutUsActivity;
import com.zbmf.StocksMatch.activity.AccountActivity;
import com.zbmf.StocksMatch.activity.ActivesActivity;
import com.zbmf.StocksMatch.activity.AllMatchActivity;
import com.zbmf.StocksMatch.activity.AnnouncementActivity;
import com.zbmf.StocksMatch.activity.ApplyMatchActivity;
import com.zbmf.StocksMatch.activity.ExActivity;
import com.zbmf.StocksMatch.activity.FeedbackActivity;
import com.zbmf.StocksMatch.activity.FindPwdActivity;
import com.zbmf.StocksMatch.activity.FocusActivity;
import com.zbmf.StocksMatch.activity.IssueActivity;
import com.zbmf.StocksMatch.activity.LoginActivity;
import com.zbmf.StocksMatch.activity.MainActivity;
import com.zbmf.StocksMatch.activity.MatchDescActivity;
import com.zbmf.StocksMatch.activity.MatchDetailActivity;
import com.zbmf.StocksMatch.activity.MatchHoldActivity;
import com.zbmf.StocksMatch.activity.MatchTransactionActivity;
import com.zbmf.StocksMatch.activity.MatchTrustsActivity;
import com.zbmf.StocksMatch.activity.OpreatActivity;
import com.zbmf.StocksMatch.activity.PayTipActivity;
import com.zbmf.StocksMatch.activity.PersonInfoActivity;
import com.zbmf.StocksMatch.activity.RecordActivity;
import com.zbmf.StocksMatch.activity.RegistActivity;
import com.zbmf.StocksMatch.activity.RootActivity;
import com.zbmf.StocksMatch.activity.SeaStockActivity;
import com.zbmf.StocksMatch.activity.SearchActivity;
import com.zbmf.StocksMatch.activity.ShareActivity;
import com.zbmf.StocksMatch.activity.SotreActivity;
import com.zbmf.StocksMatch.activity.StockBuyActivity;
import com.zbmf.StocksMatch.activity.StockDetailActivity;
import com.zbmf.StocksMatch.activity.StockSellActivity;
import com.zbmf.StocksMatch.activity.TextActivity;
import com.zbmf.StocksMatch.activity.UserActivity;
import com.zbmf.StocksMatch.activity.UserHolderActivity;
import com.zbmf.StocksMatch.activity.UserTransactionActivity;
import com.zbmf.StocksMatch.activity.WebActivity;
import com.zbmf.StocksMatch.activity.YieldActivity;
import com.zbmf.StocksMatch.api.GenApiHashUrl;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.listener.DialogListener;
import com.zbmf.StocksMatch.listener.DialogListener1;
import com.zbmf.StocksMatch.wxapi.WXEntryActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum UiCommon {
    INSTANCE;

    public static final int ACTIVITY_IDX_ROOT = 0; // 初始化页面
    public static final int ACTIVITY_IDX_LOGIN = 1; // 登录
    public static final int ACTIVITY_IDX_MATCHDESC = 2;//比赛详情
    public static final int ACTIVITY_IDX_MATCHDETAIL = 3;//比赛详情1
    public static final int ACTIVITY_IDX_ALLMATCH = 4;//所有比赛
    public static final int ACTIVITY_IDX_APPLYMATCH = 5;//报名参加比赛
    public static final int ACTIVITY_IDX_MAIN = 6;
    public static final int ACTIVITY_IDX_STOCKDETAIL = 7;//股票详情
    public static final int ACTIVITY_IDX_MATCHSEARCH = 8;//比赛、用户、股票搜索
    public static final int ACTIVITY_IDX_ACTIVES = 9;//活动
    public static final int ACTIVITY_IDX_REGISTER = 10;//注册
    public static final int ACTIVITY_IDX_FINDPWD = 11;//找回密码
    public static final int ACTIVITY_IDX_ACCOUNT = 12;
    public static final int ACTIVITY_IDX_ABOUT = 13;
    public static final int ACTIVITY_IDX_ABOUTUS = 14;
    public static final int ACTIVITY_IDX_FOCUS = 15;//我的关注列表
    public static final int ACTIVITY_IDX_USER = 16;//用户主页
    public static final int ACTIVITY_IDX_FEEDBACK = 17;
    public static final int ACTIVITY_IDX_ISSUE = 18;//常见问题
    public static final int ACTIVITY_IDX_OPERAT = 19;//操作记录
    public static final int ACTIVITY_IDX_RECORD = 20;//获奖记录
    public static final int ACTIVITY_IDX_PERINFO = 21;//个人信息
    public static final int ACTIVITY_IDX_ANNOUNCEMENT = 22;//公告
    public static final int ACTIVITY_IDX_RANK = 23;//榜单
    public static final int ACTIVITY_IDX_TRUST = 24;//委托
    public static final int ACTIVITY_IDX_HOLD = 25;//持仓
    public static final int ACTIVITY_IDX_TRANSACTION = 26;//交易记录
    public static final int ACTIVITY_IDX_BUY = 27;
    public static final int ACTIVITY_IDX_SALE = 28;
    public static final int ACTIVITY_IDX_USERHOLDER = 29;//用户持仓
    public static final int ACTIVITY_IDX_USERTRANSACTION = 30;//用户交易记录
    public static final int ACTIVITY_IDX_PAYTIP = 31;//支付提示
    public static final int ACTIVITY_IDX_WX = 32;
    public static final int ACTIVITY_IDX_ACCOUNT_Web = 33;
    public static final int ACTIVITY_IDX_TEXT = 34;
    public static final int ACTIVITY_IDX_STORE = 35;
    public static final int ACTIVITY_IDX_SELSTOCK = 36;//股票搜索
    public static final int ACTIVITY_IDX_SHARE = 37;//分享

    private User iUser;

    private List<String> iActiNameList = new ArrayList<String>(); // Activity名称集合
    public List<Activity> iAllActi; // Activity集合
    private int iCurrActiIdx; // 当前Activity Index

    private static final String DEFAULT_DATA_BASEPATH = "/com.zbmf.StocksMatch"; // 缓存目录
    //	private  String DEFAULT_DATA_BASEPATH = "/"+ AppContext.getInstance().getPackageName();
    public String DEFAULT_DATA_IMAGEPATH = DEFAULT_DATA_BASEPATH + "/IMAGE";
    public String DEFAULT_DATA_FILE = DEFAULT_DATA_BASEPATH + "/FILE";
    public String DEFAULT_DATA_TEMP = DEFAULT_DATA_BASEPATH + "/TEMP"; // 备份数据地址
    public String DEFAULT_DATA_BIG_IMAGEPATH = DEFAULT_DATA_BASEPATH + "/BIGIMAGE"; // 大图缓存地址
    public static final String APK_CONTENT_TYPE = "application/vnd.android.package-archive";

    public static int MY_PID = 0; // 进程id
    public static String VERSION_NAME = "1.0.0"; // versionName

    public static double widthPixels = 320;
    public static double higthPixels = 480;


    private UiCommon() {
        iActiNameList.add(ACTIVITY_IDX_ROOT, RootActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_LOGIN, LoginActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_MATCHDESC, MatchDescActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_MATCHDETAIL, MatchDetailActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_ALLMATCH, AllMatchActivity.class.getName());


        iActiNameList.add(ACTIVITY_IDX_APPLYMATCH, ApplyMatchActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_MAIN, MainActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_STOCKDETAIL, StockDetailActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_MATCHSEARCH, SearchActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_ACTIVES, ActivesActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_REGISTER, RegistActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_FINDPWD, FindPwdActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_ACCOUNT, AccountActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_ABOUT, AboutActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_ABOUTUS, AboutUsActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_FOCUS, FocusActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_USER, UserActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_FEEDBACK, FeedbackActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_ISSUE, IssueActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_OPERAT, OpreatActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_RECORD, RecordActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_PERINFO, PersonInfoActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_ANNOUNCEMENT, AnnouncementActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_RANK, YieldActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_TRUST, MatchTrustsActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_HOLD, MatchHoldActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_TRANSACTION, MatchTransactionActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_BUY, StockBuyActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_SALE, StockSellActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_USERHOLDER, UserHolderActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_USERTRANSACTION, UserTransactionActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_PAYTIP, PayTipActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_WX, WXEntryActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_ACCOUNT_Web, WebActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_TEXT, TextActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_STORE, SotreActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_SELSTOCK, SeaStockActivity.class.getName());
        iActiNameList.add(ACTIVITY_IDX_SHARE, ShareActivity.class.getName());

        // 初始化整个Activity队列
        iAllActi = new ArrayList<Activity>(iActiNameList.size());
        for (int i = 0, len = iActiNameList.size(); i < len; i++) {
            iAllActi.add(i, null);
        }

        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        DEFAULT_DATA_IMAGEPATH = rootPath + DEFAULT_DATA_IMAGEPATH;
        DEFAULT_DATA_BIG_IMAGEPATH = rootPath + DEFAULT_DATA_BIG_IMAGEPATH;
        DEFAULT_DATA_FILE = rootPath + DEFAULT_DATA_FILE;
        DEFAULT_DATA_TEMP = rootPath + DEFAULT_DATA_TEMP;

    }

    public User getiUser() {

        return iUser;
    }

    public void setiUser(User iUser) {
        this.iUser = iUser;
    }

    public int setCurrActivity(Activity aActivity) {
        iCurrActiIdx = getActiIdxFromClsName(aActivity.getClass().getName());
        if (iAllActi.get(iCurrActiIdx) != aActivity) {
            iAllActi.set(iCurrActiIdx, aActivity);
        }
        return iCurrActiIdx;
    }

    public void doOnActivityDestroy(Activity aActivity) {
        int actiIdx = getActiIdxFromClsName(aActivity.getClass().getName());
        if (iAllActi.get(actiIdx) == aActivity) {
            iAllActi.set(actiIdx, null);
        }
    }

    public int getActiIdxFromClsName(String aClsName) {
        int i = 0, len = iActiNameList.size();
        for (; i < len; i++) {
            if (iActiNameList.get(i).equals(aClsName)) {
                break;
            }
        }
        return i;
    }

    public Activity getCurrActivity() {
        return iAllActi.get(iCurrActiIdx);
    }

    public boolean processBackKey() {
        boolean ret = true;
        switch (iCurrActiIdx) {
            case ACTIVITY_IDX_ROOT:
            case ACTIVITY_IDX_MAIN:
//			finishApp();
                finishAppNow();
                break;
            default:
                ret = false;
                break;
        }
        return ret;
    }

    public void finishApp() {

        Activity context = getCurrActivity();
        showDialog(context, context.getString(R.string.exit_prompt), new DialogListener() {
            @Override
            public void onCancl(Dialog dialog) {
                dialog.cancel();
            }

            @Override
            public void onConfirm(Dialog dialog) {
                finishAppNow();
                dialog.cancel();
            }
        }, R.string.cancel, R.string.exit);
    }

    public void finishAppNow() {
        for (Activity act : iAllActi) {
            if (null != act) {
                act.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void finishAct() {
        for (Activity act : iAllActi) {
            if (null != act) {
                act.finish();
            }
        }
    }

    public void finishOtherAct() {
        for (Activity act : iAllActi) {
//			if (null != act && iAllActi.get(ACTIVITY_IDX_HOME) != act
//					&& iAllActi.get(ACTIVITY_IDX_MyWallet) != act
//					&& iAllActi.get(ACTIVITY_IDX_ZZHK) != act
//					&& iAllActi.get(ACTIVITY_IDX_CreditcardManage) != act
//					&& iAllActi.get(ACTIVITY_IDX_MyCreditcard) != act) {
//				act.finish();
//			}
        }
    }

    public void logout(Context act) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(act);
        sharedPreferencesUtils.setAccount("");
        finishALL();
        showActivity(UiCommon.ACTIVITY_IDX_LOGIN, null);
    }

    public void logoutApp() {

    }

    public void finishALL() {
        for (Activity act : iAllActi) {
            if (null != act) {
                act.finish();
            }
        }
    }

    public void logoutAppNow() {
        for (int i = 1, len = iAllActi.size(); i < len; i++) {
            Activity act = iAllActi.get(i);
            if (null != act) {
                iAllActi.set(i, null);
                act.finish();
            }
        }

    }


    public void showActivity(int aTargetActiIdx, Bundle aBundle) {

        Activity currActivity = getCurrActivity();
        Intent intent = new Intent();
        intent.setClassName(currActivity.getPackageName(),
                iActiNameList.get(aTargetActiIdx));

        if (null != aBundle) {
            intent.putExtras(aBundle);
        }

        if (aTargetActiIdx == ACTIVITY_IDX_ROOT) {
            finishAct();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }


        currActivity.startActivity(intent);
    }

    public void showActivityForResult(int aTargetActiIdx, Bundle aBundle,
                                      int requestCode) {
        Activity currActivity = getCurrActivity();
        Intent intent = new Intent();
        intent.setClassName(currActivity.getPackageName(),
                iActiNameList.get(aTargetActiIdx));

        if (null != aBundle) {
            intent.putExtras(aBundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        currActivity.startActivityForResult(intent, requestCode);
    }

    /****
     * MD5加密
     *
     * @param string
     * @return
     */
    public static String getMD5String(String string) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(string.getBytes("UTF-8"));

        } catch (NoSuchAlgorithmException e) {

            System.out.println("NoSuchAlgorithmException caught!");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }


    /**
     * 计算相对应的长度
     *
     * @param dip
     * @return float
     */
    public float convertDip2Pixel(int dip) {
        final float scale = AppContext.getInstance()
                .getApplicationContext().getResources().getDisplayMetrics().density;
        float pixel = (int) (dip * scale + 0.5f);
        return pixel;
    }

    /**
     * 取当前版本号(数值)(程序版本号的是放在AndroidManifest.xml文件中)
     *
     * @return
     */
    private int getCurrVersionCode() {
        int currVerCode = -1;
        ExActivity acti = (ExActivity) getCurrActivity();
        if (null != acti) {
            try {
                currVerCode = acti.getPackageManager().getPackageInfo(
                        acti.getPackageName(), 0).versionCode;
            } catch (NameNotFoundException e) {
                Log.e("atan", "getCurrVersionCode() NameNotFoundException");
            }
        }
        return currVerCode;
    }

    /**
     * 取当前版本名(给用户看的文字)(通过版本号转换, 程序版本号的是放在AndroidManifest.xml文件中)
     *
     * @return
     */
    public String getCurrVersionName() {
        int currVerCode = getCurrVersionCode();
        if (currVerCode > 0) {
            int sub1 = currVerCode % 10;
            int sub2 = (currVerCode / 10) % 10;
            int sub3 = currVerCode / 100;
            VERSION_NAME = String.format("%1$d.%2$d.%3$d", sub3, sub2, sub1);
            return VERSION_NAME + ".1";
        }
        return "";
    }

    /**
     * 取当前版本名(给用户看的文字)
     *
     * @return
     */
    public String getCurr_VersionName() {
        ExActivity acti = (ExActivity) getCurrActivity();
        PackageManager pm = acti.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(acti.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0";
        }

    }

    /**
     * 提示信息
     *
     * @param aFormatMsg
     * @param aMsgArgs
     */
    public void showTip(String aFormatMsg, Object... aMsgArgs) {
        String outString = String.format(aFormatMsg, aMsgArgs);
        int duration = (outString.length() > 10) ? Toast.LENGTH_LONG
                : Toast.LENGTH_SHORT;
        Toast.makeText(AppContext.getInstance().getApplicationContext(),
                outString, duration).show();
    }


    /**
     * 图片压缩
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public int computeSampleSize(BitmapFactory.Options options,
                                 int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private int computeInitialSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public boolean install(Activity activity, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(i);
            return true;
        } else {
            return false;
        }

    }

    public static Bitmap createRepeater(int width, Bitmap src) {
        int count = (width + src.getWidth() - 1) / src.getWidth(); //计算出平铺填满所给width（宽度）最少需要的重复次数
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth() * count, src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx) {
            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }
        return bitmap;
    }

    public boolean isChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }

    /**
     * 弹出分享
     */
    public Dialog showDialog(View.OnClickListener listener) {
        Activity currActivity = getCurrActivity();
        while (currActivity.getParent() != null) {
            currActivity = currActivity.getParent();
        }
        final Dialog dialog = new Dialog(currActivity, R.style.myDialogTheme);
        LayoutInflater inflater = currActivity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.share_layout, null);

        layout.findViewById(R.id.ll_s1).setOnClickListener(listener);
        layout.findViewById(R.id.ll_s2).setOnClickListener(listener);
        layout.findViewById(R.id.ll_s3).setOnClickListener(listener);
        layout.findViewById(R.id.ll_s4).setOnClickListener(listener);

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

    /**
     * 撤销委托对话框
     *
     * @param activity
     * @param tip
     * @param tip2
     * @param listener1
     * @param canclStr
     * @param confirmStr
     */
//	public void showDialog(Activity activity, int tip, int tip2,final DialogListener listener1, int canclStr, int confirmStr) {
    public void showDialog(Activity activity, int tip, String tip2, final DialogListener listener1, int confirmStr, int canclStr) {
        final Dialog dialog1 = new Dialog(activity, R.style.myDialogTheme);
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_tip, null);
        // 可指定对话框的背景色彩那些
        TextView tvTip = (TextView) layout.findViewById(R.id.tvTip);
        tvTip.setText(tip);
        TextView tvTip2 = (TextView) layout.findViewById(R.id.tvTip2);
        tvTip2.setText(tip2);
        TextView tvCancl = (TextView) layout.findViewById(R.id.tvCancl);
        tvCancl.setText(canclStr);
        TextView tvConfirm = (TextView) layout.findViewById(R.id.tvConfirm);
        tvConfirm.setText(confirmStr);
        tvCancl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listener1.onCancl(dialog1);
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listener1.onConfirm(dialog1);
            }
        });
        dialog1.setContentView(layout);

        // 设置对话框的出现位置，借助于window对象
        Window win = dialog1.getWindow();
        win.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (UiCommon.widthPixels * 0.7);
        win.setAttributes(lp);
        dialog1.setCancelable(false);
        dialog1.show();
    }

    /**
     * 持仓操作
     *
     * @param activity
     * @param tip2
     * @param listener1
     */
    public void showDialog(Activity activity, String tip2, final DialogListener1 listener1) {
        final Dialog dialog1 = new Dialog(activity, R.style.myDialogTheme);
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_tip2, null);
        // 可指定对话框的背景色彩那些
        TextView tvTip = (TextView) layout.findViewById(R.id.tvTip);
        tvTip.setText(tip2);
        TextView tvCancl = (TextView) layout.findViewById(R.id.tvCancl);
        TextView tvConfirm = (TextView) layout.findViewById(R.id.tvConfirm);
        TextView tvConfirm1 = (TextView) layout.findViewById(R.id.tvConfirm1);
        tvCancl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listener1.onCancl(dialog1);
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listener1.onConfirm(dialog1);
            }
        });

        tvConfirm1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listener1.onConfirm1(dialog1);
            }
        });
        dialog1.setContentView(layout);

        // 设置对话框的出现位置，借助于window对象
        Window win = dialog1.getWindow();
        win.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (UiCommon.widthPixels * 0.7);
        win.setAttributes(lp);
        dialog1.setCancelable(false);
        dialog1.show();
    }

    public void showDialog(Activity activity, String tip,
                           final DialogListener listener1, int canclStr, int confirmStr) {
        // if (dialog1 == null) {
        final Dialog dialog1 = new Dialog(activity, R.style.myDialogTheme);
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_tip3, null);
        // 可指定对话框的背景色彩那些
        TextView tvTip = (TextView) layout.findViewById(R.id.tvTip);
        tvTip.setText(tip);
        TextView tvConfirm = (TextView) layout.findViewById(R.id.tvConfirm);
        tvConfirm.setText(confirmStr);
        TextView tvCancl = (TextView) layout.findViewById(R.id.tvCancl);
        tvCancl.setText(canclStr);
        tvCancl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                listener1.onCancl(dialog1);
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                listener1.onConfirm(dialog1);
            }
        });
        dialog1.setContentView(layout);

        // 设置对话框的出现位置，借助于window对象
        Window win = dialog1.getWindow();
        win.setGravity(Gravity.CENTER);
        // win.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//
        // 弹出对话框时，底部窗体，不变暗。

        WindowManager.LayoutParams lp = win.getAttributes();
        // lp.x = -200;// x=0,y=0时，显示位置是屏幕中心。
        // lp.y = 0;
        // lp.alpha = 0.6f;// 对话框的透明度
        lp.width = (int) (UiCommon.widthPixels * 0.7);
        win.setAttributes(lp);
        dialog1.setCancelable(false);
        dialog1.show();
        // }else if (!dialog1.isShowing()) {
        // dialog1.show();
        // }

    }

    public String subTitle(String title_msg) {
        if (title_msg.length() >= 15) {
            return title_msg.substring(0, 15) + "...";
        }
        return title_msg;
    }

    private ProgressDialog mProgressDialog;

    public void showDialog(final Context context, final int s) {
        String message = context.getString(s);
        String title = "";
        mProgressDialog = ProgressDialog.show(context, title, message, true, true, null);
    }

    public void DialogDismiss() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public static void openOther(String packageName, Context context, Activity activity, String url) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo pi = packageManager.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            boolean isIntentSafe = activities.size() > 0;

            if (isIntentSafe)
                activity.startActivity(intent);
            return;
        }
        Intent resolveIntent = packageManager.getLaunchIntentForPackage(packageName);
        activity.startActivity(resolveIntent);
    }


    /**
     * 删除指定文件夹内所有文件
     *
     * @param
     */
    public boolean deletAllImgFiles() {
        try {
            File file = new File(Environment
                    .getExternalStorageDirectory().getAbsolutePath()
                    + DEFAULT_DATA_BASEPATH);

            if (file.exists() && file.isDirectory()) {
                File[] arrayFiles = file.listFiles();
                for (File f : arrayFiles) {
                    File[] arrayImgFile = f.listFiles();
                    for (File Imgfile : arrayImgFile) {
                        Imgfile.delete();
                    }
                    f.delete();
                }
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * SD卡正确挂载并且处于可读写状态
     *
     * @return boolean
     */
    public boolean isExternalStorageExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断SD卡中文件是否存在
     *
     * @param filePath
     * @return boolean
     */
    public boolean isFileExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 下载图片
     *
     * @param urlStr
     * @return Bitmap
     */
    public Bitmap downloadPic(String urlStr) {
//		if (null == urlStr || urlStr.length() < 8)
//			return null;

        String fileName = UiCommon.INSTANCE.getMD5String(urlStr);
        Bitmap bitmap = null;
        File file = null;
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream bais = null;

        try {
            if (isExternalStorageExist()
                    && isFileExists(DEFAULT_DATA_IMAGEPATH
                    + File.separator + fileName)) {
                file = new File(DEFAULT_DATA_IMAGEPATH
                        + File.separator + fileName + ".jpg");
                fis = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(fis);
            } else {
                bitmap = GenApiHashUrl.getInstance().downloadPic(urlStr);

                if (isExternalStorageExist()) {
                    baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    bais = new ByteArrayInputStream(baos.toByteArray());
                    writeFileToSDFromInput(DEFAULT_DATA_IMAGEPATH, fileName,
                            bais);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (fis != null)
                    fis.close();
                if (baos != null)
                    baos.close();
                if (bais != null)
                    bais.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 保存文件到SD卡
     *
     * @param path
     * @param fileName
     * @param is
     */
    public void writeFileToSDFromInput(String path, String fileName,
                                       InputStream is) {
        File file = null;
        FileOutputStream fos = null;
        try {
            file = new File(path);
            if (!file.exists())
                file.mkdirs();
            file = new File(path + File.separator + fileName);
            file.createNewFile();

            fos = new FileOutputStream(file);
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
