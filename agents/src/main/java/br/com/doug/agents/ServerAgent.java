package br.com.doug.agents;

import br.com.doug.AntrouteApplication;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

import java.io.Serial;

public class ServerAgent extends Agent {

    @Serial
    private static final long serialVersionUID = -4507878042435134622L;

    @Override
    protected void setup() {

        addBehaviour(new OneShotBehaviour() {
            @Serial
            private static final long serialVersionUID = 5985309262402008575L;

            @Override
            public void action() {
                AntrouteApplication.initServer(new String[]{});
            }
        });
    }
}
