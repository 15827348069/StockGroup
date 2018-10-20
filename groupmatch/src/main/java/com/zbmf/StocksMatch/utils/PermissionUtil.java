package com.zbmf.StocksMatch.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2016/1/27.
 */
public class PermissionUtil {

    private Context cxt;

    public PermissionUtil(Context cxt) {
        this.cxt = cxt;
    }

    public void checkSelfPermission(Activity cxt, String permission) {
        if (ContextCompat.checkSelfPermission(cxt, permission)!= PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(cxt, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }else{

        }

    }


}
