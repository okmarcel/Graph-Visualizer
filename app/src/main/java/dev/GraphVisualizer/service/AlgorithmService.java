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
     * time variable needed for the DFS algorithm - represents proceed time of algorithm on a node
     */
    private int time = 0;

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
    public void runBFS(AlgorithmService service, Node sourceNode) {
        resetState();
        BFS.runBFS(service, sourceNode);
    }

    /**
     * Runs DFS algorithm
     * @param service API with the representation of graph and adjacency list for every node
     * @param sourceNode from which the algorithm starts
     */
    public void runDFS(AlgorithmService service, Node sourceNode) {
        resetState();
        DFS.runDFS(service, sourceNode);
    }

    /**
     * Runs Dijkstra algorithm
     * @param service API with the representation of graph and adjacency list for every node
     * @param sourceNode from which the algorithm starts
     */
    public void runDijkstra(AlgorithmService service, Node sourceNode) {
        resetState();
        Dijkstra.runDijkstra(service, sourceNode);
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
        resetTime();
        for(Node i : service.getGraph().getAllNodes()) {
            state.put(i, new ExtraInfo());
        }
    }

    /**
     * time getter
     * @return time
     */
    public int getTime() {
        return time;
    }
    
    /**
     * time incrementer
     */
    public void incrementTime(){
        time++;
    }
    
    /**
     * time reseter
     */
    public void resetTime() {
        time = 0;
    }
}

