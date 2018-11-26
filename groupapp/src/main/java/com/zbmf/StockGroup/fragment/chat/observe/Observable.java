package com.zbmf.StockGroup.fragment.chat.observe;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Object object);
}
