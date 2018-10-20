package com.zbmf.groupro.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by xuhao on 2017/2/24.
 */

public class SendBrodacast {
    public static void send(Context context,String flag){
        Intent intent = new Intent();
        intent.setAction(flag);
        context.sendBroadcast(intent);
    }
    public static void send(Context context,Intent intent,String flag){
        intent.setAction(flag);
        context.sendBroadcast(intent);
    }
}
