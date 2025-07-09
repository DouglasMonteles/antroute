package br.com.doug.agents;

import br.com.doug.ant.Ant;
import br.com.doug.ant.Graph;
import br.com.doug.ant.Node;
import br.com.doug.ant.impl.AntDensityAlgorithm;
import br.com.doug.exceptions.AgentException;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
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

//        SequentialBehaviour antMoveCharacteristics = new SequentialBehaviour();
//        antMoveCharacteristics.addSubBehaviour(new ChooseTownToGo());
//        antMoveCharacteristics.addSubBehaviour(new CalcAvailableNodes());
//        antMoveCharacteristics.addSubBehaviour(new MoveAntOnGraphBehaviour());
//        antMoveCharacteristics.addSubBehaviour(new LaysTrailOnEdgeBehaviour());
//        antMoveCharacteristics.addSubBehaviour(new UpdateTabuListBehaviour());
//        antMoveCharacteristics.addSubBehaviour(new UpdateNodeAndPathBehaviour());

//        addBehaviour(antMoveCharacteristics);
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
            MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage request = receive(messageTemplate);

            if (request != null) {
                try {
                    Object body = request.getContentObject();
                    if (body == null) return;

                    Node actualNode = (Node) body;

                    // Only move ants that are in the actual node
                    if (ant == null || ant.getActualNode() == null || !ant.getActualNode().equals(actualNode))
                        return;

                    switch (step) {
                        case 1:
                            System.out.println("Step 1");
                            ant.setInitialNodeInPathFound();
                            step = 2;
                        case 2:
                            System.out.println("Step 2");
                            moveNodes = ant.getAvailableNodes(graph);
                            step = 3;
                        case 3:
                            System.out.println("Step 3");

                            if (moveNodes != null) {
                                nextNode = ant.getNodeWithMaxProbabilityToMove(ant.getActualNode(), moveNodes, graph);
                                ant.setNextNode(nextNode);
                            }

                            step = 4;
                        case 4:
                            System.out.println("Step 4");
                            // Update pheromone on edge
                            graph.incrementPheromoneOnEdge(ant.getActualNode(), nextNode, AntDensityAlgorithm.Q1);
                            step = 5;
                        case 5:
                            System.out.println("Step 5");
                            ant.addNodeToTabuList(nextNode);
                            step = 6;
                        case 6:
                            System.out.println("Step 6");

                            // Move to select next node
                            ant.setActualNode(nextNode);

                            // Update path found
                            ant.getPathFound().add(nextNode);

                            ACLMessage response = request.createReply();
                            response.setPerformative(Peformative.ANT_RESPONSE_OK);
                            response.setContentObject(ant);
                            send(response);
                            step = 1;
                    }
                } catch (IOException | UnreadableException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }
    }

    /*
    * It chooses the town to go to with a probability that is a function of the town distance and
    * of the amount of trail present on the connecting edge.
    * */
    private class ChooseTownToGo extends SimpleBehaviour {

        private boolean isDone = false;

        @Override
        public void action() {

            isDone = true;
        }

        @Override
        public boolean done() {
            return isDone;
        }
    }

    /*
     *
     * */
    private class CalcAvailableNodes extends SimpleBehaviour {

        private boolean isDone = false;

        @Override
        public void action() {

            isDone = true;
        }

        @Override
        public boolean done() {
            return isDone;
        }
    }

    /*
     *
     * */
    private class MoveAntOnGraphBehaviour extends SimpleBehaviour {

        private boolean isDone = false;

        @Override
        public void action() {

        }

        @Override
        public boolean done() {
            return isDone;
        }
    }

    /*
    * When going from town i to town j it lays a substance, called trail, on edge (i,j).
    * */
    private class LaysTrailOnEdgeBehaviour extends SimpleBehaviour {

        @Override
        public void action() {

        }

        @Override
        public boolean done() {
            return false;
        }
    }

    /*
    * To force ants to make legal tours, transitions to already visited towns are inhibited till a
    * tour is completed (see the tabu list in the following).
    * */
    private class UpdateTabuListBehaviour extends SimpleBehaviour {

        @Override
        public void action() {

        }

        @Override
        public boolean done() {
            return false;
        }
    }

    /*
     * To force ants to make legal tours, transitions to already visited towns are inhibited till a
     * tour is completed (see the tabu list in the following).
     * */
    private class UpdateNodeAndPathBehaviour extends SimpleBehaviour {

        private boolean isDone = false;

        @Override
        public void action() {


            isDone = true;
        }

        @Override
        public boolean done() {
            return isDone;
        }
    }

}
