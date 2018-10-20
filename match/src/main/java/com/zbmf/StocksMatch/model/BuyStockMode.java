package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.StockBean;
import com.zbmf.StocksMatch.bean.StockInfo;
import com.zbmf.StocksMatch.model.imode.IBuyStockMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.GsonUtil;

/**
 * Created by pq
 * on 2018/3/27.
 */

public class BuyStockMode extends BaseMatchMode implements IBuyStockMode {
    private static BuyStockMode sBuyStockMode;
    public static BuyStockMode getInstance(){
        if (sBuyStockMode==null){
            synchronized (BuyStockMode.class){
                if (sBuyStockMode==null){
                    sBuyStockMode=new BuyStockMode();
                }
            }
        }
        return sBuyStockMode;
    }
    //实时更新股票信息
    @Override
    public void getStockRealInfo(String symbol, final CallBack callBack) {
          postSubscrube(Method.STOCK_REAL_TIME_INFO, SendParam.stockRealTimeInfo(symbol), new CallBack() {
              @Override
              public void onSuccess(Object o) {
                  StockInfo stockInfo = GsonUtil.parseData(o, StockInfo.class);
                  if (stockInfo.getStatus()){
                      StockInfo.Result result = stockInfo.getResult();
                      assert result!=null;
                      callBack.onSuccess(result);
                  }else {
                      callBack.onFail("仅支持A股交易");
                  }
              }

              @Override
              public void onFail(String msg) {
                  callBack.onFail(msg);
              }
          });
    }

    public void getStockInfo(String symbol, final CallBack callBack){
        postSubscrube(Method.STOCK_TIME_INFO, SendParam.getStockInfo(symbol), new CallBack() {
            @Override
            public void onSuccess(Object o) {
                StockInfo stockInfo = GsonUtil.parseData(o, StockInfo.class);
                if (stockInfo.getStatus()){
                    StockInfo.Result result = stockInfo.getResult();
                    assert result!=null;
                    callBack.onSuccess(result);
                }else {
                    callBack.onFail("实时获取股票信息失败");
                }
            }

            @Override
            public void onFail(String msg) {
                callBack.onFail(msg);
            }
        });
    }

    @Override
    public void getStock(String searchKey, CallBack callBack) {
        if(searchKey.contains(")")||searchKey.contains("(")){
            return ;
        }else{
            postSubscrube(Method.GET_STOCK+SendParam.getStock(searchKey),null, new CallBack() {
                @Override
                public void onSuccess(Object o) {
                    StockBean stockBean = GsonUtil.parseData(o, StockBean.class);

                }

                @Override
                public void onFail(String msg) {

                }
            });
        }
    }
}
