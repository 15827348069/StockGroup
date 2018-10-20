package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.model.imode.IGetStockMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.util.Logx;

/**
 * Created by pq
 * on 2018/3/29.
 */

public class GetStockMode extends BaseGetStockMode implements IGetStockMode {
private static GetStockMode mGetStockMode;
public static GetStockMode getInstance(){
    if (mGetStockMode==null){
        synchronized (GetStockMode.class){
            if (mGetStockMode==null){
                mGetStockMode=new GetStockMode();
            }
        }
    }
    return mGetStockMode;
}
    @Override
    public void getStockMode(final String searchKey, CallBack callBack) {
        setSearchKey(searchKey);
        if(searchKey.contains(")")||searchKey.contains("(")){
            return ;
        }else{
//            client.get(GET_STOCK+getRequest(searchKey),null, responseHandler);
            getSubscrube(/*Method.GET_STOCK+ SendParam.getStock(searchKey),*/null, new CallBack() {
                @Override
                public void onSuccess(Object o) {
//                    Log.i("--TAG","----------------- o "+o);
//                    StockBean stockBean = GsonUtil.parseData(o, StockBean.class);
//                    if (stockBean)
                }

                @Override
                public void onFail(String msg) {
                    Logx.e(msg);
                }
            });
        }
    }
}
