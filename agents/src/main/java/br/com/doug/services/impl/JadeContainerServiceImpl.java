package br.com.doug.services.impl;

import br.com.doug.exceptions.AgentException;
import br.com.doug.services.JadeContainerService;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class JadeContainerServiceImpl implements JadeContainerService {

    public static final JadeContainerService INSTANCE = new JadeContainerServiceImpl();

    private final Runtime jadeRuntime = Runtime.instance();
    private final Profile jadeProfile = new ProfileImpl();
    private final ContainerController containerController = jadeRuntime.createMainContainer(jadeProfile);

    public AgentController createAgent(String nickname, String className, Object[] args) {
        AgentController agentController;

        try {
            agentController = containerController.createNewAgent(nickname, className, args);
        } catch (StaleProxyException e) {
            throw new AgentException(e);
        }

        return agentController;
    }

}
