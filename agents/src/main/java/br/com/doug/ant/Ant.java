package br.com.doug.ant;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Data
public class Ant {

    private String label;
    private Node initialNode;
    private Node actualNode;
    private Node nextNode;

    private final Set<Node> tabuList = new HashSet<>();

    public Ant(String label, Node initialNode) {
        this.label = label;
        this.initialNode = initialNode;
        this.actualNode = initialNode;
        this.nextNode = null;

        this.addNodeToTabuList(initialNode);
    }

    public void move(Graph graph) {
        Node currentNode = this.nextNode == null ? this.initialNode : this.nextNode;

        // Only node not in tabulist are available
        List<Node> moveNodes = graph
                .getEdges(currentNode)
                .stream()
                .filter(this::isNodeNotInTabuList)
                .toList();

        if (!moveNodes.isEmpty()) {
            Node nextNode = this.getNodeWithMaxProbabilityToMove(moveNodes, graph);
            this.nextNode = nextNode;
            this.addNodeToTabuList(nextNode);

            // Update pheromone on edge
            graph.incrementPheromoneOnEdge(actualNode, nextNode, AntDensityAlgorithm.Q1);

            // Move to select next node
            this.actualNode = nextNode;

            System.out.println("Ant: " + this.label + " -> Next node: " + nextNode.getName());
        }
    }

    private boolean isNodeNotInTabuList(Node node) {
        return !this.tabuList.contains(node);
    }

    private void addNodeToTabuList(Node node) {
        this.tabuList.add(node);
    }

    private Node getNodeWithMaxProbabilityToMove(List<Node> nodes, Graph graph) {
        float maxProbability = Float.MIN_VALUE;
        Node selectedNode = nodes.get(0);

        for (Node node : nodes) {
            for (Edge edge : graph.getEdges()) {
                if (edge.isBidirectional()) {
                    if (edge.getNodeA().equals(node) || edge.getNodeB().equals(node)) {
                        double probability = this.calcProbability(graph, edge);

                        if (maxProbability < probability) {
                            maxProbability = (float) probability;
                            selectedNode = edge.getNodeB();
                        }
                    }
                } else {
                    if (edge.getNodeA().equals(actualNode) && edge.getNodeB().equals(node)) {
                        double probability = this.calcProbability(graph, edge);

                        if (maxProbability < probability) {
                            maxProbability = (float) probability;
                            selectedNode = edge.getNodeB();
                        }
                    }
                }
            }
        }

        return selectedNode;
    }

    private double calcProbability(Graph graph, Edge edge) {
        double numerator = (Math.pow(edge.getIntensityOfTrail(), AntAlgorithm.ALPHA) * Math.pow(edge.getVisibility(), AntAlgorithm.BETA));
        double denominator = 0f;

        for (Edge edge2 : graph.getEdges()) {
            denominator += (Math.pow(edge2.getIntensityOfTrail(), AntAlgorithm.ALPHA) * Math.pow(edge2.getVisibility(), AntAlgorithm.BETA));
        }

        return (denominator <= 0) ? 0d : (numerator / denominator);
    }

}
