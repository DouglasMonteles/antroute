package br.com.doug.agents;

import br.com.doug.AntrouteApplication;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class ServerAgent extends Agent {

    @Override
    protected void setup() {

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                AntrouteApplication.initServer(new String[]{});
            }
        });
    }
}
