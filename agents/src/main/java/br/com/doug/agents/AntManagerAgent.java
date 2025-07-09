package br.com.doug.agents;

import br.com.doug.ant.Ant;
import br.com.doug.ant.Graph;
import br.com.doug.ant.Node;
import br.com.doug.ant.impl.AntDensityAlgorithm;
import br.com.doug.exceptions.AgentException;
import br.com.doug.services.JadeContainerService;
import br.com.doug.services.impl.JadeContainerServiceImpl;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.io.Serial;
import java.util.*;

import static br.com.doug.ant.AntAlgorithm.NC_MAX;

public class AntManagerAgent extends Agent {

    @Serial
    private static final long serialVersionUID = -5837718391546224594L;
    
    private final JadeContainerService jadeContainerService = JadeContainerServiceImpl.INSTANCE;

    private final Graph graph = new Graph();

    private final Node nodeA = new Node("A", new Node.Position(10f, 10f));
    private final Node nodeB = new Node("B", new Node.Position(20f, 20f));
    private final Node nodeC = new Node("C", new Node.Position(30f, 10f));

    Ant ant1 = new Ant("AntA", nodeA);
    Ant ant2 = new Ant("AntB", nodeB);
    Ant ant3 = new Ant("AntC", nodeC);

    List<Ant> ants = List.of(ant1, ant2, ant3);

    AntDensityAlgorithm antDensityAlgorithm = new AntDensityAlgorithm(graph, ants);

    private int NC = 0;

    @Override
    protected void setup() {
        graph.addEdge(nodeA, nodeB);
        graph.addEdge(nodeA, nodeC);
        graph.addEdge(nodeB, nodeC);

        addBehaviour(new InitializeAntBehaviour());
        addBehaviour(new AntResponseBehaviour());
    }

    private class InitializeAntBehaviour extends OneShotBehaviour {

        @Serial
        private static final long serialVersionUID = 8364774258260754806L;

        @Override
        public void action() {
            for (Node node : graph.getNodes()) {
                var antLabel = "Ant" + node.getName();
                var antContainer = jadeContainerService.createAgent(antLabel, AntAgent.class.getName(), new Object[]{
                        antLabel,
                        node,
                        graph,
                });

                try {
                    antContainer.start();
                } catch (StaleProxyException e) {
                    throw new AgentException(e);
                }
            }

            for (; NC < NC_MAX; NC++) {
                // Repeat until tabu list is full
                //while (true) {
                    // for every town
                    graph.getNodes().forEach(node -> {
                        // for every k-th ant on town i still not moved
                        // Send message to move all ants in that node

                        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                        try {
                            message.addReceiver(getAID("AntA"));
                            message.addReceiver(getAID("AntB"));
                            message.addReceiver(getAID("AntC"));
                            message.setContentObject(node);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        send(message);
                        
//                        ants.forEach(ant -> {
//                            if (ant.getActualNode().equals(node)) {
//                                ant.move(graph);
//                            }
//                        });
                    });

//                    boolean isAllAntsWithTabuListFull = ants.stream().allMatch(ant -> ant.isTabuListFull(graph.getNodes().size()));
//                    if (isAllAntsWithTabuListFull) {
//                        break;
//                    }
                //}

//                ants.forEach(ant -> {
//                    System.out.println(ant.pathFound());
//                });
//
//                graph.updateShortestPath(ants);
//                graph.computeEvaporationOfTrail();
//
//                /*
//                 * TODO: Tours in TSP are closed loops:
//                 *   Ex: a tour A → B → C → D → A is the same as B → C → D → A → B, or C → D → A → B → C.
//                 *   In TSP, these are considered the same tour, just with a different rotation.
//                 * */
//                boolean isAllAntsWithSamePath = true;
//
//                for (int i = 0; i < ants.size() - 1; i++) {
//                    Ant fistAnt = ants.get(i);
//                    Ant secondAnt = ants.get(i + 1);
//
//                    if (!antDensityAlgorithm.isSameTour(fistAnt.getPathFound(), secondAnt.getPathFound())) {
//                        isAllAntsWithSamePath = false;
//                        break;
//                    }
//                }

//            if (isAllAntsWithSamePath) {
//                // All the ants choose the same tour
//                System.out.println();
//                System.out.println("is all same");
//                break;
//            }

//                if (NC >= NC_MAX - 1 || isAllAntsWithSamePath) {
//                    System.out.println("Shortest path found:");
//                    System.out.println(graph.getShortestPath().toString());
//                    System.out.println("Shortest distance found:");
//                    System.out.println(graph.getShortestPathDistance());
//                    break;
//                }
//
//                ants.forEach(ant -> {
//                    // Empty all tabu lists
//                    ant.getTabuList().clear();
//
//                    // Empty path found
//                    ant.getPathFound().clear();
//
//                    // Reset nextNode and actualNode
//                    ant.setNextNode(null);
//                    ant.setActualNode(ant.getInitialNode());
//                });
//
//                // For every edge (i,j) set ∆τij(t,t+1):=0
//                graph.getEdges().forEach(edge -> edge.setPheromoneOnEdge(0f));
//
//                System.out.println();
            }
        }
    }

    private class AntResponseBehaviour extends CyclicBehaviour {

        @Serial
        private static final long serialVersionUID = 6620246288146601025L;

        private final Set<Ant> ants = new HashSet<>();

        @Override
        public void action() {
            MessageTemplate replyTemplate = MessageTemplate.MatchPerformative(Peformative.ANT_RESPONSE_OK);
            ACLMessage antReply = receive(replyTemplate);

            if (antReply != null) {
                try {
                    Ant ant = (Ant) antReply.getContentObject();
                    System.out.println(ant);
                    ants.add(ant);

                    System.out.println("Ants size: " + ants.size());

                    if (ant != null && ant.getPathFound() != null && !ant.getPathFound().isEmpty())
                        System.out.println(ant.getPathFound().stream().filter(Objects::nonNull).map(Node::getName).toList());
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }

                boolean isAllAntsWithTabuListFull = ants.stream().allMatch(ant -> ant.isTabuListFull(graph.getNodes().size()));

                if (isAllAntsWithTabuListFull) {
                    System.out.println("recebeu");
                    ants.forEach(ant -> {
                        System.out.println(ant.pathFound());
                    });

                    graph.updateShortestPath(ants.stream().toList());
                    graph.computeEvaporationOfTrail();

                    /*
                     * TODO: Tours in TSP are closed loops:
                     *   Ex: a tour A → B → C → D → A is the same as B → C → D → A → B, or C → D → A → B → C.
                     *   In TSP, these are considered the same tour, just with a different rotation.
                     * */
                    boolean isAllAntsWithSamePath = true;

                    for (int i = 0; i < ants.size() - 1; i++) {
                        Ant fistAnt = ants.stream().toList().get(i);
                        Ant secondAnt = ants.stream().toList().get(i + 1);

                        if (!antDensityAlgorithm.isSameTour(fistAnt.getPathFound(), secondAnt.getPathFound())) {
                            isAllAntsWithSamePath = false;
                            break;
                        }
                    }

                    if (NC >= NC_MAX - 1 || isAllAntsWithSamePath) {
                        System.out.println("Shortest path found:");
                        System.out.println(graph.getShortestPath().toString());
                        System.out.println("Shortest distance found:");
                        System.out.println(graph.getShortestPathDistance());
                    }

                    ants.forEach(ant -> {
                        // Empty all tabu lists
                        ant.getTabuList().clear();

                        // Empty path found
                        ant.getPathFound().clear();

                        // Reset nextNode and actualNode
                        ant.setNextNode(null);
                        ant.setActualNode(ant.getInitialNode());
                    });

                    // For every edge (i,j) set ∆τij(t,t+1):=0
                    graph.getEdges().forEach(edge -> edge.setPheromoneOnEdge(0f));

                    System.out.println();
                }
            } else {
                block();
            }
        }
    }

}
