package com.zbmf.StocksMatch.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zbmf.StocksMatch.activity.AccountActivity;
import com.zbmf.StocksMatch.activity.LoginActivity;

public class FinishReceiver extends BroadcastReceiver {
    public FinishReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(context instanceof LoginActivity){
            LoginActivity cxt = (LoginActivity)context;
            cxt.finish();
        }
    }
}
