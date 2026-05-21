package dev.GraphVisualizer.algorithms;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/** Class BFS - implementation of breadth first search algorithm */
public final class BFS {
    /**
     * Classical CLRS implementation of Breadth First Search algorithm
     * @param service API with representation of graph and Adjacency List for all nodes
     * @param sourceNode Node from which the algorithm runs
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