package com.zbmf.StocksMatch.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.zbmf.worklibrary.dialog.CustomProgressDialog;

/**
 * Created by pq
 * on 2018/4/10.
 */

public class ShowOrHideProgressDialog {

    private static CustomProgressDialog mProgressDialog;
    @SuppressLint("StaticFieldLeak")
    private static Activity sActivity;

    public static void showProgressDialog(){
        boolean finishing = sActivity.isFinishing();
        if(mProgressDialog!=null&&!mProgressDialog.isShowing()&&!finishing){
            mProgressDialog.show();
        }
    }

    public static void disMissProgressDialog() {
        if(mProgressDialog!=null&&mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    public static void initProgressDialog(Context context) {
//        if (mProgressDialog == null) {
            mProgressDialog = CustomProgressDialog.createDialog(context);
//        }
    }

    public static void setDialogCancel(boolean b){
        if (mProgressDialog!=null){
            mProgressDialog.setEnable(b);
        }
    }

    public static void setProgressDialogMsg(String dialogMessage){
        if(mProgressDialog!=null&&!mProgressDialog.isShowing()&&!TextUtils.isEmpty(dialogMessage)){
            mProgressDialog.setMessage(dialogMessage);
        }
    }

    public static void showProgressDialog(Activity activity,Context context, String tipMsg){
        disMissProgressDialog();
        if (activity.isFinishing())return;
        sActivity=activity;
        initProgressDialog(context);
        setProgressDialogMsg(tipMsg);
        showProgressDialog();
        setDialogCancel(false);
    }
}
