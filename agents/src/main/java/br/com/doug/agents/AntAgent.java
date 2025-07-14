package br.com.doug.agents;

import br.com.doug.agents.builders.AgentMessageBuilder;
import br.com.doug.agents.utils.AgentUtils;
import br.com.doug.agents.utils.Performative;
import br.com.doug.ant.Ant;
import br.com.doug.ant.Graph;
import br.com.doug.ant.Node;
import br.com.doug.ant.impl.AntDensityAlgorithm;
import br.com.doug.exceptions.AgentException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.util.List;

/*
* Each ant is a simple agent with the following characteristics:
* - When going from town i to town j it lays a substance, called trail, on edge (i,j);
* - It chooses the town to go to with a probability that is a function of the town distance and
*   of the amount of trail present on the connecting edge;
* - To force ants to make legal tours, transitions to already visited towns are inhibited till a
*   tour is completed (see the tabu list in the following).
* */
public class AntAgent extends Agent {

    private static Logger LOG = LoggerFactory.getLogger(AntAgent.class);

    @Serial
    private static final long serialVersionUID = 3818585241476465782L;
    private Ant ant;
    private Graph graph;
    private List<Node> moveNodes = null;
    private Node nextNode;

    @Override
    protected void setup() {
        Object[] args = getArguments();

        if (args == null || args.length < 3) {
            throw new AgentException("AntAgent needs at lest three arguments.");
        }

        String antLabel = (String) args[0];
        Node antInitialNode = (Node) args[1];

        this.ant = new Ant(antLabel, antInitialNode);
        this.graph = (Graph) args[2];

        addBehaviour(new ReceiveRequestBehaviour());
    }

    @Override
    protected void takeDown() {

    }

    private class ReceiveRequestBehaviour extends CyclicBehaviour {

        @Serial
        private static final long serialVersionUID = -139692447381391989L;

        private int step = 1;

        @Override
        public void action() {
            MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(Performative.ANT_REQUEST);
            ACLMessage request = receive(messageTemplate);

            if (request != null) {
                Node actualNode = AgentUtils.getContentObject(request, Node.class);

                if (actualNode == null)
                    return;

                // Only move ants that are in the actual node
                if (ant == null || ant.getActualNode() == null || !ant.getActualNode().equals(actualNode))
                    return;

                switch (step) {
                    case 1:
                        ant.setInitialNodeInPathFound();
                        step = 2;
                    case 2:
                        moveNodes = ant.getAvailableNodes(graph);
                        step = 3;
                    case 3:
                        if (moveNodes != null) {
                            nextNode = ant.getNodeWithMaxProbabilityToMove(ant.getActualNode(), moveNodes, graph);
                            ant.setNextNode(nextNode);
                        }

                        step = 4;
                    case 4:
                        // Update pheromone on edge
                        graph.incrementPheromoneOnEdge(ant.getActualNode(), nextNode, AntDensityAlgorithm.Q1);
                        step = 5;
                    case 5:
                        ant.addNodeToTabuList(nextNode);
                        step = 6;
                    case 6:
                        // Move to select next node
                        ant.setActualNode(nextNode);

                        // Update path found
                        ant.getPathFound().add(nextNode);

                        ACLMessage response = new AgentMessageBuilder(request.createReply())
                            .setPerformative(Performative.ANT_RESPONSE_OK)
                            .setContentObject(ant)
                            .build();

                        LOG.info("Send: {} - {}", getAgent().getLocalName(), ant.getTabuList());
                        send(response);
                        step = 1;
                }
            } else {
                block();
            }
        }
    }

}
