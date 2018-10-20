package com.zbmf.StockGroup.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pq
 * on 2018/5/25.
 * 本地自造的掘金半时的数据模型
 */

public class NuggetsStockBean1 implements Serializable {
    private String toDayV;//当天的日期值
    private ToDayModel tooDayModel;//当天的数据模型
    private String yesterdayV;//昨天的日期值
    private YesterdayModel yesterdayModel;//昨天的数据模型

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

    public String getYesterdayV() {
        return yesterdayV;
    }

    public void setYesterdayV(String yesterdayV) {
        this.yesterdayV = yesterdayV;
    }

    public YesterdayModel getYesterdayModel() {
        return yesterdayModel;
    }

    public void setYesterdayModel(YesterdayModel yesterdayModel) {
        this.yesterdayModel = yesterdayModel;
    }

    public class ToDayModel implements Serializable {
        private String Clock_1030V;
        private List<Clock_1030Model> clock_1030Model;
        private String Clock_1100V;
        private List<Clock_1100Model> clock_1100Model;
        private String Clock_1130V;
        private List<Clock_1130Model> clock_1130Model;
        private String Clock_1330V;
        private List<Clock_1330Model> clock_1330Model;
        private String Clock_1400V;
        private List<Clock_1400Model> clock_1400Model;

        public String getClock_1030V() {
            return Clock_1030V;
        }

        public void setClock_1030V(String clock_1030V) {
            Clock_1030V = clock_1030V;
        }

        public List<Clock_1030Model> getClock_1030Model() {
            return clock_1030Model;
        }

        public void setClock_1030Model(List<Clock_1030Model> clock_1030Model) {
            this.clock_1030Model = clock_1030Model;
        }

        public String getClock_1100V() {
            return Clock_1100V;
        }

        public void setClock_1100V(String clock_1100V) {
            Clock_1100V = clock_1100V;
        }

        public List<Clock_1100Model> getClock_1100Model() {
            return clock_1100Model;
        }

        public void setClock_1100Model(List<Clock_1100Model> clock_1100Model) {
            this.clock_1100Model = clock_1100Model;
        }

        public String getClock_1130V() {
            return Clock_1130V;
        }

        public void setClock_1130V(String clock_1130V) {
            Clock_1130V = clock_1130V;
        }

        public List<Clock_1130Model> getClock_1130Model() {
            return clock_1130Model;
        }

        public void setClock_1130Model(List<Clock_1130Model> clock_1130Model) {
            this.clock_1130Model = clock_1130Model;
        }

        public String getClock_1330V() {
            return Clock_1330V;
        }

        public void setClock_1330V(String clock_1330V) {
            Clock_1330V = clock_1330V;
        }

        public List<Clock_1330Model> getClock_1330Model() {
            return clock_1330Model;
        }

        public void setClock_1330Model(List<Clock_1330Model> clock_1330Model) {
            this.clock_1330Model = clock_1330Model;
        }

        public String getClock_1400V() {
            return Clock_1400V;
        }

        public void setClock_1400V(String clock_1400V) {
            Clock_1400V = clock_1400V;
        }

        public List<Clock_1400Model> getClock_1400Model() {
            return clock_1400Model;
        }

        public void setClock_1400Model(List<Clock_1400Model> clock_1400Model) {
            this.clock_1400Model = clock_1400Model;
        }

        public class Clock_1030Model implements Serializable{
            private StockMode stockMode;

            public StockMode getStockMode() {
                return stockMode;
            }

            public void setStockMode(StockMode stockMode) {
                this.stockMode = stockMode;
            }
        }

        public class Clock_1100Model implements Serializable{
            private StockMode stockMode;

            public StockMode getStockMode() {
                return stockMode;
            }

            public void setStockMode(StockMode stockMode) {
                this.stockMode = stockMode;
            }
        }

        public class Clock_1130Model implements Serializable{
            private StockMode stockMode;

            public StockMode getStockMode() {
                return stockMode;
            }

            public void setStockMode(StockMode stockMode) {
                this.stockMode = stockMode;
            }
        }

        public class Clock_1330Model implements Serializable{
            private StockMode stockMode;

            public StockMode getStockMode() {
                return stockMode;
            }

            public void setStockMode(StockMode stockMode) {
                this.stockMode = stockMode;
            }
        }

        public class Clock_1400Model implements Serializable{
            private StockMode stockMode;

            public StockMode getStockMode() {
                return stockMode;
            }

            public void setStockMode(StockMode stockMode) {
                this.stockMode = stockMode;
            }
        }
    }

    public class YesterdayModel implements Serializable {
        private String Clock_1030V;
        private List<Clock_1030Model> clock_1030Model;
        private String Clock_1100V;
        private List<Clock_1100Model> clock_1100Model;
        private String Clock_1130V;
        private List<Clock_1130Model> clock_1130Model;
        private String Clock_1330V;
        private List<Clock_1330Model> clock_1330Model;
        private String Clock_1400V;
        private List<Clock_1400Model> clock_1400Model;

        public String getClock_1030V() {
            return Clock_1030V;
        }

        public void setClock_1030V(String clock_1030V) {
            Clock_1030V = clock_1030V;
        }

        public List<Clock_1030Model> getClock_1030Model() {
            return clock_1030Model;
        }

        public void setClock_1030Model(List<Clock_1030Model> clock_1030Model) {
            this.clock_1030Model = clock_1030Model;
        }

        public String getClock_1100V() {
            return Clock_1100V;
        }

        public void setClock_1100V(String clock_1100V) {
            Clock_1100V = clock_1100V;
        }

        public List<Clock_1100Model> getClock_1100Model() {
            return clock_1100Model;
        }

        public void setClock_1100Model(List<Clock_1100Model> clock_1100Model) {
            this.clock_1100Model = clock_1100Model;
        }

        public String getClock_1130V() {
            return Clock_1130V;
        }

        public void setClock_1130V(String clock_1130V) {
            Clock_1130V = clock_1130V;
        }

        public List<Clock_1130Model> getClock_1130Model() {
            return clock_1130Model;
        }

        public void setClock_1130Model(List<Clock_1130Model> clock_1130Model) {
            this.clock_1130Model = clock_1130Model;
        }

        public String getClock_1330V() {
            return Clock_1330V;
        }

        public void setClock_1330V(String clock_1330V) {
            Clock_1330V = clock_1330V;
        }

        public List<Clock_1330Model> getClock_1330Model() {
            return clock_1330Model;
        }

        public void setClock_1330Model(List<Clock_1330Model> clock_1330Model) {
            this.clock_1330Model = clock_1330Model;
        }

        public String getClock_1400V() {
            return Clock_1400V;
        }

        public void setClock_1400V(String clock_1400V) {
            Clock_1400V = clock_1400V;
        }

        public List<Clock_1400Model> getClock_1400Model() {
            return clock_1400Model;
        }

        public void setClock_1400Model(List<Clock_1400Model> clock_1400Model) {
            this.clock_1400Model = clock_1400Model;
        }

        public class Clock_1030Model implements Serializable{
            private StockMode stockMode;

            public StockMode getStockMode() {
                return stockMode;
            }

            public void setStockMode(StockMode stockMode) {
                this.stockMode = stockMode;
            }
        }

        public class Clock_1100Model implements Serializable{
            private StockMode stockMode;

            public StockMode getStockMode() {
                return stockMode;
            }

            public void setStockMode(StockMode stockMode) {
                this.stockMode = stockMode;
            }
        }

        public class Clock_1130Model implements Serializable{
            private StockMode stockMode;

            public StockMode getStockMode() {
                return stockMode;
            }

            public void setStockMode(StockMode stockMode) {
                this.stockMode = stockMode;
            }
        }

        public class Clock_1330Model implements Serializable{
            private StockMode stockMode;

            public StockMode getStockMode() {
                return stockMode;
            }

            public void setStockMode(StockMode stockMode) {
                this.stockMode = stockMode;
            }
        }

        public class Clock_1400Model implements Serializable{
            private StockMode stockMode;

            public StockMode getStockMode() {
                return stockMode;
            }

            public void setStockMode(StockMode stockMode) {
                this.stockMode = stockMode;
            }
        }
    }

}
