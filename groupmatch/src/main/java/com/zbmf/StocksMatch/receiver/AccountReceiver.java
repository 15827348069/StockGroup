package com.zbmf.StocksMatch.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zbmf.StocksMatch.activity.AccountActivity;
import com.zbmf.StocksMatch.activity.AllMatchActivity;
import com.zbmf.StocksMatch.activity.FocusActivity;
import com.zbmf.StocksMatch.activity.MatchDetailActivity;
import com.zbmf.StocksMatch.beans.MatchBean;

import org.w3c.dom.Text;

public class AccountReceiver extends BroadcastReceiver {
    public AccountReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(context instanceof AccountActivity){
            String user_id = (String)intent.getSerializableExtra("user_id");
            AccountActivity cxt = (AccountActivity)context;
            if(cxt!=null  && !TextUtils.isEmpty(user_id))
                  cxt.updateUi(user_id);
        }

        if(context instanceof FocusActivity){
            String gid = (String)intent.getSerializableExtra("gid");
            FocusActivity cxt = (FocusActivity)context;
            if(cxt!=null  && !TextUtils.isEmpty(gid)){
                cxt.updateUi(gid);
            }
        }

    }
}
