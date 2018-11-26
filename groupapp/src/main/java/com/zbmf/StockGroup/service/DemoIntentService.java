package com.zbmf.StockGroup.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.BoxBean;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.NoticeBean;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.beans.TopticBean;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.MessageType;
import com.zbmf.StockGroup.utils.NotificationUtil;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class DemoIntentService extends GTIntentService {

    private static final String TAG = "GetuiSdkDemo";

    /**
     * 为了观察透传数据变化.
     */
    private static int cnt;

    public DemoIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.d(TAG, "onReceiveServicePid -> " + pid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        byte[] payload = msg.getPayload();
        if (payload != null) {
            String data = new String(payload);
            sendMessage(data);
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
//        sendMessage(clientid, 1);
        SettingDefaultsManager.getInstance().setPushCilentId(clientid);
        LogUtil.e(TAG + "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        LogUtil.e(TAG + "onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        LogUtil.e("onReceiveCommandResult -> " + cmdMessage);
        int action = cmdMessage.getAction();

        if (action == PushConsts.SET_TAG_RESULT) {
            setTagResult((SetTagCmdMessage) cmdMessage);
        } else if ((action == PushConsts.THIRDPART_FEEDBACK)) {
            feedbackResult((FeedbackCmdMessage) cmdMessage);
        }
    }

    private void setTagResult(SetTagCmdMessage setTagCmdMsg) {
        String sn = setTagCmdMsg.getSn();
        String code = setTagCmdMsg.getCode();

        String text = "设置标签失败, 未知异常";
        switch (Integer.valueOf(code)) {
            case PushConsts.SETTAG_SUCCESS:
                text = "设置标签成功";
                break;

            case PushConsts.SETTAG_ERROR_COUNT:
                text = "设置标签失败, tag数量过大, 最大不能超过200个";
                break;

            case PushConsts.SETTAG_ERROR_FREQUENCY:
                text = "设置标签失败, 频率过快, 两次间隔应大于1s且一天只能成功调用一次";
                break;

            case PushConsts.SETTAG_ERROR_REPEAT:
                text = "设置标签失败, 标签重复";
                break;

            case PushConsts.SETTAG_ERROR_UNBIND:
                text = "设置标签失败, 服务未初始化成功";
                break;

            case PushConsts.SETTAG_ERROR_EXCEPTION:
                text = "设置标签失败, 未知异常";
                break;

            case PushConsts.SETTAG_ERROR_NULL:
                text = "设置标签失败, tag 为空";
                break;

            case PushConsts.SETTAG_NOTONLINE:
                text = "还未登陆成功";
                break;

            case PushConsts.SETTAG_IN_BLACKLIST:
                text = "该应用已经在黑名单中,请联系售后支持!";
                break;

            case PushConsts.SETTAG_NUM_EXCEED:
                text = "已存 tag 超过限制";
                break;

            default:
                break;
        }

        Log.d(TAG, "settag result sn = " + sn + ", code = " + code + ", text = " + text);
    }

    private void feedbackResult(FeedbackCmdMessage feedbackCmdMsg) {
        String appid = feedbackCmdMsg.getAppid();
        String taskid = feedbackCmdMsg.getTaskId();
        String actionid = feedbackCmdMsg.getActionId();
        String result = feedbackCmdMsg.getResult();
        long timestamp = feedbackCmdMsg.getTimeStamp();
        String cid = feedbackCmdMsg.getClientId();
        LogUtil.e("onReceiveCommandResult -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nactionid = " + actionid + "\nresult = " + result
                + "\ncid = " + cid + "\ntimestamp = " + timestamp);
    }

    private void sendMessage(String data) {
        LogUtil.e("推送数据"+data);
        try {
            JSONObject obj = new JSONObject(data);
            switch (obj.optString("type")) {
                case Constants.PUSH_BLOG_TYPE:
                    BlogBean blogBean = new BlogBean();
                    blogBean.setBlog_id(obj.optString("blog_id"));
                    blogBean.setTitle(obj.optString("content"));
                    blogBean.setImg(obj.optString("cover"));
                    blogBean.setLook_number(obj.optJSONObject("stat").optString("views"));
                    blogBean.setPinglun(obj.optJSONObject("stat").optString("replys"));
                    blogBean.setWap_link(obj.optJSONObject("link").optString("wap"));
                    blogBean.setApp_link(obj.optJSONObject("link").optString("app"));
                    NotificationUtil.showNoticeLayout(getBaseContext(), blogBean);
                    sendNewMessage(obj.optString("type"));
                    break;
                case Constants.PUSH_GROUP_TYPE:
                    //推送圈子
                    TopticBean group_tp = new TopticBean();
                    group_tp.setContent(obj.optString("content"));
                    group_tp.setSubject(obj.optString("subject"));
                    group_tp.setId(obj.optString("id"));
                    NotificationUtil.showGroupNoticeLayout(getBaseContext(), group_tp);
                    break;
                case Constants.PUSH_BOX_TYPE:
                    //宝盒
                    BoxBean boxBean = new BoxBean();
                    boxBean.setBox_id(obj.optString("box_id"));
                    boxBean.setDescription(obj.optString("content"));
                    boxBean.setId(obj.optString("id"));
                    boxBean.setSubject(obj.optString("subject"));
                    NotificationUtil.showBoxNoticeLayout(getBaseContext(), boxBean);
                    sendNewMessage(obj.optString("type"));
                    break;
                case Constants.PUSH_TOPIC_TYPE:
                    TopticBean tb = new TopticBean();
                    if (!obj.isNull("link") && obj.optString("link").length() > 0) {
                        //广告链接
                        tb.setContent(obj.optString("content"));
                        tb.setSubject(obj.optString("subject"));
                        tb.setLink(obj.optString("link"));
                    } else {
                        //纯文本
                        tb.setContent(obj.optString("content"));
                        tb.setSubject(obj.optString("subject"));
                    }
                    NotificationUtil.showTopicNoticeLayout(getBaseContext(), tb);
                    break;
                case MessageType.FANS:
                    Group group = new Group();
                    group.setId(obj.optString("id"));
                    group.setContent(obj.optString("content"));
                    group.setSubject(obj.optString("subject"));
                    group.setMsg_id(obj.optString("msg_id"));
                    NotificationUtil.showGroupNoticeLayout(getBaseContext(), group);
                    break;
                case Constants.PUSH_SCREEN:
                    Screen screen = new Screen();
                    screen.setScreen_id(obj.optString("id"));
                    screen.setName(obj.optString("subject"));
                    screen.setDescripition(obj.optString("content"));
                    NotificationUtil.showScreenNotification(getBaseContext(), screen);
                    break;
                default:
                    NoticeBean noticeBean = new NoticeBean();
                    noticeBean.setType(obj.optString("type"));
                    noticeBean.setTitle(obj.optString("subject"));
                    noticeBean.setContent(obj.optString("content"));
                    noticeBean.setId(obj.optInt("id"));
                    NotificationUtil.showNotification(getBaseContext(),noticeBean);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNewMessage(String type) {
        Intent intent = new Intent(Constants.USER_NEW_MESSAGE);
        intent.putExtra("type", type);
        this.sendBroadcast(intent);
    }
}
