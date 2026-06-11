package dev.GraphVisualizer.algorithms;
import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Class Dijkstra - implementation of Dijkstra algorithm */
public final class Dijkstra {
    /**
     * Classical CLRS implementation of Dijkstra's algorithm.
     * @param adjacent adjacency list for all nodes
     * @param state mutable algorithm state for all nodes
     * @param edges list of all edges in the graph
     * @param sourceNode node from which the algorithm starts
     * @param step callback invoked after each significant state change
     */
    public static void runDijkstra(Map<Node, List<Node>> adjacent, Map<Node, AlgorithmAddInfo> state, List<Edge> edges, Node sourceNode, Runnable step) {
        initializeSingleSource(state, sourceNode);
        step.run();
        Set<Node> visited = new HashSet<>();
        while (visited.size() < adjacent.size()) {
            Node u = null;
            for (Node n : adjacent.keySet()) {
                if (!visited.contains(n)) {
                    if (u == null || state.get(n).getD() < state.get(u).getD()) {
                        u = n;
                    }
                }
            }
            if (u == null || state.get(u).getD() == Double.POSITIVE_INFINITY)
                break;
            visited.add(u);
            state.get(u).setNodeColor(AlgorithmAddInfo.NodeColor.GREY);
            step.run();
            for (Node v : adjacent.get(u)) {
                double w = getWeight(edges, u, v);
                if (relax(state, u, v, w)) step.run();
            }
            state.get(u).setNodeColor(AlgorithmAddInfo.NodeColor.BLACK);
            step.run();
        }
    }

    /**
     * Initializes distances and parents before a Dijkstra run.
     * @param state mutable algorithm state for all nodes
     * @param sourceNode node from which the algorithm starts
     */
    public static void initializeSingleSource(Map<Node, AlgorithmAddInfo> state, Node sourceNode) {
        for (Node v : state.keySet()) {
            state.get(v).setD(Double.POSITIVE_INFINITY);
            state.get(v).setPi(null);
        }
        state.get(sourceNode).setD(0.0);
    }

    /**
     * Relaxes the edge between two adjacent nodes.
     * @param state mutable algorithm state for all nodes
     * @param u source node
     * @param v target node
     * @param w weight of edge between u and v
     * @return true when the distance to {@code v} was improved
     */
    public static boolean relax(Map<Node, AlgorithmAddInfo> state, Node u, Node v, double w) {
        double distU = state.get(u).getD();
        double distV = state.get(v).getD();
        if (distU != Double.POSITIVE_INFINITY && distV > distU + w) {
            state.get(v).setD(distU + w);
            state.get(v).setPi(u);
            return true;
        }
        return false;
    }

    /**
     * Returns weight of edge between u and v, or infinity if no such edge exists
     * @param edges List of all edges
     * @param u source node
     * @param v target node
     * @return weight of edge between u and v, or Double.POSITIVE_INFINITY if not found
     */
    private static double getWeight(List<Edge> edges, Node u, Node v) {
        for (Edge e : edges) {
            if (e.getSource().equals(u) && e.getTarget().equals(v))
                return e.getWeight();
            if (e.getSource().equals(v) && e.getTarget().equals(u))
                return e.getWeight();
        }
        return Double.POSITIVE_INFINITY;
    }
}
