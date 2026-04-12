package dev.GraphVisualizer.service;

import dev.GraphVisualizer.models.*;
import dev.GraphVisualizer.algorithms.*;

import java.util.HashMap;
import java.util.Map; 

public class AlgorithmService {

    /**
     * graph representation plus the adjacency list for every node
     */
    private GraphService service;

    /**
     * Map holding the state of ExtraInfo needed for algorithm maped to every node in graph
     */
    private Map<Node, ExtraInfo> state;

    /**
     * Constructor - sets the initial state of nodes neeeded for BFS and DFS algorithms
     * @param service
     */
    public AlgorithmService(GraphService service) {
        this.service = service;
        this.state = new HashMap<>();
        for(Node i : this.service.getGraph().getAllNodes()) {
            state.put(i, new ExtraInfo());
        }
    }

    /**
     * Runs BFS algorithm
     * @param service API with the representation of graph and adjacency list for every node
     * @param sourceNode from which the algorithm starts
     */
    public void runBFS(Node sourceNode) {
        resetState();
        BFS.runBFS(service.getAdjacent(), state, sourceNode);
    }

    /**
     * Runs DFS algorithm
     * @param service API with the representation of graph and adjacency list for every node
     * @param sourceNode from which the algorithm starts
     */
    public void runDFS(Node sourceNode) {
        resetState();
        double[] time = {0.0};
        DFS.runDFS(service.getAdjacent(), state, time, sourceNode);
    }

    /**
     * Runs Dijkstra algorithm
     * @param service API with the representation of graph and adjacency list for every node
     * @param sourceNode from which the algorithm starts
     */
    public void runDijkstra(Node sourceNode) {
        resetState();
        Dijkstra.runDijkstra(service.getAdjacent(), state, service.getGraph().getAllEdges(), sourceNode);
    }

    /**
     * service getter
     * @return service
     */
    public GraphService getService() {
        return service;
    }

    /**
     * state getter
     * @return state
     */
    public Map<Node, ExtraInfo> getState() {
        return state;
    }

    /**
     * Resets the state of the state Map which holds the state of algorithm
     */
    private void resetState() {
        state.clear();
        for(Node i : service.getGraph().getAllNodes()) {
            state.put(i, new ExtraInfo());
        }
    }
}

