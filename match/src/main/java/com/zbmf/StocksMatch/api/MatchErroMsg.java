package com.zbmf.StocksMatch.api;

import android.content.Context;

import com.zbmf.StocksMatch.R;

/**
 * Created by pq
 * on 2018/3/29.
 */

public class MatchErroMsg {
    public static String getErroMsg(Context context,int code){
        String erroMsg="";
        switch (code){
            case 1014:
                erroMsg=context.getString(R.string.exists);
                break;
        }
        return erroMsg;
    }
}
