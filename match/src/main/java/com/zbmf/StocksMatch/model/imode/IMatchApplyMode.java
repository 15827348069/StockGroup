package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

import java.util.Map;

/**
 * Created by xuhao
 * on 2017/12/12.
 */

public interface IMatchApplyMode {
    void onApplyMatch(Map<String ,String> map, CallBack callBack);
    void applyMatchCode(int match_id,String mobile,CallBack callBack);
}
