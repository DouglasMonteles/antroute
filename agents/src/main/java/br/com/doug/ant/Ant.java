package br.com.doug.ant;

import lombok.Data;

import java.util.*;

@Data
public class Ant {

    private String label;
    private Node initialNode;
    private Node actualNode;
    private Node nextNode;

    private final Set<Node> tabuList = new HashSet<>();
    private final List<Node> pathFound = new ArrayList<>();

    public Ant(String label, Node initialNode) {
        this.label = label;
        this.initialNode = initialNode;
        this.actualNode = initialNode;
        this.nextNode = null;

        this.addNodeToTabuList(initialNode);
        this.pathFound.add(initialNode);
    }

    public void move(Graph graph) {
        // First node is always the initial node
        if (!pathFound.contains(initialNode)) {
            pathFound.add(initialNode);
        }

        Node currentNode = this.nextNode == null ? this.initialNode : this.nextNode;

        // Only node not in tabulist are available
        List<Node> moveNodes = graph
                .getEdges(currentNode)
                .stream()
                .filter(this::isNodeNotInTabuList)
                .toList();

        if (!moveNodes.isEmpty()) {
            Node nextNode = this.getNodeWithMaxProbabilityToMove(actualNode, moveNodes, graph);
            this.nextNode = nextNode;
            this.addNodeToTabuList(nextNode);

            // Update pheromone on edge
            graph.incrementPheromoneOnEdge(actualNode, nextNode, AntDensityAlgorithm.Q1);

            // Move to select next node
            this.actualNode = nextNode;

            // Update path found
            this.pathFound.add(nextNode);

            // System.out.println("Ant: " + this.label + " -> Next node: " + nextNode.getName());
        }
    }

    public String pathFound() {
        return pathFound.stream().map(Node::getName).toList().toString();
    }

    private boolean isNodeNotInTabuList(Node node) {
        return !this.tabuList.contains(node);
    }

    private void addNodeToTabuList(Node node) {
        this.tabuList.add(node);
    }

    private Node getNodeWithMaxProbabilityToMove(Node actualNode, List<Node> nodesNotInTabuList, Graph graph) {
        float maxProbability = -1f;
        Node selectedNode = null;

        List<Edge> edgesToMoveTo = graph.getEdges()
                .stream()
                .filter(edge -> edge.getNodeA().equals(actualNode) && nodesNotInTabuList.contains(edge.getNodeB()))
                .toList();

        for (Edge edge : edgesToMoveTo) {
            double probability = this.calcProbability(graph, edge);

            if (maxProbability < probability) {
                maxProbability = (float) probability;
                selectedNode = edge.getNodeB();
            }
        }

        return selectedNode;
    }

    private double calcProbability(Graph graph, Edge edge) {
        double numerator = (Math.pow(edge.getIntensityOfTrail(), AntAlgorithm.ALPHA) * Math.pow(edge.getVisibility(), AntAlgorithm.BETA));
        double denominator = 0d;

        for (Edge edge2 : graph.getEdges()) {
            denominator += (Math.pow(edge2.getIntensityOfTrail(), AntAlgorithm.ALPHA) * Math.pow(edge2.getVisibility(), AntAlgorithm.BETA));
        }

        return (denominator <= 0) ? 0d : (numerator / denominator);
    }

}
