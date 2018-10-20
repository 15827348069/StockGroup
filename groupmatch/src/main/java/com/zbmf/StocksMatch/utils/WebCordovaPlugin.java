//package com.zbmf.StocksMatch.utils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.cordova.CallbackContext;
//import org.apache.cordova.CordovaInterface;
//import org.json.JSONArray;
//
//import com.alibaba.fastjson.JSON;
//import com.kwlopen.sdk.KwlOpenCordovaPlugin;
//import com.kwlstock.sdk.cordova.QHTCordovaPlugin;
//import com.kwlstock.sdk.entity.UserInfoEtity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//
///**
// *
// */
//public class WebCordovaPlugin extends QHTCordovaPlugin {
//
//    @Override
//    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
//        // 先执行开户的cordova插件
//        boolean result = KwlOpenCordovaPlugin.getInstance().execute(cordova, this, action, args, callbackContext);
//
//        if (result) {
//            return result;
//
//        } else {
//            return super.execute(action, args, callbackContext);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        KwlOpenCordovaPlugin.getInstance().onActivityResult(requestCode, resultCode, intent);
//        super.onActivityResult(requestCode, resultCode, intent);
//
//    }
//
//    /**
//     * 初始化用户信息给H5
//     *
//     * @param etity
//     *            USER_ID 用户id
//     */
//    @Override
//    public void initUserInfo(UserInfoEtity entity) {
//        // 信息采集
//        initSystemInfo(entity);
//    }
//
//    /**
//     * 初始化设备信息-券商必须采集的信息
//     *
//     * @param entity
//     */
//    private void initSystemInfo(UserInfoEtity entity) {
//        TelephonyManager tm = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
//        String version = "";
//        try {
//            PackageManager manager = cordova.getActivity().getPackageManager();
//            PackageInfo info = manager.getPackageInfo(cordova.getActivity().getPackageName(), 0);
//            version = info.versionName;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        entity.setDeviceId(tm.getDeviceId());
//        entity.setOsVer(android.os.Build.VERSION.RELEASE);
//        entity.setAppName("android");
//        entity.setAppVer(version);
//
//        entity.setChannelNo("一账通服务器通道号");
//        entity.setPhone("一账通账户提供的手机号码");
//    }
//
//    /**
//     * 跳转网页
//     *
//     * @param context
//     *            上下文
//     * @param title
//     *            标题
//     * @param url
//     *            访问地址
//     * @param type
//     *            1-免责声明 2-添加券商 3-买卖下单 4-资金详情 5-查询记录 6-银证转账
//     */
//    @Override
//    public void startWebActivity(Context context, String title, String url, int type, int companyId) {
//
////		switch (type) {
////		case Constants.CORDOVA_WEB_TYPE_DECLARE:
////			break;
////		case Constants.CORDOVA_WEB_TYPE_ADD_COMPANY:
////			CordovaWebActivity.show(context, type, companyId);
////			((Activity) context).finish();
////			break;
////		case Constants.CORDOVA_WEB_TYPE_RECORD:
////			QueryRecordH5Activity.show(context, type);
////			break;
////		case Constants.CORDOVA_WEB_TYPE_ORDER:
////			BuySellH5Activity.show(context, "", "", type);
////			break;
////		case Constants.CORDOVA_WEB_TYPE_CAPITAL:
////			SignInfoEntity curCompanyEntity = QHTConfig.getInstance().getCurCompanyEntity();
////
////			if (curCompanyEntity == null) {
////				return;
////			}
////			CordovaWebActivity.show(context, type, curCompanyEntity.getCompanyId());
////			break;
////		case Constants.CORDOVA_WEB_TYPE_TRANS:
////			CapitalTransferH5Activity.show(context, type);
////			break;
////		default:
////			CordovaWebActivity.show(context, title, url);
////			break;
////		}
//    }
//
//    /**
//     * 跳转到买卖页面
//     *
//     * @param context
//     * @param url
//     *            地址
//     * @param market
//     *            市场
//     * @param secuCode
//     *            股票代码
//     * @param type
//     *            1：买入 2：卖出
//     */
//    @Override
//    public void startBuySellActivity(Context context, String url, String market, String secuCode, int type) {
//        // SignInfoEntity curCompanyEntity =
//        // QHTConfig.getInstance().getCurCompanyEntity();
//        //
//        // if (curCompanyEntity == null) {
//        // return;
//        // }
//
//        // CordovaWebActivity.showTrade(context,
//        // context.getResources().getString(R.string.title_trade), market,
//        // secuCode,
//        // type, curCompanyEntity.getCompanyId());
//
////		BuySellH5Activity.show(context, market, secuCode, type);
//    }
//
//    /**
//     * 跳转到转账页面
//     *
//     * @param context
//     * @param type
//     *            0：转入 1：转出
//     */
//    @Override
//    public void startTransActivity(Context context, int type) {
//        // SignInfoEntity curCompanyEntity =
//        // QHTConfig.getInstance().getCurCompanyEntity();
//        //
//        // if (curCompanyEntity == null) {
//        // return;
//        // }
//        // CordovaWebActivity.showTrans(context, type,
//        // curCompanyEntity.getCompanyId());
//
////		CapitalTransferH5Activity.show(context, type);
//    }
//
//    /**
//     * 跳转到记录查询页面
//     *
//     * @param context
//     * @param type
//     *            0:成交查询 1:委托查询 2:转账查询 3:对账单
//     */
//    @Override
//    public void startRecordActivity(Context context, int type) {
////		QueryRecordH5Activity.show(context, type);
//    }
//
//    /**
//     * 委托下单成功后的操作
//     *
//     * @param activity
//     */
//    @Override
//    public void orderFinish(Activity activity) {
//        activity.finish();
//    }
//
//    /**
//     * 跳转到行情页面
//     *
//     * @param context
//     * @param url
//     *            访问地址
//     * @param title
//     *            标题
//     * @param market
//     *            市场
//     * @param stockCode
//     *            股票代码
//     * @param stockName
//     *            股票名称
//     */
//    @Override
//    public void startMarketDetailActivity(Context context, String url, String title, String market, String stockCode,
//                                          String stockName) {
//        // CordovaWebActivity.show(context, title, url);
//    }
//
//    /**
//     * 检查是否登录
//     *
//     * @param cordova
//     * @param callbackContext
//     * @param isTrade
//     * @output FLAG 0:成功 -1：失败
//     */
//    @Override
//    public void checkLogin(CordovaInterface cordova, final CallbackContext callbackContext, boolean isTrade) {
//
//        // TODO Auto-generated method stub
//
//        // TODO 此块用于处理账户是否登录、session超时登操作，根据状态返回FLAG的值高速H5页面
//        Log.d("tag1", "checklogin");
//        checkLoginSuccess(callbackContext, 0);
//
//    }
//
//    public void checkLoginSuccess(CallbackContext callbackContext, int result) {
//        Map<String, Object> data = new HashMap<String, Object>();
//
//        // flag=0 H5可以继续下一步操作，flag=-1，无法下一步操作，有可能session超时
//        data.put("FLAG", result);
//        String datas = JSON.toJSONString(data);
//
//        callbackContext.success(datas);
//    }
//
//    /**
//     *
//     * @param cordova
//     * @param status
//     *            0：成功，-1：失败
//     * @param companyId
//     *            券商代码
//     * @param cuacctCode
//     *            资金账户
//     */
//    @Override
//    public void customerSigned(CordovaInterface cordova, int status, int companyId, String cuacctCode) {
////		if (status == 0) {
////			// TODO 绑定到券商列表成功后处理的业务
////			// 遍历判断是否已经存在
////			boolean result = true;
////			for (SignInfoEntity entity : QHTConfig.getInstance().signInfoList) {
////				if (entity.getCompanyId() == companyId && entity.getCuacctCode().equals(cuacctCode)) {
////					result = false;
////				}
////			}
////
////			if (result) {
////				Toast.makeText(cordova.getActivity(), "签约成功   券商ID=" + companyId + ",资金号码=" + cuacctCode,
////						Toast.LENGTH_SHORT).show();
////
////				signedSuccess(cordova.getActivity(), companyId, cuacctCode);
////			} else {
////				Toast.makeText(cordova.getActivity(), "该资金账户" + cuacctCode + "已签约", Toast.LENGTH_SHORT).show();
////			}
////
////		} else {
////			Toast.makeText(cordova.getActivity(), "签约失败  券商ID=" + companyId + ",资金号码=" + cuacctCode, Toast.LENGTH_SHORT)
////					.show();
////		}
//    }
//
//    /**
//     *
//     * @param cordova
//     * @param status
//     *            0：成功，-1：失败
//     * @param companyId
//     *            券商代码
//     * @param cuacctCode
//     *            资金账户
//     */
//    @Override
//    public void customerUnsigned(CordovaInterface cordova, int status, int companyId, String cuacctCode,
//                                 CallbackContext callbackContext) {
//        // if (status == 0) {
//        // // TODO 解约券商成功后处理的业务
//        // Toast.makeText(cordova.getActivity(), "解约成功 券商ID=" + companyId +
//        // ",资金号码=" + cuacctCode,
//        // Toast.LENGTH_SHORT).show();
//        //
//        // signedSuccess(cordova.getActivity(), companyId, cuacctCode);
//        //
//        // } else {
//        // Toast.makeText(cordova.getActivity(), "解约失败 券商ID=" + companyId +
//        // ",资金号码=" + cuacctCode, Toast.LENGTH_SHORT)
//        // .show();
//        // }
//    }
//
//    /**
//     * 签约成功后的处理
//     *
//     * @param activity
//     * @param companyId
//     * @param cuacctCode
//     */
//    private void signedSuccess(Activity activity, int companyId, String cuacctCode) {
//
////		// 查询一下数据
////		QHTConfig.getInstance().signInfoList.add(new SignInfoEntity(companyId, cuacctCode));
////		activity.finish();
//    }
//
//    @Override
//    public void startCustomerSign(Context context, int companyId) {
////
////		// 判断是否已经绑定过，如果绑定过则不需要再保存多份
////		for (SignInfoEntity entity : QHTConfig.getInstance().signInfoList) {
////			if (entity.getCompanyId() == companyId) {
////
////				Toast.makeText(cordova.getActivity(), R.string.kwlstock_demo_msg_repeat_binding, Toast.LENGTH_SHORT)
////						.show();
////				// 跳出
////				return;
////			}
////		}
////
////		// 获取券商地址
////		SecuUrlEntity secuUrl = QHTConfig.getInstance().secuUrlMap.get(companyId);
////
////		if (secuUrl == null) {
////			return;
////		}
////
////		// 获取持仓路径
////		String path = QHTConfig.getInstance().tradeH5Map.get(Constants.PHONE_VER_URL);
////
////		if (path == null) {
////			return;
////		}
////
////		// 根据券商url、交易H5路径、券商ID拼接完整url
////		CordovaWebActivity.show(context, R.string.kwlstock_demo_title_phone_ver,
////				H5UrlUtil.getUrl(secuUrl.TRADE_URL, path, companyId));
//
//    }
//}
