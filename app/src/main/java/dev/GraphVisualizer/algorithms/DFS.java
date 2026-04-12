package dev.GraphVisualizer.algorithms;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;

import javax.xml.transform.Source;
import javafx.scene.paint.Color;

/**
 * class DFS
 */
public class DFS {
    /**
     * Classical CLRS implementation of Depth First Search algorithm
     * @param service API with representation of graph and Adjacency List for all nodes
     * @param sourceNode Node from which the algorithm runs
     */
    public static void runDFS(AlgorithmService service, Node sourceNode) {
        for(Node u : service.getService().getGraph().getAllNodes()) {
            if(service.getState().get(u).getAlgorithmColor() == AlgorithmColor.WHITE) {
                DFSVisit(service, u);
            }
        }
    }

    /**
     * Helper function invoked recursively
     * @param service API with representation of graph and Adjacency List for all nodes
     * @param u Node on which the function was invoked
     */
    public static void DFSVisit(AlgorithmService service, Node u) {
        service.incrementTime();
        service.getState().get(u).setD(service.getTime());
        service.getState().get(u).setAlgorithmColor(AlgorithmColor.GREY);
        for(Node v : service.getService().getAdjacent().get(u)) {
            if(service.getState().get(v).getAlgorithmColor() == AlgorithmColor.WHITE) {
                service.getState().get(v).setPi(u);
                DFSVisit(service, v);
            }
        }
        service.getState().get(u).setAlgorithmColor(AlgorithmColor.BLACK);
        service.incrementTime();
        service.getState().get(u).setF(service.getTime());
    }

}
