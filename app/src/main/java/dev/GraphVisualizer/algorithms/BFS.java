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
 * class BFS
 */
public class BFS {
    /**
     * Classical CLRS implementation of Breadth First Search algorithm
     * @param service API with representation of graph and Adjacency List for all nodes
     * @param sourceNode Node from which the algorithm runs
     */
    public static void runBFS(Map<Node, List<Node>> adjacent, Map<Node, ExtraInfo> state, Node sourceNode) {
        state.get(sourceNode).setAllBFS(AlgorithmColor.GREY, 0, null);
        Queue<Node> q = new LinkedList<>();
        q.add(sourceNode);
        while(!q.isEmpty()) { 
            Node u = q.remove();
            for(Node v : adjacent.get(u)) {
                if(state.get(v).getAlgorithmColor() == AlgorithmColor.WHITE) {
                    state.get(v).setAllBFS(AlgorithmColor.GREY, state.get(u).getD() + 1, u);
                    q.add(v);
                }
            }
            state.get(u).setAlgorithmColor(AlgorithmColor.BLACK);
        }
    }
}