package br.com.doug.ant;

import lombok.Data;

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
        // Init graph
        Graph graph = new Graph();
        Node nodeA = new Node("A", new Node.Position(10f, 10f));
        Node nodeB = new Node("B", new Node.Position(20f, 20f));
        Node nodeC = new Node("C", new Node.Position(10f, 10f));
        Node nodeD = new Node("D", new Node.Position(10f, 10f));
        Node nodeE = new Node("E", new Node.Position(10f, 10f));
        Node nodeF = new Node("F", new Node.Position(10f, 10f));

        Ant ant1 = new Ant("AntA", nodeA);

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

        graph.getNodes().forEach(__ -> ant1.move(graph));

        graph.computeEvaporationOfTrail();

        System.out.println(graph);
    }



}
