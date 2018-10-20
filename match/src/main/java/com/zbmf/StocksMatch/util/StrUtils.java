package com.zbmf.StocksMatch.util;

import java.util.regex.Pattern;

/**
 * Created by pq
 * on 2018/3/30.
 * 有关字符串的工具类
 */

public class StrUtils {
    /**
     * 判断字符串是否券是数字
     * @param str
     * @return
     */
    public static boolean isNumeric1(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否券是数字
     *  Character.isDigit()是java提供的API，这个跟C语言的很像。
     */
    public static boolean isNumeric2(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否券是数字
     * 通过ASCII码判断
     * @param str
     * @return
     */
    public static boolean isNumeric3(String str){
        for(int i=str.length();--i>=0;){
            int chr=str.charAt(i);
            if(chr<48 || chr>57)
                return false;
        }
        return true;
    }
}
