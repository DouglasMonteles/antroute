package br.com.doug;

import br.com.doug.agents.AntManagerAgent;
import br.com.doug.agents.ServerAgent;
import br.com.doug.exceptions.AgentException;
import br.com.doug.services.JadeContainerService;
import br.com.doug.services.impl.JadeContainerServiceImpl;
import jade.wrapper.StaleProxyException;

public class AgentApplication {

    private static final JadeContainerService jadeContainerService = JadeContainerServiceImpl.INSTANCE;

    public static void main(String[] args) {
        try {
            var rmaAgent = jadeContainerService.createAgent("rma", "jade.tools.rma.rma", null);
            var antManagerAgent = jadeContainerService.createAgent("antManagerAgent", AntManagerAgent.class.getName(), null);
            var serverAgent = jadeContainerService.createAgent("server", ServerAgent.class.getName(), null);

            rmaAgent.start();
            antManagerAgent.start();
            serverAgent.start();
        } catch (StaleProxyException e) {
            throw new AgentException(e);
        }
    }

}
