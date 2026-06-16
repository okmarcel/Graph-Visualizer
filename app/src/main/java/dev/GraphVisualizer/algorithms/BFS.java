package dev.GraphVisualizer.algorithms;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/** Class BFS - implementation of breadth first search algorithm */
public final class BFS {
    /** Creates a BFS algorithm helper. */
    public BFS() {
    }

    /**
     * Classical CLRS implementation of the Breadth First Search algorithm.
     * @param adjacent adjacency list for all nodes
     * @param state mutable algorithm state for every node
     * @param sourceNode node from which the algorithm starts
     * @param step callback invoked after each significant state change
     */
    public static void runBFS(Map<Node, List<Node>> adjacent, Map<Node, AlgorithmAddInfo> state, Node sourceNode, Runnable step) {
        state.get(sourceNode).setAllBFS(AlgorithmAddInfo.NodeColor.GREY, 0.0, null);
        step.run();
        Queue<Node> q = new LinkedList<>();
        q.add(sourceNode);
        while(!q.isEmpty()) {
            Node u = q.remove();
            for(Node v : adjacent.get(u)) {
                if(state.get(v).getNodeColor() == AlgorithmAddInfo.NodeColor.WHITE) {
                    state.get(v).setAllBFS(AlgorithmAddInfo.NodeColor.GREY, state.get(u).getD() + 1, u);
                    step.run();
                    q.add(v);
                }
            }
            state.get(u).setNodeColor(AlgorithmAddInfo.NodeColor.BLACK);
            step.run();
        }
    }
}
