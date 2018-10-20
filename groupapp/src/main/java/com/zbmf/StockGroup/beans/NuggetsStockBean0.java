package com.zbmf.StockGroup.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/5/25.
 * 给模型选股造本地的数据模型---掘金十点
 */

public class NuggetsStockBean0 implements Serializable {
    private String toDayV;//当天的日期值
    private ToDayModel tooDayModel;//当天的数据模型
//    private String yesterdayV;//昨天的日期值
//    private YesterdayModel yesterdayModel;//昨天的数据模型

    public String getToDayV() {
        return toDayV;
    }

    public void setToDayV(String toDayV) {
        this.toDayV = toDayV;
    }

    public ToDayModel getTooDayModel() {
        return tooDayModel;
    }

    public void setTooDayModel(ToDayModel tooDayModel) {
        this.tooDayModel = tooDayModel;
    }

//    public String getYesterdayV() {
//        return yesterdayV;
//    }
//
//    public void setYesterdayV(String yesterdayV) {
//        this.yesterdayV = yesterdayV;
//    }
//
//    public YesterdayModel getYesterdayModel() {
//        return yesterdayModel;
//    }
//
//    public void setYesterdayModel(YesterdayModel yesterdayModel) {
//        this.yesterdayModel = yesterdayModel;
//    }

    public static class ToDayModel implements Serializable{
        private String clock_10V;
        private List<Clock_10Model> clock_10Model;

        public String getClock_10V() {
            return clock_10V;
        }

        public void setClock_10V(String clock_10V) {
            this.clock_10V = clock_10V;
        }

        public List<Clock_10Model> getClock_10Model() {
            return clock_10Model;
        }

        public void setClock_10Model(List<Clock_10Model> clock_10Model) {
            this.clock_10Model = clock_10Model;
        }

        public static class Clock_10Model implements Serializable{
            private List<StockMode> stockMode;

            public List<StockMode> getStockMode() {
                return stockMode;
            }

            public void setStockMode(List<StockMode> stockMode) {
                this.stockMode = stockMode;
            }
        }
    }

//    public class YesterdayModel implements Serializable{
//        private String clock_10V;
//        private List<Clock_10Model> clock_10Model;
//
//        public String getClock_10V() {
//            return clock_10V;
//        }
//
//        public void setClock_10V(String clock_10V) {
//            this.clock_10V = clock_10V;
//        }
//
//        public List<Clock_10Model> getClock_10Model() {
//            return clock_10Model;
//        }
//
//        public void setClock_10Model(List<Clock_10Model> clock_10Model) {
//            this.clock_10Model = clock_10Model;
//        }
//
//        public class Clock_10Model implements Serializable{
//            private StockMode stockMode;
//
//            public StockMode getStockMode() {
//                return stockMode;
//            }
//
//            public void setStockMode(StockMode stockMode) {
//                this.stockMode = stockMode;
//            }
//        }
//    }
}
