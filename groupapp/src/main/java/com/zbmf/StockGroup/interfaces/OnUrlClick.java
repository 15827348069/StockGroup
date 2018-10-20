package com.zbmf.StockGroup.interfaces;

import com.zbmf.StockGroup.beans.BlogBean;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.Screen;
import com.zbmf.StockGroup.beans.Stock;
import com.zbmf.StockGroup.beans.Video;

/**
 * Created by xuhao on 2017/8/28.
 */

public interface OnUrlClick {
    void onGroup(Group group);
    void onBolg(BlogBean blogBean);
    void onVideo(Video video);
    void onWeb(String url);
    void onImage(String url);
    void onPay();
    void onStock(Stock stock);
    void onScreen();
    void onScreenDetail(Screen screen);
    void onModeStock();
    void onDongAsk();
    void onQQ(String url);
}
