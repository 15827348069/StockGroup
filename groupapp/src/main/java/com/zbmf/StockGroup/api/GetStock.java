package com.zbmf.StockGroup.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zbmf.StockGroup.utils.LogUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xuhao on 2017/9/1.
 */

public class GetStock {
    private static final String GET_STOCK="https://gupiao.baidu.com/api/search/stockquery?";
    private static AsyncHttpClient client = new AsyncHttpClient();
    static {
        client.setTimeout(2000);
        client.setMaxConnections(6);
    }
    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    private static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
    /**
     * 最后组装数据
     *
     * @param
     * @return
     */
    private static RequestParams getRequest(String searchKey) {
        Map<String,String>  map = new HashMap<>();
        map.put("from","mobile");
        map.put("os_ver","1");
        map.put("cuid","xxx");
        map.put("vv","3.2");
        map.put("format","json");
        map.put("query_content",searchKey+"");
        map.put("asset","0%2C4%2C14");
        long time=new Date().getTime();
        map.put("timestamp",String.valueOf(time));
        map = sortMapByKey(map);
        RequestParams params = new RequestParams();
        for (String key : map.keySet()) {
            params.put(key, map.get(key));
        }
        return params;
    }
    public static void getStock(String searchKey, AsyncHttpResponseHandler responseHandler){
        if(searchKey.contains(")")||searchKey.contains("(")){
            return ;
        }else{
            client.get(GET_STOCK+getRequest(searchKey),null, responseHandler);
        }

    }
}
