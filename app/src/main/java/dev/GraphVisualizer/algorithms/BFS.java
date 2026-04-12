package dev.GraphVisualizer.algorithms;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;

import java.util.LinkedList;
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
    public static void runBFS(AlgorithmService service, Node sourceNode) {
        service.getState().get(sourceNode).setAllBFS(AlgorithmColor.GREY, 0, null);
        Queue<Node> q = new LinkedList<>();
        q.add(sourceNode);
        while(!q.isEmpty()) { 
            Node u = q.remove();
            for(Node v : service.getService().getAdjacent().get(u)) {
                if(service.getState().get(v).getAlgorithmColor() == AlgorithmColor.WHITE) {
                    service.getState().get(v).setAllBFS(AlgorithmColor.GREY, service.getState().get(u).getD() + 1, u);
                    q.add(v);
                }
            }
            service.getState().get(u).setAlgorithmColor(AlgorithmColor.BLACK);
        }
    }
}