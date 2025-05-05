package br.com.doug.ant;

import lombok.Data;

import java.util.*;

@Data
public class Graph {

    private final Map<Node, List<Node>> graph = new HashMap<>();

    /*
    * Bidirectional graph
    * */
    public void addEdge(Node startNode, Node finalNode) {
        float distance = calcEuclideanDistanceBetweenTwoConnectedNodes(startNode, finalNode);
        startNode.setDistance(distance);
        finalNode.setDistance(distance);

        this.graph.putIfAbsent(startNode, Collections.synchronizedList(new ArrayList<>()));
        this.graph.get(startNode).add(finalNode);

        this.graph.putIfAbsent(finalNode, Collections.synchronizedList(new ArrayList<>()));
        this.graph.get(finalNode).add(startNode);
    }

    public Set<Node> getNodes() {
        return graph.keySet();
    }

    public List<Node> getEdges(Node node) {
        return graph.get(node);
    }

    public void printGraph() {
        graph.forEach((node, edges) -> {
            String edgesNames = edges.stream().map(Node::getName).toList().toString();
            System.out.println(node.getName() + " -> " + edgesNames);
        });
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
