package br.com.doug.agents;

import br.com.doug.agents.builders.AgentMessageBuilder;
import br.com.doug.agents.utils.AgentUtils;
import br.com.doug.agents.utils.Performative;
import br.com.doug.ant.Ant;
import br.com.doug.ant.Graph;
import br.com.doug.ant.Node;
import br.com.doug.ant.impl.AntDensityAlgorithm;
import br.com.doug.ants.AntObserverService;
import br.com.doug.ants.observer.AntObserver;
import br.com.doug.ants.AntSimulationDataDTO;
import br.com.doug.exceptions.AgentException;
import br.com.doug.services.JadeContainerService;
import br.com.doug.services.impl.JadeContainerServiceImpl;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.StaleProxyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static br.com.doug.ant.AntAlgorithm.NC_MAX;

public class AntManagerAgent extends Agent implements AntObserver {

    private static final Logger LOG = LoggerFactory.getLogger(AntManagerAgent.class);

    @Serial
    private static final long serialVersionUID = -5837718391546224594L;
    
    private final JadeContainerService jadeContainerService = JadeContainerServiceImpl.INSTANCE;

    private Graph graph;

    private final List<Ant> ants = new ArrayList<>();

    private AntDensityAlgorithm antDensityAlgorithm;

    private int NC = 0;

    private final AntObserverService antObserverService = AntObserverService.INSTANCE;

    public AntManagerAgent() {
        antObserverService.registerObserver(this);
    }

    @Override
    public void update(AntSimulationDataDTO antSimulationDataDTO) {
        LOG.info("Received simulation data: {}", antSimulationDataDTO);

        this.graph = Graph.buildGraph(antSimulationDataDTO);
        this.graph.getNodes().forEach(node -> ants.add(new Ant("Ant" + node.getName(), node)));
        this.antDensityAlgorithm = new AntDensityAlgorithm(graph, ants);

        // trigger every time we receive a simulation data
        addBehaviour(new InitializeAntBehaviour());
    }

    @Override
    protected void setup() {

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
                // for every town
                graph.getNodes().forEach(node -> {
                    // for every k-th ant on town i still not moved
                    // Send message to move all ants in that node
                    send(new AgentMessageBuilder(Performative.ANT_REQUEST)
                            .setReceivers(ants.stream()
                                    .map(Ant::getLabel)
                                    .map(AntManagerAgent.this::getAID)
                                    .toList())
                            .setContentObject(node)
                            .build()
                    );
                });
            }
        }
    }

    private class AntResponseBehaviour extends CyclicBehaviour {

        @Serial
        private static final long serialVersionUID = 6620246288146601025L;

        private final Set<Ant> ants = new HashSet<>();

        @Override
        public void action() {
            MessageTemplate replyTemplate = MessageTemplate.MatchPerformative(Performative.ANT_RESPONSE_OK);
            ACLMessage antReply = receive(replyTemplate);

            if (antReply != null) {
                Ant antObj = AgentUtils.getContentObject(antReply, Ant.class);
                LOG.info("Received: {} - {}", antReply.getSender().getLocalName(), antObj.getTabuList());

//                send(new AgentMessageBuilder(Performative.HTTP_CLIENT_ANT_REQUEST)
//                        .setReceiver(getAID("http"))
//                        .setContentObject(antObj)
//                        .build());

                if (antObj.getTabuList().size() == graph.getNodes().size())
                    ants.add(antObj);

                boolean isAllAntsWithTabuListFull = ants.stream().allMatch(ant -> ant.isTabuListFull(graph.getNodes().size()));

                if (isAllAntsWithTabuListFull && graph.getNodes().size() == ants.size()) {
                    ants.forEach(ant -> {
                        LOG.info(ant.pathFound());

                        LOG.info("Send to http agent");
                        send(new AgentMessageBuilder(Performative.HTTP_CLIENT_ANT_REQUEST)
                                .setReceiver(getAID("http"))
                                .setContentObject(ant)
                                .build());
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
                        LOG.info("Shortest path found:");
                        LOG.info(graph.getShortestPath().toString());
                        LOG.info("Shortest distance found:");
                        LOG.info(String.valueOf(graph.getShortestPathDistance()));

//                        graph.getNodes().clear();
//                        graph.getEdges().clear();
//                        graph = null;
//                        return;
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
