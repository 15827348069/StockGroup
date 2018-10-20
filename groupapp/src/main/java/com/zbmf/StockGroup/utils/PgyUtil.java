package com.zbmf.StockGroup.utils;

import android.content.Context;

import com.pgyersdk.crash.PgyCrashManager;


public class PgyUtil {
    public static void registerCrash(Context context, boolean isDebug) {
        if (isDebug) {
            PgyCrashManager.register(context);
        }
    }
}
