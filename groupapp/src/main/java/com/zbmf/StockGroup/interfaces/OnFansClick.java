package com.zbmf.StockGroup.interfaces;

import com.zbmf.StockGroup.beans.BoxBean;
import com.zbmf.StockGroup.beans.NewsFeed;

/**
 * Created by xuhao on 2017/8/28.
 */

public interface OnFansClick{
    void onBox(BoxBean boxBean);
    void onFans(String groupId);
    void onGroup(String groupId);
}
