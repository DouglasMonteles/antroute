package br.com.doug.ant;

import br.com.doug.graph.Graph;
import br.com.doug.graph.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphTest {

    @Test
    public void generateBidirectionalGraph() {
        boolean isBidirectional = true;

        Graph graph = new Graph();
        Node nodeA = new Node("A", new Node.Position(10f, 10f));
        Node nodeB = new Node("B", new Node.Position(10f, 10f));

        graph.addEdge(nodeA, nodeB, isBidirectional);

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
    public void calcDistanceBetweenTwoNodesWhenAddEdgeInTheGraph() {
        Graph graph = new Graph();
        Node nodeA = new Node("A", new Node.Position(10f, 10f));
        Node nodeB = new Node("B", new Node.Position(20f, 20f));

        graph.addEdge(nodeA, nodeB);

        // Euclidian distance expected
        double expectedDistance = 14.14d;
        double delta = 0.01d;

        graph.getEdges().forEach(edge -> {
            Assert.assertEquals(expectedDistance, edge.getDistance().doubleValue(), delta);
        });
    }

    @Test
    public void generateNoBidirectionalGraph() {
        boolean isBidirectional = false;

        Graph graph = new Graph();
        Node nodeA = new Node("A", new Node.Position(10f, 10f));
        Node nodeB = new Node("B", new Node.Position(10f, 10f));

        graph.addEdge(nodeA, nodeB, isBidirectional);

        Set<Node> expectedNodes = new HashSet<>();
        expectedNodes.add(nodeA);
        expectedNodes.add(nodeB);

        List<Node> expectedEdgesNodeA = new ArrayList<>();
        expectedEdgesNodeA.add(nodeB);

        // List should be empty
        List<Node> expectedEdgesNodeB = new ArrayList<>();

        Assert.assertEquals(expectedNodes, graph.getNodes());
        Assert.assertEquals(expectedEdgesNodeA, graph.getEdges(nodeA));
        Assert.assertEquals(expectedEdgesNodeB, graph.getEdges(nodeB));
    }

}
