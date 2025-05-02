package br.com.doug;

import br.com.doug.agents.ServerAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class AgentApplication {

    public static void main(String[] args) {
        Runtime jadeRuntime = Runtime.instance();
        Profile jadeProfile = new ProfileImpl();

        ContainerController containerController = jadeRuntime.createMainContainer(jadeProfile);

        try {
            var rmaAgent = containerController.createNewAgent("rma", "jade.tools.rma.rma", null);
            var serverAgent = containerController.createNewAgent("server", ServerAgent.class.getName(), null);

            rmaAgent.start();
            serverAgent.start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
    }

}
