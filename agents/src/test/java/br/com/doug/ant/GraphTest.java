package br.com.doug.ant;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphTest {

    @Test
    public void generateBidirectionalGraph() {
        Graph graph = new Graph();
        Node nodeA = new Node("A", 10.0f, new Node.Position(10f, 10f));
        Node nodeB = new Node("B", 10.0f, new Node.Position(10f, 10f));

        graph.addEdge(nodeA, nodeB);

        Set<Node> expectedNodes = new HashSet<>();
        expectedNodes.add(nodeA);
        expectedNodes.add(nodeB);

        List<Node> expectedEdgesNodeA = new ArrayList<>();
        expectedEdgesNodeA.add(nodeB);

        List<Node> expectedEdgesNodeB = new ArrayList<>();
        expectedEdgesNodeB.add(nodeA);

        Assert.assertEquals(expectedNodes, graph.getNodes());
        Assert.assertEquals(expectedEdgesNodeA, graph.getEdges(nodeA));
        Assert.assertEquals(expectedEdgesNodeB, graph.getEdges(nodeB));
    }

    @Test
    public void printGraph() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        System.setOut(ps);

        Graph graph = new Graph();
        Node nodeA = new Node("A", 10.0f, new Node.Position(10f, 10f));
        Node nodeB = new Node("B", 10.0f, new Node.Position(10f, 10f));
        Node nodeC = new Node("C", 10.0f, new Node.Position(10f, 10f));

        graph.addEdge(nodeA, nodeB);
        graph.addEdge(nodeA, nodeC);

        graph.printGraph();

        String printExpected = """
        B -> [A]
        A -> [B, C]
        C -> [A]""";
        Assert.assertEquals(printExpected, byteArrayOutputStream.toString().trim());
        System.setOut(System.out);
    }

    @Test
    public void calcDistanceBetweenTwoNodesWhenAddEdgeInTheGraph() {
        Graph graph = new Graph();
        Node nodeA = new Node("A", null, new Node.Position(10f, 10f));
        Node nodeB = new Node("B", null, new Node.Position(20f, 20f));

        graph.addEdge(nodeA, nodeB);

        // Euclidian distance expected
        double expectedDistance = 14.14d;
        double delta = 0.01d;

        graph.getNodes().forEach(node -> {
            Assert.assertEquals(expectedDistance, node.getDistance().doubleValue(), delta);
        });
    }

}
