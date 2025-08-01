package br.com.doug.ants;

import br.com.doug.ants.observer.AntObserver;
import br.com.doug.ants.observer.AntSubject;

import java.util.ArrayList;
import java.util.List;

public class AntObserverService implements AntSubject {

    public static AntObserverService INSTANCE = new AntObserverService();

    private AntSimulationDataDTO antSimulationDataDTO;

    List<AntObserver> observers = new ArrayList<>();

    public void initSimulation(AntSimulationDataDTO antSimulationDataDTO) {
        this.antSimulationDataDTO = antSimulationDataDTO;
        this.notifyObservers();
    }

    @Override
    public void registerObserver(AntObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(AntObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(antSimulationDataDTO));
    }
}
