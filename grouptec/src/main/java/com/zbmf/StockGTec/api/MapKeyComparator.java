package com.zbmf.StockGTec.api;

import java.util.Comparator;

/**
 * Created by xuhao on 2016/6/12.
 */
public class MapKeyComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}