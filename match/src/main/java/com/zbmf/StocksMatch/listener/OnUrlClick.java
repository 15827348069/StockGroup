package com.zbmf.StocksMatch.listener;


import com.zbmf.StocksMatch.bean.BlogBean;
import com.zbmf.StocksMatch.bean.Group;
import com.zbmf.StocksMatch.bean.Stock;
import com.zbmf.StocksMatch.bean.Video;

/**
 * Created by xuhao
 * on 2017/8/28.
 */

public interface OnUrlClick {
    void onGroup(Group group);
    void onBolg(BlogBean blogBean);
    void onVideo(Video video);
    void onWeb(String url);
    void onImage(String url);
    void onPay();
    void onStock(Stock stock);
}
