package br.com.doug.ants.observer;

public interface AntSubject {

    void registerObserver(AntObserver observer);

    void removeObserver(AntObserver observer);

    void notifyObservers();

}
