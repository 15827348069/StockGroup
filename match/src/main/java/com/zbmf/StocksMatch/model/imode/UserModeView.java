package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.baseview.BaseView;

import org.json.JSONObject;

/**
 * Created by pq
 * on 2018/3/26.
 */

public interface UserModeView extends BaseView{
    void upAvatar(JSONObject obj);
    void nickName(String nick);
    void upErr(String msg);
    void refreshAvatar();
}
