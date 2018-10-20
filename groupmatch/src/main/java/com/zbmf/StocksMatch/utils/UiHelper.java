package com.zbmf.StocksMatch.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

public class UiHelper {

	public static void RegistBroadCast(Context c, BroadcastReceiver b, String ...action) {

		IntentFilter intent = new IntentFilter();
		
		for (String string : action) {
			intent.addAction(string);			
		}

		c.registerReceiver(b, intent);
	}
	
	

	public static void UnRegistBroadCast(Context c, BroadcastReceiver b) {

		if (c != null && b != null) {

			c.unregisterReceiver(b);

			b = null;
		}

	}
}