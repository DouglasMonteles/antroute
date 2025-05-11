package br.com.doug.ant;

import lombok.Data;

@Data
public class AntDensityAlgorithm implements AntAlgorithm {

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
        Ant ant2 = new Ant("AntB", nodeA);

        graph.addEdge(nodeA, nodeB);
        graph.addEdge(nodeB, nodeC);
        graph.addEdge(nodeC, nodeA);
        graph.addEdge(nodeD, nodeA);
        graph.addEdge(nodeF, nodeB);
        graph.addEdge(nodeB, nodeE);
        graph.addEdge(nodeD, nodeE);

        graph.getNodes().forEach(it -> ant1.move(graph));
    }



}
