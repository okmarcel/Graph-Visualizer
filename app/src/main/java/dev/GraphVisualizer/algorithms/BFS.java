package dev.GraphVisualizer.algorithms;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.xml.transform.Source;
import javafx.scene.paint.Color;

/**
 * Class BFS - implementation of breadth first search algorithm
 */
public class BFS {
    /**
     * Classical CLRS implementation of Breadth First Search algorithm
     * @param service API with representation of graph and Adjacency List for all nodes
     * @param sourceNode Node from which the algorithm runs
     */
    public static void runBFS(Map<Node, List<Node>> adjacent, Map<Node, AlgorithmAddInfo> state, Node sourceNode) {
        state.get(sourceNode).setAllBFS(AlgorithmAddInfo.NodeColor.GREY, 0.0, null);
        Queue<Node> q = new LinkedList<>();
        q.add(sourceNode);
        while(!q.isEmpty()) { 
            Node u = q.remove();
            for(Node v : adjacent.get(u)) {
                if(state.get(v).getNodeColor() == AlgorithmAddInfo.NodeColor.WHITE) {
                    state.get(v).setAllBFS(AlgorithmAddInfo.NodeColor.GREY, state.get(u).getD() + 1, u);
                    q.add(v);
                }
            }
            state.get(u).setNodeColor(AlgorithmAddInfo.NodeColor.BLACK);
        }
    }
}