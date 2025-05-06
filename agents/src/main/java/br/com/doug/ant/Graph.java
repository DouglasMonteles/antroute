package br.com.doug.ant;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Graph {

    private final List<Edge> edges = Collections.synchronizedList(new ArrayList<>());

    /*
    * Bidirectional graph
    * */
    public void addEdge(Node startNode, Node finalNode) {
        Edge edge = new Edge();

        // Set initial and final node
        edge.setNodeA(startNode);
        edge.setNodeB(finalNode);

        // Calc euclidian distance between the nodes and set the value in both nodes distance attribute
        float distance = calcEuclideanDistanceBetweenTwoConnectedNodes(startNode, finalNode);
        edge.setDistance(distance);

        // Set default trail intensity for every edge(i,j)
        float trailIntensity = 0f;
        edge.setTrailIntensity(trailIntensity);

        this.edges.add(edge);
    }

    public Set<Node> getNodes() {
        Set<Node> nodes = new HashSet<>();

        for (Edge edge : edges) {
            nodes.add(edge.getNodeA());
            nodes.add(edge.getNodeB());
        }

        return nodes;
    }

    public List<Node> getEdges(Node node) {
        List<Node> nodes = new ArrayList<>();

        for (Edge edge : edges) {
            if (edge.getNodeA().equals(node)) {
                nodes.add(edge.getNodeB());
            } else if (edge.getNodeB().equals(node)) {
                nodes.add(edge.getNodeA());
            }
        }

        return nodes;
    }

    private float calcEuclideanDistanceBetweenTwoConnectedNodes(Node startNode, Node finalNode) {
        Node.Position startNodePosition = startNode.getPosition();
        Node.Position finalNodePosition = finalNode.getPosition();

        float x0 = startNodePosition.getX();
        float y0 = startNodePosition.getY();

        float x1 = finalNodePosition.getX();
        float y1 = finalNodePosition.getY();

        return (float) Math.sqrt(Math.pow((x0 - x1), 2) + Math.pow((y0 - y1), 2));
    }

}
