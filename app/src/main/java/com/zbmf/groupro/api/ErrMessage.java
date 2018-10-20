package com.zbmf.groupro.api;

import com.zbmf.groupro.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 服务器返回错误对照
 * Created by xuhao on 2016/12/21.
 */

public class ErrMessage {
    public static String GetErrMessage(JSONObject obj) throws JSONException {
        String err_message=null;
        switch (obj.getJSONObject("err").optInt("code")){
            case 1000:
                err_message=obj.getJSONObject("err").optString("msg");
                break;
            case 1001:
                err_message="HTTP请求错误";
                break;
            case 1003:
                err_message="签名错误";
                break;
            case 1004:
                err_message= Constants.NEED_LOGIN;
                break;
            case 1005:
                err_message="用户登录失败或已过期";
                break;
            case 1006:
                err_message="用户未登录或已过期";
                break;
            case 1007:
                err_message="不允许的API KEY";
                break;
            case 1008:
                err_message="服务器未响应";
                break;
            case 1009:
                err_message="请求方法不存在";
                break;
            case 1010:
                err_message="请求参数错误";
                break;
            case 1011:
                err_message="缺少请求参数";
                break;
            case 1012:
                err_message="API Key已过期";
                break;
            case 1013:
                err_message=obj.getJSONObject("err").optString("msg");
                break;
            case 1014:
                err_message="请求对象不存在";
                break;
            case 1015:
                err_message="请求对象已存在";
                break;
            case 2101:
                err_message="用户已被禁止登录";
                break;
            case 2102:
                err_message="用户不存在";
                break;
            case 2103:
                err_message="该手机号已经注册，请直接登录";
                break;
            case 2104:
                err_message="用户身份已存在";
                break;
            case 2105:
                err_message="昵称已被占用，换个昵称吧";
                break;
            case 2106:
                err_message="原密码不正确";
                break;
            case 2107:
                err_message="未实名认证";
                break;
            case 2108:
                err_message="银行卡不存在";
                break;
            case 2109:
                err_message="提现超过额度";
                break;
            case 2110:
                err_message="资金不足";
                break;
            case 2111:
                err_message="金豆不足";
                break;
            case 2112:
                err_message="消息不存在";
                break;
            case 2113:
                err_message="用户真实姓名错误";
                break;
            case 2114:
                err_message="用户身份证错误";
                break;
            case 2301:
                err_message="行情不存在";
                break;
            case 2401:
                err_message="短信验证码不存在";
                break;
            case 2402:
                err_message="短信验证码请求频繁";
                break;
            case 2501:
                err_message="系统公告不存在";
                break;
            case 2601:
                err_message="支付密码已存在";
                break;
            case 2602:
                err_message="支付密码不正确";
                break;
            case 2603:
                err_message="优惠券已拥有";
                break;
            case 2604:
                err_message="优惠券抢的人太多，请等等";
                break;
            case 2605:
                err_message="优惠券已抢完";
                break;
            case 2606:
                err_message="优惠券已用";
                break;
            case 2607:
                err_message="优惠券不允许使用";
                break;
            case 3000:
                err_message="直播室链接已断开";
                break;
            case 3001:
                err_message="没有权限";
                break;
            case 3002:
                err_message="不支持的功能";
                break;
            case 3003:
                err_message="消息内容违规";
                break;
            case 3010:
                err_message="发送图片失败";
                break;
            case 3011:
                err_message="发送语音失败";
                break;
            case 3012:
                err_message="缺少图片文件";
                break;
            case 3013:
                err_message="缺少语音文件";
                break;
            case 2801:
                err_message="圈子不存在";
                break;
            case 2802:
                err_message="圈子未关注";
                break;
            case 2803:
                err_message="圈子已关注";
                break;
            case 2804:
                err_message="圈主不允许";
                break;
            case 2805:
                err_message="圈子等级不足";
                break;
            case 2806:
                err_message="圈子权限不足";
                break;
            case 2807:
                err_message="没有开启积分兑换";
                break;
            case 2808:
                err_message="超过最大积分兑换";
                break;
            case 2901:
                err_message="宝盒不存在";
                break;
            case 3004:
                err_message="您被管理员禁言了";
                break;
            default:
                err_message="错误编码:"+obj.getJSONObject("err").optString("code")+"错误信息："+obj.getJSONObject("err").optString("msg");
                break;
        }
        return err_message;
    }
}
