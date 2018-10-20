package com.zbmf.StocksMatch.model.imode;

import com.zbmf.worklibrary.model.CallBack;

/**
 * Created by pq
 * on 2018/3/19.
 */

public interface IMatchDescMode {
    void matchDesc(int match_id, CallBack callBack);
//    void applyMatch(String match_id,String mobile,String code,String username,String email,String school,String student_id);
}
