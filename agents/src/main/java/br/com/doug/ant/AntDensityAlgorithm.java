package br.com.doug.ant;

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

    public static void main(String[] args) {
        int NC = 0;

        // Init graph
        Graph graph = new Graph();
        Node nodeA = new Node("A", new Node.Position(10f, 10f));
        Node nodeB = new Node("B", new Node.Position(20f, 20f));
        Node nodeC = new Node("C", new Node.Position(30f, 10f));
        Node nodeD = new Node("D", new Node.Position(40f, 10f));
        Node nodeE = new Node("E", new Node.Position(50f, 10f));
        Node nodeF = new Node("F", new Node.Position(60f, 10f));

        Ant ant1 = new Ant("AntA", nodeA);
        Ant ant2 = new Ant("AntB", nodeA);

        List<Ant> ants = List.of(ant1, ant2);

        graph.addEdge(nodeA, nodeB);
        graph.addEdge(nodeA, nodeC);
        graph.addEdge(nodeA, nodeF);

        graph.addEdge(nodeB, nodeD);
        graph.addEdge(nodeB, nodeE);

        graph.addEdge(nodeC, nodeB);
        graph.addEdge(nodeC, nodeF);

        graph.addEdge(nodeD, nodeC);
        graph.addEdge(nodeD, nodeA);

        graph.addEdge(nodeE, nodeD);
        graph.addEdge(nodeE, nodeF);

        graph.addEdge(nodeF, nodeB);
        graph.addEdge(nodeF, nodeD);

        for (; NC < AntAlgorithm.NC_MAX; NC++) {
            // for every town
            graph.getNodes().forEach(node -> {
                // for every k-th ant on town i still not moved
                ants.forEach(ant -> {
                    ant.move(graph);
                });
            });

            ants.forEach(ant -> {
                System.out.println(ant.pathFound());
            });

            graph.computeEvaporationOfTrail();

            // Break case
            List<List<Node>> antsWhoChoseDistinctPath = ants.stream().map(Ant::getPathFound).distinct().toList();
            boolean isAllAntsWithSamePath = antsWhoChoseDistinctPath.size() == 1;

            if (NC > 0 && isAllAntsWithSamePath) {
                // All the ants choose the same tour
                break;
            }


            ants.forEach(ant -> {
                // Empty all tabu lists
                ant.getTabuList().clear();

                // Empty path found
                ant.getPathFound().clear();
            });

            // For every edge (i,j) set ∆τij(t,t+1):=0
            graph.getEdges().forEach(edge -> edge.setPheromoneOnEdge(0f));

            System.out.println();
        }
    }



}
