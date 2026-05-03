package dev.GraphVisualizer.algorithms;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;
import dev.GraphVisualizer.service.AlgorithmAddInfo.NodeColor;

import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javafx.scene.paint.Color;

/**
 * Class DFS - implementation of depth first search algorithm
 */
public class DFS {
    /**
     * Classical CLRS implementation of Depth First Search algorithm
     * @param adjacent Adjacency List for all nodes
     * @param state Map of AlgorithmAddInfo for all nodes
     * @param time array of size 1 used as mutable integer counter
     * @param sourceNode Node from which the algorithm runs
     */
    public static void runDFS(Map<Node, List<Node>> adjacent, Map<Node, AlgorithmAddInfo> state, double[] time, Node sourceNode) {
        for(Node u : adjacent.keySet()) {
            if(state.get(u).getNodeColor() == AlgorithmAddInfo.NodeColor.WHITE) {
                DFSVisit(adjacent, state, time, u);
            }
        }
    }

    /**
     * Helper function invoked recursively
     * @param adjacent Adjacency List for all nodes
     * @param state Map of AlgorithmAddInfo for all nodes
     * @param time array of size 1 used as mutable integer counter
     * @param u Node on which the function was invoked
     */
    public static void DFSVisit(Map<Node, List<Node>> adjacent, Map<Node, AlgorithmAddInfo> state, double[] time, Node u) {
        state.get(u).setD(++time[0]);
        state.get(u).setNodeColor(AlgorithmAddInfo.NodeColor.GREY);
        for(Node v : adjacent.get(u)) {
            if(state.get(v).getNodeColor() == AlgorithmAddInfo.NodeColor.WHITE) {
                state.get(v).setPi(u);
                DFSVisit(adjacent, state, time, v);
            }
        }
        state.get(u).setNodeColor(AlgorithmAddInfo.NodeColor.BLACK);
        state.get(u).setF(++time[0]);
    }
}
