package br.com.doug.services;

import jade.wrapper.AgentController;

public interface JadeContainerService {

    AgentController createAgent(String nickname, String className, Object[] args);

}
