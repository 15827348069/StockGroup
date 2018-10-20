package com.zbmf.StocksMatch.model.imode;

import android.content.Context;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/26.
 */

public interface IUserMode {
    void upAvatar(String avatar, Context context,CallBack callBack);//上传图像
    void getDefaultAvatar(CallBack callBack);//获取系统图像
    void upDateUser(String nickName,CallBack callBack);//更新昵称图像
}
