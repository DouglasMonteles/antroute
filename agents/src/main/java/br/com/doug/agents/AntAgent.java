package br.com.doug.agents;

import jade.core.Agent;
import jade.core.Node;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;

import java.util.ArrayList;
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

    private final List<Node> tabuList = new ArrayList<>();

    @Override
    protected void setup() {

        SequentialBehaviour antMoveCharacteristics = new SequentialBehaviour();
        antMoveCharacteristics.addSubBehaviour(new ChooseTownToGo());
        antMoveCharacteristics.addSubBehaviour(new LaysTrailOnEdge());
        antMoveCharacteristics.addSubBehaviour(new UpdateTabuList());

        addBehaviour(antMoveCharacteristics);
    }

    @Override
    protected void takeDown() {
        tabuList.clear();
    }

    /*
    * It chooses the town to go to with a probability that is a function of the town distance and
    * of the amount of trail present on the connecting edge.
    * */
    private static class ChooseTownToGo extends SimpleBehaviour {

        @Override
        public void action() {

        }

        @Override
        public boolean done() {
            return false;
        }
    }

    /*
    * When going from town i to town j it lays a substance, called trail, on edge (i,j).
    * */
    private static class LaysTrailOnEdge extends SimpleBehaviour {

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
    private static class UpdateTabuList extends SimpleBehaviour {

        @Override
        public void action() {

        }

        @Override
        public boolean done() {
            return false;
        }
    }

}
