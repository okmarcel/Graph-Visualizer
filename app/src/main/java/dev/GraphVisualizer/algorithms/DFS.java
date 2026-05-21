package dev.GraphVisualizer.algorithms;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;

import java.util.List;
import java.util.Map;

/** Class DFS - implementation of depth first search algorithm */
public final class DFS {
    /**
     * Classical CLRS implementation of Depth First Search algorithm.
     * Visits sourceNode first, then any remaining unvisited nodes (full-forest DFS).
     * @param adjacent Adjacency List for all nodes
     * @param state Map of AlgorithmAddInfo for all nodes
     * @param time array of size 1 used as mutable integer counter
     * @param sourceNode Node visited first
     */
    public static void runDFS(Map<Node, List<Node>> adjacent, Map<Node, AlgorithmAddInfo> state, double[] time, Node sourceNode, Runnable step) {
        if (state.get(sourceNode).getNodeColor() == AlgorithmAddInfo.NodeColor.WHITE) {
            dfsVisit(adjacent, state, time, sourceNode, step);
        }
        for (Node u : adjacent.keySet()) {
            if (state.get(u).getNodeColor() == AlgorithmAddInfo.NodeColor.WHITE) {
                dfsVisit(adjacent, state, time, u, step);
            }
        }
    }

    /**
     * Helper function invoked recursively
     * @param adjacent Adjacency List for all nodes
     * @param state Map of AlgorithmAddInfo for all nodes
     * @param time array of size 1 used as mutable integer counter
     * @param u Node on which the function was invoked
     * @param step callback invoked after each significant state change
     */
    private static void dfsVisit(Map<Node, List<Node>> adjacent, Map<Node, AlgorithmAddInfo> state, double[] time, Node u, Runnable step) {
        state.get(u).setD(++time[0]);
        state.get(u).setNodeColor(AlgorithmAddInfo.NodeColor.GREY);
        step.run();
        for (Node v : adjacent.get(u)) {
            if (state.get(v).getNodeColor() == AlgorithmAddInfo.NodeColor.WHITE) {
                state.get(v).setPi(u);
                dfsVisit(adjacent, state, time, v, step);
            }
        }
        state.get(u).setNodeColor(AlgorithmAddInfo.NodeColor.BLACK);
        state.get(u).setF(++time[0]);
        step.run();
    }
}
