package dev.GraphVisualizer.algorithms;
import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.service.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.xml.transform.Source;

/**
 * class DFS
 */
public class DFS {
    public static void runDFS(AlgorithmService service, Node sourceNode) {
        for(Node u : service.getService().getGraph().getAllNodes()) {
            if(service.getState().get(u).getAlgorithmColor() == AlgorithmColor.WHITE) {
                DFSVisit(service, u);
            }
        }
    }

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
