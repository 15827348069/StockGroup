package com.zbmf.StockGroup.liveMsgObserve;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Object object);
}
