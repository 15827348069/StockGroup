package com.zbmf.StocksMatch.bean;

import java.util.List;

/**
 * Created by xuhao
 * on 2017/11/27.
 */

public class TraderList extends BaseBean{
    private List<Traders>traders;

    public List<Traders> getTraders() {
        return traders;
    }

    public void setTraders(List<Traders> traders) {
        this.traders = traders;
    }
}
