package com.zbmf.StockGroup.liveMsgObserve;

import java.util.ArrayList;

public class ConcreteObservableA implements Observable{
    private ArrayList<Observer>observers;
    @Override
    public void addObserver(Observer observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        if(observers==null||observers.size()==0){
            return;
        }
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object object) {
        if(observers!=null&&observers.size()>0){
            for(Observer observer:observers){
                observer.updata(object);
            }
        }
    }
}
