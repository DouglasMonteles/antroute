package br.com.doug.ant;

import br.com.doug.ant.impl.AntDensityAlgorithm;
import br.com.doug.ants.AntSimulationDataDTO;
import br.com.doug.graph.GraphNodeDTO;
import br.com.doug.utils.ObjectConversorUtils;
import br.com.doug.utils.RandomUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Graph {

    private final Set<Edge> edges = Collections.synchronizedSet(new HashSet<>());
    private List<Node> shortestPath = null;
    private Float shortestPathDistance = null;

    /*
    * Bidirectional graph
    * */
    public void addEdge(Node startNode, Node finalNode) {
        Edge edge = new Edge();
        edge.setBidirectional(true);

        // Set initial and final node
        edge.setNodeA(startNode);
        edge.setNodeB(finalNode);

        this.calcDefaultParamsForEdge(edge);

        this.edges.add(edge);
    }

    public void addEdge(Node startNode, Node finalNode, boolean isBidirectional) {
        Edge edge = new Edge();
        edge.setBidirectional(isBidirectional);

        // Set initial and final node
        edge.setNodeA(startNode);
        edge.setNodeB(finalNode);

        this.calcDefaultParamsForEdge(edge);

        this.edges.add(edge);
    }

    public void incrementPheromoneOnEdge(Node actualNode, Node nextNode, Float pheromone) {
        for (var edge : getEdges()) {
            if ((edge.getNodeA().equals(actualNode) && edge.getNodeB().equals(nextNode)) || (edge.getNodeA().equals(nextNode) && edge.getNodeB().equals(actualNode))) {
                edge.incrementPheromone(AntDensityAlgorithm.Q1);
                break;
            }
        }
    }

    public void computeEvaporationOfTrail() {
        // For every edge (i,j) compute τ ij(t+1) according to equation (1)
        for (var edge : getEdges()) {
            var intensity = edge.getIntensityOfTrail();
            var pheromone = edge.getPheromoneOnEdge();
            var intensityOfTrailUpdated = (AntAlgorithm.RHO * intensity) + pheromone;

            edge.setIntensityOfTrail(intensityOfTrailUpdated);
        }
    }

    public static Graph buildGraph(String jsonName) {
        Graph graph = new Graph();

        List<GraphNodeDTO> graphJson = ObjectConversorUtils.convertJsonInObject(jsonName, new TypeReference<>() {
        });

        Map<String, Node> nodeMap = graphJson
                .stream()
                .map(graphNode -> new Node(graphNode.getNode().getName(), new Node.Position(graphNode.getNode().getPosition().getX(), graphNode.getNode().getPosition().getY())))
                .collect(Collectors.toMap(Node::getName, node -> node));

        graphJson.forEach(graphNode -> {
            Node initalNode = nodeMap.get(graphNode.getNode().getName());
            List<Node> edges = graphNode.getEdges().stream().map(nodeMap::get).toList();

            edges.forEach(edge -> graph.addEdge(initalNode, edge));
        });

        return graph;
    }

    public static Graph buildGraph(AntSimulationDataDTO antSimulationDataDTO) {
        Graph graph = new Graph();
        List<Node> nodes = new ArrayList<>();

        // Generate and insert node
        for (int i = 0; i < antSimulationDataDTO.getAntQuantity(); i++) {
            var x = RandomUtils.randFloat(10, 1000);
            var y = RandomUtils.randFloat(10, 1000);
            var node = new Node("N" + (i+1), new Node.Position(x, y));

            nodes.add(node);
        }

        // Every node is connected with the others
        for (int i = 0; i < nodes.size(); i++) {
            Node initialNode = nodes.get(i);

            for (int j = i + 1; j < nodes.size(); j++) {
                Node edgeNode = nodes.get(j);
                graph.addEdge(initialNode, edgeNode);
            }
        }

        return graph;
    }

    private void calcDefaultParamsForEdge(Edge edge) {
        // Calc euclidian distance between the nodes and set the value in both nodes distance attribute
        float distance = calcEuclideanDistanceBetweenTwoConnectedNodes(edge.getNodeA(), edge.getNodeB());
        edge.setDistance(distance);

        // Calc visibility: probability from town i to town j for the k-th ant
        float visibility = calcVisibilityBetweenToNodes(edge);
        edge.setVisibility(visibility);

        // Set default trail intensity for every edge(i,j)
        float intensityOfTrail = 0.1f;
        edge.setIntensityOfTrail(intensityOfTrail);

        // Set default total pheromone for every edge(i,j)
        float pheromoneOnEdge = 0f;
        edge.setPheromoneOnEdge(pheromoneOnEdge);
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
            if (edge.isBidirectional()) {
                if (edge.getNodeA().equals(node)) {
                    nodes.add(edge.getNodeB());
                } else if (edge.getNodeB().equals(node)) {
                    nodes.add(edge.getNodeA());
                }
            } else {
                if (edge.getNodeA().equals(node)) {
                    nodes.add(edge.getNodeB());
                }
            }
        }

        return nodes;
    }

    public void updateShortestPath(List<Ant> ants) {
        float shortestDistance = Float.MAX_VALUE;

        for (Ant ant : ants) {
            if (ant.getPathFoundLength(this) < shortestDistance && !ant.getPathFound().isEmpty()) {
                this.shortestPath = ant.getPathFound();
                this.shortestPathDistance = shortestDistance;
            }
        }
    }

    public Optional<Float> getEdgeDistance(Node node1, Node node2) {
        return edges.stream()
                .filter(edge -> edge.getNodeA().equals(node1) && edge.getNodeB().equals(node2) || edge.getNodeA().equals(node2) && edge.getNodeB().equals(node1))
                .findFirst()
                .map(Edge::getDistance);
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

    private float calcVisibilityBetweenToNodes(Edge edge) {
        float distance = edge.getDistance();

        if (distance == 0f)
            return 0f;

        return 1 / distance;
    }

}
