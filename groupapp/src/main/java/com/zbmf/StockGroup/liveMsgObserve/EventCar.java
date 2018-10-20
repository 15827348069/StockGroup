package com.zbmf.StockGroup.liveMsgObserve;

public class EventCar {
    private static EventCar instance;
    private ConcreteObservableA observableA;

    public static EventCar getDefault() {
        if (instance == null) {
            synchronized (EventCar.class) {
                if (instance == null) {
                    instance = new EventCar();
                }
            }
        }
        return instance;
    }

    private EventCar() {
        observableA = new ConcreteObservableA();
    }

    /**
     * register a observer
     *
     * @param observer
     */
    public void register(Observer observer) {
        observableA.addObserver(observer);
    }

    /**
     * unregister a observer
     *
     * @param observer
     */
    public void unregister(Observer observer) {
        observableA.removeObserver(observer);
    }

    /**
     * post a objict message to all observer
     *
     * @param obj
     */
    public void post(Object obj) {
        observableA.notifyObservers(obj);
    }
}
