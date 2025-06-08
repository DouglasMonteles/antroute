package br.com.doug.agents;

import br.com.doug.ant.Graph;
import br.com.doug.ant.Node;
import br.com.doug.exceptions.AgentException;
import br.com.doug.services.JadeContainerService;
import br.com.doug.services.impl.JadeContainerServiceImpl;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.StaleProxyException;

public class AntAlgorithmAgent extends Agent {

    private static final int NUM_ANTS = 3;
    private static final JadeContainerService jadeContainerService = JadeContainerServiceImpl.INSTANCE;

    private final Graph graph = new Graph();
    private final Node nodeA = new Node("A", new Node.Position(10f, 10f));
    private final Node nodeB = new Node("B", new Node.Position(20f, 20f));
    private final Node nodeC = new Node("C", new Node.Position(30f, 10f));

    @Override
    protected void setup() {
        graph.addEdge(nodeA, nodeB);
        graph.addEdge(nodeA, nodeC);
        graph.addEdge(nodeB, nodeC);

        addBehaviour(new InitializeAntBehaviour());
    }

    private class InitializeAntBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            for (Node node : graph.getNodes()) {
                var antLabel = "Ant" + node.getName();
                var antContainer = jadeContainerService.createAgent(antLabel, AntAgent.class.getName(), new Object[]{
                        antLabel,
                        node,
                });

                try {
                    antContainer.start();
                } catch (StaleProxyException e) {
                    throw new AgentException(e);
                }
            }
        }
    }

}
