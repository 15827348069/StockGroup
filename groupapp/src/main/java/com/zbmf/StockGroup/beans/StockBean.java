package com.zbmf.StockGroup.beans;

/**
 * Created by xuhao on 2017/9/1.
 */

public class StockBean {
    private String f_symbolName;
    private String f_symbol;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getF_symbolName() {
        return f_symbolName;
    }

    public void setF_symbolName(String f_symbolName) {
        this.f_symbolName = f_symbolName;
    }

    public String getF_symbol() {
        return f_symbol;
    }

    public void setF_symbol(String f_symbol) {
        this.f_symbol = f_symbol;
    }

    public StockBean(String f_symbolName, String f_symbol) {
        this.f_symbolName = f_symbolName;
        this.f_symbol = f_symbol;
    }
    public StockBean(String f_symbolName, String f_symbol,boolean isCheck) {
        this.f_symbolName = f_symbolName;
        this.f_symbol = f_symbol;
        this.isCheck=isCheck;
    }
    @Override
    public String toString() {
        return "StockBean{" +
                "f_symbolName='" + f_symbolName + '\'' +
                ", f_symbol='" + f_symbol + '\'' +
                '}';
    }
}
