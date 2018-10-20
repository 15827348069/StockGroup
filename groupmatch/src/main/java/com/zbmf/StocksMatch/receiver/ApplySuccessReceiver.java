package com.zbmf.StocksMatch.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.zbmf.StocksMatch.activity.AllMatchActivity;
import com.zbmf.StocksMatch.activity.ApplyMatchActivity;
import com.zbmf.StocksMatch.activity.MatchDetailActivity;
import com.zbmf.StocksMatch.activity.UserHolderActivity;
import com.zbmf.StocksMatch.activity.UserTransactionActivity;
import com.zbmf.StocksMatch.beans.MatchBean;

public class ApplySuccessReceiver extends BroadcastReceiver {
    public ApplySuccessReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(context instanceof MatchDetailActivity){
            MatchDetailActivity cxt = (MatchDetailActivity)context;
            MatchBean matchBean = (MatchBean)intent.getSerializableExtra("match");
            if(cxt!=null && matchBean!=null  )
             cxt.updateUi(matchBean);
        }

        if(context instanceof AllMatchActivity){
            AllMatchActivity cxt = (AllMatchActivity)context;
            MatchBean matchBean = (MatchBean)intent.getSerializableExtra("match");
            if(cxt!=null && matchBean!=null)
                cxt.updateUi(matchBean.getId());
        }

        if(context instanceof UserHolderActivity){
            UserHolderActivity cxt = (UserHolderActivity)context;
            String symbol = (String)intent.getSerializableExtra("symbol");
            if(!TextUtils.isEmpty(symbol))
                cxt.updateUi(symbol);
        }

        if(context instanceof UserTransactionActivity){
            UserTransactionActivity cxt = (UserTransactionActivity)context;
            String symbol = (String)intent.getSerializableExtra("symbol");
            if(!TextUtils.isEmpty(symbol))
                cxt.updateUi(symbol);
        }
    }
}
