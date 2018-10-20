package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/4/10.
 */

public interface IPassUserMode {
   void loginWeChat(String code, CallBack callBack);
   void loginQQSina(String open_id,String token,String api_type, CallBack callBack);
}
