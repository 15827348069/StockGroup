package com.zbmf.StockGroup.utils;

import java.text.DecimalFormat;

/**
 * Created by xuhao on 2017/9/11.
 */

public class DoubleFromat {
    public static String getDouble(double d,int f){
        StringBuffer format=new StringBuffer();
        format.append(f>0?"#0.":"#0");
        for(int i=0;i<f;i++){
            format.append("0");
        }
        DecimalFormat df=new DecimalFormat(format.toString());
        if (f<3&&df.format(d).endsWith(".0")||df.format(d).endsWith(".00")) {
            DecimalFormat df0 = new DecimalFormat("");
            return df0.format(d);
        } else{
            return df.format(d);
        }
    }
    public static String getStockDouble(double d,int f){
        StringBuffer format=new StringBuffer();
        format.append(f>0?"#0.":"#0");
        for(int i=0;i<f;i++){
            format.append("0");
        }
        DecimalFormat df=new DecimalFormat(format.toString());
        return df.format(d);
    }
}
