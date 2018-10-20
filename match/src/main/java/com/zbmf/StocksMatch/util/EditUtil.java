package com.zbmf.StocksMatch.util;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;

/**
 * Created by xuhao
 * on 2017/12/12.
 */

public class EditUtil {
    /**
     * 验证是否为空
     * @param account
     * @return
     */
    public static boolean isEmpty(String account) {
        if (TextUtils.isEmpty(account)) {
            return true;
        }
        return false;
    }
    public static boolean isEmpty(TextInputLayout textInputLayout,String message,String errMessage){
        if(isEmpty(message)){
            showError(textInputLayout,errMessage);
            return true;
        }else{
            clearError(textInputLayout);
            return false;
        }
    }
    /**
     * 显示错误提示，并获取焦点
     *
     * @param textInputLayout
     * @param error
     */
    private static void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }
    private static void clearError(TextInputLayout textInputLayout){
        textInputLayout.setErrorEnabled(false);
    }
}
