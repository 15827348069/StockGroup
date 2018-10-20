package com.zbmf.StockGTec.service;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;

/**
 * Created by iMac on 2016/12/14.
 */

public class DemoIntentService extends GTIntentService {

    public DemoIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    // 获取透传数据
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
//         String appid = msg.getAppid();
        byte[] payload = msg.getPayload();

        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();

        // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

        if (payload != null) {
            String data = new String(payload);
            Log.e(TAG, "onReceiveMessageData: "+data);
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        SettingDefaultsManager.getInstance().setPushCilentId(clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
}