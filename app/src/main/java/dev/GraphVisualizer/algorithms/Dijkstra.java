package dev.GraphVisualizer.algorithms;
import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Class Dijkstra - implementation of Dijkstra algorithm
 */
public class Dijkstra { 

    /**
     * Classical CLRS implementation of Dijkstra algorithm
     * @param adjacent Adjacency List for all nodes
     * @param state Map of ExtraInfo for all nodes
     * @param edges List of all edges in the graph
     * @param sourceNode Node from which the algorithm runs
     */
    public static void runDijkstra(Map<Node, List<Node>> adjacent, Map<Node, ExtraInfo> state, List<Edge> edges, Node sourceNode) {
        for(Edge e : edges) {
            if(e.getWeight() < 0.0) {
                throw new NegativeWeightException("Dijkstra cannot run on graphs with negative weights. Edge: "
                    + e.getSource().getLabel() + " -> " + e.getTarget().getLabel());
            }
        }
        InitializeSingleSource(state, sourceNode);
        PriorityQueue<Node> q = new PriorityQueue<>(
            Comparator.comparingDouble(n -> state.get(n).getD())
        );
        q.addAll(adjacent.keySet());
        while(!q.isEmpty()) {
            Node u = q.poll();
            for(Node v : adjacent.get(u)) {
                double w = getWeight(edges, u, v);
                Relax(state, u, v, w);
                q.remove(v);
                q.add(v);
            }
        }
    }

    /**
     * Initializes single source for Dijkstra
     * @param state Map of ExtraInfo for all nodes
     * @param sourceNode Node from which the algorithm runs
     */
    public static void InitializeSingleSource(Map<Node, ExtraInfo> state, Node sourceNode) {
        for(Node v : state.keySet()) {
            state.get(v).setD(Double.POSITIVE_INFINITY);
            state.get(v).setPi(null);
        }
        state.get(sourceNode).setD(0.0);
    }

    /**
     * Relaxes edge between u and v
     * @param state Map of ExtraInfo for all nodes
     * @param u source node
     * @param v target node
     * @param w weight of edge between u and v
     */
    public static void Relax(Map<Node, ExtraInfo> state, Node u, Node v, double w) {
        double distU = state.get(u).getD();
        double distV = state.get(v).getD();
        if(distU != Double.POSITIVE_INFINITY && distV > distU + w) {
            state.get(v).setD(distU + w);
            state.get(v).setPi(u);
        }
    }

    /**
     * Returns weight of edge between u and v
     * @param edges List of all edges
     * @param u source node
     * @param v target node
     * @return weight of edge between u and v
     */
    private static double getWeight(List<Edge> edges, Node u, Node v) {
        for(Edge e : edges) {
            if(e.getSource().equals(u) && e.getTarget().equals(v)) {
                return e.getWeight();
            }
        }
        return 0.0;
    }
}