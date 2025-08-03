package br.com.doug.ant;

import br.com.doug.ant.impl.AntDensityAlgorithm;
import br.com.doug.graph.Edge;
import br.com.doug.graph.Graph;
import br.com.doug.graph.Node;
import br.com.doug.utils.RandomUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Data
@EqualsAndHashCode(of = "label")
public class Ant implements Serializable {

    @Serial
    private static final long serialVersionUID = 173894473866953571L;

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

    public void setInitialNodeInPathFound() {
        // First node is always the initial node
        if (!pathFound.contains(initialNode)) {
            pathFound.add(initialNode);
            this.addNodeToTabuList(initialNode);
        }
    }

    public List<Node> getAvailableNodes(Graph graph) {
        return graph.getEdges(actualNode)
            .stream()
            .filter(this::isNodeNotInTabuList)
            .filter(node -> !node.equals(actualNode))
            .toList();
    }

    public void move(Graph graph) {
        setInitialNodeInPathFound();

        // Only node not in tabulist are available
        List<Node> moveNodes = getAvailableNodes(graph);

        if (!moveNodes.isEmpty()) {
            Node nextNode = getNodeWithMaxProbabilityToMove(actualNode, moveNodes, graph);
            this.nextNode = nextNode;
            this.addNodeToTabuList(nextNode);

            // Update pheromone on edge
            graph.incrementPheromoneOnEdge(actualNode, nextNode, AntDensityAlgorithm.Q1);

            // Move to select next node
            this.actualNode = nextNode;

            // Update path found
            this.pathFound.add(nextNode);
        }
    }

    public String pathFound() {
        return pathFound.stream().map(Node::getName).toList().toString();
    }

    public boolean isTabuListFull(int numberOfNodes) {
        return this.tabuList.size() == numberOfNodes;
    }

    public float getPathFoundLength(Graph graph) {
        float distance = 0f;

        for (int i = 0, j = 1; i < pathFound.size() - 1; i++, j++) {
            Optional<Float> dist = graph.getEdgeDistance(pathFound.get(i), pathFound.get(j));

            if (dist.isPresent()) {
                distance += dist.get();
            }
        }

        return distance;
    }

    public Node getNodeWithMaxProbabilityToMove(Node actualNode, List<Node> nodesNotInTabuList, Graph graph) {
        float maxProbability = -1f;
        Node selectedNode = null;

        List<Edge> edgesToMoveTo = graph.getEdges()
                .stream()
                .filter(edge -> (edge.getNodeA().equals(actualNode) && nodesNotInTabuList.contains(edge.getNodeB())) || (edge.getNodeB().equals(actualNode) && nodesNotInTabuList.contains(edge.getNodeA())))
                .toList();

        for (Edge edge : edgesToMoveTo) {
            double probability = this.tabuList.size() == 1  ? RandomUtils.randInt(0, 1) : this.calcProbability(graph, edge);

            if (maxProbability < probability) {
                maxProbability = (float) probability;

                selectedNode = actualNode.equals(edge.getNodeA()) ? edge.getNodeB() : edge.getNodeA();
            }
        }

        return selectedNode;
    }

    private boolean isNodeNotInTabuList(Node node) {
        return !this.tabuList.contains(node);
    }

    public void addNodeToTabuList(Node node) {
        if (node != null)
            this.tabuList.add(node);
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
