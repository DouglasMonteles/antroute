package br.com.doug.ant.impl;

import br.com.doug.ant.Ant;
import br.com.doug.ant.AntAlgorithm;
import br.com.doug.graph.Graph;
import br.com.doug.graph.Node;
import lombok.Data;

import java.util.List;

@Data
public class AntDensityAlgorithm implements AntAlgorithm {

    /*
    * In the Ant-density model a quantity Q 1 of trail for every unit of length is left on edge (i,j)
    * every time an ant goes from i to j;
    * */
    public static Float Q1 = 100f;

    /*
    * timeCounter (interactions)
    * */
    private Integer t = 0;

    private final Graph graph;
    private final List<Ant> ants;

    public AntDensityAlgorithm(Graph graph, List<Ant> ants) {
        this.graph = graph;
        this.ants = ants;
    }

    public static void main(String[] args) {
        // Init graph
        Graph graph = new Graph();
        Node nodeA = new Node("A", new Node.Position(10f, 10f));
        Node nodeB = new Node("B", new Node.Position(20f, 20f));
        Node nodeC = new Node("C", new Node.Position(30f, 10f));

        Ant ant1 = new Ant("AntA", nodeA);
        Ant ant2 = new Ant("AntB", nodeB);
        Ant ant3 = new Ant("AntC", nodeC);

        List<Ant> ants = List.of(ant1, ant2, ant3);

        graph.addEdge(nodeA, nodeB);
        graph.addEdge(nodeA, nodeC);

        graph.addEdge(nodeB, nodeC);
        AntDensityAlgorithm antDensityAlgorithm = new AntDensityAlgorithm(graph, ants);
        antDensityAlgorithm.run();
    }

    public void run() {
        int NC = 0;

        for (; NC < AntAlgorithm.NC_MAX; NC++) {
            // Repeat until tabu list is full
            while (true) {
                // for every town
                graph.getNodes().forEach(node -> {
                    // for every k-th ant on town i still not moved
                    ants.forEach(ant -> {
                        if (ant.getActualNode().equals(node)) {
                            ant.move(graph);
                        }
                    });
                });

                boolean isAllAntsWithTabuListFull = ants.stream().allMatch(ant -> ant.isTabuListFull(graph.getNodes().size()));
                if (isAllAntsWithTabuListFull) {
                    break;
                }
            }

            ants.forEach(ant -> {
                System.out.println(ant.pathFound());
            });

            graph.updateShortestPath(ants);
            graph.computeEvaporationOfTrail();

            /*
            * TODO: Tours in TSP are closed loops:
            *   Ex: a tour A → B → C → D → A is the same as B → C → D → A → B, or C → D → A → B → C.
            *   In TSP, these are considered the same tour, just with a different rotation.
            * */
            boolean isAllAntsWithSamePath = true;

            for (int i = 0; i < ants.size() - 1; i++) {
                Ant fistAnt = ants.get(i);
                Ant secondAnt = ants.get(i+1);

                if (!isSameTour(fistAnt.getPathFound(), secondAnt.getPathFound())) {
                    isAllAntsWithSamePath = false;
                    break;
                }
            }

//            if (isAllAntsWithSamePath) {
//                // All the ants choose the same tour
//                System.out.println();
//                System.out.println("is all same");
//                break;
//            }

            if (NC >= NC_MAX - 1 || isAllAntsWithSamePath) {
                System.out.println("Shortest path found:");
                System.out.println(graph.getShortestPath().toString());
                System.out.println("Shortest distance found:");
                System.out.println(graph.getShortestPathDistance());
                break;
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
    }

    public List<Node> shortestPath() {
        return graph.getShortestPath();
    }

    public Float shortestDistance() {
        return graph.getShortestPathDistance();
    }

    /*
        Double the fist tour and search and verify if tour2 is a sublist of tour1.
        Ex:
            tour1 = [1,2,3]
            aux = [1,2,3,1,2,3]
            tour2 = [3,1,2]
            isSameTour(tour1,tour2) is true because:
                aux [1,2,|3,1,2|,3]
    */
    public boolean isSameTour(List<Node> tour1, List<Node> tour2) {
        if (tour1.size() != tour2.size())
            return false;

        int lenTour1 = tour1.size();
        Node[] doubleTour1 = new Node[lenTour1 * 2];

        // When i >= lenTour1 then i resets to 0, 1, 2,...
        for (int i = 0; i < lenTour1 * 2; i++)
            doubleTour1[i] = tour1.get(i % lenTour1);

        for (int i = 0; i < lenTour1; i++) {
            boolean isMatch = true;

            for (int j = 0; j < lenTour1; j++) {
                if (!doubleTour1[i + j].equals(tour2.get(j))) {
                    isMatch = false;
                    break;
                }
            }

            if (isMatch) return true;
        }

        return false;
    }

}
