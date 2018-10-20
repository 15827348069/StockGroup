package com.zbmf.StockGroup.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by pq
 * on 2018/6/22.
 */

public class UTFUtils {
    //将UTF-8解码
    public static String jmUTF_8(String utf8Str){
        String decode = null;
        try {
             decode= URLDecoder.decode(utf8Str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }
}
